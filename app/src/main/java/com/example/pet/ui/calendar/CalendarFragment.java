package com.example.pet.ui.calendar;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.pet.R;
import com.example.pet.Task;
import com.example.pet.User;
import com.example.pet.ui.home.HomeViewModel;

import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class CalendarFragment extends Fragment {

    private CalendarViewModel calendarViewModel;
    private User user;
    private int currentYear, currentMonth, currentDate, displayYear, displayMonth;
    private TextView top_date_text;
    private TextView[] table_text;
    private RelativeLayout[] table_layout;

    private int[] IMAGE_SOURCE;
    private final int TABLE_X_MAX = 140;
    private final int TABLE_Y_MAX = 200;
    private Random random;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        calendarViewModel =
                new ViewModelProvider(this).get(CalendarViewModel.class);
        View root = inflater.inflate(R.layout.fragment_calendar, container, false);

        user = User.Initialize();
        IMAGE_SOURCE = new int[]{R.drawable.education_icon, R.drawable.habbit_icon, R.drawable.sport_icon, R.drawable.work_icon};
        random = new Random();

        currentYear = Integer.parseInt(new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date()));
        currentMonth = Integer.parseInt(new SimpleDateFormat("M", Locale.getDefault()).format(new Date()));
        currentDate = Integer.parseInt(new SimpleDateFormat("dd", Locale.getDefault()).format(new Date()));

        displayYear = currentYear;
        displayMonth = currentMonth;

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
            }
        });
        root.findViewById(R.id.calendar_top_today).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                displayYear = currentYear;
                displayMonth = currentMonth;
                updateTable();
            }
        });

        table_text = new TextView[35];
        table_layout = new RelativeLayout[35];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                table_text[i * 7 + j] = root.findViewById(r.getIdentifier("calendar_table_text_" + i + j, "id", getContext().getPackageName()));
                table_layout[i * 7 + j] = root.findViewById(r.getIdentifier("calendar_table_layout_" + i + j, "id", getContext().getPackageName()));
            }
        }

        updateTable();

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
        YearMonth yearMonthObject = YearMonth.of(displayYear, displayMonth);
        int daysInMonth = yearMonthObject.lengthOfMonth();
        table_text[0].setText("" + start_day);
        table_text[1].setText("" + daysInMonth);
        int indx = 0;
        for (; indx < start_day; indx++) {
            table_text[indx].setText("");
            table_layout[indx].removeAllViews();
        }
        int dayNum = 1;
        for (; indx < start_day + YearMonth.of(displayYear, displayMonth).lengthOfMonth() && indx < 35; indx++) {
            if (displayYear == currentYear && displayMonth == currentMonth && dayNum == currentDate) {
                Drawable drawable = getResources().getDrawable(R.drawable.calendar_date);
                table_text[indx].setBackground(drawable);
            } else {
                table_text[indx].setBackgroundColor(0x00000000);
            }
            table_text[indx].setText("" + dayNum);

            table_layout[indx].removeAllViews();
            ArrayList<Task> theseTasks = user.getTasks(displayYear, displayMonth, dayNum);
            int maxRadius = maxRadiusSelection(TABLE_X_MAX,TABLE_Y_MAX,theseTasks.size());
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
    private int maxRadiusSelection(int width, int height, int numberOfTasks){
        return (int) ((double) width / 2.0 / numberOfTasks);
    }
}