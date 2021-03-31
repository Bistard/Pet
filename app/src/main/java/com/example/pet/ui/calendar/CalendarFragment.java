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
    private RelativeLayout[] table_layout;

    private int[] IMAGE_SOURCE;
    private final int TABLE_X_MAX = 140;
    private final int TABLE_Y_MAX = 160;
    private Random random;


    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        calendarViewModel =
                new ViewModelProvider(this).get(CalendarViewModel.class);
        root = inflater.inflate(R.layout.fragment_calendar, container, false);

        user = User.Initialize();
        IMAGE_SOURCE = new int[]{R.drawable.ic_baseline_education_24, R.drawable.ic_baseline_habbit_24, R.drawable.ic_baseline_sport_24, R.drawable.ic_baseline_work_24};
        random = new Random();

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
        table_layout = new RelativeLayout[35];
        for (int _i = 0; _i < 5; _i++) {
            for (int _j = 0; _j < 7; _j++) {
                int i = _i;
                int j = _j;
                table_text[i * 7 + j] = root.findViewById(r.getIdentifier("calendar_table_text_" + i + j, "id", getContext().getPackageName()));
                table_text[i * 7 + j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateDisplayDate(i, j);
                    }
                });
                table_layout[i * 7 + j] = root.findViewById(r.getIdentifier("calendar_table_layout_" + i + j, "id", getContext().getPackageName()));
                table_layout[i * 7 + j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateDisplayDate(i, j);
                    }
                });
            }
        }

        updateTable();
        updateBottomScrollView();

        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateTable() {
        top_date_text.setText(User.month2string(displayMonth) + " " + displayYear);

        int start_day = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", java.util.Locale.ENGLISH);
            Date myDate = sdf.parse("01/" + displayMonth + "/" + displayYear);
            start_day = myDate.getDay();
        } catch (Exception e) {
            start_day = 0;
        }
