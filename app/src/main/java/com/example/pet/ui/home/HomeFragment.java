package com.example.pet.ui.home;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.pet.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private ArrayList<Goal> currentGoals;
    private int goalIndex = -1;
    private TextView goalNameDisplay;
    private TextView percentageDisplay;
    private LinearLayout progressBar;
    private LinearLayout.LayoutParams params;
    private int WIDTH;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        /*
         * Initialize
         */
        User user = User.Initialize();
        FrameLayout top = root.findViewById(R.id.fragment_home_top);
        top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                advanceGoalIndex();
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

        advanceGoalIndex();

        /*
         * Tester for I/O
         */
        final TextView textView = root.findViewById(R.id.test1);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        return root;
    }

    public void advanceGoalIndex() {
        int maxIndex = currentGoals.size() - 1;
        if (goalIndex >= maxIndex) {
            goalIndex = 0;
        } else {
            goalIndex++;
        }
        goalNameDisplay.setText(currentGoals.get(goalIndex).name());
        percentageDisplay.setText(currentGoals.get(goalIndex).finishPercentString());

        params.width = (int) ((double) WIDTH * currentGoals.get(goalIndex).finishPercent());
        progressBar.setLayoutParams(params);
    }
}