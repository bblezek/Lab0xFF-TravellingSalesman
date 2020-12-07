package com.company;

import java.util.ArrayList;
import java.util.List;

public class TSPSolution {

    public double cost;
    public List<Integer> path;
    //For Ant Colony solution
    int steps;

    public TSPSolution(){
        steps = 0;
        cost = 0;
        path = new ArrayList<Integer>();
    }


}
