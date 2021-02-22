package com.example.petevent;

import java.util.ArrayList;

public class Task {

    public String name;
    public String description;
    public int eventDate;

    public int parentGoalID;

    public boolean isRecurring = false;
    public String recurringRule;
    public int endDate;
    public ArrayList<Integer> recurringDate = new ArrayList<>();

    public Task() {
    }  // Intended to be blank for Jackson to function

    public Task(String name, String description, int startDate, int endDate,
                String recurringRule, int parentID) {
        this.name = name;
        this.description = description;
        this.parentGoalID = parentID;
        this.recurringRule = recurringRule;

        if (recurringRule == "Once") {
            isRecurring = false;
            eventDate = startDate;
        } else {
            this.eventDate = startDate;
            this.endDate = endDate;
            this.isRecurring = true;
        }
        updateRecurringList();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEventDate(int eventDate) {
        this.eventDate = eventDate;
    }

    public void setEndDate(int endDate) {
        this.endDate = endDate;
    }

    public void setParent(int parentGoalID) {
        this.parentGoalID = parentGoalID;
    }

    public void setParent(Goal parentGoal) {
        this.parentGoalID = parentGoal.ID;
    }

    public void setRecurringRule(int eventDate, int endDate, String recurringRule) {
        if (recurringRule == "Once") {
            this.isRecurring = false;
        } else {
            this.isRecurring = true;
        }
        this.eventDate = eventDate;
        this.endDate = endDate;
        this.recurringRule = recurringRule;
        updateRecurringList();
    }

    public void remove() {
        User.taskList.remove(this);
        DataManager.StoreTasks("Tasks.json", User.taskList);
    }

    public int parentType() {
        return getParentGoal().type;
    }

    public Goal getParentGoal() {
        return Goal.getGoalByID(this.parentGoalID);
    }

    private void updateRecurringList() {
        if (isRecurring) {
            recurringDate.add(eventDate);
            // need logic

        } else {
            recurringDate.add(eventDate);
        }
    }

    /**
     * Increment date according to calendar rules.
     * For instance incrementDate(20200227,7) should return 20200305
     *
     * @param date
     * @param increment
     * @return
     */
    private int incrementDate(int date, int increment) {
        return -1;
    }

    private int incrementDate(int date) {
        return incrementDate(date, 1);
    }
}