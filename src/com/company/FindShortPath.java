package com.company;

import java.util.List;

public class FindShortPath {

    //This function calls the helper function
    //Asks for path from 0 to 0, passing in a set of vertices
    TSPSolution BruteForce(int vertices, float[][] edgeCosts){
        int[] toVisit = new int[vertices-1];
        for(int x = 0; x < vertices-1; x++){
            toVisit[x] = x + 1;
        }
        TSPSolution solution = RecursiveBruteForce(0, 0, toVisit, edgeCosts);
        return solution;
    }

    //"Helper" recursive brute force algorithm
    TSPSolution RecursiveBruteForce(int start, int end, int[] toVisit, float[][] edgeCosts){
        TSPSolution solution = new TSPSolution();
        int[] verticesLeft = new int[toVisit.length-1];

        //If we only have one vertex left, create path from that to end
        //Return vertex and cost from start to vertex to end
        if(toVisit.length == 1){
            //Calculate path from start to toVisit node to end
            solution.cost = edgeCosts[start][toVisit[0]] + edgeCosts[toVisit[0]][end];
            solution.path.add(toVisit[0]);
        //If we have more than one node left
        } else {
            TSPSolution potentialSolution = new TSPSolution();
            int vertex;
            //Loop through each vertex in our "toVisit" array
            for(int idx = 0; idx < toVisit.length; idx++){
                //Get actual integer value of vertex at index
                vertex = toVisit[idx];
                //Get set of vertices left to visit (not including current vertex)
                for(int leftIdx = 0; leftIdx < toVisit.length-1; leftIdx++){
                    if(leftIdx >= idx){
                        verticesLeft[leftIdx] = toVisit[leftIdx+1];
                    } else if(leftIdx < idx) {
                        verticesLeft[leftIdx] = toVisit[leftIdx];
                    }
                }
                //Calling brute force algorithm on the vertices left
                potentialSolution = RecursiveBruteForce(vertex, end, verticesLeft, edgeCosts);

                //Set initial values for potential solution or found a better solution
                if(idx == 0 || potentialSolution.cost + edgeCosts[start][vertex] < solution.cost){
                    solution.cost = potentialSolution.cost + edgeCosts[start][vertex];
                    solution.path.clear();
                    //Transferring potential solution path to solution path
                    for(int x = 0; x < potentialSolution.path.size(); x++){
                        solution.path.add(x, potentialSolution.path.get(x));
                    }
                    //Add current vertex to path
                    solution.path.add(0, vertex);
                }
            }
        }
        return solution;
    }
}
