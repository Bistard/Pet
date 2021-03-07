package com.example.pet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class AddGoalActivity extends AppCompatActivity {

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

        // finish button listener
        Button finishButton = findViewById(R.id.finishCreateGoal);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewGoal();
            }
        });

    }

    public void createNewGoal() {
        TextView goalName        = findViewById(R.id.addGoalName);
        TextView goalDescription = findViewById(R.id.addGoalDescription);
        EditText goalStartTime   = findViewById(R.id.addGoalStartTime);
        EditText goalDeadline    = findViewById(R.id.addGoalDeadline);

        User user = User.Initialize();
        // TODO: testing only
        user.addGoal(goalName.toString(),
                     goalDescription.toString(),
                     2021, 3, 6,
                     2021, 3, 30,
                     0
        );


        finish();
    }
    
}