package com.api.run;

import com.api.base.ApiUtil;


public class PostMethod {
    private ApiUtil apiUtil;

    public PostMethod(ApiUtil apiUtil) {
        this.apiUtil = apiUtil;
    }

    public void runSteps() {
        System.out.println("Test");
    }
}
