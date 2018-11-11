package com.api.base;

import java.util.HashMap;

public class ApiUtil {
    private String testCaseName;
    private HashMap<String, String> body;
    private String responseMessage;
    private final String host = "https://logistics-api.epsit.cn";
    private String url;
    private HashMap<String, String> header;
    private HashMap<String, String> expected;
    private String expectedStatusCode;
    private String actualStatusCode;
    private Boolean runStatus;

    public String getExpectedStatusCode() {
        return expectedStatusCode;
    }

    public Boolean getRunStatus() {
        return runStatus;
    }

    public void setRunStatus(Boolean runStatus) {
        this.runStatus = runStatus;
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

    public String getResponseMessage() {
        return responseMessage;
    }

    public HashMap<String, String> getBody() {
        return body;
    }

    public void setBody(HashMap<String, String> body) {
        this.body = body;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
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

    public String getTestCaseName() {
        return this.testCaseName;
    }

    public void setTestCaseName(String name) {
        this.testCaseName = name;
    }
}
