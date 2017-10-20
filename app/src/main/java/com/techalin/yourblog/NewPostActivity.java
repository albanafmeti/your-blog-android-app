package com.techalin.yourblog;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.techalin.yourblog.controllers.PostsController;
import com.techalin.yourblog.libs.Session;
import com.techalin.yourblog.models.Category;
import com.techalin.yourblog.models.Post;
import com.techalin.yourblog.models.User;

import java.util.List;

public class NewPostActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private Button createBtn;
    private TextInputEditText titleField;
    private TextInputEditText subtitleField;
    private TextInputEditText bodyField;
    private TextInputEditText slugField;
    private Spinner categoryIdField;

    //Nav View
    NavigationView navigationView;

    private PostsController postCon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Create Post");
        setSupportActionBar(toolbar);


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerToggle.setDrawerIndicatorEnabled(false);
        drawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAfterTransition();
            }
        });

        toolbar.setNavigationIcon(this.getDrawerToggleDelegate().getThemeUpIndicator());
        //drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        postCon = new PostsController(NewPostActivity.this);

        //Inicializimi i komponenteve
        createBtn = (Button) findViewById(R.id.create);
        titleField = (TextInputEditText) findViewById(R.id.title);
        subtitleField = (TextInputEditText) findViewById(R.id.subtitle);
        bodyField = (TextInputEditText) findViewById(R.id.body);
        slugField = (TextInputEditText) findViewById(R.id.slug);
        categoryIdField = (Spinner) findViewById(R.id.category_id);


        setUpCategoriesSpinner(postCon.categories_list());
        handlePostCreation();
        handleNavHeader();
    }

    private void handlePostCreation() {

        titleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String titleText = titleField.getText().toString().trim();
                slugField.setText(titleText.replaceAll("\\s+", "-"));
            }
        });

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Send request to api to store post
                String title = titleField.getText().toString().trim();
                String subtitle = subtitleField.getText().toString().trim();
                String body = bodyField.getText().toString().trim();
                String slug = slugField.getText().toString().trim();

                Category selectedCateg = (Category) categoryIdField.getSelectedItem();
                String category_id = Integer.toString(selectedCateg.getId());

                Post returned = postCon.store_post(title, subtitle, body, slug, category_id);

                if (returned != null) {
                    Intent in = new Intent(NewPostActivity.this, PostsActivity.class);
                    in.putExtra("FROM_ACTIVITY", "NewPostActivity");
                    startActivity(in);
                }
            }
        });
    }

    private void setUpCategoriesSpinner(List<Category> categories) {


        ArrayAdapter<Category> dataAdapter = new ArrayAdapter<Category>(this,
                android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryIdField.setAdapter(dataAdapter);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.posts) {
            Intent intent = new Intent(NewPostActivity.this, PostsActivity.class);
            startActivity(intent);
        } else if (id == R.id.post) {

        } else if (id == R.id.contact) {
            Intent intent = new Intent(NewPostActivity.this, ContactActivity.class);
            startActivity(intent);
        } else if (id == R.id.about) {
            Intent intent = new Intent(NewPostActivity.this, AboutActivity.class);
            startActivity(intent);
        } else if (id == R.id.log_out) {
            Intent i = new Intent(NewPostActivity.this, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void handleNavHeader() {
        View view = navigationView.getHeaderView(0);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.nav_header) {
                    Intent i = new Intent(NewPostActivity.this, DashboardActivity.class);
                    startActivity(i);
                }
            }
        });

        TextView nameLabel = (TextView) view.findViewById(R.id.nameLabel);
        TextView emailLabel = (TextView) view.findViewById(R.id.emailLabel);

        if (Session.isSet("auth")) {
            User user = (User) Session.getItem("auth");

            nameLabel.setText(user.getName());
            emailLabel.setText(user.getEmail());
        }
    }
}
