package com.example.pet;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

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
    public ArrayList<Integer> finished = new ArrayList<>();
    public ArrayList<Integer> recurringDates = new ArrayList<>();

    public Task parent;
    public int childDate;

    /**
     * Reserved for Jackson
     */
    public Task() {
    }

    /**
     * To be used for User class
     *
     * @param name
     * @param description
     * @param startDate
     * @param endDate
     * @param recurringRule
     * @param parentID
     */
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

    /**
     * To be used for DataManager to read task object
     *
     * @param td
     */
    public Task(TaskData td) {
        this.name = td.name;
        this.description = td.description;
        this.eventDate = td.eventDate;
        this.parentGoalID = td.parentGoalID;
        this.isRecurring = td.isRecurring;
        this.recurringRule = td.recurringRule;
        this.endDate = td.endDate;
        this.finished = td.finished;
        this.recurringDates = td.recurringDates;
    }

    /**
     * To be used to create temporary object for UI
     *
     * @param parent
     * @param childDate
     */
    private Task(Task parent, int childDate) {
        this.parent = parent;
        this.childDate = childDate;
    }

    /**
     * Create a temporary task object for UI, stores date instance information
     *
     * @param date date instance
     * @return temp task object
     */
    public Task makeChild(int date) {
        Task task = new Task(this, date);
        return task;
    }

    public void changeFinished() {
        int indx = this.parent.recurringDates.indexOf(this.childDate);
        this.parent.finished.set(indx, this.parent.finished.get(indx) == 0 ? 1 : 0);
    }

    public Boolean isFininshed() {
        int indx = this.parent.recurringDates.indexOf(this.childDate);
        return this.parent.finished.get(indx) == 1;
    }

    public void changeName(String name) {
        this.parent.name = name;
    }

    public void changeDescription(String description) {
        this.parent.description = description;
    }

    public void changeEventDate(int year, int month, int day) {
        this.parent.eventDate = year * 10000 + month * 100 + day;
    }

    public void changeEndDate(int year, int month, int day) {
        this.parent.endDate = year * 10000 + month * 100 + day;
    }

    public void changeParent(Goal parentGoal) {
        this.parent.parentGoalID = parentGoal.ID;
    }

    public void changeRecurringRule(int startYear, int startMonth, int startDay, int endYear,
                                    int endMonth, int endDay, String recurringRule) {
        eventDate = startYear * 10000 + startMonth * 100 + startDay;
        endDate = endYear * 10000 + endMonth * 100 + endDay;
        if (recurringRule == "Once") {
            this.parent.isRecurring = false;
        } else {
            this.parent.isRecurring = true;
        }
        this.parent.eventDate = eventDate;
        this.parent.endDate = endDate;
        this.parent.recurringRule = recurringRule;
        this.parent.updateRecurringList();
    }

    public void changeRecurringRule(int startYear, int startMonth, int startDay, String recurringRule) {
        changeRecurringRule(startYear, startMonth, startDay, getParentGoal().endYear(),
                getParentGoal().endMonth(), getParentGoal().endDay(), recurringRule);
    }

    public int startYear() {
        return this.parent.eventDate / 10000;
    }

    public int startMonth() {
        return (this.parent.eventDate % 10000) / 100;
    }

    public int startDay() {
        return this.parent.eventDate % 100;
    }

    public int endYear() {
        return this.parent.endDate / 10000;
    }

    public int endMonth() {
        return (this.parent.endDate % 10000) / 100;
    }

    public int endDay() {
        return this.parent.endDate % 100;
    }

    public int Year() {
        return this.childDate / 10000;
    }

    public int Month() {
        return (this.childDate % 10000) / 100;
    }

    public int Day() {
        return this.childDate % 100;
    }

    @JsonIgnore
    public String name() {
        return this.parent.name;
    }

    public String description() {
        return this.parent.description;
    }

    public boolean isRecurring() {
        return this.parent.isRecurring;
    }

    public String recurringRule() {
        return this.parent.recurringRule;
    }

    /**
     * Removes the task object from the taskList.
     */
    public void remove() {
        User.taskList.remove(this.parent);
        DataManager.StoreTasks("Tasks.json", User.taskList);
    }

    /**
     * Find the value of its parent's type attribute.
     *
     * @return an integer indicating the parent's type.
     */
    public int parentType() {
        return getParentGoal().type;
    }

    /**
     * Find the parent of this task.
     *
     * @return Goal object.
     */
    @JsonIgnore
    public Goal getParentGoal() {
        return Goal.getGoalByID(this.parent.parentGoalID);
    }

    /**
     * Generates a list of dates according to the recurring rule.
     * Can be called to update the list when the recurring rule has been updated.
     */
    private void updateRecurringList() {
        recurringDates = new ArrayList<>();
        if (isRecurring) {
            int currentDate = eventDate;
            if (recurringRule.equals("Monthly")) {
                while (currentDate <= endDate) {
                    recurringDates.add(currentDate);
                    if (currentDate % 10000 / 100 == 12) {
                        currentDate += 10000 - 1100;
                    } else {
                        currentDate += 100;
                    }
                }
            } else {
                int increment = 1;
                switch (recurringRule) {
                    case "Weekly":
                        increment = 7;
                        break;
                    case "Bi-weekly":
                        increment = 14;
                        break;
                    default:
                        increment = 1;
                }
                while (currentDate <= endDate) {
                    this.recurringDates.add(currentDate);
                    currentDate = incrementDate(currentDate, increment);
                }
            }
        } else {
            this.recurringDates.add(eventDate);
        }
        ArrayList<Integer> temp = new ArrayList<>();
        for (int i = 0; i < recurringDates.size(); i++) {
            try {
                temp.add(this.finished.get(i));
            } catch (Exception e) {
                temp.add(0);
            }
        }
        this.finished = temp;
    }

    /**
     * Increment date according to calendar rules.
     * For instance incrementDate(20200227,7) should return 20200305, in YYYYMMDD
     */
    public static int incrementDate(int date, int increment) {

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
