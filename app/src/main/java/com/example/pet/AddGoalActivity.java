package com.example.pet;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class AddGoalActivity extends AppCompatActivity {

    static Goal goal = null;

    // DatePicker attributes
    public DatePickerDialog startDatePickerDialog;
    Button editStartDate;

    public DatePickerDialog endDatePickerDialog;
    Button editEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goal);

        // return button listener
        ImageButton back = findViewById(R.id.returnGoal);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //spinner
        String[] options = {"education", "hobbit", "sport", "work"};
        Spinner spinner = findViewById(R.id.addCategory);
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,options);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa);

        // initial datePicker
        editStartDate = findViewById(R.id.addGoalStartTime);
        startDatePickerDialog = Date.initDatePicker(this, startDatePickerDialog, editStartDate);

        editEndDate = findViewById(R.id.addGoalDeadline);
        endDatePickerDialog = Date.initDatePicker(this, startDatePickerDialog, editEndDate);

        // finish button listener
        Button finishButton = findViewById(R.id.finishCreateGoal);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGoal();
            }
        });

        if (goal != null) {
            TextView title = findViewById(R.id.addGoalTitle);
            title.setText("Change Existing Goal");
            finishButton.setText("Change Goal");

            EditText goalName = findViewById(R.id.addGoalName);
            goalName.setText(goal.name());
            EditText goalDescription = findViewById(R.id.addGoalDescription);
            goalDescription.setText(goal.description());

            spinner.setSelection(goal.type());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            java.util.Date date = new java.util.Date();
            try {
                date = sdf.parse("" + User.Initialize().longTermGoalStart);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            startDatePickerDialog.getDatePicker().setMinDate(date.getTime());
            endDatePickerDialog.getDatePicker().setMinDate(date.getTime());
            sdf = new SimpleDateFormat("yyyyMMdd");
            date = new java.util.Date();
            try {
                date = sdf.parse("" + User.Initialize().longTermGoalEnd);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            startDatePickerDialog.getDatePicker().setMaxDate(date.getTime());
            endDatePickerDialog.getDatePicker().setMaxDate(date.getTime());

            startDatePickerDialog.updateDate(goal.startYear(), goal.startMonth() - 1, goal.startDay());
            editStartDate.setText(User.month2string(goal.startMonth()) + " " + goal.startDay() + " " + goal.startYear());

            endDatePickerDialog.updateDate(goal.endYear(), goal.endMonth() - 1, goal.endDay());
            editEndDate.setText(User.month2string(goal.endMonth()) + " " + goal.endDay() + " " + goal.endYear());
        }


    }

    @Override
    public void finish() {
        goal = null;
        super.finish();
    }

    public void setGoal() {
        TextView goalName = findViewById(R.id.addGoalName);
        TextView goalDescription = findViewById(R.id.addGoalDescription);
        TextView goalStartTime = findViewById(R.id.addGoalStartTime);
        TextView goalDeadline = findViewById(R.id.addGoalDeadline);

        Spinner spinner = findViewById(R.id.addCategory);

        User user = User.Initialize();
        String[] startDate = goalStartTime.getText().toString().split(" ");
        String[] endDate = goalDeadline.getText().toString().split(" ");
        int startYear = Integer.parseInt(startDate[2]);
        int startMonth = Date.toNumMonthFormat(startDate[0]);
        int startDay = Integer.parseInt(startDate[1]);
        int endYear = Integer.parseInt(endDate[2]);
        int endMonth = Date.toNumMonthFormat(endDate[0]);
        int endDay = Integer.parseInt(endDate[1]);

        if (goal == null) {
            user.addGoal(goalName.getText().toString(),
                    goalDescription.getText().toString(),
                    startYear, startMonth, startDay,
                    endYear, endMonth, endDay,
                    spinner.getSelectedItemPosition()
            );
        } else {
            goal.changeName(goalName.getText().toString());
            goal.changeDescription(goalDescription.getText().toString());
            goal.changeStartDate(startYear, startMonth, startDay);
            goal.changeEndDate(endYear, endMonth, endDay);
            goal.changeType(spinner.getSelectedItemPosition());
            user.SaveFiles();
        }

        finish();
    }

    public void openStartDataPicker(View view) {
        startDatePickerDialog.show();
    }

    public void openEndDataPicker(View view) {
        endDatePickerDialog.show();
    }

    public static void openAddGoalActivity(Context context, Goal g) {
        goal = g;
        Intent intent = new Intent(context, AddGoalActivity.class);
        context.startActivity(intent);
    }
}