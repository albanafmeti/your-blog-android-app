package com.techalin.yourblog.libs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class Helpers {

    public static boolean isJSONValid(String content) {
        try {
            new JSONObject(content);
        } catch (JSONException ex) {
            try {
                new JSONArray(content);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }
}
