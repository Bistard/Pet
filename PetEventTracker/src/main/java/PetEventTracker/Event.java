package PetEventTracker;

import java.util.ArrayList;

public abstract class Event {
    static ArrayList<Goal> goalList = new ArrayList<> ();
    static ArrayList<Task> taskList = new ArrayList<> ();
    
    private String name;
    private String discription;
    private int eventDate;   //time of the first appearance, currently in int. i.e. 20210219
    
    final void setEvent(String name,String discription,int eventDate){
        this.name = name;
        this.discription = discription;
        this.eventDate = eventDate;
    }
    
    String getName(){
        return this.name;
    }
    String getDiscription(){
        return this.discription;
    }
    int getEventDate(){
        return this.eventDate;
    }
    // need methods to change the value; consider saving after the change?
    
    /*
    static int loadEvents(){
        // load all the events into goalList and taskList
        // return 0 if successful
    }
    
    static int saveEvents(){
        // save all the events to local file
        // return 0 if successful
    }
    */
    
    static ArrayList<Goal> getGoals(int date){
        // code here to return all goals for the day
        // may be placed in Goal class
        return goalList;
    }
    
    static ArrayList<Task> getTasks(int date){
        // code here to return all tasks for the day
        // may be placed in Goal class
        return taskList;
    }
    
}

class Goal extends Event {
    public Goal(String name,String discription,int eventDate){
        goalList.add(this);
        setEvent(name,discription,eventDate);
    }
}

class Task extends Event {
    private Goal parentGoal;
    
    // need to add logic for recurring events
    
    public Task (String name,String discription,int eventDate,Goal parent){
        taskList.add(this);
        setEvent(name,discription,eventDate);
        this.parentGoal = parent;
    }
    
    public Goal getParent(){
        return this.parentGoal;
    }
}
