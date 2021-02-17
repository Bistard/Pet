package com.example.pet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_up1);
    }

    // setUp page button click method
    public void buttonClick (View view) {
        TextView setupText = findViewById(R.id.setUpText1);
        EditText petName = findViewById(R.id.setUpEnterBox1);
        setupText.setText(petName.getText().toString());
    }

}