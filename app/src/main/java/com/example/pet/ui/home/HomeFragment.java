package com.example.pet.ui.home;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.app.LoaderManager;

import com.example.pet.AddTaskActivity;
import com.example.pet.Goal;
import com.example.pet.NotificationApplication;
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
import java.util.Random;

public class HomeFragment extends Fragment {

    public HomeViewModel homeViewModel;
    private User user;

    private int[] IMAGE_SOURCE;

    private int currentYear;
    private int currentMonth;
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

    //pet interaction
    private boolean bubbleLayoutVisible = true;
    private FrameLayout bubbleLayout;
    private TextView bubbleText;

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

        //pet bubble
        ImageView petImg = root.findViewById(R.id.PetImage);
        petImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bubbleDisappearAnim();
                refreshBubble();
                bubbleAppearAnim();
            }
        });

        bubbleLayout = root.findViewById(R.id.home_pet_bubble_layout);
        bubbleText = root.findViewById(R.id.home_pet_bubble_text);
        bubbleLayoutVisible = false;
        bubbleLayout.setVisibility(View.INVISIBLE);

        final Handler refreshHandler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                double rand = new Random().nextDouble();
                if (bubbleLayoutVisible) {
                    if (rand > 0.3) {
                        bubbleDisappearAnim();
                    }
                } else {
                    if (rand > 0.1 && displayBubble()) {
                        bubbleAppearAnim();
                    }
                }

                refreshHandler.postDelayed(this, 30 * 1000);
            }
        };
        refreshHandler.postDelayed(runnable, 30 * 1000);

        return root;
    }

    private boolean displayBubble() {
        try {
            bubbleText.setText(user.pet.getPhrase());
        } catch (Exception e) {
            Log.i("home", e.toString());
            return false;
        }
        return true;
    }

    private void refreshBubble() {
        String str = null;
        while (str == null) {
            try {
                str = user.pet.getPhrase();
            } catch (Exception e) {
                Log.i("home", e.toString());
            }
        }
        bubbleText.setText(str);
    }

    private void bubbleAppearAnim() {
        bubbleLayoutVisible = true;
        bubbleLayout.setVisibility(View.VISIBLE);

        try {
            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
            bubbleLayout.startAnimation(animation);
        } catch (Exception ignored) {
            bubbleLayout.setAlpha(1);
        }
    }

    private void bubbleDisappearAnim() {
        try {
            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
            bubbleLayout.startAnimation(animation);
        } catch (Exception ignored) {
        }

        bubbleLayoutVisible = false;
        bubbleLayout.setVisibility(View.INVISIBLE);
    }

    private void displayUpcomingTasks() {
        upcomingLayout.removeAllViews();
        upcomingTasks = user.getUnfinishedTasks(currentYear, currentMonth, currentDate);
        for (Task t : user.getTasks(currentYear, currentMonth, currentDate)) {
            if (!t.isFininshed()) {
                upcomingTasks.add(t);
            }
        }
        // display each upcoming task
        int WINDOW_NUMBER = Math.min(MAX_DISPLAY_WINDOW, upcomingTasks.size());
        if (WINDOW_NUMBER != 0) {
            for (int i = 0; i < WINDOW_NUMBER; i++) {
                makeUpcomingTextView(upcomingTasks.get(i), makeInnerLayout(upcomingLayout));
                makeEmptyLine(upcomingLayout, 5, getResources().getColor(R.color.grey_50));
            }
            //make custom empty layout
            makeEmptyLine(upcomingLayout, 50, getResources().getColor(R.color.white_transparent));
        } else {
            makeTextView("You don't have anything for today.", makeInnerLayout(upcomingLayout));
        }
    }

    private void displayGoal() {
        if (goalIndex > currentGoals.size() - 1 || goalIndex < 0) {
            goalIndex = 0;

            goalNameDisplay.setText(user.longTermGoal);

            percentageDisplay.setText(user.longTermGoalFinishPercentString());
            longTermGoalDisplay.setText("Long-term Goal:");
            goalType.setVisibility(View.INVISIBLE);

            params.width = (int) ((double) WIDTH * user.longTermGoalFinishPercent());
            progressBar.setLayoutParams(params);
        } else {
            goalNameDisplay.setText(currentGoals.get(goalIndex).name());
            percentageDisplay.setText(currentGoals.get(goalIndex).finishPercentString());
            longTermGoalDisplay.setText("Short-term Goal:");
            goalType.setVisibility(View.VISIBLE);
            goalType.setImageResource(IMAGE_SOURCE[currentGoals.get(goalIndex).type()]);

            params.width = (int) ((double) WIDTH * currentGoals.get(goalIndex).finishPercent());
            progressBar.setLayoutParams(params);

            goalIndex++;
        }
    }

    private LinearLayout makeInnerLayout(LinearLayout parent) {
        LinearLayout ll = new LinearLayout(getContext());
        LinearLayout.LayoutParams llparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        llparams.setMargins(30, 0, 30, 0);
        ll.setLayoutParams(llparams);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setBackgroundColor(0x00000000);

        LinearLayout layout = new LinearLayout(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 0);
        layout.setLayoutParams(params);
        layout.setOrientation(LinearLayout.VERTICAL);
        // layout.setBackgroundColor(getResources().getColor(R.color.yellow_150));
        // layout.setBackgroundResource(R.drawable.home_page_task_window);
        // Drawable drawable = getResources().getDrawable(R.drawable.backgraound_round_corner);
        // layout.setBackground(drawable);

        ll.addView(layout);
        parent.addView(ll);
        return layout;
    }

    private TextView makeUpcomingTextView(Task t, LinearLayout parent) {
        int height = 200;
        // Linear Layout
        LinearLayout linearLayout = new LinearLayout(getContext());
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
        layoutParams.setMargins(0, 0, 0, 0);
        linearLayout.setLayoutParams(layoutParams);

        // RadioGroup
        RadioGroup radioGroup = new RadioGroup(getContext());
        radioGroup.setLayoutParams(new RadioGroup.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, height));
        radioGroup.setOrientation(LinearLayout.HORIZONTAL);

        // CHECK BOX
        CheckBox checkbox = new CheckBox(getContext());
        // params
        LinearLayout.LayoutParams paramsCB = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsCB.gravity = Gravity.CENTER_VERTICAL;
        paramsCB.rightMargin = 50;
        checkbox.setLayoutParams(paramsCB);
        linearLayout.addView(checkbox);
        // functionality
        checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t.changeFinished();
                user.SaveFiles();
                // displayUpcomingTasks();
                goalIndex--;
                displayGoal();
            }
        });

        // TASK NAME
        TextView tv = new TextView(getContext());
        tv.setId(R.id.layout2);
        tv.setText(t.name());
        // params
        LinearLayout.LayoutParams paramsTV = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        paramsTV.setMargins(40, 0, 0, 0);
        paramsTV.gravity = Gravity.CENTER_VERTICAL;
        tv.setLayoutParams(paramsTV);
        tv.setTextColor(getResources().getColor(R.color.black));
        tv.setTextSize(20);
        linearLayout.addView(tv);
        // functionality
        radioGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddTaskActivity.openAddTaskActivity(getContext(), t);
            }
        });

        // TASK TYPE
        ImageView img = new ImageView(getContext());
        img.setImageResource(IMAGE_SOURCE[t.parentType()]);
        // params
        RadioGroup.LayoutParams paramsIMG = new RadioGroup.LayoutParams(80, 80);
        paramsIMG.gravity = Gravity.CENTER_VERTICAL;
        paramsIMG.rightMargin = 25;
        img.setLayoutParams(paramsIMG);
        radioGroup.addView(img);

        linearLayout.addView(radioGroup);
        parent.addView(linearLayout);
        return tv;
    }

    private TextView makeTextView(String text, LinearLayout parent) {
        TextView tv = new TextView(getContext());
        tv.setText(text);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(75, 20, 70, 20);
        tv.setLayoutParams(params);
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(0xFFFFFFFF);
        tv.setTextSize(20);
        parent.addView(tv);
        return tv;
    }

    private LinearLayout makeEmptyLine(LinearLayout parent, int height, int color) {
        LinearLayout empty = new LinearLayout(getContext());
        empty.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height));
        empty.setOrientation(LinearLayout.VERTICAL);
        empty.setBackgroundColor(color);
        parent.addView(empty);
        return empty;
    }
}