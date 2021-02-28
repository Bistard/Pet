package com.example.pet.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.pet.*;

import java.util.ArrayList;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HomeViewModel() {

        /**
         * Date Display
         */


        /**
         * Tester for I/O
         */
        mText = new MutableLiveData<>();
        User user = User.Initialize();
        Goal g = user.addGoal("","",2021,02,20,2021,02,22,0);
        user.addTask("Task 1","This is for goal 1", 2021,02,21, g.ID);
        ArrayList<Task> tasks = user.getTasks(2021,02,21);
        mText.setValue(tasks.get(0).name);
    }

    public LiveData<String> getText() {
        return mText;
    }
}