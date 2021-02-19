package PetEventTracker;

public class Main {
    public static void main(String[] args) {
        Goal g1 = new Goal("Goal 1","Discription 1",20210219);
        new Task("Task 1","Task Discription",20210218,g1);
        System.out.println(Event.getTasks(20210218).get(0).getDiscription());
    }
    
}
