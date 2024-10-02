package com.myorg.codelambda;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.Map;

@SuppressWarnings("unused")
public class MyLambdaHandler implements RequestHandler<Map<String,Object>, String> {

    @Override
    public String handleRequest(Map<String, Object> input, Context context) {
        return "Hello from Java lambda (changed)";
    }
}
