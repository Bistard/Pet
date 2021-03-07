package com.example.pet;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class HomeActivity extends AppCompatActivity {

    // fab animation
    FloatingActionButton fabMain, fab1, fab2;
    ObjectAnimator fabAnim;
    boolean fabAnimationFlag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the status bar transparent
        makeStatusBarTransparent(this);

        setContentView(R.layout.activity_home);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home,
                R.id.navigation_todo,
                R.id.navigation_calendar,
                R.id.navigation_statistics)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        // The below line of code causes crashes when applied a change on "NoActionBar"
        // NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        // nav bar UI
        com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx nav_view = (BottomNavigationViewEx) findViewById(R.id.nav_view);
        // nav_view.enableAnimation(false);
        nav_view.setTextVisibility(true);
        nav_view.enableShiftingMode(false);
        nav_view.enableItemShiftingMode(false);

        // fab animation
        init_fab_animation();

        // link add button to a new activity
        // to the AddTaskActivity
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddTaskPage();
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddGoalPage();
            }
        });

        // Data initializing
        DataManager.init(getFilesDir());
        // TODO: test only
        User user = Tester.makeCustomUser();
        // User user = User.Initialize();


        /*
        Testing only!!!
         */
        //User user = Tester.makeCustomUser(getFilesDir());
        //user.SaveFiles();
        //Log.i(null,User.goalList.toString());
        //Log.i(null,user.getUnfinishedTasks(2021,1,1).toString());
        // end testing

    }

    /**
     * @param activity the activity that you want the status bar to be transparent
     */
    public static void makeStatusBarTransparent(Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            int option = window.getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            window.getDecorView().setSystemUiVisibility(option);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * initialize animation of fab
     */
    public void init_fab_animation() {
        fabMain = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab_add_task);
        fab2 = (FloatingActionButton) findViewById(R.id.fab_add_goal);
        fabMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fabAnimationFlag) {
                    fab1.show();
                    fab2.show();
                    fabAnimation(fabAnim, fabMain, "rotation", 45, 250);
                    fabAnimation(fabAnim, fab1, "translationX", -fabMain.getHeight(), 250);
                    fabAnimation(fabAnim, fab1, "translationY", -fabMain.getHeight(), 250);
                    fabAnimation(fabAnim, fab2, "translationX",  fabMain.getHeight(), 250);
                    fabAnimation(fabAnim, fab2, "translationY", -fabMain.getHeight(), 250);
                    fabAnimationFlag = false;
                } else {
                    fabAnimation(fabAnim, fabMain, "rotation", 0, 250);
                    fabAnimation(fabAnim, fab1, "translationX", 0, 250);
                    fabAnimation(fabAnim, fab1, "translationY", 0, 250);
                    fabAnimation(fabAnim, fab2, "translationX", 0, 250);
                    fabAnimation(fabAnim, fab2, "translationY", 0, 250);
                    fab1.hide();
                    fab2.hide();
                    fabAnimationFlag = true;
                }
            }
        });
    }

    /**
     * @param anim ObjectAnimator
     * @param fab view
     * @param propertyName the property you want to change
     * @param value the change value
     * @param duration time duration
     */
    public void fabAnimation(ObjectAnimator anim, FloatingActionButton fab, String propertyName, int value, int duration) {
        anim = ObjectAnimator.ofFloat(fab, propertyName, value);
        anim.setDuration(duration);
        anim.start();
    }

    /**
     * Open the AddTaskActivity
     */
    public void openAddTaskPage() {
        Intent intent = new Intent(this, AddTaskActivity.class);
        startActivity(intent);
    }

    /**
     * Open the AddGoalActivity
     */
    public void openAddGoalPage() {
        Intent intent = new Intent(this, AddGoalActivity.class);
        startActivity(intent);
    }



}