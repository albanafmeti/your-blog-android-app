package com.techalin.yourblog.controllers;

import android.app.ProgressDialog;
import android.content.Context;

import com.techalin.yourblog.libs.Alert;
import com.techalin.yourblog.libs.ApiRequest;
import com.techalin.yourblog.libs.ApiResponse;
import com.techalin.yourblog.libs.Settings;
import com.techalin.yourblog.models.Category;
import com.techalin.yourblog.models.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PostsController {

    private Context context;
    private String token;

    public PostsController(Context context) {
        this.context = context;
        this.token = Settings.getSetting("token");
    }

    public ArrayList<Post> posts_list() {

        ArrayList<Post> postsList = new ArrayList<>();

        ProgressDialog progress = new ProgressDialog(context);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");

        ApiRequest request = null;
        try {
            request = new ApiRequest("http://intirana.com/api/v1/post");
            request.addParameter("token", token);
            request.setMethod("GET");
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
                JSONArray posts = json.getJSONArray("posts");


                for (int i = 0; i < posts.length(); i++) {
                    try {
                        JSONObject postJSON = posts.getJSONObject(i);

                        Post post = new Post();
                        post.setId(Integer.parseInt(postJSON.getString("id")));
                        post.setTitle(postJSON.getString("title"));
                        post.setSubtitle(postJSON.getString("subtitle"));
                        post.setBody(postJSON.getString("body"));
                        post.setSlug(postJSON.getString("slug"));
                        post.setCategoryId(Integer.parseInt(postJSON.getString("category_id")));
                        post.setUserId(Integer.parseInt(postJSON.getString("user_id")));
                        post.setCreatedAt(postJSON.getString("created_at"));
                        post.setUpdatedAt(postJSON.getString("updated_at"));

                        postsList.add(post);

                    } catch (JSONException e) {
                        continue;
                    }
                }

                return postsList;

            } catch (JSONException e) {
                return new ArrayList<Post>();
            }

        } else if (statusCode != 0 && json != null) {
            ApiResponse response = new ApiResponse();
            response.setContext(context);
            response.processResponse(statusCode, json);
        } else {
            Alert alert = new Alert(context);
            alert.showAlert("Error", request.getError());
        }
        return new ArrayList<Post>();
    }


    public ArrayList<Category> categories_list() {

        ArrayList<Category> categoriesList = new ArrayList<>();

        ProgressDialog progress = new ProgressDialog(context);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");

        ApiRequest request = null;
        try {
            request = new ApiRequest("http://intirana.com/api/v1/post/categories");
            request.addParameter("token", token);
            request.setMethod("GET");
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
                JSONArray categories = json.getJSONArray("categories");


                for (int i = 0; i < categories.length(); i++) {
                    try {
                        JSONObject categoryJSON = categories.getJSONObject(i);

                        Category category = new Category();
                        category.setId(Integer.parseInt(categoryJSON.getString("id")));
                        category.setName(categoryJSON.getString("name"));
                        category.setCreatedAt(categoryJSON.getString("created_at"));
                        category.setUpdatedAt(categoryJSON.getString("updated_at"));

                        categoriesList.add(category);

                    } catch (JSONException e) {
                        continue;
                    }
                }

                return categoriesList;

            } catch (JSONException e) {
                return new ArrayList<Category>();
            }

        } else if (statusCode != 0 && json != null) {
            ApiResponse response = new ApiResponse();
            response.setContext(context);
            response.processResponse(statusCode, json);
        } else {
            Alert alert = new Alert(context);
            alert.showAlert("Error", request.getError());
        }
        return new ArrayList<Category>();
    }


    public Post store_post(String title, String subtitle, String body, String slug, String category_id) {

        ProgressDialog progress = new ProgressDialog(context);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");

        ApiRequest request = null;
        try {
            request = new ApiRequest("http://intirana.com/api/v1/post");
            request.addParameter("token", token);
            request.addParameter("title", title);
            request.addParameter("subtitle", subtitle);
            request.addParameter("body", body);
            request.addParameter("slug", slug);
            request.addParameter("category_id", category_id);
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
                JSONObject postJSON = json.getJSONObject("post");

                Post post = new Post();
                post.setId(Integer.parseInt(postJSON.getString("id")));
                post.setTitle(postJSON.getString("title"));
                post.setSubtitle(postJSON.getString("subtitle"));
                post.setBody(postJSON.getString("body"));
                post.setSlug(postJSON.getString("slug"));
                post.setCategoryId(Integer.parseInt(postJSON.getString("category_id")));
                post.setUserId(Integer.parseInt(postJSON.getString("user_id")));
                post.setCreatedAt(postJSON.getString("created_at"));
                post.setUpdatedAt(postJSON.getString("updated_at"));

                String message = json.getString("message");
                Alert alert = new Alert(context);
                alert.showAlert("Success", message);

                return post;
            } catch (JSONException e) {
                return null;
            }

        } else if (statusCode != 0 && json != null) {
            ApiResponse response = new ApiResponse();
            response.setContext(context);
            response.processResponse(statusCode, json);
        } else {
            Alert alert = new Alert(context);
            alert.showAlert("Error", request.getError());
        }

        return null;
    }


    public boolean delete_post(Post post) {

        ProgressDialog progress = new ProgressDialog(context);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");

        ApiRequest request = null;
        try {
            request = new ApiRequest("http://intirana.com/api/v1/post/" + post.getId());
            request.addParameter("token", token);
            request.setMethod("DELETE");
            request.addProgressDialog(progress);
            request.execute().get();
        } catch (Exception e) {
            Alert alert = new Alert(context);
            alert.showAlert("Error", e.getMessage());
            return false;
        }

        int statusCode = request.getResponseCode();
        JSONObject json = request.getResponseJson();
        if (statusCode == 200) {
            try {
                String message = json.getString("message");
                Alert alert = new Alert(context);
                alert.showAlert("Success", message);
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

    public Post get_post(int id_post) {

        ProgressDialog progress = new ProgressDialog(context);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");

        ApiRequest request = null;
        try {
            request = new ApiRequest("http://intirana.com/api/v1/post/" + id_post);
            request.addParameter("token", token);
            request.setMethod("GET");
            request.addProgressDialog(progress);
            request.execute().get();
        } catch (Exception e) {
            Alert alert = new Alert(context);
            alert.showAlert("Error", e.getMessage());
            return null;
        }

        int statusCode = request.getResponseCode();
        JSONObject json = request.getResponseJson();
        if (statusCode == 200) {
            try {
                JSONObject postJSON = json.getJSONObject("post");
                JSONObject categoryJSON = json.getJSONObject("category");

                Post post = new Post();
                post.setId(Integer.parseInt(postJSON.getString("id")));
                post.setTitle(postJSON.getString("title"));
                post.setSubtitle(postJSON.getString("subtitle"));
                post.setBody(postJSON.getString("body"));
                post.setSlug(postJSON.getString("slug"));
                post.setCategoryId(Integer.parseInt(postJSON.getString("category_id")));
                post.setUserId(Integer.parseInt(postJSON.getString("user_id")));
                post.setCreatedAt(postJSON.getString("created_at"));
                post.setUpdatedAt(postJSON.getString("updated_at"));

                Category category = new Category();
                category.setId(Integer.parseInt(categoryJSON.getString("id")));
                category.setName(categoryJSON.getString("name"));
                category.setCreatedAt(categoryJSON.getString("created_at"));
                category.setUpdatedAt(categoryJSON.getString("updated_at"));
                post.setCategory(category);

                return post;
            } catch (JSONException e) {
                return null;
            }

        } else if (statusCode != 0 && json != null) {
            ApiResponse response = new ApiResponse();
            response.setContext(context);
            response.processResponse(statusCode, json);
        } else {
            Alert alert = new Alert(context);
            alert.showAlert("Error", request.getError());
        }

        return null;
    }

    public Post update_post(int id_post, String title, String subtitle, String body, String slug, String category_id) {

        ProgressDialog progress = new ProgressDialog(context);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");

        ApiRequest request = null;
        try {
            request = new ApiRequest("http://intirana.com/api/v1/post/" + id_post);
            request.addParameter("token", token);
            request.addParameter("title", title);
            request.addParameter("subtitle", subtitle);
            request.addParameter("body", body);
            request.addParameter("slug", slug);
            request.addParameter("category_id", category_id);
            request.setMethod("PUT");
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
                JSONObject postJSON = json.getJSONObject("post");

                Post post = new Post();
                post.setId(Integer.parseInt(postJSON.getString("id")));
                post.setTitle(postJSON.getString("title"));
                post.setSubtitle(postJSON.getString("subtitle"));
                post.setBody(postJSON.getString("body"));
                post.setSlug(postJSON.getString("slug"));
                post.setCategoryId(Integer.parseInt(postJSON.getString("category_id")));
                post.setUserId(Integer.parseInt(postJSON.getString("user_id")));
                post.setCreatedAt(postJSON.getString("created_at"));
                post.setUpdatedAt(postJSON.getString("updated_at"));

                String message = json.getString("message");
                Alert alert = new Alert(context);
                alert.showAlert("Success", message);

                return post;
            } catch (JSONException e) {
                return null;
            }

        } else if (statusCode != 0 && json != null) {
            ApiResponse response = new ApiResponse();
            response.setContext(context);
            response.processResponse(statusCode, json);
        } else {
            Alert alert = new Alert(context);
            alert.showAlert("Error", request.getError());
        }

        return null;
    }
}
