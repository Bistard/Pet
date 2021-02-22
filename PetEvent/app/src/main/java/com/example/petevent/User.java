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

    /**
     * Have to leave constructor public for JSON library to work, plz don't call this constructor.
     */
    public User() {
    }

    /**
     * Use this to create the user object. Currently support only one user. Multi-user is possible,
     * but needs more work to store the files.
     *
     * @return the initialized user object
     */
    public static User Initialize() {
        if (!initialized) {
            User user = DataManager.LoadUser("User.json");
            user.LoadFiles();
            return user;
        } else {
            throw new IllegalStateException("Do not currently support multiple users");
        }
    }

    /**
     * Call this method to add goals.
     *
     * @param name        Name of the goal
     * @param description description of the goal
     * @param startDate   startDate of the goal in YYYYMMDD, inclusive
     * @param endDate     endDate of the goal in YYYYMMDD, inclusive
     * @param type        type of the goal
     * @return the newly created goal object
     * @throws IllegalArgumentException
     */
    public Goal addGoal(String name, String description, int startDate, int endDate, int type)
            throws IllegalArgumentException {
        if (endDate < startDate || endDate > longTermGoalDDL) {
            throw new IllegalArgumentException("Time is incorrect.");
        }
        Goal newGoal = new Goal(name, description, startDate, endDate, type, nextGoalID);
        nextGoalID++;
        goalList.add(newGoal);
        SaveFiles();
        return newGoal;
    }

    /**
     * Fetches a list of goals that takes place at the specified date.
     *
     * @param date
     * @return ArrayList of Goals
     */
    public ArrayList<Goal> getGoals(int date) {
        ArrayList<Goal> lst = new ArrayList<>();
        lst.add(goalList.get(0));
        // need the logic

        return lst;
    }

    /**
     * Call this method to add tasks.
     *
     * @param name          name of the task
     * @param description   description of the task
     * @param startDate     starting date of the task
     *                      OR
     *                      the event date if the task is not recurring
     * @param endDate       end date of the task if the task is recurring
     * @param recurringRule how the tasks will be recurring. Supports:
     *                      "Once", "Weekly", TBD...
     * @param parentID      the ID of its parent
     * @param parent        the parent object
     * @return the newly created goal object
     * @throws IllegalArgumentException
     */
    public Task addTask(String name, String description, int startDate, int endDate,
                        String recurringRule, int parentID) throws IllegalArgumentException {
        Goal parent = Goal.getGoalByID(parentID);
        if (endDate < startDate || startDate < parent.startDate || endDate > parent.endDate) {
            throw new IllegalArgumentException("Time is incorrect.");
        }
        Task newTask = new Task(name, description, startDate, endDate, recurringRule, parentID);
        taskList.add(newTask);
        SaveFiles();
        return newTask;
    }

    public Task addTask(String name, String description, int startDate, int endDate,
                        String recurringRule, Goal parent) throws IllegalArgumentException {
        return addTask(name, description, startDate, endDate, recurringRule, parent.ID);
    }

    public Task addTask(String name, String description, int startDate, int parentID)
            throws IllegalArgumentException {
        return addTask(name, description, startDate, startDate, "Once", parentID);
    }

    public Task addTask(String name, String description, int startDate, Goal parent)
            throws IllegalArgumentException {
        return addTask(name, description, startDate, startDate, "Once", parent.ID);
    }

    /**
     * Fetches a list of tasks that takes place at the specified date.
     *
     * @param date
     * @return ArrayList of Goals
     */
    public ArrayList<Task> getTasks(int date) {
        ArrayList<Task> lst = new ArrayList<>();
        lst.add(taskList.get(0));
        // need the logic

        return lst;
    }

    /**
     * Fetches a list of goals that should be finished before the specified date but was not finished.
     *
     * @param date search events due before this date, exclusive
     * @return ArrayList of Goals
     */
    public ArrayList<Task> getUnfinishedTasks(int date) {
        return null;
    }

    /**
     * Saves all the files, should be called when changed are being made to individual objects,
     * but not when new objects are created or removed.
     */
    public void SaveFiles() {
        DataManager.StoreUser("User.json", this);
        DataManager.StoreGoals("Goals.json", goalList);
        DataManager.StoreTasks("Tasks.json", taskList);
    }

    /**
     * Loads goalList and taskList
     */
    public void LoadFiles() {
        try {
            goalList = new ArrayList<>(DataManager.LoadGoals("Goals.json"));
            taskList = new ArrayList<>(DataManager.LoadTasks("Tasks.json"));
        } catch (Exception e) {
        }

    }
}
