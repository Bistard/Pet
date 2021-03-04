package com.example.pet;

import java.util.ArrayList;
import java.util.Arrays;

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

        // seperate out the year,month and day from given date
        int year = date / 1000;
        int rest = date - (year * 10000);
        int month = rest / 100;
        int day = rest - (month * 100);
        boolean leap_year = false; // if the year have 366 days

        //define the months with 31, 30 or feb
        Integer[] thirty_one_days = {1, 3, 5, 7, 8, 10, 12};
        Integer[] thirty_days = {4, 6, 9, 11};
        int special_month = 2;
        int feb_days = 28;

        //check leap_year

        if ((year % 4).intValue() == 0) {
            leap_year = true;
            feb_days = 29;
        }

        // check how many days is in the month
        String check_31 = Arrays.toString(thirty_one_days);
        boolean is_thirty_one_days = check_31.contains(Integer.toString(month));

        String check_30 = Arrays.toString(thirty_days);
        boolean is_thirty_days = check_30.contains(Integer.toString(month));


        //increment the date
        day += increment;

        while (increment > 365) {
            //check if need to update the month
            if (is_thirty_one_days) {
                System.out.println(" 31 days month");
                if (day > 31) {
                    month += 1;
                    day -= 31;
                }

            } else if (is_thirty_days) {
                System.out.println(" 30 days month");
                {
                    if (day > 30) {
                        month += 1;
                        day -= 30;
                    }
                }
            } else {
                System.out.println(" feb");
                if (day > feb_days) {
                    month += 1;
                    day -= feb_days;
                }
            }

            //check if need to update year
            if (month > 12) {
                month -= 12;
                year += 1;
            }

        }
        return year * 10000 + month * 1000 + day;
    }
}
