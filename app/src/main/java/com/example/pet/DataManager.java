package com.example.pet;

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

    static int StoreUser(String filename, User obj) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File(path, filename), obj);
        } catch (IOException e) {
            return -1;
        }
        return 0;
    }

    static User LoadUser(String filename) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File(path, filename), User.class);
    }

    static int StoreGoals(String filename, ArrayList<Goal> lst) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File(path, filename), lst);
        } catch (IOException e) {
            return 0;
        }
        return 1;
    }

    static List<Goal> LoadGoals(String filename) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        List<Goal> lst = Arrays.asList(mapper.readValue(new File(path, filename), Goal[].class));
        return lst;
    }

    static int StoreTasks(String filename, ArrayList<Task> lst) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File(path, filename), lst);
        } catch (IOException e) {
            return 0;
        }
        return 1;
    }

    static List<Task> LoadTasks(String filename) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        List<Task> lst = Arrays.asList(mapper.readValue(new File(path, filename), Task[].class));
        return lst;
    }
}
