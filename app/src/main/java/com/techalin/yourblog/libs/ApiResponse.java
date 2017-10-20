package com.techalin.yourblog.libs;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by albanafmeti on 16-08-19.
 */
public class ApiResponse {

    private Context context;

    public void processResponse(int code, JSONObject json) {
        Alert alert = new Alert(context);
        String message = "";
        try {
            switch (code) {
                case HttpsURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_BAD_REQUEST:
                    message = json.getString("message");

                    if (json.has("errors")) {
                        JSONObject errors = json.getJSONObject("errors");
                        Iterator<?> keys = errors.keys();

                        while (keys.hasNext()) {
                            String key = (String) keys.next();
                            JSONArray arrKey = errors.getJSONArray(key);
                            alert.showAlert("Error", arrKey.get(0).toString());
                            break;
                        }
                    } else {
                        alert.showAlert("Error", message);
                    }

                    break;
                case HttpURLConnection.HTTP_FORBIDDEN:
                    message = json.getString("message");
                    alert.showAlert("Error", message);
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    message = json.getString("message");
                    alert.showAlert("Error", message);
                    break;
                case HttpURLConnection.HTTP_UNAUTHORIZED:
                    message = json.getString("message");
                    alert.showAlert("Error", message);
                    break;
                case HttpURLConnection.HTTP_INTERNAL_ERROR:
                    message = json.getString("message");
                    alert.showAlert("Error", message);
                    break;
            }

        } catch (Exception e) {
            alert.showAlert("Error", e.getMessage());
        }
    }

    public void setContext(Context context) {
        this.context = context;
    }
}