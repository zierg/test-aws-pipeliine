package com.myorg;

import com.myorg.lambda.MyPipelineLambdaStack;
import software.amazon.awscdk.services.codepipeline.Artifact;
import software.amazon.awscdk.services.codepipeline.actions.CloudFormationCreateUpdateStackAction;
import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.Stage;
import software.amazon.awscdk.StageProps;

import java.util.List;

public class MyPipelineAppStage extends Stage {
    @SuppressWarnings("unused")
    public MyPipelineAppStage(final Construct scope, final String id) {
        this(scope, id, null, List.of());
    }

    public MyPipelineAppStage(
            final Construct scope,
            final String id,
            final StageProps props,
            final List<Artifact> inputs
    ) {
        super(scope, id, props);

        Stack lambdaStack = new MyPipelineLambdaStack(this, "LambdaStack");

        CloudFormationCreateUpdateStackAction.Builder.create()
                .actionName("Deploy Lambdas")
                .extraInputs(inputs).
                stackName(lambdaStack.getStackName())
                .build();



    }

}