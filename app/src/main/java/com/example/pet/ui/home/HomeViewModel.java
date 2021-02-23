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
         * Tester for I/O
         */
        mText = new MutableLiveData<>();
        User user = User.Initialize();
        Goal g = user.addGoal("","",20210220,20210222,0);
        user.addTask("Task 1","This is for goal 1", 20210221, g.ID);
        ArrayList<Task> tasks = user.getTasks(20210221);
        mText.setValue(tasks.get(0).name);
    }

    public LiveData<String> getText() {
        return mText;
    }
}