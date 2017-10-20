package com.techalin.yourblog.controllers;

import android.app.ProgressDialog;
import android.content.Context;

import com.techalin.yourblog.libs.Alert;
import com.techalin.yourblog.libs.ApiRequest;
import com.techalin.yourblog.libs.ApiResponse;
import com.techalin.yourblog.libs.Settings;

import org.json.JSONException;
import org.json.JSONObject;


public class ContactController {

    private Context context;
    private String token;

    public ContactController(Context context) {
        this.context = context;
        this.token = Settings.getSetting("token");
    }


    public boolean contact(String name, String email, String message) {

        ProgressDialog progress = new ProgressDialog(context);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");

        ApiRequest request = null;
        try {
            request = new ApiRequest("http://intirana.com/api/v1/contact");
            request.addParameter("token", token);
            request.addParameter("name", name);
            request.addParameter("email", email);
            request.addParameter("message", message);
            request.setMethod("POST");
            request.addProgressDialog(progress);
            request.execute().get();
        } catch (Exception e) {
            Alert alert = new Alert(context);
            alert.showAlert("Error", e.getMessage());
        }

        int statusCode = request.getResponseCode();
        JSONObject json = request.getResponseJson();
        if (statusCode == 200) {
            try {
                String msg = json.getString("message");
                Alert alert = new Alert(context);
                alert.showAlert("Success", msg);

                return true;
            } catch (JSONException e) {
                return false;
            }

        } else if (statusCode != 0 && json != null) {
            ApiResponse response = new ApiResponse();
            response.setContext(context);
            response.processResponse(statusCode, json);
        } else {
            Alert alert = new Alert(context);
            alert.showAlert("Error", request.getError());
        }
        return false;
    }
}
