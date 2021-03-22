package com.example.pet.ui.todo;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.graphics.Bitmap;

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

    private final int MAX_LINE_PER_DAY = 3;
    private final int NUMBER_OF_DAYS = 7;

    private int[] IMAGE_SOURCE;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        todoViewModel =
                new ViewModelProvider(this).get(TodoViewModel.class);
        View root = inflater.inflate(R.layout.fragment_todo, container, false);

        LinearLayout linearLayout = root.findViewById(R.id.todo_main_layout);

        // user initialize
        user = User.Initialize();
        IMAGE_SOURCE = new int[]{R.drawable.education_icon, R.drawable.habbit_icon, R.drawable.sport_icon, R.drawable.work_icon};

        int currentYear = Integer.parseInt(new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date()));
        int currentMonth = Integer.parseInt(new SimpleDateFormat("M", Locale.getDefault()).format(new Date()));
        int currentDate = Integer.parseInt(new SimpleDateFormat("dd", Locale.getDefault()).format(new Date()));

        ArrayList<Task> unfinishedTasks = user.getUnfinishedTasks(currentYear, currentMonth, currentDate);

        if (unfinishedTasks.size() > 0) {
            makeTab("Unfinished Tasks", linearLayout);
            LinearLayout unfinishedLayout = makeInnerLayout(linearLayout);

            makeEmptyLine(unfinishedLayout, 30);
            int i = MAX_LINE_PER_DAY;
            for (Task t : unfinishedTasks) {
                if (i == 0) {
                    makeTextView(new ArrayList<Task>(unfinishedTasks.subList(MAX_LINE_PER_DAY, unfinishedTasks.size())), unfinishedLayout);
                    break;
                }
                i--;
                makeTextView(t, unfinishedLayout);
            }
            makeEmptyLine(unfinishedLayout, 30);
        }

        ArrayList<Task> todaysTasks = user.getTasks(currentYear, currentMonth, currentDate);
        makeTab("Today's Tasks", linearLayout);

        LinearLayout todaysLayout = makeInnerLayout(linearLayout);

        makeEmptyLine(todaysLayout, 30);
        if (todaysTasks.size() == 0) {
            makeTextView("You have finished all the tasks today!", todaysLayout);
        } else {
            int i = MAX_LINE_PER_DAY;
            for (Task t : todaysTasks) {
                if (i == 0) {
                    makeTextView(new ArrayList<Task>(todaysTasks.subList(MAX_LINE_PER_DAY, todaysTasks.size())), todaysLayout);
                    break;
                }
                i--;
                makeTextView(t, todaysLayout);
            }
        }
        makeEmptyLine(todaysLayout, 30);

        for (int d = 1; d <= NUMBER_OF_DAYS; d++) {
            int searchDate = Task.incrementDate(currentYear * 10000 + currentMonth * 100 + currentDate, d);
            int searchYear = searchDate / 10000;
            int searchMonth = (searchDate % 10000) / 100;
            int searchDay = searchDate % 100;

            ArrayList<Task> theTasks = user.getTasks(searchYear, searchMonth, searchDay);
            makeTab(searchYear + "/" + searchMonth + "/" + searchDay, linearLayout);

            LinearLayout theLayout = makeInnerLayout(linearLayout);
            makeEmptyLine(theLayout, 30);
            if (theTasks.size() == 0) {
                makeTextView("You have finished all the tasks today!", theLayout);
            } else {
                int i = MAX_LINE_PER_DAY;
                for (Task t : theTasks) {
                    if (i == 0) {
                        makeTextView(new ArrayList<Task>(theTasks.subList(MAX_LINE_PER_DAY, theTasks.size())), theLayout);
                        break;
                    }
                    i--;
                    makeTextView(t, theLayout);
                }
            }
            makeEmptyLine(theLayout, 30);
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

    public TextView makeTextView(Task t, LinearLayout parent) {
        LinearLayout layout = new LinearLayout(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(75, 20, 70, 20);
        layout.setLayoutParams(params);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setBackgroundColor(0x00FFFFFF);

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

    public TextView makeTextView(ArrayList<Task> lst, LinearLayout parent) {
        LinearLayout layout = new LinearLayout(getContext());
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundColor(0x00FFFFFF);

        TextView tv = new TextView(getContext());
        tv.setText(lst.size() + " more tasks...");
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(75, 20, 70, 20);
        tv.setLayoutParams(params);
        tv.setTextColor(0xFFFFFFFF);
        tv.setTextSize(20);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setVisibility(View.GONE);
                for (Task t : lst) {
                    makeTextView(t, layout);
                }
            }
        });

        layout.addView(tv);
        parent.addView(layout);
        return tv;
    }
}