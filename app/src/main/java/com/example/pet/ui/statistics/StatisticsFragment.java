package com.example.pet.ui.statistics;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import java.util.Date;
import java.util.Locale;

public class StatisticsFragment extends Fragment {

    private StatisticsViewModel statisticsViewModel;
    private User user;
    private int[] IMAGE_SOURCE;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        statisticsViewModel =
                new ViewModelProvider(this).get(StatisticsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_statistics, container, false);
        LinearLayout linearLayout = root.findViewById(R.id.statistics_main_layout);

        user = User.Initialize();
        IMAGE_SOURCE = new int[]{R.drawable.education_icon, R.drawable.habbit_icon, R.drawable.sport_icon, R.drawable.work_icon};

        int currentYear = Integer.parseInt(new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date()));
        int currentMonth = Integer.parseInt(new SimpleDateFormat("M", Locale.getDefault()).format(new Date()));
        int currentDate = Integer.parseInt(new SimpleDateFormat("dd", Locale.getDefault()).format(new Date()));

        ArrayList<Goal> allGoals = user.getGoals();
        ArrayList<Goal> finishedGoals = new ArrayList<>();
        for (Goal g : allGoals) {
            if (g.isEnded(currentYear, currentMonth, currentDate)) {
                finishedGoals.add(g);
            }
        }

        if (finishedGoals.size() == 0) {
            makeTab("You have not finished any goals.", linearLayout);
        } else {
            for (Goal g : finishedGoals) {
                makeTab(g, linearLayout);
                LinearLayout innerLayout = makeInnerLayout(linearLayout);
                makeEmptyLine(innerLayout, 30);

                ArrayList<Task> tasksOfTheGoal = user.getTasks(g);
                ArrayList<Task> taskLst = new ArrayList<>();
                for (Task t : tasksOfTheGoal) {
                    if (!taskLst.contains(t.parent)) {
                        taskLst.add(t.parent);
                        makeTextView(t.name(), innerLayout);
                    }
                }
                makeEmptyLine(innerLayout, 30);
            }
        }

        //make custom empty layout
        makeEmptyLine(linearLayout, 200);

        return root;
    }

    private TextView makeTab(String text, LinearLayout parent) {
        TextView tab = new TextView(getContext());
        tab.setText(text);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(30, 30, 30, 10);
        tab.setLayoutParams(params);
        tab.setTextSize(25);
        parent.addView(tab);
        return tab;
    }

    private TextView makeTab(Goal g, LinearLayout parent) {
        LinearLayout layout = new LinearLayout(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(30, 30, 30, 10);
        layout.setLayoutParams(params);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setBackgroundColor(0x00FFFFFF);

        ImageView img = new ImageView(getContext());
        img.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
        img.setImageResource(IMAGE_SOURCE[g.type()]);
        img.setScaleType(ImageView.ScaleType.FIT_XY);
        layout.addView(img);

        TextView tv = new TextView(getContext());
        tv.setText(g.name());
        LinearLayout.LayoutParams tparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tparams.setMargins(20, 0, 0, 0);
        tv.setLayoutParams(tparams);
        tv.setTextColor(0xFF424949);
        tv.setTextSize(25);

        layout.addView(tv);
        parent.addView(layout);
        return tv;
    }

    private LinearLayout makeInnerLayout(LinearLayout parent) {
        LinearLayout layout = new LinearLayout(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(30, 0, 30, 0);
        layout.setLayoutParams(params);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundColor(0xFF7EA48F);
        Drawable drawable = getResources().getDrawable(R.drawable.backgraound_round_corner);

        layout.setBackground(drawable);

        parent.addView(layout);
        return layout;
    }

    private LinearLayout makeEmptyLine(LinearLayout parent, int height) {
        LinearLayout empty = new LinearLayout(getContext());
        empty.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height));
        empty.setOrientation(LinearLayout.VERTICAL);
        empty.setBackgroundColor(0x000000FF);
        parent.addView(empty);
        return empty;
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
}