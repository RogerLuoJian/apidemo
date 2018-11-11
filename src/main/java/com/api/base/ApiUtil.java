package com.api.base;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

import java.io.*;
import java.util.HashMap;

public class ApiUtil {
	private String testCaseName;
    private String body;
    private String responseMessage;
    private String requestMethod;
    private final String host = "https://logistics-api.epsit.cn";
    private String url;
    private HashMap<String, String> header;
    private HashMap<String, String> expected;
    private String expectedStatusCode;
    private String actualStatusCode;

    public String getExpectedStatusCode() {
        return expectedStatusCode;
    }

    public void setExpectedStatusCode(String expectedStatusCode) {
        this.expectedStatusCode = expectedStatusCode;
    }

    public String getActualStatusCode() {
        return actualStatusCode;
    }

    public void setActualStatusCode(String actualStatusCode) {
        this.actualStatusCode = actualStatusCode;
    }

    public HashMap<String, String> getHeader() {
        return header;
    }

    public HashMap<String, String> getExpected() {
        return expected;
    }

    public void setExpected(HashMap<String, String> expected) {
        this.expected = expected;
    }

    public void setHeader(HashMap<String, String> header) {
        this.header = header;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getHost() {
        return host;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public String getTestCaseName(){
    	return this.testCaseName;
    }
    public void setTestCaseName(String name){
    	this.testCaseName=name;
    }
}
