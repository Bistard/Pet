package com.example.pet;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class Pet {
    public String name;

    public String[] phrases = {
            "$$HOUR LEFT$$ hours left for today, $$NUMBER OF UNFINISHED$$ uncompleted tasks",
            "You got this! Just $$NUMBER OF UNFINISHED$$ more tasks",
            "How is $$UNFINISHED TASK$$ going?",
            "Good job on finishing all the tasks for today, you are one more step towards $$LONG TERM GOAL NAME$$",
            "You have finished $$GOAL PERCENT$$% of your goal $$GOAL NAME$$! Want to do more?",
            "$$PET NAME$$ has finished a lot today, how about you?"
    };

    public Pet() {
    }

    public Pet(String name) {
        this.name = name;
    }

    @JsonIgnore
    public String getPhrase() {
        Random rand = new Random();
        String phrase = phrases[rand.nextInt(phrases.length)];

        User user = User.Initialize();
        int currentYear = Integer.parseInt(new SimpleDateFormat("yyyy", Locale.getDefault()).format(new java.util.Date()));
        int currentMonth = Integer.parseInt(new SimpleDateFormat("M", Locale.getDefault()).format(new java.util.Date()));
        int currentDate = Integer.parseInt(new SimpleDateFormat("dd", Locale.getDefault()).format(new Date()));
        int currentHour = new Date().getHours();

        ArrayList<Task> overdue = user.getUnfinishedTasks(currentYear, currentMonth, currentDate);
        ArrayList<Task> finished = new ArrayList<>();
        ArrayList<Task> unfinished = new ArrayList<>();
        for (Task t : user.getTasks(currentYear, currentMonth, currentDate)) {
            if (t.isFininshed()) {
                finished.add(t);
            } else {
                unfinished.add(t);
            }
        }
        ArrayList<Goal> curGoals = user.getGoals(currentYear, currentMonth, currentDate);
        Goal chosenGoal = null;
        if (curGoals.size() > 0) {
            chosenGoal = curGoals.get(rand.nextInt(curGoals.size()));
        }

        phrase = phrase.replaceAll("\\$\\$PET NAME\\$\\$", this.name)
                .replaceAll("\\$\\$HOUR LEFT\\$\\$", "" + (24 - currentHour))
                .replaceAll("\\$\\$LONG TERM GOAL NAME\\$\\$", user.longTermGoal)
                .replaceAll("\\$\\$LONG TERM GOAL PERCENT\\$\\$", user.longTermGoalFinishPercentString());

        if (phrase.contains("$$FINISHED TASK$$") || phrase.contains("$$NUMBER OF UNFINISHED$$")) {
            if (finished.size() > 0) {
                phrase = phrase.replaceAll("\\$\\$FINISHED TASK\\$\\$", finished.get(rand.nextInt(finished.size())).name())
                        .replaceAll("\\$\\$NUMBER OF FINISHED\\$\\$", "" + unfinished.size());
            } else {
                throw new NullPointerException("No finished tasks today");
            }
        }
        if (phrase.contains("$$OVERDUE TASK$$") || phrase.contains("$$NUMBER OF OVERDUE$$")) {
            if (overdue.size() > 0) {
                phrase = phrase.replaceAll("\\$\\$OVERDUE TASK\\$\\$", finished.get(rand.nextInt(overdue.size())).name())
                        .replaceAll("\\$\\$NUMBER OF OVERDUE\\$\\$", "" + overdue.size());
            } else {
                throw new NullPointerException("No overdue tasks today");
            }
        }
        if (phrase.contains("$$UNFINISHED TASK$$") || phrase.contains("$$NUMBER OF ALL UNFINISHED$$")) {
            if (unfinished.size() > 0) {
                phrase = phrase.replaceAll("\\$\\$UNFINISHED TASK\\$\\$", unfinished.get(rand.nextInt(unfinished.size())).name())
                        .replaceAll("\\$\\$NUMBER OF UNFINISHED\\$\\$", "" + (unfinished.size() + overdue.size()));
            } else {
                throw new NullPointerException("No unfinished tasks today");
            }
        }
        if (phrase.contains("$$GOAL NAME$$")) {
            if (chosenGoal != null) {
                phrase = phrase.replaceAll("\\$\\$GOAL NAME\\$\\$", chosenGoal.name())
                        .replaceAll("\\$\\$GOAL PERCENT\\$\\$", chosenGoal.finishPercentString());
            } else {
                throw new NullPointerException("No goal today");
            }
        }
        if (phrase.contains("$$GOAL PERCENT$$")) {
            throw new NullPointerException("No goal chosen");
        }

        return phrase;
    }

}
