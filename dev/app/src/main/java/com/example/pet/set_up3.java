package com.example.pet;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import com.example.pet.Date;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class set_up3 extends AppCompatActivity {

    // DatePicker attributes
    public DatePickerDialog datePickerDialog;
    public Button datePickerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_up3);

        User user = User.Initialize();

        // initial datePicker
        datePickerButton = findViewById(R.id.datePickerButton);
        datePickerDialog = Date.initDatePicker(this, datePickerDialog, datePickerButton);

        // open next activity
        Button nextButton = (Button) findViewById(R.id.setUpButton3);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentYear = Integer.parseInt(new SimpleDateFormat("yyyy", Locale.getDefault()).format(new java.util.Date()));
                int currentMonth = Integer.parseInt(new SimpleDateFormat("M", Locale.getDefault()).format(new java.util.Date()));
                int currentDate = Integer.parseInt(new SimpleDateFormat("dd", Locale.getDefault()).format(new java.util.Date()));
                user.longTermGoalStart=currentYear*10000+currentMonth*100+currentDate;

                DatePicker date = datePickerDialog.getDatePicker();
                user.longTermGoalEnd = date.getYear()*10000+date.getMonth()*100+date.getDayOfMonth();

                user.isFirstTime = false;
                Log.i("setup3","save files");
                user.SaveFiles();

                openHomeActivity();
            }
        });

    }

    public void openDataPicker(View view) {
        datePickerDialog.show();
    }

    public void openHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}