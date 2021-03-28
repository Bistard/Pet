package com.example.pet;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class User {
    public static boolean IsInitialized = false;
    public static User self;

    public boolean isFirstTime = true;

    public String longTermGoal;
//    public String longTermGoalType;
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
                IsInitialized = true;
                self = new User();
                return self;
            }
            self.LoadFiles();
        }
        IsInitialized = true;
        return self;
    }

    public Pet addPet(String name) {
        this.pet = new Pet(name);
        return this.pet;
    }

    /**
     * get the name of the pet
     *
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

    @JsonIgnore
    public ArrayList<Goal> getGoals() {
        return goalList;
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

    @JsonIgnore
    public ArrayList<Task> getTasks(Goal goal) {
        ArrayList<Task> lst = new ArrayList<>();
        for (Task t : taskList) {
            if (t.parentGoalID == goal.ID) {
                for (int d : t.recurringDates) {
                    lst.add(t.makeChild(d));
                }
            }
        }
        return lst;
    }

    @JsonIgnore
    public ArrayList<Task> getTasks() {
        ArrayList<Task> lst = new ArrayList<>();
        for (Task t : taskList) {
            for (int d : t.recurringDates) {
                lst.add(t.makeChild(d));
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

    public double longTermGoalFinishPercent(){
        int finished = 0;
        int total = 0;
        for (Task t : this.getTasks()) {
            total++;
            if (t.isFininshed()) {
                finished++;
            }
        }
        if (total==0){
            return 1;
        }
        return (double) finished / (double) total;
    }

    public String longTermGoalFinishPercentString() {
        DecimalFormat df = new DecimalFormat("##0.0");
        return df.format(longTermGoalFinishPercent() * 100.0);
    }

    public static String month2string(int month){
        switch (month) {
            case 1:
                return "Jan.";
            case 2:
                return "Feb.";
            case 3:
                return "Mar.";
            case 4:
                return "Apr.";
            case 5:
                return "May ";
            case 6:
                return "Jun.";
            case 7:
                return "Jul.";
            case 8:
                return "Aug.";
            case 9:
                return "Sep.";
            case 10:
                return "Oct.";
            case 11:
                return "Nov.";
            case 12:
                return "Dec";
            default:
                return "MONTH";
        }
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
