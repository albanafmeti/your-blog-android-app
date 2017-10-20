package com.techalin.yourblog;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.techalin.yourblog.controllers.PostsController;
import com.techalin.yourblog.libs.Session;
import com.techalin.yourblog.models.Post;
import com.techalin.yourblog.models.User;

public class ShowPostActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    private TextView titleView;
    private TextView subtitleView;
    private TextView bodyView;
    private TextView categoryView;
    private TextView createdView;
    private TextView updatedView;

    NavigationView navigationView;

    private PostsController postCon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_post);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Show Post");
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

        titleView = (TextView) findViewById(R.id.titleView);
        subtitleView = (TextView) findViewById(R.id.subtitleView);
        bodyView = (TextView) findViewById(R.id.bodyView);
        categoryView = (TextView) findViewById(R.id.categoryView);
        createdView = (TextView) findViewById(R.id.createdView);
        updatedView = (TextView) findViewById(R.id.updatedView);

        postCon = new PostsController(ShowPostActivity.this);

        Intent intent = getIntent();
        int id_post = intent.getIntExtra("id_post", 0);

        handlePostData(id_post);
        handleNavHeader();
    }

    private void handlePostData(int id_post) {

        Post post = postCon.get_post(id_post);

        titleView.setText(post.getTitle());
        subtitleView.setText(post.getSubtitle());
        bodyView.setText(post.getBody());
        categoryView.setText("Category: " + post.getCategory().getName());
        createdView.setText("Created at: " + post.getCreatedAt());
        updatedView.setText("Updated at: " + post.getUpdatedAt());
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
            Intent intent = new Intent(ShowPostActivity.this, PostsActivity.class);
            startActivity(intent);
        } else if (id == R.id.post) {
            Intent intent = new Intent(ShowPostActivity.this, NewPostActivity.class);
            startActivity(intent);
        } else if (id == R.id.contact) {
            Intent intent = new Intent(ShowPostActivity.this, ContactActivity.class);
            startActivity(intent);
        } else if (id == R.id.about) {
            Intent intent = new Intent(ShowPostActivity.this, AboutActivity.class);
            startActivity(intent);
        } else if (id == R.id.log_out) {
            Intent i = new Intent(ShowPostActivity.this, LoginActivity.class);
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
                    Intent i = new Intent(ShowPostActivity.this, DashboardActivity.class);
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
