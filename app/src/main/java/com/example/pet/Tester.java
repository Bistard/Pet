package com.example.pet;

import java.io.File;
import java.util.ArrayList;

public class Tester {
    static void deleteAllJSON() {
        File path = DataManager.path;
        new File(path, "User.json").delete();
        new File(path, "Goals.json").delete();
        new File(path, "Tasks.json").delete();
    }

    /*
    goal type:
    0 -- education
    1 -- habbit
    2 -- sport
    3 -- work
     */

    static public User makeCustomUser() {
        final int today = 20210331;

        Tester.deleteAllJSON();
        User user = User.Initialize();

        user.isFirstTime = false;
        user.longTermGoal = "Graduate with laifu & waifu";
        user.longTermGoalStart = 20200901;
        user.longTermGoalEnd = 20240430;  // YYYYMMDD
        user.addPet("Bary");

        User.goalList = new ArrayList<>();
        user.nextGoalID = 0;
        User.taskList = new ArrayList<>();

        Goal goal1 = user.addGoal("Survive the First Semester", "Please Uni don't fail me in my first semester!!!", 2020, 9, 1, 2020, 12, 20, 0);
        Task t = user.addTask("Reading", "", 2020, 9, 1, "Daily", goal1);
        t = t.parent;
        for (int i = 0; i < t.finished.size(); i++) {
            t.finished.set(i, 1);
        }
        t = user.addTask("Do Calc Assignment", "", 2020, 9, 12, "Weekly", goal1);
        t = t.parent;
        for (int i = 0; i < t.finished.size(); i++) {
            t.finished.set(i, 1);
        }
        t = user.addTask("CS Lab", "", 2020, 9, 16, "Weekly", goal1);
        t = t.parent;
        for (int i = 0; i < t.finished.size(); i++) {
            t.finished.set(i, 1);
        }
        t = user.addTask("Physics Test", "", 2020, 9, 22, "Monthly", goal1);
        t = t.parent;
        for (int i = 0; i < t.finished.size(); i++) {
            t.finished.set(i, 1);
        }


        Goal goal2 = user.addGoal("Lose Weight", "Reduce my body weight to 10kg", 2020, 9, 1, 2021, 8, 31, 2);
        t = user.addTask("Go to GYM", "", 2020, 11, 8, 2021, 4, 20, "Weekly", goal2);
        t = t.parent;
        for (int i = 0; i < t.finished.size(); i++) {
            if (t.recurringDates.get(i) < today) {
                t.finished.set(i, 1);
            }
        }
        t = user.addTask("Skiing", "", 2020, 10, 11, 2021, 2, 28, "Weekly", goal2);
        t = t.parent;
        for (int i = 0; i < t.finished.size(); i++) {
            t.finished.set(i, 1);
        }
        t = user.addTask("Swimming", "", 2021, 3, 1, 2021, 8, 31, "Weekly", goal2);
        t = t.parent;
        for (int i = 0; i < t.finished.size(); i++) {
            if (t.recurringDates.get(i) < today) {
                t.finished.set(i, 1);
            }
        }

        Goal intern = user.addGoal("Become a Google Software Developer", "", 2020, 9, 30, 2021, 3, 31, 3);
        t = user.addTask("Google Solution Challenge", "Work on the pet project.", 2021, 2, 3, "Daily", intern);
        t = t.parent;
        for (int i = 0; i < t.finished.size(); i++) {
            if (t.recurringDates.get(i) < today) {
                t.finished.set(i, 1);
            }
        }

        user.addTask("Solution Challenge Beta", "", 2021, 3, 27,"Once", intern);
        user.addTask("Solution Challenge DDL", "", 2021, 3, 31, "Once", intern);

        t = user.addTask("Write 3 question on LeetCode", "", 2021, 2, 2, "Weekly", intern);
        t = t.parent;
        for (int i = 0; i < t.finished.size(); i++) {
            if (t.recurringDates.get(i) < today) {
                t.finished.set(i, 1);
            }
        }


        Goal goal3 = user.addGoal("Survive First Year", "target: grade of 90", 2021, 1, 11, 2021, 4, 30, 0);
        t = user.addTask("Reading", "", 2021, 1, 16, "Bi-weekly", goal3);
        t = t.parent;
        for (int i = 0; i < t.finished.size(); i++) {
            if (t.recurringDates.get(i) < today) {
                t.finished.set(i, 1);
            }
        }

        t = user.addTask("Do Calc Assignment", "", 2021, 1, 16, "Weekly", goal3);
        t = t.parent;
        for (int i = 0; i < t.finished.size(); i++) {
            if (t.recurringDates.get(i) < today) {
                t.finished.set(i, 1);
            }
        }
        t = user.addTask("CS Lab", "", 2021, 1, 19, "Weekly", goal3);
        t = t.parent;
        for (int i = 0; i < t.finished.size(); i++) {
            if (t.recurringDates.get(i) < today) {
                t.finished.set(i, 1);
            }
        }
        t = user.addTask("Linear Algebra", "", 2021, 1, 27, "Bi-weekly", goal3);
        t = t.parent;
        for (int i = 0; i < t.finished.size(); i++) {
            if (t.recurringDates.get(i) < today) {
                t.finished.set(i, 1);
            }
        }
        t = user.addTask("Design Club", "", 2021, 1, 23, "Bi-weekly", goal3);
        t = t.parent;
        for (int i = 0; i < t.finished.size(); i++) {
            if (t.recurringDates.get(i) < today) {
                t.finished.set(i, 1);
            }
        }

        Goal goal4 = user.addGoal("Mental Health", " ", 2020, 9, 1, 2024, 4, 30, 1);
        t = user.addTask("Movie night", "", 2020, 9, 4, "Weekly", goal4);
        t = t.parent;
        for (int i = 0; i < t.finished.size(); i++) {
            if (t.recurringDates.get(i) < today) {
                t.finished.set(i, 1);
            }
        }

        Goal goal = user.addGoal("Deal with next 3 years", " ", 2021, 9, 1, 2024, 4, 30, 1);
        user.addTask("Do things", "", 2021, 9, 1, "Daily", goal);

        user.SaveFiles();

        return user;
    }

    static public User makeEmptyUser(){
        Tester.deleteAllJSON();
        User user = User.Initialize();

        user.isFirstTime = false;
        user.longTermGoal = "goal";
        user.longTermGoalStart = 20200901;
        user.longTermGoalEnd = 20240430;  // YYYYMMDD
        user.addPet("Snail");

        User.goalList = new ArrayList<>();
        user.nextGoalID = 0;
        User.taskList = new ArrayList<>();

        return user;
    }
}
