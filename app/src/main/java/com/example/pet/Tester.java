package com.example.pet;

import java.io.File;

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
        user.longTermGoalEnd = 20210331;  // YYYYMMDD

        Goal goal1 = user.addGoal("Goal 1", "",2021,02,20,2021,02,22,0);
        user.addTask("Task 1","This is for goal 1", 2021,02,21, "Once", goal1);
        user.addTask("Task 2","This is also for goal 1", 2021,02,22, "Weekly", goal1);

        int goal2ID = user.addGoal("Goal 2","",2021,02,20,2021,03,29,1).ID;
        user.addTask("Task 3","For goal 2",2021,02,20,2021,03,20,"Bi-weekly",Goal.getGoalByID(goal2ID));  // none of the recurring rule is implemented yet

        return user;
    }

    static User readCustomUser(){
        User user = User.Initialize();
        return user;
    }
}
