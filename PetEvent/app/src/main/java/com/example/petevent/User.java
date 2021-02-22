package com.example.petevent;

import java.security.PublicKey;
import java.util.ArrayList;

public class User {
    public static boolean initialized = false;

    public boolean isFirstTime = true;

    public String longTermGoal;
    public int longTermGoalDDL;

    public static ArrayList<Goal> goalList = new ArrayList<>();
    public int nextGoalID = 0;
    public static ArrayList<Task> taskList = new ArrayList<>();

    public User() {}

    public static User Initialize() {
        if (!initialized) {
            User user = DataManager.LoadUser("User.json");
            user.LoadFiles();
            return user;
        } else {
            throw new IllegalStateException("Do not currently support multiple users");
        }
    }

    public Goal addGoal(String name, String description, int startDate, int endDate, int type) {
        Goal newGoal = new Goal(name, description, startDate, endDate, type, nextGoalID);
        nextGoalID++;
        goalList.add(newGoal);
        return newGoal;
    }


    /**
     * Find all the goals that is set for the inputted date.
     *
     * @param date The date to be searched in YYYYMMDD
     *             If date ends with XXXX0000, return all goals in that year;
     *             If date ends with XXXXXX00, return all goals in that month;
     *             If date ends with XXXXXXXX, return all goals in that day.
     * @return An arraylist of goals happening at the specified time.
     */
    public ArrayList<Goal> getGoals(int date) {

        // need the logic

        return null;
    }

    public Task addTask(String name, String description, int startDate, int endDate,
                        String recurringRule, int parentID) {
        Task newTask = new Task(name, description, startDate, endDate, recurringRule, parentID);
        taskList.add(newTask);
        return newTask;
    }

    public Task addTask(String name, String description, int startDate, int endDate,
                        String recurringRule, Goal parent) {
        return addTask(name, description, startDate, endDate, recurringRule, parent.ID);
    }

    public Task addTask(String name, String description, int startDate, int parentID) {
        return addTask(name, description, startDate, startDate, "Once", parentID);
    }

    public Task addTask(String name, String description, int startDate, Goal parent) {
        return addTask(name, description, startDate, startDate, "Once", parent.ID);
    }


    /**
     * Find all the goals that is set for the inputted date.
     *
     * @param date The date to be searched in YYYYMMDD
     *             If date ends with XXXX0000, return all goals in that year;
     *             If date ends with XXXXXX00, return all goals in that month;
     *             If date ends with XXXXXXXX, return all goals in that day.
     * @return An arraylist of goals happening at the specified time.
     * Recurring task would only appear once.
     */
    public static ArrayList<Task> getTasks(int date) {

        // need logic

        return null;
    }

    public void SaveFiles() {
        DataManager.StoreUser("User.json", this);
        DataManager.StoreGoals("Goals.json", goalList);
        DataManager.StoreTasks("Tasks.json", taskList);
    }

    public void LoadFiles() {
        try{
            goalList = new ArrayList<>(DataManager.LoadGoals("Goals.json"));
            taskList = new ArrayList<>(DataManager.LoadTasks("Tasks.json"));
        } catch (Exception e){}

    }
}
