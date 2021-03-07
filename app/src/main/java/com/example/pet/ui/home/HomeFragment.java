package com.example.pet.ui.home;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.pet.Goal;
import com.example.pet.R;
import com.example.pet.Task;
import com.example.pet.Tester;
import com.example.pet.User;

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

    // progress bar declaration
    private ArrayList<Goal> currentGoals;
    private int goalIndex = Integer.MAX_VALUE;
    private TextView goalNameDisplay;
    private TextView percentageDisplay;
    private LinearLayout progressBar;
    private LinearLayout.LayoutParams params;
    private int WIDTH;
    // Linear layout for upComing
    public LinearLayout upcomingLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // user initialize
        user = User.Initialize();
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
        percentageDisplay = root.findViewById(R.id.fragment_home_top_percentage);
        progressBar = root.findViewById(R.id.fragment_home_top_progressBar);
        params = (LinearLayout.LayoutParams) progressBar.getLayoutParams();
        int currentYear = Integer.parseInt(new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date()));
        int currentMonth = Integer.parseInt(new SimpleDateFormat("M", Locale.getDefault()).format(new Date()));
        int currentDate = Integer.parseInt(new SimpleDateFormat("dd", Locale.getDefault()).format(new Date()));
        WIDTH = Resources.getSystem().getDisplayMetrics().widthPixels;
        currentGoals = user.getGoals(currentYear, currentMonth, currentDate);
        displayGoal();
        /*
        upcoming tasks
        */
        final int MAX_DISPLAY_WINDOW = 10;
        upcomingLayout = root.findViewById(R.id.upComingTasks);
        int WINDOW_NUMBER = Math.min(MAX_DISPLAY_WINDOW, User.goalList.size());
        if (WINDOW_NUMBER != 0) {
            for (int i = 0; i < WINDOW_NUMBER; i++) {
                createUpComingWindow(root, upcomingLayout, User.goalList.get(i).name);
            }
        } else {
            // TODO: shows some messages
        }





        return root;
    }

    public void displayGoal() {
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

            params.width = (int) ((double) WIDTH * finishPercent);
            progressBar.setLayoutParams(params);
        } else {
            goalNameDisplay.setText(currentGoals.get(goalIndex).name());
            percentageDisplay.setText(currentGoals.get(goalIndex).finishPercentString());

            params.width = (int) ((double) WIDTH * currentGoals.get(goalIndex).finishPercent());
            progressBar.setLayoutParams(params);

            goalIndex++;
        }
    }

    public void createUpComingWindow(View v, LinearLayout layout, String name) {
        Button taskWindow = new Button(v.getContext());
        taskWindow.setText(name);
        taskWindow.setBackgroundResource(R.drawable.home_page_task_window);
        // TODO: set margin left

        layout.addView(taskWindow);
    }
}