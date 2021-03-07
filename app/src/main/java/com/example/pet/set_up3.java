package com.example.pet;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import com.example.pet.Date;

import java.util.Calendar;

public class set_up3 extends AppCompatActivity {

    // DatePicker attributes
    private DatePickerDialog datePickerDialog;
    private Button datePickerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_up3);

        // initial datePicker
        datePickerButton = findViewById(R.id.datePickerButton);
        datePickerDialog = Date.initDatePicker(this, datePickerDialog, datePickerButton);

        // open next activity
        Button nextButton = (Button) findViewById(R.id.setUpButton3);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openHomeActivity();
            }
        });

    }

    public void openDataPicker(View view) {
        datePickerDialog.show();
    }

    // TODO: store the date
    public void buttonClick(View view) {

    }

    public void openHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}