package com.example.pet.ui.todo;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.pet.AddTaskActivity;
import com.example.pet.R;
import com.example.pet.Task;
import com.example.pet.User;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class TodoFragment extends Fragment {

    private TodoViewModel todoViewModel;
    private User user;

    private final int MAX_LINE_PER_DAY = 3;
    private final int NUMBER_OF_DAYS = 7;

    private int[] IMAGE_SOURCE;

    private final int[] backgroundSource = {R.drawable.background_round_color_0, R.drawable.background_round_color_1,
                                            R.drawable.background_round_color_2, R.drawable.background_round_color_3,
                                            R.drawable.background_round_color_4, R.drawable.background_round_color_5,
                                            R.drawable.background_round_color_6, R.drawable.background_round_color_7};
    private int prevBackground = -1;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        todoViewModel =
                new ViewModelProvider(this).get(TodoViewModel.class);
        View root = inflater.inflate(R.layout.fragment_todo, container, false);

        LinearLayout linearLayout = root.findViewById(R.id.todo_main_layout);

        // user initialize
        user = User.Initialize();
        IMAGE_SOURCE = new int[]{R.drawable.ic_baseline_education_24, R.drawable.ic_baseline_habbit_24, R.drawable.ic_baseline_sport_24, R.drawable.ic_baseline_work_24};

        int totalTasks = 0;
        int currentYear = Integer.parseInt(new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date()));
        int currentMonth = Integer.parseInt(new SimpleDateFormat("M", Locale.getDefault()).format(new Date()));
        int currentDate = Integer.parseInt(new SimpleDateFormat("dd", Locale.getDefault()).format(new Date()));

        // Unfinished Task Display
        ArrayList<Task> unfinishedTasks = user.getUnfinishedTasks(currentYear, currentMonth, currentDate);
        int unfinishedTasksSize = unfinishedTasks.size();
        if (unfinishedTasksSize > 0) {
            makeTab("Overdue", linearLayout);
            LinearLayout unfinishedLayout = makeInnerLayout(linearLayout);

            makeEmptyLine(unfinishedLayout, 30);

            Comparator<Task> compareTask = new Comparator<Task>() {
                public int compare(Task t1, Task t2) {
                    int date1 = t1.Year() * 10000 + t1.Month() * 100 + t1.Day();
                    int date2 = t2.Year() * 10000 + t2.Month() * 100 + t2.Day();
                    return date1 - date2;
                }
            };
            Collections.sort(unfinishedTasks, compareTask);

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

        // Today's Task Display
        ArrayList<Task> todayTasks = user.getTasks(currentYear, currentMonth, currentDate);
        int todayTasksSize = todayTasks.size();
        makeTab("Today", linearLayout);

        LinearLayout todaysLayout = makeInnerLayout(linearLayout);

        makeEmptyLine(todaysLayout, 30);
        if (todayTasksSize == 0) {
            makeTextView("You have no task today!", todaysLayout);
        } else {
            int i = MAX_LINE_PER_DAY;
            for (Task t : todayTasks) {
                if (i == 0) {
                    makeTextView(new ArrayList<Task>(todayTasks.subList(MAX_LINE_PER_DAY, todayTasks.size())), todaysLayout);
                    break;
                }
                i--;
                makeTextView(t, todaysLayout);
            }
        }
        makeEmptyLine(todaysLayout, 30);

        // Tomorrow's Task Display
        int tomorrowDate = Task.incrementDate(currentYear * 10000 + currentMonth * 100 + currentDate, 1);
        int tomorrowYear = tomorrowDate / 10000;
        int tomorrowMonth = (tomorrowDate % 10000) / 100;
        int tomorrowDay = tomorrowDate % 100;
        ArrayList<Task> tomorrowTasks = user.getTasks(tomorrowYear, tomorrowMonth, tomorrowDay);
        int tomorrowTasksSize = tomorrowTasks.size();
        makeTab("Tomorrow", linearLayout);

        LinearLayout tomorrowsLayout = makeInnerLayout(linearLayout);

        makeEmptyLine(tomorrowsLayout, 30);
        if (tomorrowTasksSize == 0) {
            makeTextView("You have no task tomorrow!", tomorrowsLayout);
        } else {
            int i = MAX_LINE_PER_DAY;
            for (Task t : tomorrowTasks) {
                if (i == 0) {
                    makeTextView(new ArrayList<Task>(tomorrowTasks.subList(MAX_LINE_PER_DAY, todayTasks.size())), tomorrowsLayout);
                    break;
                }
                i--;
                makeTextView(t, tomorrowsLayout);
            }
        }
        makeEmptyLine(tomorrowsLayout, 30);

        for (int d = 2; d <= NUMBER_OF_DAYS; d++) {
            int searchDate = Task.incrementDate(currentYear * 10000 + currentMonth * 100 + currentDate, d);
            int searchYear = searchDate / 10000;
            int searchMonth = (searchDate % 10000) / 100;
            int searchDay = searchDate % 100;

            ArrayList<Task> theTasks = user.getTasks(searchYear, searchMonth, searchDay);
            int theTasksSize = theTasks.size();
            totalTasks += theTasksSize;
            makeTab(com.example.pet.Date.makeDateString(searchYear, searchMonth, searchDay), linearLayout);

            LinearLayout theLayout = makeInnerLayout(linearLayout);
            makeEmptyLine(theLayout, 30);
            if (theTasksSize == 0) {
                makeTextView("You have nothing left to do!", theLayout);
            } else{
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

        // make custom empty layout
        makeEmptyLine(linearLayout, 200);

        // update stat_overdue_number/stat_today_number/stat_tomorrow_number/stat_unfinished_number
        TextView overdue = root.findViewById(R.id.stat_overdue_number);
        overdue.setText(String.valueOf(unfinishedTasksSize));

        TextView today = root.findViewById(R.id.stat_today_number);
        today.setText(String.valueOf(todayTasksSize));

        TextView tomorrow = root.findViewById(R.id.stat_tomorrow_number);
        tomorrow.setText(String.valueOf(tomorrowTasksSize));

        TextView unfinished = root.findViewById(R.id.stat_todo_number);
        unfinished.setText(String.valueOf(unfinishedTasksSize+todayTasksSize+tomorrowTasksSize+totalTasks));

        return root;
    }

    private TextView makeTab(String text, LinearLayout parent) {
        // tab
        TextView tab = new TextView(getContext());
        tab.setText(text);
        tab.setTextSize(20);
        tab.setTextColor(getResources().getColor(R.color.black));
        // font
        Typeface font = ResourcesCompat.getFont(getContext(), R.font.roboto);
        tab.setTypeface(font);
        // params
        LinearLayout.LayoutParams paramsTAB = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsTAB.setMargins(30, 30, 30, 10);
        tab.setLayoutParams(paramsTAB);

        parent.addView(tab);
        return tab;
    }

    /*
    build a container for all the relevant tasks
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private LinearLayout makeInnerLayout(LinearLayout parent) {
        Random rand = new Random();
        // BACKGROUND LINEAR LAYOUT
        LinearLayout bglayout = new LinearLayout(getContext());
        // random color displayed
        int curBackground = backgroundSource[rand.nextInt(backgroundSource.length)];
        while (curBackground == prevBackground) {
            curBackground = backgroundSource[rand.nextInt(backgroundSource.length)];
        }
        prevBackground = curBackground;
        bglayout.setBackground(getResources().getDrawable(curBackground));
        bglayout.setElevation(20);
        // params
        LinearLayout.LayoutParams paramsBG = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsBG.setMargins(30, 0, 30, 0);
        bglayout.setLayoutParams(paramsBG);

        // SMALLER LINEAR LAYOUT
        LinearLayout layout = new LinearLayout(getContext());
        // setting
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackground(getResources().getDrawable(R.drawable.backgraound_round_corner));
        // params
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(15, 0, 0, 0);
        layout.setLayoutParams(params);

        bglayout.addView(layout);
        parent.addView(bglayout);
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
        tv.setTextColor(getResources().getColor(R.color.black));
        tv.setTextSize(20);
        parent.addView(tv);
        return tv;
    }

    private TextView makeTextView(Task t, LinearLayout parent) {
        LinearLayout layout = new LinearLayout(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(75, 20, 70, 20);
        layout.setLayoutParams(params);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setBackgroundColor(0x00FFFFFF);

        ImageView checkmark = new ImageView(getContext());
        checkmark.setLayoutParams(new LinearLayout.LayoutParams(80, 80));
        if (t.isFininshed()) {
            checkmark.setImageResource(R.drawable.ic_baseline_indeterminate_check_box_24);
        } else {
            checkmark.setImageResource(R.drawable.ic_baseline_check_box_24);
        }
        checkmark.setScaleType(ImageView.ScaleType.FIT_XY);
        layout.addView(checkmark);

        ImageView img = new ImageView(getContext());
        img.setLayoutParams(new LinearLayout.LayoutParams(80, 80));
        img.setImageResource(IMAGE_SOURCE[t.parentType()]);
        img.setScaleType(ImageView.ScaleType.FIT_XY);
        layout.addView(img);

        TextView tv = new TextView(getContext());
        tv.setText(t.name() + " ");
        LinearLayout.LayoutParams tparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tparams.setMargins(20, 0, 0, 0);
        tv.setLayoutParams(tparams);
        tv.setTextColor(getResources().getColor(R.color.black_50));
        tv.setTextSize(16);
        // font
        Typeface font = ResourcesCompat.getFont(getContext(), R.font.roboto_light);
        tv.setTypeface(font);

        if (t.isFininshed()) {
            tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddTaskActivity.openAddTaskActivity(getContext(), t);
            }
        });

        checkmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t.changeFinished();
                user.SaveFiles();
                if (t.isFininshed()) {
                    tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    checkmark.setImageResource(R.drawable.ic_baseline_indeterminate_check_box_24);
                } else {
                    checkmark.setImageResource(R.drawable.ic_baseline_check_box_24);
                    tv.setPaintFlags(tv.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                }
            }
        });

        layout.addView(tv);
        parent.addView(layout);
        return tv;
    }

    private TextView makeTextView(ArrayList<Task> lst, LinearLayout parent) {
        LinearLayout layout = new LinearLayout(getContext());
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundColor(0x00FFFFFF);

        TextView tv = new TextView(getContext());
        tv.setText(lst.size() + " more tasks...");
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(75, 20, 70, 20);
        tv.setLayoutParams(params);
        tv.setTextColor(getResources().getColor(R.color.black));
        tv.setTextSize(16);
        // font
        Typeface font = ResourcesCompat.getFont(getContext(), R.font.roboto);
        tv.setTypeface(font);

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