package com.example.petevent;

import java.util.ArrayList;

public class Goal {
    public String name;
    public String description;
    public int startDate;
    public int endDate;
    public int type;
    public int ID;

    public Goal(){}

    public Goal(String name, String description, int startDate, int endDate,int type,int ID) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
        this.ID = ID;
    }

}
