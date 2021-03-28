package com.example.pet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableRow;
import android.widget.TextView;

public class AddTaskActivity extends AppCompatActivity {

    static Task task = null;

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

        if(task!=null){
            Log.i("task",task.name());

            EditText tv = findViewById(R.id.addTaskName);
            tv.setHint(task.name());
        }else{
            Log.i("task","null");

        }



        // finish button listener
        Button finishButton = findViewById(R.id.finishCreateTask);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewTask();
            }
        });
    }

    public void createNewTask() {
        // TODO:

        finish();
    }


    @Override
    public void finish() {
        task = null;
        super.finish();
    }

    public static void openAddTaskActivity(Context context, Task t) {
        task = t;
        Intent intent = new Intent(context, AddTaskActivity.class);
        context.startActivity(intent);
    }
}