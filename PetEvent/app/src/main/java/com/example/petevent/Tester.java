package com.example.petevent;

import java.io.File;
import java.util.ArrayList;

public class Tester {
    static void deleteAllJSON(File path){
        new File(path,"User.json").delete();
        new File(path,"Goals.json").delete();
        new File(path,"Tasks.json").delete();
    }

    static User makeCustomUser(File path){
        Tester.deleteAllJSON(path);
        User user = User.Initialize();

        user.isFirstTime = false;
        user.longTermGoal = "Finish this app";
        user.longTermGoalDDL = 20210331;  // YYYYMMDD

        Goal goal1 = user.addGoal("Goal 1", "",20210220,20210222,0);
        user.addTask("Task 1","This is for goal 1", 20210221, goal1.ID);
        user.addTask("Task 2","This is also for goal 1", 20210222, goal1.ID);

        int goal2ID = user.addGoal("Goal 2","",20210220,20210329,1).ID;
        user.addTask("Task 3","For goal 2",20210220,20210320,"Weekly",goal2ID);  // none of the recurring rule is implemented yet

        return user;
    }

    static User readCustomUser(){
        User user = User.Initialize();
        return user;
    }
}