//        YearMonth yearMonthObject = YearMonth.of(displayYear, displayMonth);
//        int daysInMonth = yearMonthObject.lengthOfMonth();
//        table_text[0].setText("" + start_day);
//        table_text[1].setText("" + daysInMonth);
        int indx = 0;
        for (; indx < start_day; indx++) {
            table_text[indx].setBackgroundColor(0x00000000);
            table_text[indx].setText("");
            table_layout[indx].removeAllViews();
        }
        int dayNum = 1;
        for (; indx < start_day + YearMonth.of(displayYear, displayMonth).lengthOfMonth() && indx < 35; indx++) {
            table_text[indx].setBackgroundColor(0x00000000);
            if(dayNum==displayDate){
                Drawable drawable = getResources().getDrawable(R.drawable.calendar_date_2);
                table_text[indx].setBackground(drawable);
            }
            if (displayYear == currentYear && displayMonth == currentMonth && dayNum == currentDate) {
                Drawable drawable = getResources().getDrawable(R.drawable.calendar_date);
                table_text[indx].setBackground(drawable);
            }
            table_text[indx].setText("" + dayNum);

            table_layout[indx].removeAllViews();
            ArrayList<Task> theseTasks = user.getTasks(displayYear, displayMonth, dayNum);
            int maxRadius = maxRadiusSelection(TABLE_X_MAX, TABLE_Y_MAX, theseTasks.size());
            int[][] circles = new int[theseTasks.size()][3];
            boolean made_circle;
            for (int i = 0; i < theseTasks.size(); i++) {
                made_circle = false;
                for (int iteration = 0; iteration < 100; iteration++) {
                    // TODO: optimize intensity
                    int x = curvedRandom(0, TABLE_X_MAX, 0, 5.0);
                    int y = curvedRandom(0, TABLE_Y_MAX, 0, 5.0);
                    int r = curvedRandom(5, maxRadius, 1, 3.0);
                    boolean can_make_circle = true;
                    for (int j = 0; j < i; j++) {
                        double d = Math.pow((x - circles[j][0]) * (x - circles[j][0]) + (y - circles[j][1]) * (y - circles[j][1]), 0.5);
                        if (d < r + circles[j][2]) {
                            can_make_circle = false;
                            break;
                        }
                    }
                    if (can_make_circle) {
                        circles[i][0] = x;
                        circles[i][1] = y;
                        circles[i][2] = r;
                        Task t = theseTasks.get(i);
                        circle(x, y, r, IMAGE_SOURCE[t.parentType()], table_layout[indx]);
                        made_circle = true;
                        break;
                    }
                }
                if (!made_circle) {
                    circles[i][0] = curvedRandom(0, TABLE_X_MAX, 0, 2.0);
                    circles[i][1] = curvedRandom(0, TABLE_Y_MAX, 0, 2.0);
                    circles[i][2] = curvedRandom(5, maxRadius / 2, 1, 4.0);
                    Task t = theseTasks.get(i);
                    circle(circles[i][0], circles[i][1], circles[i][2], IMAGE_SOURCE[t.parentType()], table_layout[indx]);
                }
            }

            dayNum++;
        }
        for (; indx < 35; indx++) {
            table_text[indx].setBackgroundColor(0x00000000);
            table_text[indx].setText("");
            table_layout[indx].removeAllViews();
        }
    }

    private ImageView circle(int x, int y, int radius, int source, RelativeLayout layout) {
        ImageView img = new ImageView(getContext());
        img.setLayoutParams(new LinearLayout.LayoutParams(radius * 2, radius * 2));
        img.setImageResource(source);
        img.setScaleType(ImageView.ScaleType.FIT_XY);
        img.setTranslationX(x - radius);
        img.setTranslationY(y - radius);
        layout.addView(img);
        return img;
    }

    /**
     * @param min
     * @param max
     * @param centerPosition -1 for center at min, 0 for center at center, +1 for center at max
     * @param intensity
     * @return
     */
    private int curvedRandom(int min, int max, int centerPosition, double intensity) {
        double randDouble;
        if (centerPosition == 0) {
            randDouble = random.nextGaussian();
            if (randDouble < -1 * intensity) {
                randDouble = -1 * intensity;
            } else if (randDouble > intensity) {
                randDouble = intensity;
            }
            return (int) ((max - min) * 0.5 / intensity * randDouble + (max + min) * 0.5);
        } else if (centerPosition < 0) {
            randDouble = curvedRandom(min - (max - min), max, 0, intensity);
            if (randDouble < min) {
                randDouble = (double) min + (double) min - randDouble;
            }
            return (int) randDouble;
        } else {
            randDouble = curvedRandom(min, max + (max - min), 0, intensity);
            if (randDouble > max) {
                randDouble = (double) max - (randDouble - (double) max);
            }
            return (int) randDouble;
        }
    }

    //TODO: change max size selection method
    private int maxRadiusSelection(int width, int height, int numberOfTasks) {
        return (int) ((double) width / 2.0 / numberOfTasks);
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
        TextView bottom_tasks_num =root.findViewById(R.id.calendar_bottom_tasks_num);
        bottom_tasks_num.setText(String.valueOf(WINDOW_NUMBER) + " " + "tasks on that day");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateDisplayDate(int i, int j) {
        if (!table_text[i * 7 + j].getText().equals("")) {
            displayDate = Integer.parseInt((String) table_text[i * 7 + j].getText());
        }
        updateBottomScrollView();

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
            table_text[indx].setBackgroundColor(0x00000000);
        }
        int dayNum = 1;
        for (; indx < start_day + YearMonth.of(displayYear, displayMonth).lengthOfMonth() && indx < 35; indx++) {
            table_text[indx].setBackgroundColor(0x00000000);
            if(dayNum==displayDate){
                Drawable drawable = getResources().getDrawable(R.drawable.calendar_date_2);
                table_text[indx].setBackground(drawable);
            }
            if (displayYear == currentYear && displayMonth == currentMonth && dayNum == currentDate) {
                Drawable drawable = getResources().getDrawable(R.drawable.calendar_date);
                table_text[indx].setBackground(drawable);
            }
            dayNum++;
        }
        for (; indx < 35; indx++) {
            table_text[indx].setBackgroundColor(0x00000000);
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private TextView makeUpcomingTextView(Task t, LinearLayout parent) {
        int height = 200;
        // Linear Layout
        LinearLayout linearLayout = new LinearLayout(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
        layoutParams.setMargins(0, 0, 0, 0);
        linearLayout.setLayoutParams(layoutParams);
        // CHECK BOX
        CheckBox checkbox = new CheckBox(getContext());
        // TODO: change color due to DDL
        // overdue: turn to R.drawable.my_check_box_overdue
        // today: turn to R.drawable.my_check_box_today
        // tomorrow: turn to R.drawable.my_check_tomorrow
        // others: turn to R.drawable.my_check
        if (true) {
            checkbox.setButtonDrawable(R.drawable.my_check_box);
        } else {

        }
        checkbox.setId(R.id.layout3);
        // params
        LinearLayout.LayoutParams paramsCB = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsCB.gravity = Gravity.CENTER_VERTICAL;
        paramsCB.rightMargin = 50;
        checkbox.setLayoutParams(paramsCB);
        linearLayout.addView(checkbox);
        // animation
        // checkbox.setStateListAnimator(AnimatorInflater.loadStateListAnimator(getContext(), R.drawable.check_box_anim_smooth));
        // functionality
        checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t.changeFinished();
                user.SaveFiles();
            }
        });

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
        // TODO: change color due to the DDL
        // overdue: turn to R.color.red
        // today: turn to R.color.red_50
        // tomorrow: turn to R.color.yellow_100
        // others: turn to R.color.grey
        if (true) {
            ddl.setTextColor(getResources().getColor(R.color.grey));
        } else {

        }
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