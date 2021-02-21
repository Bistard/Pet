package com.example.petevent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}

/*
package PetEventTracker;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Goal g1 = new Goal("Goal 1","Description 1","FEB 18 2021");
        new Task("Task 1","Task 1 Description","FEB 18 2021",g1);
        new Task("Task 2","Task 2 Description","FEB 18 2021",g1,"FEB 30 2021","Weekly");
        new Task("Task 3","Task 3 Description","FEB 25 2021",g1);
        new Task("Task 4","Task 4 Description","MAR 18 2021",g1);

        Goal.getGoals("FEB 18 2021");

        ArrayList<Task> lst = Task.getTasks("FEB 18 2021");
        System.out.println(lst);
    }

}

 */