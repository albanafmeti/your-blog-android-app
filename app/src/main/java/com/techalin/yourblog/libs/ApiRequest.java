package com.techalin.yourblog.libs;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.Map;

public class ApiRequest extends AsyncTask<String, Void, String> {

    private String url_string;
    private String method = "GET";
    private ContentValues params;

    private ProgressDialog progress;
    private int responseCode = 0;
    private JSONObject responseJson = null;
    private String error;

    public ApiRequest(String url) {
        this.url_string = url;
        params = new ContentValues();
    }

    public void setUrl(String url) {
        this.url_string = url;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void addParameter(String key, String value) {
        params.put(key, value);
    }

    public String getQueryString() {
        try {
            if (params != null) {
                return this.getQuery(params).toString();
            }
        } catch (UnsupportedEncodingException e) {
            return "";
        }
        return "";
    }

    private String getQuery(ContentValues params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (Map.Entry<String, Object> pair : params.valueSet()) {

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue().toString(), "UTF-8"));
        }

        return result.toString();
    }


    @Override
    protected void onPreExecute() {
        progress.show();
    }

    @Override
    protected String doInBackground(String... params) {

        try {
            String queryString = this.getQueryString();
            this.url_string = this.url_string + "?" + queryString;

            URL url = new URL(this.url_string); // here is your URL path

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod(this.method);
            conn.setRequestProperty("Accept-Charset", "UTF-8");

            conn.setDoInput(true);
            conn.setRequestProperty("User-Agent", "YourBlog/1.0");

            if (method.equals("POST") || method.equals("PUT")) {
                conn.setFixedLengthStreamingMode(queryString.getBytes().length);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(this.getQueryString());
                writer.flush();
                writer.close();
                os.close();
            }

            int responseCode = conn.getResponseCode();
            this.setResponseCode(responseCode);

            String responseContent;
            if (responseCode != 200) {
                responseContent = this.getResponseContent(conn.getErrorStream());
            } else {
                responseContent = this.getResponseContent(conn.getInputStream());
            }

            if (Helpers.isJSONValid(responseContent)) {

                JSONObject json = this.parseRequest(responseContent);
                this.setResponseJson(json);
            } else {
                this.setError("Error: " + responseCode);
            }

        } catch (JSONException e) {
            this.setError("Response from the server was invalid.");
        } catch (NullPointerException e) {
            this.setError("Application error. Please contact your administrator.");
        } catch (UnknownHostException e) {
            this.setError("Unknown host. Please contact your administrator.");
        } catch (ConnectException e) {
            this.setError("The application was not able to connect to the Internet.");
        } catch (SocketTimeoutException e) {
            this.setError("Read timed out.");
        } catch (Exception e) {
            this.setError(e.getMessage());
        } finally {
            this.progress.dismiss();
        }

        return null;
    }

    public void addProgressDialog(ProgressDialog progress) {
        this.progress = progress;
    }


    public JSONObject parseRequest(String content) throws JSONException {
        JSONObject root = new JSONObject(content);
        return root;
    }

    public String getResponseContent(InputStream inputS) {
        BufferedReader in = new BufferedReader(new InputStreamReader(inputS));
        StringBuffer sb = new StringBuffer("");
        String line = "";
        try {
            while ((line = in.readLine()) != null) {
                sb.append(line);
                break;
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return sb.toString();
    }


    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseJson(JSONObject responseJson) {
        this.responseJson = responseJson;
    }

    public JSONObject getResponseJson() {
        return responseJson;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
