package com.example.pet.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.app.LoaderManager;

import com.example.pet.Goal;
import com.example.pet.R;
import com.example.pet.Task;
import com.example.pet.Tester;
import com.example.pet.User;
import com.example.pet.ui.todo.TodoFragment;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HomeFragment extends Fragment {

    public HomeViewModel homeViewModel;
    private User user;

    private int[] IMAGE_SOURCE;

    private int currentYear ;
    private int currentMonth ;
    private int currentDate;

    // progress bar declaration
    private ArrayList<Goal> currentGoals;
    private int goalIndex = Integer.MAX_VALUE;
    private TextView goalNameDisplay;
    private TextView longTermGoalDisplay;
    private ImageView goalType;
    private TextView percentageDisplay;
    private LinearLayout progressBar;
    private LinearLayout.LayoutParams params;
    private int WIDTH;
    //  upComing
    private final int MAX_DISPLAY_WINDOW = 10;
    private LinearLayout upcomingLayout;
    private ArrayList<Task> upcomingTasks;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // user initialize
        user = User.Initialize();
        IMAGE_SOURCE = new int[]{R.drawable.education_icon, R.drawable.habbit_icon, R.drawable.sport_icon, R.drawable.work_icon};
        /*
         * fragment_home_top Initialize
         */
        FrameLayout top = root.findViewById(R.id.fragment_home_top);
        top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayGoal();
            }
        });
        /*
        date display
         */
        TextView dateDisplay = root.findViewById(R.id.fragment_home_top_date);
        String date = new SimpleDateFormat("MMMM dd", Locale.getDefault()).format(new Date());
        dateDisplay.setText(date);
        /*
        progressBar & goalName display
         */
        goalNameDisplay = root.findViewById(R.id.fragment_home_top_goal_name);
        longTermGoalDisplay = root.findViewById(R.id.fragment_home_top_goal_longtermgoal);
        goalType = root.findViewById(R.id.fragment_home_top_goal_type);
        percentageDisplay = root.findViewById(R.id.fragment_home_top_percentage);
        progressBar = root.findViewById(R.id.fragment_home_top_progressBar);
        params = (LinearLayout.LayoutParams) progressBar.getLayoutParams();
        currentYear = Integer.parseInt(new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date()));
        currentMonth = Integer.parseInt(new SimpleDateFormat("M", Locale.getDefault()).format(new Date()));
        currentDate = Integer.parseInt(new SimpleDateFormat("dd", Locale.getDefault()).format(new Date()));
        WIDTH = Resources.getSystem().getDisplayMetrics().widthPixels;
        currentGoals = user.getGoals(currentYear, currentMonth, currentDate);
        displayGoal();

        /*
        upcoming tasks
        */
        upcomingLayout = root.findViewById(R.id.upComingTasks);
        displayUpcomingTasks();

        return root;
    }

    private void displayUpcomingTasks(){
        upcomingLayout.removeAllViews();
        upcomingTasks = user.getUnfinishedTasks(currentYear,currentMonth,currentDate);
        for (Task t: user.getTasks(currentYear,currentMonth,currentDate)){
            if(!t.isFininshed()){
                upcomingTasks.add(t);
            }
        }
        int WINDOW_NUMBER = Math.min(MAX_DISPLAY_WINDOW, upcomingTasks.size());
        if (WINDOW_NUMBER != 0) {
            for (int i = 0; i < WINDOW_NUMBER; i++) {
                makeUpcomingTextView(upcomingTasks.get(i),makeInnerLayout(upcomingLayout));
            }
            //make custom empty layout
            makeEmptyLine(upcomingLayout, 50);
        } else {
            makeTextView("You don't have anything for today.",makeInnerLayout(upcomingLayout));
        }
    }

    private void displayGoal() {
        int maxIndex = currentGoals.size() - 1;
        if (goalIndex > maxIndex) {
            goalIndex = 0;

            int finished = 0;
            int total = 0;
            for (Task t : user.getTasks()) {
                total++;
                if (t.isFininshed()) {
                    finished++;
                }
            }
            double finishPercent = (double) finished * 100.0 / (double) total;

            goalNameDisplay.setText(user.longTermGoal);

            DecimalFormat df = new DecimalFormat("##0.0");
            percentageDisplay.setText(df.format(finishPercent));
            longTermGoalDisplay.setVisibility(View.VISIBLE);
            goalType.setVisibility(View.INVISIBLE);

            params.width = (int) ((double) WIDTH * finishPercent / 100.0);
            progressBar.setLayoutParams(params);
        } else {
            goalNameDisplay.setText(currentGoals.get(goalIndex).name());
            percentageDisplay.setText(currentGoals.get(goalIndex).finishPercentString());
            longTermGoalDisplay.setVisibility(View.INVISIBLE);
            goalType.setVisibility(View.VISIBLE);
            goalType.setImageResource(IMAGE_SOURCE[currentGoals.get(goalIndex).type()]);

            params.width = (int) ((double) WIDTH * currentGoals.get(goalIndex).finishPercent());
            progressBar.setLayoutParams(params);

            goalIndex++;
        }
    }

    public void createUpComingWindow(View v, LinearLayout layout, String name) {
        Button taskWindow = new Button(v.getContext());
        taskWindow.setText(name);
        //taskWindow.setBackgroundResource(R.drawable.home_page_task_window);
        taskWindow.setBackgroundColor(getResources().getColor(R.color.yellow_150));
        // TODO: set margin left
        LinearLayout.LayoutParams paramTaskWindow =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                              LinearLayout.LayoutParams.MATCH_PARENT);
        paramTaskWindow.gravity = Gravity.CENTER_HORIZONTAL;
        //paramTaskWindow.leftMargin = 20;
        //paramTaskWindow.rightMargin = 20;
        taskWindow.setLayoutParams(paramTaskWindow);
        layout.addView(taskWindow);
    }

    private LinearLayout makeInnerLayout(LinearLayout parent) {
        LinearLayout ll = new LinearLayout(getContext());
        LinearLayout.LayoutParams llparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        llparams.setMargins(30, 10, 30, 10);
        ll.setLayoutParams(llparams);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setBackgroundColor(0x00000000);

        LinearLayout layout = new LinearLayout(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(30, 0, 30, 0);
        layout.setLayoutParams(params);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundColor(getResources().getColor(R.color.yellow_150));
        //Drawable drawable = getResources().getDrawable(R.drawable.backgraound_round_corner);
        //layout.setBackground(drawable);

        ll.addView(layout);
        parent.addView(ll);
        return layout;
    }
    private TextView makeUpcomingTextView(Task t, LinearLayout parent) {
        LinearLayout layout = new LinearLayout(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(20, 20, 20, 20);
        layout.setLayoutParams(params);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setBackgroundColor(0x00FFFFFF);

        ImageView checkMark = new ImageView(getContext());
        checkMark.setLayoutParams(new LinearLayout.LayoutParams(80, 80));
        checkMark.setImageResource(R.drawable.ic_baseline_check_box_24);
        checkMark.setScaleType(ImageView.ScaleType.FIT_XY);
        checkMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t.changeFinished();
                user.SaveFiles();
                displayUpcomingTasks();
            }
        });
        layout.addView(checkMark);

        ImageView img = new ImageView(getContext());
        img.setLayoutParams(new LinearLayout.LayoutParams(80, 80));
        img.setImageResource(IMAGE_SOURCE[t.parentType()]);
        img.setScaleType(ImageView.ScaleType.FIT_XY);
        layout.addView(img);

        TextView tv = new TextView(getContext());
        tv.setText(t.name());
        LinearLayout.LayoutParams tparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tparams.setMargins(20, 0, 0, 0);
        tv.setLayoutParams(tparams);
        tv.setTextColor(0xFFFFFFFF);
        tv.setTextSize(20);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        layout.addView(tv);
        parent.addView(layout);
        return tv;
    }
    private TextView makeTextView(String text, LinearLayout parent) {
        TextView tv = new TextView(getContext());
        tv.setText(text);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(75, 20, 70, 20);
        tv.setLayoutParams(params);
        tv.setTextColor(0xFFFFFFFF);
        tv.setTextSize(20);
        parent.addView(tv);
        return tv;
    }
    private LinearLayout makeEmptyLine(LinearLayout parent, int height) {
        LinearLayout empty = new LinearLayout(getContext());
        empty.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height));
        empty.setOrientation(LinearLayout.VERTICAL);
        empty.setBackgroundColor(0x000000FF);
        parent.addView(empty);
        return empty;
    }
}