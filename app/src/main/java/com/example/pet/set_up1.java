package com.example.pet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class set_up1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_up1);

        // to the set up page#2
        Button nextButton = (Button)findViewById(R.id.setUpButton1);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSetUp2();
            }
        });

    }

    // TODO: store the name
    public void buttonClick (View view) {
        TextView setupText = findViewById(R.id.setUpText1);
        EditText petName = findViewById(R.id.setUpEnterBox1);
        setupText.setText(petName.getText().toString());
    }

    public void openSetUp2() {
        Intent intent = new Intent(this, set_up2.class);
        startActivity(intent);
    }

}