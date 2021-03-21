package com.example.pet.ui.calendar;

import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.pet.User;
import com.example.pet.ui.home.HomeViewModel;

import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.Date;
import java.util.Locale;

public class CalendarFragment extends Fragment {

    private CalendarViewModel calendarViewModel;
    private User user;
    private int currentYear, currentMonth, currentDate, displayYear, displayMonth;
    private TextView top_date_text;
    private TextView[] table_text;
    private RelativeLayout[] table_layout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        calendarViewModel =
                new ViewModelProvider(this).get(CalendarViewModel.class);
        View root = inflater.inflate(R.layout.fragment_calendar, container, false);

        user = User.Initialize();
        currentYear = Integer.parseInt(new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date()));
        currentMonth = Integer.parseInt(new SimpleDateFormat("M", Locale.getDefault()).format(new Date()));
        currentDate = Integer.parseInt(new SimpleDateFormat("dd", Locale.getDefault()).format(new Date()));

        displayYear = currentYear;
        displayMonth = currentMonth;

        Resources r = getResources();

        TableLayout table = root.findViewById(R.id.calendar_table);

        top_date_text = root.findViewById(R.id.calendar_top_date);
        root.findViewById(R.id.calendar_top_left_arrow).setOnClickListener(new View.OnClickListener() {
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
        top_date_text.setText(month2string(displayMonth) + " " + displayYear);

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
        }
        int dayNum = 1;
        for (; indx < start_day + YearMonth.of(displayYear, displayMonth).lengthOfMonth() && indx < 35; indx++) {
            table_text[indx].setText("" + dayNum);
            dayNum++;
        }
        for (; indx < 35; indx++) {
            table_text[indx].setText("");
        }
    }

    private String month2string(int month) {
        switch (month) {
            case 1:
                return "Jan.";
            case 2:
                return "Feb.";
            case 3:
                return "Mar.";
            case 4:
                return "Apr.";
            case 5:
                return "May ";
            case 6:
                return "Jun.";
            case 7:
                return "Jul.";
            case 8:
                return "Aug.";
            case 9:
                return "Sep.";
            case 10:
                return "Oct.";
            case 11:
                return "Nov.";
            case 12:
                return "Dec";
            default:
                return "MONTH";
        }
    }
}