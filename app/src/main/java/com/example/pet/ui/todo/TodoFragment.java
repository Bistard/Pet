package com.example.pet.ui.todo;

import android.annotation.SuppressLint;
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

import com.example.pet.R;
import com.example.pet.Task;
import com.example.pet.User;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class TodoFragment extends Fragment {

    private TodoViewModel todoViewModel;
    private User user;

    private int MAX_LINE_PER_DAY = 1000;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        todoViewModel =
                new ViewModelProvider(this).get(TodoViewModel.class);
        View root = inflater.inflate(R.layout.fragment_todo, container, false);

        LinearLayout linearLayout = root.findViewById(R.id.todo_main_layout);
//        final TextView textView = root.findViewById(R.id.text_todo);
//        todoViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });


        // user initialize
        user = User.Initialize();
        int currentYear = Integer.parseInt(new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date()));
        int currentMonth = Integer.parseInt(new SimpleDateFormat("M", Locale.getDefault()).format(new Date()));
        int currentDate = Integer.parseInt(new SimpleDateFormat("dd", Locale.getDefault()).format(new Date()));

        ArrayList<Task> unfinishedTasks = user.getUnfinishedTasks(currentYear, currentMonth, currentDate);

        if (unfinishedTasks.size() > 0) {

            TextView unfinishedTab = new TextView(getContext());
            unfinishedTab.setText("Unfinished Taskes");
            unfinishedTab.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            unfinishedTab.setTextSize(25);

            linearLayout.addView(unfinishedTab);

            LinearLayout unfinishedLayout = new LinearLayout(getContext());
            unfinishedLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            unfinishedLayout.setOrientation(LinearLayout.VERTICAL);
            unfinishedLayout.setBackgroundColor(0xFF0000FF);
            linearLayout.addView(unfinishedLayout);

            int i = MAX_LINE_PER_DAY;
            for (Task t : unfinishedTasks) {
                if (i == 0) {
                    TextView tv = new TextView(getContext());
                    tv.setText((unfinishedTasks.size() - 3) + " more...");
                    tv.setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
                    tv.setTextColor(0xFFFFFFFF);
                    unfinishedLayout.addView(tv);
                    break;
                }
                i--;
                TextView tv = new TextView(getContext());
                tv.setText(t.name() + "    due:" + t.Year() + "/" + t.Month() + "/" + t.Day());
                tv.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                tv.setTextColor(0xFFFFFFFF);
                unfinishedLayout.addView(tv);
            }
        }

        ArrayList<Task> todaysTasks = user.getTasks(currentYear, currentMonth, currentDate);
        TextView todaysTab = new TextView(getContext());
        todaysTab.setText("Today's Tasks");
        todaysTab.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        todaysTab.setTextSize(25);

        linearLayout.addView(todaysTab);

        LinearLayout todaysLayout = new LinearLayout(getContext());
        todaysLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        todaysLayout.setOrientation(LinearLayout.VERTICAL);
        todaysLayout.setBackgroundColor(0xFF0000FF);
        linearLayout.addView(todaysLayout);

        if (todaysTasks.size() == 0) {
            TextView tv = new TextView(getContext());
            tv.setText("You have finished all the tasks today!");
            tv.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            tv.setTextColor(0xFFFFFFFF);
            todaysLayout.addView(tv);
        } else {
            int i = MAX_LINE_PER_DAY;
            for (Task t : todaysTasks) {
                if (i == 0) {
                    TextView tv = new TextView(getContext());
                    tv.setText((todaysTasks.size() - 3) + " more...");
                    tv.setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
                    tv.setTextColor(0xFFFFFFFF);
                    todaysLayout.addView(tv);
                    break;
                }
                i--;
                TextView tv = new TextView(getContext());
                tv.setText(t.name() + "    due:" + t.Year() + "/" + t.Month() + "/" + t.Day());
                tv.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                tv.setTextColor(0xFFFFFFFF);
                todaysLayout.addView(tv);
            }
        }

        for (int d = 1; d <= 7; d++) {
            ArrayList<Task> theTasks = user.getTasks(currentYear, currentMonth, currentDate+d);
            TextView theTab = new TextView(getContext());
            theTab.setText(d+" Days Later");
            theTab.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            theTab.setTextSize(25);

            linearLayout.addView(theTab);

            LinearLayout theLayout = new LinearLayout(getContext());
            theLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            theLayout.setOrientation(LinearLayout.VERTICAL);
            theLayout.setBackgroundColor(0xFF0000FF);
            linearLayout.addView(theLayout);

            if (theTasks.size() == 0) {
                TextView tv = new TextView(getContext());
                tv.setText("You have finished all the tasks today!");
                tv.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                tv.setTextColor(0xFFFFFFFF);
                theLayout.addView(tv);
            } else {
                int i = MAX_LINE_PER_DAY;
                for (Task t : theTasks) {
                    if (i == 0) {
                        TextView tv = new TextView(getContext());
                        tv.setText((theTasks.size() - 3) + " more...");
                        tv.setLayoutParams(new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));
                        tv.setTextColor(0xFFFFFFFF);
                        theLayout.addView(tv);
                        break;
                    }
                    i--;
                    TextView tv = new TextView(getContext());
                    tv.setText(t.name() + "    due:" + t.Year() + "/" + t.Month() + "/" + t.Day());
                    tv.setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
                    tv.setTextColor(0xFFFFFFFF);
                    theLayout.addView(tv);
                }
            }
        }

        return root;
    }
}