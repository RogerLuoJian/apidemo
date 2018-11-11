package com.api.run;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.api.base.ApiUtil;
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
                Boolean runStatus = true;
                Assert.assertEquals(runStatus, apiUtil.getRunStatus());
                break;
        }
    }

    @AfterTest
    public void afterRun() {
        logger.info(testName + " - End test case");
    }

    private void generateDataFromJSON() throws IOException {
        File file = new File("API/DataDriver/robot.json");
        String contentList = FileUtils.readFileToString(file, "UTF-8");
        JSONArray currentList = JSON.parseArray(contentList);
        for (int i = 0; i < currentList.size(); i++) {
            JSONObject jsonObject = currentList.getJSONObject(i);
            if (jsonObject.get("TestName").equals(testName)) {
                apiUtil.setTestCaseName(testName);
                String header = JSON.toJSONString(jsonObject.get("Header"));
                logger.info(testName + " - Build header: " + header);
                apiUtil.setHeader(generateMap(header));
                String body = JSON.toJSONString(jsonObject.get("Body"));
                logger.info(testName + " - Build body: " + body);
                apiUtil.setBody(generateMap(body));
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
}
