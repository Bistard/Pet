package com.example.pet;

import java.io.File;
import java.util.ArrayList;

public class Tester {
    static void deleteAllJSON(){
        File path = DataManager.path;
        new File(path,"User.json").delete();
        new File(path,"Goals.json").delete();
        new File(path,"Tasks.json").delete();
    }

    static public User makeCustomUser(){
        Tester.deleteAllJSON();
        User user = User.Initialize();

        user.isFirstTime = false;
        user.longTermGoal = "Finish this app";
        user.longTermGoalEnd = 20220331;  // YYYYMMDD

        User.goalList = new ArrayList<>();
        user.nextGoalID = 0;
        User.taskList = new ArrayList<>();

        Goal goal1 = user.addGoal("ECON 101", "\"target: grade of 80\"",2021,2,20,2021,3,22,0);
        user.addTask("Task 1","This is for goal 1", 2021,2,21, "Daily", goal1);
        user.addTask("Task 2","This is also for goal 1", 2021,2,22, "Weekly", goal1);

        Goal goal2 = user.addGoal("STAT 230","target: grade of 80",2020,2,20,2021,3,29,1);
        user.addTask("Task","",2020,2,20,2021,3,20,"Monthly",goal2);  // none of the recurring rule is implemented yet
        user.addTask("Task 3","For goal 2",2021,2,20,2021,3,20,"Bi-weekly",goal2);  // none of the recurring rule is implemented yet

        Goal goal3 = user.addGoal("CS 136","target: grade of 90",2020,2,20,2021,3,29,1);

        Goal goal = user.addGoal("g", " ", 2021, 2, 10, 2021, 4, 15, 1);
        Task task1 = user.addTask("t", "", 2021, 3, 4, 2021, 4, 7, "Weekly", goal);
        task1.parent.finished.set(0, 1);

        user.SaveFiles();

        return user;
    }

    static public User readCustomUser(){
        User user = User.Initialize();
        return user;
    }
}
