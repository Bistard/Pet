package com.example.pet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class set_up2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_up2);

        User user = User.Initialize();

        // to the set up page#3
        Button nextButton = (Button)findViewById(R.id.setUpButton2);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) findViewById(R.id.setUpEnterBox2);
                String str = editText.getText().toString();
                Log.i("setup",str);
                user.longTermGoal = str;
                user.SaveFiles();
                openSetUp3();
            }
        });

//
//        //get the spinner from the xml.
//        Spinner dropdown = findViewById(R.id.goal_spinner);
//        //create a list of items for the spinner.
//        String[] items = new String[]{"Study", "Exercise", "Work", "Others"};
//        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
//        //There are multiple variations of this, but this is the basic variant.
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
//        //set the spinners adapter to the previously created one.
//        dropdown.setAdapter(adapter);
    }

//
//    // TO DO: store the type
//    public void buttonClick (View view) {
//    }

    public void openSetUp3() {
        Intent intent = new Intent(this, set_up3.class);
        startActivity(intent);
    }
}