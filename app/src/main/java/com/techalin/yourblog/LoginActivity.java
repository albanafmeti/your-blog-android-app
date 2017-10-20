package com.techalin.yourblog;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.techalin.yourblog.controllers.AuthController;
import com.techalin.yourblog.libs.Settings;

public class LoginActivity extends AppCompatActivity {

    AuthController auth = new AuthController(LoginActivity.this);

    TextInputEditText emailField;
    EditText passField;
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        Settings.setContext(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Sign In");
        toolbar.setLogo(R.drawable.logo_toolbar);
        toolbar.setPadding(1, 0, 0, 0);
        setSupportActionBar(toolbar);

        Button loginBtn = (Button) findViewById(R.id.sign_in_button);
        emailField = (TextInputEditText) findViewById(R.id.email);
        passField = (EditText) findViewById(R.id.password);

        assert loginBtn != null;
        loginBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {

                String email = emailField.getText().toString();
                String password = passField.getText().toString();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    toast = Toast.makeText(LoginActivity.this, "Fields are required.", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    boolean loggedIn = auth.authenticate(email, password);

                    if (loggedIn) {
                        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.exit:
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}


