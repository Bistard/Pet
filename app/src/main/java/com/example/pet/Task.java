package com.example.pet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

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

    /**
     * Removes the task object from the taskList.
     */
    public void remove() {
        User.taskList.remove(this);
        DataManager.StoreTasks("Tasks.json", User.taskList);
    }

    /**
     * Find the value of its parent's type attribute.
     *
     * @return an integer indicating the parent's type.
     */
    public int parentType() {
        return gParentGoal().type;
    }

    /**
     * Find the parent of this task.
     *
     * @return Goal object.
     */
    public Goal gParentGoal() {
        return Goal.getGoalByID(this.parentGoalID);
    }

    /**
     * Generates a list of dates according to the recurring rule.
     * Can be called to update the list when the recurring rule has been updated.
     */
    private void updateRecurringList() {
        if (isRecurring) {
            recurringDate.add(eventDate);
            recurringDate.add(eventDate + 1);
            recurringDate.add(eventDate + 2);
            recurringDate.add(eventDate + 3);
            // need logic

        } else {
            recurringDate.add(eventDate);
        }
    }

    /**
     * Increment date according to calendar rules.
     * For instance incrementDate(20200227,7) should return 20200305
     */
    private int incrementDate(int date, int increment) {

        //convert date to string and set the date format
        String oldDate = Integer.toString(date);
        //System.out.println("Date before Addition: "+ oldDate);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();

        try{
            //Setting the date to the given date
            c.setTime(sdf.parse(oldDate));
        }catch(ParseException e){
            e.printStackTrace();
        }

        //increment date
        c.add(Calendar.DAY_OF_MONTH, increment);
        //Date after adding the days to the given date
        String newDate = sdf.format(c.getTime());
        int result = Integer.parseInt(newDate);

        return result;
    }
}
