package com.example.petevent;

import java.io.File;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;


public abstract class Event {
    static ArrayList<Goal> goalList = new ArrayList<>();
    static ArrayList<Task> taskList = new ArrayList<>();

    public String name;
    public String description;
    public int eventDate;

    static int loadEvents(String path) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File(path), goalList.get(0));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // load all the events into goalList and taskList
        // return 0 if successful
        return -1;
    }

    static int saveEvents(String path) {
        // save all the events to local file
        // return 0 if successful
        return -1;
    }

    /**
     * Convert string date to int date
     *
     * @param date   "YYYY" or "MON DD YYYY"
     * @return       int in YYYYMMDD
     */
    static int stringDateToInt(String date) {
        if (date.length() == 4) {
            return Integer.parseInt(date) * 10000;
        } else if (date.length() == 11){
            int month;
            switch (date.substring(0, 3)) {
                case "JAN":
                    month = 1;
                    break;
                case "FEB":
                    month = 2;
                    break;
                case "MAR":
                    month = 3;
                    break;
                case "APR":
                    month = 4;
                    break;
                case "MAY":
                    month = 5;
                    break;
                case "JUN":
                    month = 6;
                    break;
                case "JUL":
                    month = 7;
                    break;
                case "AUG":
                    month = 8;
                    break;
                case "SEP":
                    month = 9;
                    break;
                case "OCT":
                    month = 10;
                    break;
                case "NOV":
                    month = 11;
                    break;
                case "DEC":
                    month = 12;
                    break;
                default:
                    throw new IllegalArgumentException("Date should be formated as MON-DD-YYYY");
            }
            return Integer.parseInt(date.substring(7, 11))*10000 +
                    month*100 + Integer.parseInt(date.substring(4, 6));
        }
        throw new IllegalArgumentException("Date format is incorrect");
    }
}

class Goal extends Event {

    public Goal(String name, String description, String date) {
        this.eventDate = Event.stringDateToInt(date);
        this.name = name;
        this.description = description;
        goalList.add(this);
    }

    /**
     * Find all the goals that is set for the inputted date.
     *
     * @param date  The date to be searched in YYYYMMDD
     *              If date ends with XXXX0000, return all goals in that year;
     *              If date ends with XXXXXX00, return all goals in that month;
     *              If date ends with XXXXXXXX, return all goals in that day.
     * @return      An arraylist of goals happening at the specified time.
     */
    public static ArrayList<Goal> getGoals(String strdate){
        int date = Event.stringDateToInt(strdate);
        ArrayList<Goal> lst = new ArrayList<>();
        if (date % 10000 == 0) {
            for (int i = 0; i < Event.goalList.size(); i++) {
                if (Event.goalList.get(i).eventDate / 10000 == date / 10000) {
                    lst.add(Event.goalList.get(i));
                }
            }
        } else if (date % 100 == 0) {
            for (int i = 0; i < Event.goalList.size(); i++) {
                if (Event.goalList.get(i).eventDate / 100 == date / 100) {
                    lst.add(Event.goalList.get(i));
                }
            }
        } else {
            for (int i = 0; i < Event.goalList.size(); i++) {
                if (Event.goalList.get(i).eventDate == date) {
                    lst.add(Event.goalList.get(i));
                }
            }
        }
        return lst;
    }
}

class Task extends Event {

    public Goal parentGoal;

    public boolean isRecurring = false;
    public String recurringRule;
    public int endDate;
    public ArrayList<Integer> recurringDate = new ArrayList<>();

    public Task(String name, String description, String date, Goal parent) {
        this.eventDate = Event.stringDateToInt(date);
        this.name = name;
        this.description = description;
        this.parentGoal = parent;
        updateRecurringList();
        taskList.add(this);
    }

    public Task(String name, String description, String date, Goal parent,
                String strEndDate, String recurringRule) {
        this.eventDate = Event.stringDateToInt(date);
        this.endDate = Event.stringDateToInt(strEndDate);
        this.name = name;
        this.description = description;
        this.parentGoal = parent;
        this.isRecurring = true;
        this.recurringRule = recurringRule;
        updateRecurringList();
        taskList.add(this);
    }

    /**
     * Find all the goals that is set for the inputted date.
     *
     * @param date  The date to be searched in YYYYMMDD
     *              If date ends with XXXX0000, return all goals in that year;
     *              If date ends with XXXXXX00, return all goals in that month;
     *              If date ends with XXXXXXXX, return all goals in that day.
     * @return      An arraylist of goals happening at the specified time.
     *              Recurring task would only appear once.
     */
    public static ArrayList<Task> getTasks(String strdate){
        int date = Event.stringDateToInt(strdate);

        // need logic to deal with recurring event

        ArrayList<Task> lst = new ArrayList<>();
        if (date % 10000 == 0) {
            for (int i = 0; i < Event.taskList.size(); i++) {
                if (Event.taskList.get(i).eventDate / 10000 == date / 10000) {
                    lst.add(Event.taskList.get(i));
                }
            }
        } else if (date % 100 == 0) {
            for (int i = 0; i < Event.taskList.size(); i++) {
                if (Event.taskList.get(i).eventDate / 100 == date / 100) {
                    lst.add(Event.taskList.get(i));
                }
            }
        } else {
            for (int i = 0; i < Event.taskList.size(); i++) {
                if (Event.taskList.get(i).eventDate == date) {
                    lst.add(Event.taskList.get(i));
                }
            }
        }
        return lst;
    }

    private void updateRecurringList(){
        if(isRecurring){

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
    private int incrementDate(int date, int increment){
        return -1;
    }

    private int incrementDate(int date){
        return incrementDate(date,1);
    }
}
