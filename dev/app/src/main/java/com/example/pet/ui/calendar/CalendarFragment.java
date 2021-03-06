package com.example.pet.ui.calendar;

import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

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
import com.example.pet.ui.home.HomeViewModel;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class CalendarFragment extends Fragment {

    private CalendarViewModel calendarViewModel;
    private View root;
    private User user;
    private int currentYear, currentMonth, currentDate, displayYear, displayMonth, displayDate;
    private TextView top_date_text;
    private TextView[] table_text;
    private LinearLayout[] table_text_frame;
    private LinearLayout[] table_text_task;

    private int[] IMAGE_SOURCE;
    private int[] TASK_ROUND_ICON;


    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        calendarViewModel =
                new ViewModelProvider(this).get(CalendarViewModel.class);
        root = inflater.inflate(R.layout.fragment_calendar, container, false);

        user = User.Initialize();
        IMAGE_SOURCE = new int[]{R.drawable.ic_baseline_education_24, R.drawable.ic_baseline_habbit_24, R.drawable.ic_baseline_sport_24, R.drawable.ic_baseline_work_24};
        TASK_ROUND_ICON = new int[]{R.drawable.background_round_color_education, R.drawable.background_round_color_habbit, R.drawable.background_round_color_sport, R.drawable.background_round_color_work};

        currentYear = Integer.parseInt(new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date()));
        currentMonth = Integer.parseInt(new SimpleDateFormat("M", Locale.getDefault()).format(new Date()));
        currentDate = Integer.parseInt(new SimpleDateFormat("dd", Locale.getDefault()).format(new Date()));

        displayYear = currentYear;
        displayMonth = currentMonth;
        displayDate = currentDate;

        Resources r = getResources();

        TableLayout table = root.findViewById(R.id.calendar_table);

        top_date_text = root.findViewById(R.id.calendar_top_date);
        root.findViewById(R.id.calendar_top_left_arrow).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if (displayMonth == 1) {
                    displayMonth = 12;
                    displayYear--;
                } else {
                    displayMonth--;
                }
                updateTable();
                updateBottomScrollView();
            }
        });
        root.findViewById(R.id.calendar_top_right_arrow).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if (displayMonth == 12) {
                    displayMonth = 1;
                    displayYear++;
                } else {
                    displayMonth++;
                }
                updateTable();
                updateBottomScrollView();
            }
        });

        table_text = new TextView[35];
        table_text_frame = new LinearLayout[35];
        table_text_task = new LinearLayout[35];
        for (int _i = 0; _i < 5; _i++) {
            for (int _j = 0; _j < 7; _j++) {
                int i = _i;
                int j = _j;
                table_text[i * 7 + j] = root.findViewById(r.getIdentifier("calendar_table_text_" + i + j, "id", getContext().getPackageName()));
                table_text_frame[i * 7 + j] = root.findViewById(r.getIdentifier("calendar_table_text_frame_" + i + j, "id", getContext().getPackageName()));
                table_text_frame[i * 7 + j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateDisplayDate(i, j);
                    }
                });
                table_text_task[i * 7 + j] = root.findViewById(r.getIdentifier("calendar_table_task_" + i + j, "id", getContext().getPackageName()));
            }
        }

        updateTable();
        updateBottomScrollView();
        int[] c = {0, 1, 2, 3};
        return root;
    }

    private void showDots(LinearLayout parentLayout, int[] colors) {
        for (int color : colors) {
            TextView tv = new TextView(getContext());
            tv.setBackground(getResources().getDrawable(TASK_ROUND_ICON[color]));
            tv.setText("");
            LinearLayout.LayoutParams paramsDOT = new LinearLayout.LayoutParams(12, 12);
            paramsDOT.leftMargin = 5;
            tv.setLayoutParams(paramsDOT);

            parentLayout.addView(tv);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateTable() {
        top_date_text.setText(com.example.pet.Date.toFullStrMonthFormat(displayMonth) + " " + displayYear);

        int start_day = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", java.util.Locale.ENGLISH);
            Date myDate = sdf.parse("01/" + displayMonth + "/" + displayYear);
            start_day = myDate.getDay();
        } catch (Exception e) {
            start_day = 0;
        }
        int indx = 0;
        for (; indx < start_day; indx++) {
            table_text_frame[indx].setBackgroundColor(0x00000000);
            table_text[indx].setText("");
            table_text_task[indx].removeAllViews();
        }
        int dayNum = 1;
        for (; indx < start_day + YearMonth.of(displayYear, displayMonth).lengthOfMonth() && indx < 35; indx++) {
            table_text_frame[indx].setBackgroundColor(0x00000000);

            if (displayYear == currentYear && displayMonth == currentMonth && dayNum == currentDate) {
                Drawable drawable = getResources().getDrawable(R.drawable.calendar_date);
                table_text_frame[indx].setBackground(drawable);
            }
            if (dayNum == displayDate) {
                Drawable drawable = getResources().getDrawable(R.drawable.calendar_date_2);
                table_text_frame[indx].setBackground(drawable);
            }
            table_text[indx].setText("" + dayNum);

            table_text_task[indx].removeAllViews();
            ArrayList<Task> theseTasks = user.getTasks(displayYear, displayMonth, dayNum);
            int[] color = new int[Math.min(theseTasks.size(), 4)];
            for (int i = 0; i < Math.min(theseTasks.size(), 4); i++) {
                color[i] = theseTasks.get(i).parentType();
            }
            showDots(table_text_task[indx], color);

            dayNum++;
        }
        for (; indx < 35; indx++) {
            table_text_frame[indx].setBackgroundColor(0x00000000);
            table_text[indx].setText("");
            table_text_task[indx].removeAllViews();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateBottomScrollView() {
        // bottom-left date display
        TextView bottom_month = root.findViewById(R.id.calendar_bottom_month);
        bottom_month.setText(com.example.pet.Date.toStrMonthFormat(displayMonth));
        TextView bottom_day = root.findViewById(R.id.calendar_bottom_day);
        bottom_day.setText(String.valueOf(displayDate));


        LinearLayout taskLayout = root.findViewById(R.id.calendar_bottom_main_layout);
        taskLayout.removeAllViews();
        ArrayList<Task> tasks = user.getTasks(displayYear, displayMonth, displayDate);
        // display each task
        int WINDOW_NUMBER = tasks.size();
        if (WINDOW_NUMBER != 0) {
            makeEmptyLine(taskLayout, 5, getResources().getColor(R.color.grey_50));
            for (int i = 0; i < WINDOW_NUMBER; i++) {
                makeUpcomingTextView(tasks.get(i), makeInnerLayout(taskLayout));
                makeEmptyLine(taskLayout, 5, getResources().getColor(R.color.grey_50));
            }
            //make custom empty layout
            makeEmptyLine(taskLayout, 50, getResources().getColor(R.color.white_transparent));
        } else {
            makeTextView("You don't have anything for this date.", makeInnerLayout(taskLayout));
        }
        // bottom-right # of tasks display
        TextView bottom_tasks_num = root.findViewById(R.id.calendar_bottom_tasks_num);
        bottom_tasks_num.setText("+" + String.valueOf(WINDOW_NUMBER) + " " + "tasks");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateDisplayDate(int i, int j) {
        if (!table_text[i * 7 + j].getText().equals("")) {
            displayDate = Integer.parseInt((String) table_text[i * 7 + j].getText());
        }
        updateBottomScrollView();
        updateTable();
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private TextView makeUpcomingTextView(Task t, LinearLayout parent) {
        int height = 200;
        // Linear Layout
        LinearLayout linearLayout = new LinearLayout(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
        layoutParams.setMargins(0, 0, 0, 0);
        linearLayout.setLayoutParams(layoutParams);

        // CONTAINER (TASK NAME & DEADLINE)
        LinearLayout container = new LinearLayout(getContext());
        LinearLayout.LayoutParams paramsCONTAINER = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height, 1);
        paramsCONTAINER.setMargins(0, 0, 0, 0);
        container.setLayoutParams(paramsCONTAINER);
        container.setOrientation(LinearLayout.VERTICAL);

        // TASK NAME
        TextView tv = new TextView(getContext());
        tv.setText(t.name());
        // font
        Typeface font = ResourcesCompat.getFont(getContext(), R.font.roboto_light);
        tv.setTypeface(font);
        tv.setTextColor(getResources().getColor(R.color.black));
        tv.setTextSize(18);
        // params
        LinearLayout.LayoutParams paramsTV = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        paramsTV.setMargins(0, 0, 0, 0);
        paramsTV.gravity = Gravity.CENTER_VERTICAL;
        tv.setLayoutParams(paramsTV);
        container.addView(tv);
        // functionality
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddTaskActivity.openAddTaskActivity(getContext(), t);
            }
        });

        // DEADLINE
        TextView ddl = new TextView(getContext());
        ddl.setText(com.example.pet.Date.makeDateString(t.Year(), t.Month(), t.Day()));
        ddl.setTextColor(getResources().getColor(R.color.grey));
        ddl.setTextSize(12);
        // params
        LinearLayout.LayoutParams paramsDDL = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        paramsDDL.setMargins(0, 0, 0, 0);
        paramsDDL.gravity = Gravity.CENTER_VERTICAL;
        ddl.setLayoutParams(paramsDDL);
        container.addView(ddl);
        linearLayout.addView(container);

        // TASK TYPE
        ImageView img = new ImageView(getContext());
        img.setImageResource(IMAGE_SOURCE[t.parentType()]);
        // params
        RadioGroup.LayoutParams paramsIMG = new RadioGroup.LayoutParams(80, 80);
        paramsIMG.gravity = Gravity.CENTER_VERTICAL;
        paramsIMG.rightMargin = 25;
        img.setLayoutParams(paramsIMG);
        linearLayout.addView(img);

        parent.addView(linearLayout);
        return tv;
    }

    private TextView makeTextView(String text, LinearLayout parent) {
        TextView tv = new TextView(getContext());
        tv.setText(text);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        params.setMargins(75, 20, 70, 20);
        tv.setLayoutParams(params);
        tv.setGravity(Gravity.CENTER);
        // font
        Typeface font = ResourcesCompat.getFont(getContext(), R.font.roboto_light);
        tv.setTypeface(font);
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