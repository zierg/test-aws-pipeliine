package com.myorg;

import software.amazon.awscdk.Environment;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.StageProps;
import software.amazon.awscdk.pipelines.CodePipeline;
import software.amazon.awscdk.pipelines.CodePipelineSource;
import software.amazon.awscdk.pipelines.ShellStep;
import software.amazon.awscdk.pipelines.StageDeployment;
import software.constructs.Construct;

import java.util.Arrays;
import java.util.List;

public class MyPipelineStack extends Stack {
    @SuppressWarnings("unused")
    public MyPipelineStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public MyPipelineStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        CodePipeline pipeline = CodePipeline.Builder.create(this, "pipeline")
                .pipelineName("MyPipeline")
                .synth(ShellStep.Builder.create("Synth")
                               .input(CodePipelineSource.gitHub("zierg/test-aws-pipeliine", "main"))
                               .commands(Arrays.asList("npm install -g aws-cdk", "cdk synth"))
                               .build())
                .build();

        BuildLambdasStage buildLambdasStage = new BuildLambdasStage(this, "BuildLambdas", StageProps.builder()
                .env(Environment.builder()
                             .account("266735842067")
                             .region("eu-west-1")
                             .build())
                .build());

        pipeline.addStage(buildLambdasStage);


        pipeline.addStage(new MyPipelineAppStage(
                this,
                "natosha",
                StageProps.builder()
                        .env(Environment.builder()
                             .account("266735842067")
                             .region("eu-west-1")
                             .build())
                        .build(),
                List.of(buildLambdasStage.getOutput())
                                                    )

        );
    }
}