package com.myorg.lambda;

import software.amazon.awscdk.services.lambda.Code;
import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;

import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Runtime;

public class MyPipelineLambdaStack extends Stack {
    public MyPipelineLambdaStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public MyPipelineLambdaStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        Function.Builder.create(this, "LambdaFunction")
                .runtime(Runtime.JAVA_17)
                .handler("com.myorg.codelambda.MyLambdaHandler::handleRequest")
                .code(Code.fromAsset("./lambda/target/lambda-0.1.jar"))
                .build();

    }

}