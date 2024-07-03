package com.dataknocker.flink.streaming.api.graph;

import com.dataknocker.flink.configuration.Configuration;
import com.dataknocker.flink.runtime.jobgraph.IntermediateDataset;
import com.dataknocker.flink.runtime.jobgraph.JobEdge;
import com.dataknocker.flink.runtime.jobgraph.JobGraph;
import com.dataknocker.flink.runtime.jobgraph.JobVertex;
import com.dataknocker.flink.streaming.runtime.partitioner.ForwardPartitioner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 从StreamGraph转成JobGraph
 */
public class StreamJobGraphGenerator {
    private static final Logger logger = LoggerFactory.getLogger(StreamJobGraphGenerator.class);

    private StreamGraph streamGraph;

    private JobGraph jobGraph;

    private Map<Integer, JobVertex> jobVertices = new HashMap<>();
    private Set<Integer> builtVertices = new HashSet<>();

    //保存了chain中各个source对应的chain节点配置列表
    private Map<Integer, Map<Integer, StreamConfig>> chainedConfigs = new HashMap<>();


    //保存了每个算子对应的StreamConfig
    private Map<Integer, StreamConfig> vertexConfigs = new HashMap<>();


    public StreamJobGraphGenerator(StreamGraph streamGraph) {
        this.streamGraph = streamGraph;
        jobGraph = new JobGraph(streamGraph.getJobName());
    }

    public static JobGraph createJobGraph(StreamGraph streamGraph) {
        return new StreamJobGraphGenerator(streamGraph).createJobGraph();
    }

    public JobGraph createJobGraph() {
        setChaining();
        return jobGraph;
    }

    /**
     * 从source开始创建链
     */
    public void setChaining() {
        Map<Integer, OperatorChainInfo> chainEntryPoints = buildChainedInputsAndGetHeadInputs();
        //各链的起点
        List<OperatorChainInfo> initialEntryPoints = chainEntryPoints.entrySet().stream().sorted(Map.Entry.comparingByKey()).map(Map.Entry::getValue).collect(Collectors.toList());
        //从source开始遍历，里面会递归遍历各outedges
        initialEntryPoints.forEach(info -> createChain(info.getStartNodeId(), 1, info, chainEntryPoints));
    }

    /**
     * 根据outEdges来创建递归创建链，同时判断是否能成链
     *
     * @param currentNodeId
     * @param chainIndex
     * @param info
     * @param chainEntryPoints
     * @return
     */
    private List<StreamEdge> createChain(Integer currentNodeId, int chainIndex, OperatorChainInfo info, Map<Integer, OperatorChainInfo> chainEntryPoints) {
        Integer startNodeId = info.getStartNodeId();
        if (!builtVertices.contains(startNodeId)) {
            List<StreamEdge> transitiveOutEdges = new ArrayList<>();
            List<StreamEdge> chainableOutputs = new ArrayList<>();
            List<StreamEdge> nonChainableOutputs = new ArrayList<>();
            StreamNode currentNode = streamGraph.getStreamNode(currentNodeId);
            currentNode.getOutEdges().forEach(edge -> {
                if (isChainable(edge, streamGraph)) {
                    chainableOutputs.add(edge);
                } else {
                    nonChainableOutputs.add(edge);
                }
            });
            //transitiveOutEdges是用于非chain中的连接，目前这里都是chain的，所以其为空
            //这里进行递归，主要进行子节点的streamconfig配置
            chainableOutputs.forEach(edge -> transitiveOutEdges.addAll(
                    createChain(edge.getTargetId(), chainIndex + 1, info, chainEntryPoints)));
            //新chain，transitiveOutEdges是用于非chain中的连接，
            nonChainableOutputs.forEach(edge -> {
                transitiveOutEdges.add(edge);
                createChain(edge.getTargetId(), 1, info.newChainInfo(edge.getTargetId()), chainEntryPoints);
            });

            //起点的节点用来生成JobVertex, 一个JobVertex对应一个chain
            //递归时start的config是最后才设置。这里非start节点的config已经都设置好了。
            StreamConfig streamConfig = currentNodeId.equals(startNodeId)
                    ? createJobVertex(startNodeId, info, streamGraph)
                    : new StreamConfig(new Configuration());
            setVertexNodeConfig(currentNodeId, streamConfig, chainIndex, info, chainableOutputs, nonChainableOutputs);
            //jobvertex进行jobedge连接
            if(startNodeId.equals(currentNodeId)) {
                transitiveOutEdges.forEach(edge -> connect(startNodeId, edge));
            }
            return transitiveOutEdges;
        } else {
            return Collections.emptyList();
        }

    }

