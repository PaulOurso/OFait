package com.devmobile.ofait.ui.mainmenu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.devmobile.ofait.R;
import com.devmobile.ofait.ui.fragment.AddContentFragment;
import com.devmobile.ofait.ui.fragment.ContentFragment;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private boolean viewIsAtHome;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fragment = null;

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        displayDefaultView();
    }

    public static void show(Context context){
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (!viewIsAtHome) { //if the current view is not the News fragment
                displayDefaultView(); //display the News fragment
            } else {
                super.onBackPressed();
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        displayView(item.getItemId());
        return true;
    }

    public void displayDefaultView() {
        displayView(-1);
    }

    public void displayView(int viewId) {
        String title = getString(R.string.app_name);

        switch (viewId) {
            case R.id.nav_contents:
                fragment = ContentFragment.getInstance();
                title  = getString(R.string.fragment_contents_title);
                viewIsAtHome = true;
                break;
            case R.id.nav_add:
                fragment = AddContentFragment.getInstance();
                title  = getString(R.string.fragment_add_title);
                viewIsAtHome = false;
                break;
            case R.id.nav_my_contents:
                // TODO: add Fragment My Contents
                title  = getString(R.string.fragment_my_contents_title);
                viewIsAtHome = false;
                break;
            case R.id.nav_favorites:
                // TODO: add Fragment Favorites
                title  = getString(R.string.fragment_favorites_title);
                viewIsAtHome = false;
                break;
            case R.id.nav_account:
                // TODO: add Fragment Account
                title  = getString(R.string.fragment_account_title);
                viewIsAtHome = false;
                break;
            default:
                fragment = ContentFragment.getInstance();
                title  = getString(R.string.fragment_contents_title);
                viewIsAtHome = true;
                break;
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }
        // set the toolbar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    public void uiDispatcher(View v){
        String action = v.getTag().toString();
        try {
            Method m = fragment.getClass().getMethod(action, MainActivity.class);
            m.invoke(fragment, MainActivity.this);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void onClickNavAccount(View view) {
        displayView(R.id.nav_account);
    }
}
