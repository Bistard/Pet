package com.example.pet;

import java.util.ArrayList;

public class Goal {
    public String name;
    public String description;
    public int startDate;
    public int endDate;
    public int type;
    public int ID;

    public Goal() {
    }  // Intended to be blank for Jackson to function

    public Goal(String name, String description, int startDate, int endDate, int type, int ID) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
        this.ID = ID;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changeDescription(String description) {
        this.description = description;
    }

    public void changeStartDate(int year, int month, int day) {
        this.startDate = year * 10000 + month * 100 + day;
    }

    public void changeEndDate(int year, int month, int day) {
        this.endDate = year * 10000 + month * 100 + day;
    }

    public void changeType(int type) {
        this.type = type;
    }

    public int startYear() {
        return this.startDate / 10000;
    }

    public int startMonth() {
        return (this.startDate % 10000) / 100;
    }

    public int startDay() {
        return this.startDate % 100;
    }

    public int endYear() {
        return this.endDate / 10000;
    }

    public int endMonth() {
        return (this.endDate % 10000) / 100;
    }

    public int endDay() {
        return this.endDate % 100;
    }

    /**
     * Removes the goal object from the goalList, and removes all its child tasks.
     */
    public void remove() {
        ArrayList<Task> toRemove = gChildren();
        for (Task t : toRemove) {
            t.remove();
        }
        User.goalList.remove(this);
        DataManager.StoreGoals("Goals.json", User.goalList);
    }

    /**
     * Get all its children
     *
     * @return ArrayList of tasks
     */
    public ArrayList<Task> gChildren() {
        ArrayList<Task> lst = new ArrayList<>();
        for (Task t : User.taskList) {
            if (t.parentGoalID == this.ID) {
                lst.add(t);
            }
        }
        return lst;
    }

    /**
     * find the object using its id.
     *
     * @param targetID the ID of the goal
     * @return the goal object, null if couldn't find any, which should not happen.
     */
    public static Goal getGoalByID(int targetID) {
        for (Goal goal : User.goalList) {
            if (goal.ID == targetID) {
                return goal;
            }
        }
        return null;
    }
}