    /**
     * 设置各streamnode对应的streamconfig
     *
     * @param currentNodeId
     * @param config
     * @param chainableOutputs
     * @param nonChainableOutputs
     */
    private void setVertexNodeConfig(Integer currentNodeId,
                                     StreamConfig config,
                                     int chainIndex,
                                     OperatorChainInfo info,
                                     List<StreamEdge> chainableOutputs,
                                     List<StreamEdge> nonChainableOutputs) {
        Integer startNodeId = info.getStartNodeId();
        StreamNode streamNode = streamGraph.getStreamNode(currentNodeId);
        config.setVertexID(currentNodeId);
        config.setStreamOperatorFactory(streamNode.getOperatorFactory());
        config.setChainIndex(chainIndex);
        config.setOperatorName(streamNode.getOperatorName());
        config.setChainableOutEdges(chainableOutputs);
        config.setNonChainableOutEdges(nonChainableOutputs);
        if (currentNodeId.equals(startNodeId)) { //该节点是chain的起点，chain的信息都由起点来记录
            config.setChainStart();
            config.setInvokableClassName(jobVertices.get(startNodeId).getInvokableClassName());
            //保存本chain对应的各节点的streamconfig
            config.setChainedConfigs(chainedConfigs.get(startNodeId));
        } else {
            chainedConfigs.computeIfAbsent(startNodeId, k -> new HashMap<>());
            //将非start节点的config都保存到该start的配置中
            chainedConfigs.get(startNodeId).put(currentNodeId, config);
        }
        if (chainableOutputs.isEmpty()) { //该节点是chain的终点
            config.setChainEnd();
        }
        vertexConfigs.put(currentNodeId, config);
    }

    /**
     * chain起点作为jobVertex，返回的是其StreamConfig
     *
     * @param streamNodeId
     * @param info
     * @param streamGraph
     * @return
     */
    private StreamConfig createJobVertex(Integer streamNodeId, OperatorChainInfo info, StreamGraph streamGraph) {
        StreamNode streamNode = streamGraph.getStreamNode(streamNodeId);
        JobVertex jobVertex = new JobVertex();
        jobVertex.setInvokableClassName(streamNode.getVertexClass());
        builtVertices.add(streamNodeId);
        jobVertices.put(streamNodeId, jobVertex);
        jobGraph.addVertex(jobVertex);
        //返回config后，外面对config的设置都会间接操作jobVertex中的Configuration,达到设置jobVertex的配置的目的
        return new StreamConfig(jobVertex.getConfiguration());
    }

    /**
     * 两个JobVertex进行相连
     * @param headChain
     * @param edge
     */
    public void connect(int headChain, StreamEdge edge) {
        JobVertex upStreamVertex = jobVertices.get(headChain);
        JobVertex downStreamVertex = jobVertices.get(edge.getTargetId());
        downStreamVertex.connectNewDataSetAsInput(upStreamVertex);
    }

    /**
     * 节点只有一个父节点时才可以建立chain
     *
     * @param streamEdge
     * @param streamGraph
     * @return
     */
    public static boolean isChainable(StreamEdge streamEdge, StreamGraph streamGraph) {
        StreamNode downStreamNode = streamGraph.getStreamNode(streamEdge.getTargetId());
        return downStreamNode.getInEdges().size() == 1 && isChainableInput(streamEdge, streamGraph);
    }

    /**
     * TODO 能否组成chain的判断
     *
     * @param streamEdge
     * @param streamGraph
     * @return
     */
    public static boolean isChainableInput(StreamEdge streamEdge, StreamGraph streamGraph) {
        StreamNode upStreamNode = streamGraph.getStreamNode(streamEdge.getSourceId());
        StreamNode downStreamNode = streamGraph.getStreamNode(streamEdge.getTargetId());
        return streamEdge.getPartitioner() == null || streamEdge.getPartitioner() instanceof ForwardPartitioner;
    }

    public Map<Integer, OperatorChainInfo> buildChainedInputsAndGetHeadInputs() {
        Map<Integer, ChainedSourceInfo> chainedSources = new HashMap<>();
        Map<Integer, OperatorChainInfo> chainEntryPoints = new HashMap<>();
        streamGraph.getSources().forEach(sourceId -> {
            StreamNode sourceNode = streamGraph.getStreamNode(sourceId);
            //TODO SourceOperatorFactory的处理
            chainEntryPoints.put(sourceId, new OperatorChainInfo(sourceId, chainedSources, streamGraph));
        });
        return chainEntryPoints;
    }

    private static class OperatorChainInfo {
        private Integer startNodeId;

        private Map<Integer, ChainedSourceInfo> chainedSources;
        private StreamGraph streamGraph;

        public OperatorChainInfo(Integer startNodeId, Map<Integer, ChainedSourceInfo> chainedSources, StreamGraph streamGraph) {
            this.startNodeId = startNodeId;
            this.chainedSources = chainedSources;
            this.streamGraph = streamGraph;
        }

        public OperatorChainInfo newChainInfo(Integer startNodeId) {
            return new OperatorChainInfo(startNodeId, chainedSources, streamGraph);
        }

        public Integer getStartNodeId() {
            return startNodeId;
        }
    }

    private static class ChainedSourceInfo {
        private StreamConfig operatorConfig;

        private StreamConfig.SourceInputConfig inputConfig;

        public ChainedSourceInfo(StreamConfig operatorConfig, StreamConfig.SourceInputConfig inputConfig) {
            this.operatorConfig = operatorConfig;
            this.inputConfig = inputConfig;
        }

        public StreamConfig getOperatorConfig() {
            return operatorConfig;
        }

        public StreamConfig.SourceInputConfig getInputConfig() {
            return inputConfig;
        }

    }


}
