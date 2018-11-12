package com.api.run;

import com.api.base.ApiUtil;
import com.jayway.jsonpath.JsonPath;
import okhttp3.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class PostMethod {
    private static final Logger logger = Logger.getLogger(PostMethod.class.getName());
    private ApiUtil apiUtil;
    private OkHttpClient okHttpClient;

    public PostMethod(ApiUtil apiUtil) {
        this.apiUtil = apiUtil;
    }

    public void runSteps() throws IOException {
        okHttpClient = new OkHttpClient();
        Request request = getRequest();
        try {
            Response response = okHttpClient.newCall(request).execute();
            String responseMessage = response.body().string();
            logger.info(apiUtil.getTestCaseName() + " - Response message: " + responseMessage);
            int statusCode = response.code();
            apiUtil.setResponseMessage(responseMessage);
            apiUtil.setActualStatusCode(String.valueOf(statusCode));
            //Verify expected response message
            HashMap<String, String> expected = apiUtil.getExpected();
            for (String path : expected.keySet()) {
                String expectedValue = expected.get(path);
                String actualValue = jsonPath(apiUtil.getResponseMessage(), path);
                if (actualValue.trim().toLowerCase().equals(expectedValue.trim().toLowerCase())) {
                    logger.info(apiUtil.getTestCaseName() + " - Pass, Verify if expected key-value correct, The expected key is: " + path
                            + " and the actual value is: " + actualValue + ", the expected value is " + expectedValue);
                } else {
                    apiUtil.setRunStatus(false);
                    logger.info(apiUtil.getTestCaseName() + " - Fail, Verify if expected key-value correct, The expected key is: " + path
                            + " and the actual value is: " + actualValue + ", the expected value is " + expectedValue);
                }
            }
            //Verify expected status code
            if (apiUtil.getActualStatusCode().trim().toLowerCase().equals(apiUtil.getExpectedStatusCode().trim().toLowerCase())) {
                logger.info(apiUtil.getTestCaseName() + " - Pass, Verify if status code correct, The actual code is: "
                        + apiUtil.getActualStatusCode() + " and the expected code " + apiUtil.getExpectedStatusCode());
            } else {
                apiUtil.setRunStatus(false);
                logger.info(apiUtil.getTestCaseName() + " - Fail, Verify if status code correct, The actual code is: "
                        + apiUtil.getActualStatusCode() + " and the expected code " + apiUtil.getExpectedStatusCode());
            }
        } catch (Exception e) {
            logger.error("Post fail, Please check below error message: " + e.toString());
            throw e;
        }
    }

    private String buildURL() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(apiUtil.getHost() + apiUtil.getUrl()).newBuilder();
        return urlBuilder.build().toString();
    }

    private FormBody buildFormBody() {
        FormBody.Builder builder = new FormBody.Builder();
        HashMap<String, String> body = apiUtil.getBody();
        for (String key : body.keySet()) {
            builder.add(key, body.get(key));
        }
        return builder.build();
    }

    private Request getRequest() {
        Map<String, String> header = apiUtil.getHeader();
        Headers headers = Headers.of(header);
        return new Request.Builder().url(buildURL()).headers(headers).post(buildFormBody()).build();
    }

    private String jsonPath(String body, String path) {
        String result = "";
        try {
            result = JsonPath.read(body, "$." + path);
            return result;
        }catch (Exception e) {
            return path + " Not found";
        }
    }
}
