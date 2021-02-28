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
