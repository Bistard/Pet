package com.example.petevent;

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

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartDate(int startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(int endDate) {
        this.endDate = endDate;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void remove() {
        // code goes here
    }

    public ArrayList<Task> getChild() {
        // code goes here
        return null;
    }

    public static Goal getGoalByID(int targetID) {
        for (Goal goal : User.goalList) {
            if (goal.ID == targetID) {
                return goal;
            }
        }
        return null;
    }
}
