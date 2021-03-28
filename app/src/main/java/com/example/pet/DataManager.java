package com.example.pet;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataManager {
    static File path;

    static void init(File path) {
        DataManager.path = path;
    }

    static void StoreUser(String filename, User obj) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File(path, filename), obj);
        } catch (IOException e) {
            Log.i("DM", e.toString());
        }
    }

    static User LoadUser(String filename) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File(path, filename), User.class);
    }

    static void StoreGoals(String filename, ArrayList<Goal> lst) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File(path, filename), lst);
        } catch (IOException e) {
            Log.i("DM", e.toString());
        }
    }

    static List<Goal> LoadGoals(String filename) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        List<Goal> lst = Arrays.asList(mapper.readValue(new File(path, filename), Goal[].class));
        return lst;
    }

    static void StoreTasks(String filename, ArrayList<Task> lst) {
        ArrayList<TaskData> toStore= new ArrayList<>();
        for (Task t : lst) {
            toStore.add(new TaskData(t));
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File(path, filename), toStore);
        } catch (IOException e) {
            Log.i("DM", e.toString());
        }
    }

    static List<Task> LoadTasks(String filename) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        List<TaskData> lst = Arrays.asList(mapper.readValue(new File(path, filename), TaskData[].class));
        List<Task> ans = new ArrayList<>();
        for (TaskData td:lst){
            ans.add(new Task(td));
        }
        return ans;
    }


}

/**
 * Extra class used to store task data
 */
class TaskData {
    public String name;
    public String description;
    public int eventDate;
    public int parentGoalID;
    public boolean isRecurring = false;
    public String recurringRule;
    public int endDate;
    public ArrayList<Integer> finished = new ArrayList<>();
    public ArrayList<Integer> recurringDates = new ArrayList<>();

    TaskData() {
    }

    TaskData(Task t) {
        this.name = t.name;
        this.description = t.description;
        this.eventDate = t.eventDate;
        this.parentGoalID = t.parentGoalID;
        this.isRecurring = t.isRecurring;
        this.recurringRule = t.recurringRule;
        this.endDate = t.endDate;
        this.finished = t.finished;
        this.recurringDates = t.recurringDates;
    }
}