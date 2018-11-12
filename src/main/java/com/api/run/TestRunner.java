package com.api.run;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.api.base.ApiUtil;
import com.jayway.jsonpath.JsonPath;
import okhttp3.*;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.*;
import java.util.*;

public class TestRunner {
    private static final Logger logger = Logger.getLogger(TestRunner.class.getName());
    private String tag; //Will run test cases by tag.
    private String os; // Will update run test cases  in different OS env.
    private String method;
    private String testName;
    private ApiUtil apiUtil;

    @Parameters({"Tag", "URL", "OS", "Method", "TestName"})
    @BeforeTest
    public void beforeRun(String tag, String url, String os, String method, String testName) throws IOException {
        this.tag = tag;
        this.os = os;
        this.method = method;
        this.testName = testName;
        apiUtil = new ApiUtil();
        apiUtil.setUrl(url);
        logger.info(testName + " - Start to run case");
        generateDataFromJSON();
    }

    @Test
    public void run() throws IOException {
        switch (method.toUpperCase()) {
            case "POST":
                logger.info(testName + " - Ready to post url: " + apiUtil.getUrl());
                new PostMethod(apiUtil).runSteps();
                Boolean runPostStatus = true;
                Assert.assertEquals(runPostStatus, apiUtil.getRunStatus());
                break;
            case "GET":
                logger.info(testName + " - Ready to get url: " + apiUtil.getUrl());
                new GetMethod(apiUtil).runSteps();
                Boolean runGetStatus = true;
                Assert.assertEquals(runGetStatus, apiUtil.getRunStatus());
                break;
        }
    }

    @AfterTest
    public void afterRun() {
        logger.info(testName + " - End test case");
    }

    private void generateDataFromJSON() throws IOException {
        File file = new File("API/DataDriver/" + tag + ".json");
        String contentList = FileUtils.readFileToString(file, "UTF-8");
        JSONArray currentList = JSON.parseArray(contentList);
        for (int i = 0; i < currentList.size(); i++) {
            JSONObject jsonObject = currentList.getJSONObject(i);
            if (jsonObject.get("TestName").equals(testName)) {
                apiUtil.setTestCaseName(testName);
                String header = JSON.toJSONString(jsonObject.get("Header"));
                logger.info(testName + " - Build header: " + header);
                apiUtil.setHeader(generateMap(header));
                if (!testName.toLowerCase().contains("login")) {
                    generateToken();
                }
                String body = JSON.toJSONString(jsonObject.get("Body"));
                if(!body.equals("{}")) {
                    logger.info(testName + " - Build body: " + body);
                    apiUtil.setBody(generateMap(body));
                }
                apiUtil.setExpected(generateMap(JSON.toJSONString(jsonObject.get("Expected"))));
            }
        }
    }

    private HashMap<String, String> generateMap(String context) {
        HashMap<String, String> hashMap = new HashMap<>();
        context = context.replace("{", "").replace("}", "").replace("\"", "").replace(",", " ");
        String[] args = context.split(" ");
        for (int i = 0; i < args.length; i++) {
            String key = args[i].split(":")[0];
            String value = args[i].split(":")[1];
            if (key.equals("status")) {
                apiUtil.setExpectedStatusCode(value);
                continue;
            } else {
                hashMap.put(key, value);
            }
        }
        return hashMap;
    }

    private void generateToken() throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder().add("account", "robot1").add("password", "MTIzNDU2").build();
        Request request = new Request.Builder().url(apiUtil.getHost() + "/auth/robotLogin").addHeader("Content-Type", "application/json").post(requestBody).build();
        Response response = okHttpClient.newCall(request).execute();
        String resM = response.body().string();
        String token = JsonPath.read(resM, "$.data.token");
        apiUtil.getHeader().put("toekn", token);
    }
}
