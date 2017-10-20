package com.techalin.yourblog;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

public class EditPostActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private Button editBtn;
    private TextInputEditText titleField;
    private TextInputEditText subtitleField;
    private TextInputEditText bodyField;
    private TextInputEditText slugField;
    private Spinner categoryIdField;

    //Nav View
    NavigationView navigationView;

    private PostsController postCon;

    private ArrayAdapter<Category> categoriesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Edit Post");
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

        postCon = new PostsController(EditPostActivity.this);

        //Inicializimi i komponenteve
        editBtn = (Button) findViewById(R.id.editBtn);
        titleField = (TextInputEditText) findViewById(R.id.title);
        subtitleField = (TextInputEditText) findViewById(R.id.subtitle);
        bodyField = (TextInputEditText) findViewById(R.id.body);
        slugField = (TextInputEditText) findViewById(R.id.slug);

        categoryIdField = (Spinner) findViewById(R.id.category_id);
        categoryIdField.setPrompt("Select Category");

        Intent intent = getIntent();
        int id_post = intent.getIntExtra("id_post", 0);

        setUpCategoriesSpinner(postCon.categories_list());
        handlePostData(id_post);
        handlePostSave(id_post);
        handleNavHeader();
    }

    private void handlePostData(int id_post) {

        Post post = postCon.get_post(id_post);

        titleField.setText(post.getTitle());
        subtitleField.setText(post.getSubtitle());
        bodyField.setText(post.getBody());
        slugField.setText(post.getSlug());
        //int pos = categoriesAdapter.getPosition(post.getCategory()); => post.getCategory() nuk paraqet te njejten vlere me ate qe mund te jete ne liste

        //Jo Shume Optimale kjo menyre
        for (int i = 0; i < categoriesAdapter.getCount(); i++) {
            categoryIdField.setSelected(true);
            categoryIdField.setSelection(i);
            if (categoryIdField.getSelectedItem().toString().equals(post.getCategory().toString())) {
                break;
            }
        }
    }

    private void handlePostSave(int id_post) {

        final int id = id_post;
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Send request to api to store post
                String title = titleField.getText().toString().trim();
                String subtitle = subtitleField.getText().toString().trim();
                String body = bodyField.getText().toString().trim();
                String slug = slugField.getText().toString().trim();

                Category selectedCateg = (Category) categoryIdField.getSelectedItem();
                String category_id = Integer.toString(selectedCateg.getId());

                Post returned = postCon.update_post(id, title, subtitle, body, slug, category_id);

                if (returned != null) {
                    Intent in = new Intent(EditPostActivity.this, PostsActivity.class);
                    in.putExtra("FROM_ACTIVITY", "EditPostActivity");
                    startActivity(in);
                }
            }
        });
    }

    private void setUpCategoriesSpinner(List<Category> categories) {


        categoriesAdapter = new ArrayAdapter<Category>(this,
                android.R.layout.simple_spinner_item, categories);
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryIdField.setAdapter(categoriesAdapter);
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
            Intent intent = new Intent(EditPostActivity.this, PostsActivity.class);
            startActivity(intent);
        } else if (id == R.id.post) {
            Intent intent = new Intent(EditPostActivity.this, NewPostActivity.class);
            startActivity(intent);
        } else if (id == R.id.contact) {
            Intent intent = new Intent(EditPostActivity.this, ContactActivity.class);
            startActivity(intent);
        } else if (id == R.id.about) {
            Intent intent = new Intent(EditPostActivity.this, AboutActivity.class);
            startActivity(intent);
        } else if (id == R.id.log_out) {
            Intent i = new Intent(EditPostActivity.this, LoginActivity.class);
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
                    Intent i = new Intent(EditPostActivity.this, DashboardActivity.class);
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
