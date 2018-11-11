package com.api.run;

import com.api.base.ApiUtil;
import com.jayway.jsonpath.JsonPath;
import okhttp3.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static okhttp3.MultipartBody.FORM;


public class PostMethod {
    private ApiUtil apiUtil;
    private OkHttpClient okHttpClient;

    public PostMethod(ApiUtil apiUtil) {
        this.apiUtil = apiUtil;
    }

    public void runSteps() {
        System.out.println("Test");
        okHttpClient = new OkHttpClient();
        Request request = getRequest();
        try {
            Response response = okHttpClient.newCall(request).execute();
            String responseMessage = response.body().string();
            int statusCode = response.code();
            apiUtil.setResponseMessage(responseMessage);
            apiUtil.setActualStatusCode(String.valueOf(statusCode));
            //Verify expected response message
            HashMap<String, String> expected = apiUtil.getExpected();
            for (String path : expected.keySet()) {
                String expectedValue = expected.get(path);
                String actualValue = jsonPath(apiUtil.getResponseMessage(), path);
                if (actualValue.trim().toLowerCase().equals(expectedValue.trim().toLowerCase())) {
                    System.out.println("Pass");
                } else {
                    System.out.println("Fail");
                }
            }
            //Verify expected status code
            if (apiUtil.getActualStatusCode().trim().toLowerCase().equals(apiUtil.getExpectedStatusCode().trim().toLowerCase())) {
                System.out.println("Pass");
            } else {
                System.out.println("Fail");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String buildURL() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(apiUtil.getHost() + apiUtil.getUrl()).newBuilder();
        urlBuilder.addQueryParameter("account", "robot1");
        urlBuilder.addQueryParameter("password", "MTIzNDU2");
        String url = urlBuilder.build().toString();
        return url;
    }

    private RequestBody buildRequestBody() {
        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        return RequestBody.create(JSON, apiUtil.getBody());
    }

    private Request getRequest() {
        Map<String, String> header = apiUtil.getHeader();
        Headers headers = Headers.of(header);
        return new Request.Builder().url(buildURL()).headers(headers).get().build();
    }

    private String jsonPath(String body, String path) {
        return JsonPath.read(body, "$." + path);
    }
}
