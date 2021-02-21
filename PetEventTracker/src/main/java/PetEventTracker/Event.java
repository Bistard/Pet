package PetEventTracker;

import java.util.ArrayList;

public abstract class Event {
    static ArrayList<Goal> goalList = new ArrayList<>();
    static ArrayList<Task> taskList = new ArrayList<>();

    public String name;
    public String description;
    public int eventDate;

    static int loadEvents(String path) {
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
     * Checks if the formate of the date is valid. Assumes day cannot be greater
     * than 31, month cannot be greater than 12, and year is 4 digit.
     * @param date  The date to be checked
     * @return      false if format is incorrect;
     *              true  if format is correct.
     */
    static boolean checkDateFormat(int date){
        if (date > 99999999 || date < 10000000) return false;
        if (date % 100 > 31 || date % 10000 / 100 > 12) return false;
        return true;
    }
}

class Goal extends Event {

    public Goal(String name, String description, int eventDate) {
        if (!Event.checkDateFormat(eventDate)){
            throw new IllegalArgumentException("Date should be formated as YYYYMMDD");
        }
        goalList.add(this);
        this.name = name;
        this.description = description;
        this.eventDate = eventDate;
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
    public static ArrayList<Goal> getGoals(int date){
        if (!Event.checkDateFormat(date)){
            throw new IllegalArgumentException("Date should be formated as YYYYMMDD");
        }
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

    public Task(String name, String description, int eventDate, Goal parent) {
        if (!Event.checkDateFormat(eventDate)){
            throw new IllegalArgumentException("Date should be formated as YYYYMMDD");
        }
        taskList.add(this);
        this.name = name;
        this.description = description;
        this.eventDate = eventDate;
        this.parentGoal = parent;
    }
    public Task(String name, String description, int eventDate, Goal parent, 
            int endDate, String recurringRule) {
        if (!Event.checkDateFormat(eventDate) || !Event.checkDateFormat(endDate)) {
            throw new IllegalArgumentException("Date should be formated as YYYYMMDD");
        }
        taskList.add(this);
        this.name = name;
        this.description = description;
        this.eventDate = eventDate;
        this.parentGoal = parent;
        this.isRecurring = true;
        this.endDate = endDate;
        this.recurringRule = recurringRule;
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
    public static ArrayList<Task> getTasks(int date){
        if (!Event.checkDateFormat(date)){
            throw new IllegalArgumentException("Date should be formated as YYYYMMDD");
        }
        
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
}
