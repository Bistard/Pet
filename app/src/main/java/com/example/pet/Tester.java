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
        user.longTermGoalEnd = 20220331;  // YYYYMMDD

        Goal goal1 = user.addGoal("Goal 1", "",2021,2,20,2021,2,22,0);
        user.addTask("Task 1","This is for goal 1", 2021,2,21, "Once", goal1);
        user.addTask("Task 2","This is also for goal 1", 2021,2,22, "Weekly", goal1);

        Goal goal2 = user.addGoal("Goal 2","",2020,2,20,2021,3,29,1);
        user.addTask("Task","",2020,2,20,2021,3,20,"Monthly",goal2);  // none of the recurring rule is implemented yet
        user.addTask("Task 3","For goal 2",2021,2,20,2021,3,20,"Bi-weekly",goal2);  // none of the recurring rule is implemented yet

        return user;
    }

    static User readCustomUser(){
        User user = User.Initialize();
        return user;
    }
}
