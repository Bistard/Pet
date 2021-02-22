package com.example.petevent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize the DataManager class
        DataManager.init(getFilesDir());

        User user = Tester.makeCustomUser(getFilesDir());

        ArrayList<Task> todaysTasks = user.getTasks(20210221);
        Task firstTask = todaysTasks.get(0);

        TextView myTV = findViewById(R.id.textLog);
        myTV.setText(firstTask.name + ": " + firstTask.description);

        myTV.setWidth(1000);
        myTV.setHeight(1000);

    }
}
