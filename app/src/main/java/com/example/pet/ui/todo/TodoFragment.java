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

    private int MAX_LINE_PER_DAY = 10;

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
            makeTab("Unfinished Tasks", linearLayout);
            LinearLayout unfinishedLayout = makeInnerLayout(linearLayout);

            int i = MAX_LINE_PER_DAY;
            for (Task t : unfinishedTasks) {
                if (i == 0) {
                    makeTextView((unfinishedTasks.size() - 3) + " more...", unfinishedLayout);
                    break;
                }
                i--;
                makeTextView(t.name() + "    due:" + t.Year() + "/" + t.Month() + "/" + t.Day(), unfinishedLayout);
            }
        }

        ArrayList<Task> todaysTasks = user.getTasks(currentYear, currentMonth, currentDate);
        makeTab("Today's Tasks",linearLayout);

        LinearLayout todaysLayout = makeInnerLayout(linearLayout);

        if (todaysTasks.size() == 0) {
            makeTextView("You have finished all the tasks today!",todaysLayout);
        } else {
            int i = MAX_LINE_PER_DAY;
            for (Task t : todaysTasks) {
                if (i == 0) {
                    makeTextView((todaysTasks.size() - 3) + " more...",todaysLayout);
                    break;
                }
                i--;
                makeTextView(t.name() + "    due:" + t.Year() + "/" + t.Month() + "/" + t.Day(),todaysLayout);
            }
        }

        for (int d = 1; d <= 7; d++) {
            ArrayList<Task> theTasks = user.getTasks(currentYear, currentMonth, currentDate + d);
            makeTab(d + " Days Later",linearLayout);

            LinearLayout theLayout = makeInnerLayout(linearLayout);

            if (theTasks.size() == 0) {
                makeTextView("You have finished all the tasks today!",theLayout);
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