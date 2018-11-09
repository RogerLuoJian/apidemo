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
    private String url;
    private String os;
    private String method;
    private String testName;
    private ApiUtil apiUtil;

    @Parameters({"Tag", "URL", "OS", "Method", "TestName"})
    @BeforeTest
    public void beforeRun(String tag, String url, String os, String method, String testName) {
        this.tag = tag;
        this.url = url;
        this.os = os;
        this.method = method;
        this.testName = testName;
        apiUtil = new ApiUtil();

    }

    @Test
    public void run() throws IOException {
        generateDataFromJSON();
        System.out.println(tag + url + os + method + testName);
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
                String text = JSON.toJSONString(jsonObject.get("Header"));
                generateMap(text);
                System.out.println("Passed");
//                System.out.println(text);
            }
//            System.out.println(currentList.getJSONObject(0));
        }
    }

    private HashMap<String, String> generateMap(String context) {
        HashMap<String, String> hashMap = new HashMap<>();
        context = context.replace("{", "").replace("}", "").replace("\"", "").replace(",", " ");
        String[] args = context.split(" ");
        for (int i = 0; i < args.length; i++) {
            String key = args[i].split(":")[0];
            String value = args[i].split(":")[1];
            System.out.println(key + value);
        }
        System.out.println(context);
        return null;
    }
}
