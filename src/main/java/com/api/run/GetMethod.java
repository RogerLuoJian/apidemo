package com.api.run;

import com.api.base.ApiUtil;
import com.jayway.jsonpath.JsonPath;
import okhttp3.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GetMethod {
    private static final Logger logger = Logger.getLogger(GetMethod.class.getName());
    private ApiUtil apiUtil;
    private OkHttpClient okHttpClient;

    public GetMethod(ApiUtil apiUtil) {
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
        } catch (Exception e) {
            logger.error("Get fail, Please check below error message: " + e.toString());
            throw e;
        }
    }

    private String buildURL() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(apiUtil.getHost() + apiUtil.getUrl()).newBuilder();
        //Generate params to url, Please input them inside the body of json file.
        HashMap<String, String> body = apiUtil.getBody();
        if (apiUtil.getBody() != null) {
            for (String key : body.keySet()) {
                urlBuilder.addQueryParameter(key, body.get(key));
            }
        }
        return urlBuilder.build().toString();
    }

    private Request getRequest() {
        Map<String, String> header = apiUtil.getHeader();
        Headers headers = Headers.of(header);
        return new Request.Builder().url(buildURL()).headers(headers).get().build();
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
