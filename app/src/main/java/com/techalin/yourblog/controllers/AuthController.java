package com.techalin.yourblog.controllers;

import android.app.ProgressDialog;
import android.content.Context;

import com.techalin.yourblog.libs.Alert;
import com.techalin.yourblog.libs.ApiRequest;
import com.techalin.yourblog.libs.ApiResponse;
import com.techalin.yourblog.libs.Session;
import com.techalin.yourblog.libs.Settings;
import com.techalin.yourblog.models.User;

import org.json.JSONException;
import org.json.JSONObject;

public class AuthController {

    private Context context;
    public String token;

    public AuthController(Context context) {
        this.context = context;
    }

    public boolean authenticate(String email, String password) {

        ProgressDialog progress = new ProgressDialog(context);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");

        ApiRequest request = null;
        try {
            request = new ApiRequest("http://intirana.com/api/v1/user/signin");
            request.addParameter("email", email);
            request.addParameter("password", password);
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
                String token = json.getString("token");
                JSONObject userJSON = json.getJSONObject("user");

                User user = new User();
                user.setName(userJSON.getString("name"));
                user.setEmail(userJSON.getString("email"));
                user.setCreated_at(userJSON.getString("created_at"));

                this.token = json.getString("token");
                Settings.setSetting("token", token);
                Session.addItem("auth", user);
            } catch (JSONException e) {
                return false;
            }
            return true;
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
