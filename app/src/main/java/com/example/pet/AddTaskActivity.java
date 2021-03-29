package com.example.pet;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class AddTaskActivity extends AppCompatActivity {

    public static Task task = null;

    private DatePickerDialog endDatePickerDialog;
    private Button editEndDate;

    private Spinner goalsSpinner;
    private Spinner recurringOption;
    private Switch repeatSwitch;

    private ArrayList<Goal> items = new ArrayList<>();
    String[] options = {"Daily", "Weekly", "Bi-weekly", "Monthly"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        // return button listener
        ImageButton back = findViewById(R.id.returnTask);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // initial datePicker
        editEndDate = findViewById(R.id.addTaskDeadline);
        endDatePickerDialog = Date.initDatePicker(this, endDatePickerDialog, editEndDate);

        // initial goalsSpinner
        goalsSpinner = findViewById(R.id.goalsSpinner);

        User user = User.Initialize();
        int currentYear = Integer.parseInt(new SimpleDateFormat("yyyy", Locale.getDefault()).format(new java.util.Date()));
        int currentMonth = Integer.parseInt(new SimpleDateFormat("M", Locale.getDefault()).format(new java.util.Date()));
        int currentDate = Integer.parseInt(new SimpleDateFormat("dd", Locale.getDefault()).format(new java.util.Date()));

        for (Goal g : user.getGoals()) {
            if (g.endDate >= currentYear * 1000 + currentMonth * 100 + currentDate) {
                items.add(g);
            }
        }
        String[] itemNames = new String[items.size()];
        for (int i = 0; i < items.size(); i++) {
            itemNames[i] = items.get(i).name();
        }

        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, itemNames);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        goalsSpinner.setAdapter(aa);

        goalsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateDateRange(position, endDatePickerDialog);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        recurringOption = findViewById(R.id.goalsRecurrenceOption);

        ArrayAdapter aa2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, options);
        aa2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        recurringOption.setAdapter(aa2);
        recurringOption.setVisibility(View.INVISIBLE);


        repeatSwitch = findViewById(R.id.switchRepeat);
        repeatSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    recurringOption.setVisibility(View.VISIBLE);
                } else {
                    recurringOption.setVisibility(View.INVISIBLE);
                }
            }
        });

        // finish button listener
        Button finishButton = findViewById(R.id.finishCreateTask);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTask();
            }
        });


        if (task != null) {
            TextView title = findViewById(R.id.addTaskTitle);
            title.setText("Change Existing Task");
            finishButton.setText("Change Task");

            EditText taskName = findViewById(R.id.addTaskName);
            taskName.setText(task.name());
            EditText taskDescription = findViewById(R.id.addTaskDescription);
            taskDescription.setText(task.description());

            goalsSpinner.setSelection(items.indexOf(task.getParentGoal()));
            updateDateRange(items.indexOf(task.getParentGoal()), endDatePickerDialog);

            editEndDate.setText(User.month2string(task.startMonth()) + " " + task.startDay() + " " + task.startYear());

            if (task.isRecurring()) {
                repeatSwitch.setChecked(true);
                recurringOption.setVisibility(View.VISIBLE);
                recurringOption.setSelection(java.util.Arrays.asList(options).indexOf(task.recurringRule()));
            }
        }


    }

    public void setTask() {
        TextView taskName = findViewById(R.id.addTaskName);
        TextView TaskDescription = findViewById(R.id.addTaskDescription);
        TextView taskDeadLine = findViewById(R.id.addTaskDeadline);

        Spinner goalsSpinner = findViewById(R.id.goalsSpinner);

        User user = User.Initialize();
        int endYear = endDatePickerDialog.getDatePicker().getYear();
        int endMonth = endDatePickerDialog.getDatePicker().getMonth() + 1;
        int endDay = endDatePickerDialog.getDatePicker().getDayOfMonth();

        if (task == null) {
            user.addTask(taskName.getText().toString(),
                    TaskDescription.getText().toString(),
                    endYear, endMonth, endDay,
                    (repeatSwitch.isChecked() ? options[recurringOption.getSelectedItemPosition()] : "Once"),
                    items.get(goalsSpinner.getSelectedItemPosition())
            );
        } else {
            task.changeName(taskName.getText().toString());
            task.changeDescription(TaskDescription.getText().toString());
            task.changeRecurringRule(endYear, endMonth, endDay, (repeatSwitch.isChecked() ? options[recurringOption.getSelectedItemPosition()] : "Once"));
            task.changeParent(items.get(goalsSpinner.getSelectedItemPosition()));
            user.SaveFiles();
        }

        finish();
    }

    private void updateDateRange(int index, DatePickerDialog endDatePickerDialog) {
        User user = User.Initialize();
        Goal g = items.get(index);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        java.util.Date date = new java.util.Date();
        try {
            date = sdf.parse("" + user.longTermGoalStart);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        endDatePickerDialog.getDatePicker().setMinDate(date.getTime());
        try {
            date = sdf.parse("" + user.longTermGoalEnd);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        endDatePickerDialog.getDatePicker().setMaxDate(date.getTime());
        endDatePickerDialog.updateDate(g.startYear(), g.startMonth() - 1, g.startDay());
        editEndDate.setText(Date.makeDateString(g.startYear(),g.startMonth(),g.startDay()));
        try {
            date = sdf.parse("" + g.startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        endDatePickerDialog.getDatePicker().setMinDate(date.getTime());
        date = new java.util.Date();
        try {
            date = sdf.parse("" + g.endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        endDatePickerDialog.getDatePicker().setMaxDate(date.getTime());

        if (task != null) {
            int targetDate = task.startYear() * 10000 + task.startMonth() * 100 + task.startDay();
            if (g.startDate <= targetDate && targetDate <= g.endDate) {
                Log.i("task page",""+targetDate);
                Log.i("task page",""+g.startDate);
                Log.i("task page",""+g.endDate);
                Log.i("task page",(g.startDate >= targetDate && targetDate <= g.endDate)?"true":"false");
                endDatePickerDialog.updateDate(task.startYear(), task.startMonth() - 1, task.startDay());
                editEndDate.setText(Date.makeDateString(task.startYear(),task.startMonth(),task.startDay()));
            }
        }
    }


    @Override
    public void finish() {
        task = null;
        super.finish();
    }

    public void openEventDataPicker(View view) {
        endDatePickerDialog.show();
    }

    public static void openAddTaskActivity(Context context, Task t) {
        task = t;
        Intent intent = new Intent(context, AddTaskActivity.class);
        context.startActivity(intent);
    }
}