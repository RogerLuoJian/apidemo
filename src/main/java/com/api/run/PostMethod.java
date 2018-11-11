package com.api.run;

import com.api.base.ApiUtil;
import com.jayway.jsonpath.JsonPath;
import okhttp3.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static okhttp3.MultipartBody.FORM;


public class PostMethod {
	private static Logger logger = LoggerFactory.getLogger(PostMethod.class);
    private ApiUtil apiUtil;
    private OkHttpClient okHttpClient;

    public PostMethod(ApiUtil apiUtil) {
        this.apiUtil = apiUtil;
    }

    public void runSteps() {
        logger.info(apiUtil.getTestCaseName()+" Test run step");
        okHttpClient = new OkHttpClient();
        Request request = getRequest();
        try {
            Response response = okHttpClient.newCall(request).execute();
            String responseMessage = response.body().string();
            logger.info(apiUtil.getTestCaseName()+" response message "+responseMessage);
            int statusCode = response.code();
            apiUtil.setResponseMessage(responseMessage);
            apiUtil.setActualStatusCode(String.valueOf(statusCode));
            //Verify expected response message
            HashMap<String, String> expected = apiUtil.getExpected();
            for (String path : expected.keySet()) {
                String expectedValue = expected.get(path);
                String actualValue = jsonPath(apiUtil.getResponseMessage(), path);
                if (actualValue.trim().toLowerCase().equals(expectedValue.trim().toLowerCase())) {
                	logger.info(apiUtil.getTestCaseName()+" Pass");
                } else {
                	logger.info(apiUtil.getTestCaseName()+" Fail");
                }
            }
            //Verify expected status code
            if (apiUtil.getActualStatusCode().trim().toLowerCase().equals(apiUtil.getExpectedStatusCode().trim().toLowerCase())) {
            	logger.info(apiUtil.getTestCaseName()+" Pass");
            } else {
            	logger.info(apiUtil.getTestCaseName()+" Fail");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String buildURL() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(apiUtil.getHost() + apiUtil.getUrl()).newBuilder();
//        urlBuilder.addQueryParameter("account", "robot1");
//        urlBuilder.addQueryParameter("password", "MTIzNDU2");
        String url = urlBuilder.build().toString();
        return url;
    }
    
    private FormBody buildFormBody(){
    	FormBody.Builder builder=new FormBody.Builder();
    	builder.add("account", "robot1");
    	builder.add("password", "MTIzNDU2");
    	
    	return builder.build();
    }

    private RequestBody buildRequestBody() {
        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        return RequestBody.create(JSON, apiUtil.getBody());
    }

    private Request getRequest() {
        Map<String, String> header = apiUtil.getHeader();
        Headers headers = Headers.of(header);
        return new Request.Builder().url(buildURL()).headers(headers).post(buildFormBody()).build();
    }

    private String jsonPath(String body, String path) {
        return JsonPath.read(body, "$." + path);
    }
}
