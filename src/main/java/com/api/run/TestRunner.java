package com.api.run;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.api.base.ApiUtil;
import org.apache.commons.io.FileUtils;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Method;
import java.util.*;

public class TestRunner {
    private String tag;
    private String os;
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
        generateDataFromJSON();
    }

    @Test
    public void run() {
        switch (method.toUpperCase()) {
            case "POST":
                new PostMethod(apiUtil).runSteps();
                break;
        }
    }

    @AfterTest
    public void afterRun() {

    }

    private void generateDataFromJSON() throws IOException {
        File file = new File("API/DataDriver/robot.json");
        String contentList = FileUtils.readFileToString(file, "UTF-8");
        JSONArray currentList = JSON.parseArray(contentList);
        for (int i = 0; i < currentList.size(); i++) {
            JSONObject jsonObject = currentList.getJSONObject(i);
            if (jsonObject.get("TestName").equals(testName)) {
                apiUtil.setHeader(generateMap(JSON.toJSONString(jsonObject.get("Header"))));
                apiUtil.setBody(JSON.toJSONString(jsonObject.get("Body")));
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
            if(key.equals("status")) {
                apiUtil.setExpectedStatusCode(value);
                continue;
            }else
            {
                hashMap.put(key, value);
            }
        }
        return hashMap;
    }
}
