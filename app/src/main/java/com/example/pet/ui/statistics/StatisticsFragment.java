package com.example.pet.ui.statistics;

import android.os.Bundle;
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
//        final TextView textView = root.findViewById(R.id.text_statistics);
//        statisticsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        user = User.Initialize();
        int currentYear = Integer.parseInt(new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date()));
        int currentMonth = Integer.parseInt(new SimpleDateFormat("M", Locale.getDefault()).format(new Date()));
        int currentDate = Integer.parseInt(new SimpleDateFormat("dd", Locale.getDefault()).format(new Date()));

        ArrayList<Goal> finishedGoals = user.getGoals();
        for (Goal g : finishedGoals) {
            if (g.isEnded(currentYear, currentMonth, currentDate)) {
                makeTab(g.name(), linearLayout);
                LinearLayout innerLayout = makeInnerLayout(linearLayout);
                ArrayList<Task> tasksOfTheGoal = user.getTasks(g);
                for (Task t : tasksOfTheGoal) {
                    makeTextView(t.name() + " " + t.Year() + "/" + t.Month() + "/" + t.Day(), innerLayout);
                }
            }
        }


        return root;
    }

    public TextView makeTab(String text, LinearLayout parent) {
        TextView tab = new TextView(getContext());
        tab.setText(text);
        tab.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        tab.setTextSize(25);

        parent.addView(tab);
        return tab;
    }

    public LinearLayout makeInnerLayout(LinearLayout parent) {
        LinearLayout layout = new LinearLayout(getContext());
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundColor(0xFF0000FF);

        parent.addView(layout);
        return layout;
    }

    public TextView makeTextView(String text, LinearLayout parent) {
        TextView tv = new TextView(getContext());
        tv.setText(text);
        tv.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        tv.setTextColor(0xFFFFFFFF);

        parent.addView(tv);
        return tv;
    }
}