package com.example.pet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class set_up2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_up2);

        // to the set up page#1
        // TODO: testing only
        Button nextButton = (Button)findViewById(R.id.setUpButton2);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSetUp1();
            }
        });

    }

    // setUp page button click method
    public void buttonClick (View view) {
    }

    public void openSetUp1() {
        Intent intent = new Intent(this, set_up1.class);
        startActivity(intent);
    }
}