package com.dataknocker.flink.streaming.api.graph;

import com.dataknocker.flink.api.dag.Transformation;
import com.dataknocker.flink.streaming.api.transformations.LegacySinkTransformation;
import com.dataknocker.flink.streaming.api.transformations.OneInputTransformation;
import com.dataknocker.flink.streaming.api.transformations.LegacySourceTransformation;
import com.dataknocker.flink.streaming.api.transformations.PartitionTransformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static com.dataknocker.flink.streaming.api.environment.StreamExecutionEnvironment.DEFAULT_JOB_NAME;

/**
 * 通过transformation生成StreamGraph
 */
public class StreamGraphGenerator {
    private static final Logger logger = LoggerFactory.getLogger(StreamGraphGenerator.class);

    @SuppressWarnings("rawtypes")
    private static Map<Class<? extends Transformation>, TransformationTranslator<?, ? extends Transformation<?>>> translatorMap;

    static {
        @SuppressWarnings("rawtypes")
        Map<Class<? extends Transformation>, TransformationTranslator<?, ? extends Transformation<?>>> tmp = new HashMap<>();
        tmp.put(OneInputTransformation.class, new OneInputTransformationTranslator<>());
        tmp.put(LegacySourceTransformation.class, new LegacySourceTransformationTranslator<>());
        tmp.put(LegacySinkTransformation.class, new LegacySinkTransformationTranslator<>());
        tmp.put(PartitionTransformation.class, new PartitionTransformationTranslator<>());
        translatorMap = Collections.unmodifiableMap(tmp);
    }

    private String jobName = DEFAULT_JOB_NAME;

    private StreamGraph streamGraph;

    private List<Transformation<?>> transformations;

    private Set<Transformation<?>> alreadyTransformed = new HashSet<>();

    public StreamGraphGenerator(List<Transformation<?>> transformations) {
        this(transformations, DEFAULT_JOB_NAME);
    }

    public StreamGraphGenerator(List<Transformation<?>> transformations, String jobName) {
        this.transformations = transformations;
        this.jobName = jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }
    public StreamGraph generate() {
        streamGraph = new StreamGraph();
        streamGraph.setJobName(jobName);
        for (Transformation<?> transformation: transformations) {
            transform(transformation);
        }
        return streamGraph;
    }

    public void transform(Transformation<?> transformation) {
        if (alreadyTransformed.contains(transformation)) {
            return;
        }
        getParentInputIds(transformation);
        TransformationTranslator<?, Transformation<?>> translator = (TransformationTranslator<?, Transformation<?>>)translatorMap.get(transformation.getClass());
        TransformationTranslator.Context context = new ContextImpl(streamGraph);
        translator.translateForStreaming(transformation, context);
        alreadyTransformed.add(transformation);
    }

    /**
     * 加载父input
     * @param transformation
     */
    private void getParentInputIds(Transformation<?> transformation) {
        for (Transformation<?> input: transformation.getInputs()) {
            transform(input);
        }
    }

    private class ContextImpl implements TransformationTranslator.Context {

        private StreamGraph streamGraph;

        public ContextImpl(StreamGraph streamGraph) {
            this.streamGraph = streamGraph;
        }

        @Override
        public StreamGraph getStreamGraph() {
            return streamGraph;
        }
    }


}
