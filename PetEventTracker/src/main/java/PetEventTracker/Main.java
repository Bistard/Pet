package PetEventTracker;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Goal g1 = new Goal("Goal 1","Description 1",20210219);
        new Task("Task 1","Task 1 Description",20210218,g1);
        new Task("Task 2","Task 2 Description",20210218,g1,20210318,"Weekly");
        new Task("Task 3","Task 3 Description",20210220,g1);
        new Task("Task 4","Task 4 Description",20210320,g1);
        
        Goal.getGoals(20210230);
        
        ArrayList<Task> lst = Task.getTasks(20210000);
        System.out.println(lst);
    }
    
}
