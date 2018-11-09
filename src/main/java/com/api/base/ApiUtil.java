package com.api.base;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

import java.io.*;
import java.util.HashMap;

public class ApiUtil {
    public HashMap<String, String> body;
    private String responseMessage;
    private String requestMethod;
    private final String host = "https://127.0.0.1/";
    private String url;
    private HashMap<String, String> header;
    private HashMap<String, String> expected;

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

    public HashMap<String, String> getBody() {
        return body;
    }

    public void setBody(HashMap<String, String> body) {
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

}
