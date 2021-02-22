package com.example.petevent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
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


        TextView myTV = findViewById(R.id.textLog);
        myTV.setText(User.goalList.toString()+User.taskList.toString());

        myTV.setWidth(1000);
        myTV.setHeight(1000);

    }
}
