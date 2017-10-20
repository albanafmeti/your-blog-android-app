package com.techalin.yourblog;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.techalin.yourblog.adapters.PostsAdapter;
import com.techalin.yourblog.controllers.PostsController;
import com.techalin.yourblog.libs.Session;
import com.techalin.yourblog.models.Post;
import com.techalin.yourblog.models.User;

import java.util.List;

public class PostsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Nav View
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Posts");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostsActivity.this, NewPostActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        PostsController postCon = new PostsController(PostsActivity.this);
        setUpMyPostsView(postCon.posts_list());
        handleNavHeader();
    }

    private void setUpMyPostsView(List<Post> posts_list) {

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.mypostsView);
        PostsAdapter adapter = new PostsAdapter(PostsActivity.this, posts_list);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(this);
        mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLinearLayoutManagerVertical);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            boolean handled = handleNoBackActivity();
            if (!handled) {
                super.onBackPressed();
            }
        }
    }

    private boolean handleNoBackActivity() {

        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("FROM_ACTIVITY")) {
            String from_activity = getIntent().getStringExtra("FROM_ACTIVITY");

            switch (from_activity) {
                case "NewPostActivity":
                case "EditPostActivity":
                    Intent in = new Intent(PostsActivity.this, DashboardActivity.class);
                    startActivity(in);
                    break;
            }

            return true;
        }

        return false;
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

        } else if (id == R.id.post) {
            Intent intent = new Intent(PostsActivity.this, NewPostActivity.class);
            startActivity(intent);
        } else if (id == R.id.contact) {
            Intent intent = new Intent(PostsActivity.this, ContactActivity.class);
            startActivity(intent);
        } else if (id == R.id.about) {
            Intent intent = new Intent(PostsActivity.this, AboutActivity.class);
            startActivity(intent);
        } else if (id == R.id.log_out) {
            Intent i = new Intent(PostsActivity.this, LoginActivity.class);
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
                    Intent i = new Intent(PostsActivity.this, DashboardActivity.class);
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
