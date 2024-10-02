package com.myorg;

import software.amazon.awscdk.Environment;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.StageProps;
import software.amazon.awscdk.pipelines.CodePipeline;
import software.amazon.awscdk.pipelines.CodePipelineSource;
import software.amazon.awscdk.pipelines.ShellStep;
import software.amazon.awscdk.services.codebuild.BuildSpec;
import software.amazon.awscdk.services.codebuild.Project;
import software.constructs.Construct;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

        Map<String, ?> buildSpec = Map.of(
                "phases", Map.of(
                        "build", Map.of(
                                "commands", List.of("mvn -pl lambda package")
                        )
                )
        );

        Project buildLambdas = Project.Builder.create(this, "BuildLambdasProject")
                .buildSpec(BuildSpec.fromObject(buildSpec))
                .build();

        BuildLambdasStage buildLambdasStage = new BuildLambdasStage(
                this, "BuildLambdasStage",
                StageProps.builder()
                .env(Environment.builder()
                             .account("266735842067")
                             .region("eu-west-1")
                             .build())
                .build(),
                buildLambdas
        );

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