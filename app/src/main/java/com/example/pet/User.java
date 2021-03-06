package com.example.pet;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class User {
    public static boolean IsInitialized = false;
    public static User self;

    public boolean isFirstTime = true;

    public String longTermGoal;
    public String longTermGoalType;
    public int longTermGoalStart;
    public int longTermGoalEnd;

    public Pet pet = new Pet("");

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
        if (!IsInitialized) {
            try {
                self = DataManager.LoadUser("User.json");
            } catch (Exception e) {
                Log.i(null, "wrong   " + e.toString());
                self = new User();
                return self;
            }
            self.LoadFiles();
        }
        return self;
    }

    /**
     * Change if it is the first time for the user
     * @param passed
     */
    public void passedTutorial(boolean passed) {
        this.isFirstTime = !passed;
        SaveFiles();
    }

    public Pet addPet(String name) {
        this.pet = new Pet(name);
        return this.pet;
    }

    /**
     * get the name of the pet
     * @return name in String
     */
    @JsonIgnore
    public String getPetName() {
        return this.pet.name;
    }

    /**
     * Call this method to add goals.
     *
     * @param name        Name of the goal
     * @param description description of the goal
     * @param startYear   Starting year of the goal, in YYYY
     * @param startMonth  Starting month of the goal, in MM
     * @param startDay    Starting day of the goal, in DD, inclusive
     * @param endYear     Ending year of the goal, in YYYY
     * @param endMonth    Ending month of the goal, in MM
     * @param endDay      Ending day of the goal, in DD, inclusive
     * @param type        type of the goal
     * @return the newly created goal object
     * @throws IllegalArgumentException
     */
    public Goal addGoal(String name, String description, int startYear, int startMonth,
                        int startDay, int endYear, int endMonth, int endDay, int type)
            throws IllegalArgumentException {
        int startDate = startYear * 10000 + startMonth * 100 + startDay;
        int endDate = endYear * 10000 + endMonth * 100 + endDay;
        if (endDate < startDate) {
            throw new IllegalArgumentException("Time is incorrect.");
        }
        Goal newGoal = new Goal(name, description, startDate, endDate, type, nextGoalID);
        nextGoalID++;
        goalList.add(newGoal);
        SaveFiles();
        return newGoal;
    }

    /**
     * Fetches a list of goals that takes place at the specified date
     *
     * @param year  search events due this date
     * @param month search events due this date
     * @param day   search events due this date
     * @return ArrayList of Goals
     */
    @JsonIgnore
    public ArrayList<Goal> getGoals(int year, int month, int day) {
        int date = year * 10000 + month * 100 + day;
        ArrayList<Goal> lst = new ArrayList<>();
        for (Goal g : goalList) {
            if (g.startDate <= date && g.endDate >= date) {
                lst.add(g);
            }
        }
        return lst;
    }

    /**
     * @param name          name of the task
     * @param description   description of the task
     * @param startYear     Starting year of the task, in YYYY
     * @param startMonth    Starting month of the task, in MM
     * @param startDay      Starting day of the task, in DD, inclusive
     * @param endYear       Ending year of the task, in YYYY, optional if recurring
     * @param endMonth      Ending month of the task, in MM, optional if recurring
     * @param endDay        Ending day of the task, in DD, optional if recurring, inclusive
     * @param recurringRule the tasks will be recurring. Supports:
     *                      "Once", "Weekly", TBD...
     * @param parentID      the ID of its parent
     * @param parent        the parent object
     * @return the newly created goal object
     * @throws IllegalArgumentException
     */
    public Task addTask(String name, String description, int startYear, int startMonth,
                        int startDay, int endYear, int endMonth, int endDay, String recurringRule,
                        Goal parent) throws IllegalArgumentException {
        int startDate = startYear * 10000 + startMonth * 100 + startDay;
        int endDate = endYear * 10000 + endMonth * 100 + endDay;
        if (endDate < startDate || startDate < parent.startDate || endDate > parent.endDate) {
            throw new IllegalArgumentException("Time is incorrect.");
        }
        Task newTask = new Task(name, description, startDate, endDate, recurringRule, parent.ID);
        taskList.add(newTask);
        SaveFiles();
        return newTask.makeChild(startDate);
    }

    public Task addTask(String name, String description, int startYear, int startMonth,
                        int startDay, String recurringRule, Goal parent) throws IllegalArgumentException {
        return addTask(name, description, startYear, startMonth, startDay, parent.endYear(),
                parent.endMonth(), parent.endDay(), recurringRule, parent);
    }

    /**
     * Fetches a list of tasks that takes place at the specified date.
     *
     * @param year  search events due this date
     * @param month search events due this date
     * @param day   search events due this date
     * @return ArrayList of Tasks
     */
    @JsonIgnore
    public ArrayList<Task> getTasks(int year, int month, int day) {
        int date = year * 10000 + month * 100 + day;
        ArrayList<Task> lst = new ArrayList<>();
        for (Task t : taskList) {
            for (int d : t.recurringDates) {
                if (d == date) {
                    lst.add(t.makeChild(date));
                }
            }
        }
        return lst;
    }

    /**
     * Fetches a list of goals that should be finished before the specified date but was not finished.
     *
     * @param year  search events due before this date, exclusive
     * @param month search events due before this date, exclusive
     * @param day   search events due before this date, exclusive
     * @return ArrayList of Tasks
     */
    @JsonIgnore
    public ArrayList<Task> getUnfinishedTasks(int year, int month, int day) {
        ArrayList<Task> lst = new ArrayList<>();
        int date = year * 10000 + month * 100 + day;
        for (Task t : taskList) {
            for (int d : t.recurringDates) {
                if (d < date && t.finished.get(t.recurringDates.indexOf(d)) == 0) {
                    lst.add(t.makeChild(d));
                }
            }
        }
        return lst;
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
    private void LoadFiles() {
        try {
            goalList = new ArrayList<>(DataManager.LoadGoals("Goals.json"));
            taskList = new ArrayList<>(DataManager.LoadTasks("Tasks.json"));
        } catch (Exception e) {
            PanicDeleteAllFiles();
        }

    }

    /**
     * Start from fresh in case of fatal error due to lost of files
     */
    private void PanicDeleteAllFiles() {
        File path = DataManager.path;
        new File(path, "User.json").delete();
        new File(path, "Goals.json").delete();
        new File(path, "Tasks.json").delete();
    }
}
