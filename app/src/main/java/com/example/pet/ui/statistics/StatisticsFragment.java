package com.example.pet.ui.statistics;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        statisticsViewModel =
                new ViewModelProvider(this).get(StatisticsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_statistics, container, false);
        LinearLayout linearLayout = root.findViewById(R.id.statistics_main_layout);

        user = User.Initialize();
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
                makeTab(g.name(), linearLayout);
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

    public TextView makeTab(String text, LinearLayout parent) {
        TextView tab = new TextView(getContext());
        tab.setText(text);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(30, 30, 30, 10);
        tab.setLayoutParams(params);
        tab.setTextSize(25);
        parent.addView(tab);
        return tab;
    }

    public LinearLayout makeInnerLayout(LinearLayout parent) {
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

    public LinearLayout makeEmptyLine(LinearLayout parent, int height) {
        LinearLayout empty = new LinearLayout(getContext());
        empty.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height));
        empty.setOrientation(LinearLayout.VERTICAL);
        empty.setBackgroundColor(0x000000FF);
        parent.addView(empty);
        return empty;
    }

    public TextView makeTextView(String text, LinearLayout parent) {
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