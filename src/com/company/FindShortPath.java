package com.company;

import java.util.List;

public class FindShortPath {

    //Greedy algorithm - takes shortest path from start node, then shortest path from next node, etc.
    public TSPSolution Greedy(int start, int end, int vertices, double[][] edgeCosts){
        TSPSolution solution = new TSPSolution();
        //Stores whether we have already "visited" the vertex before
        Boolean[] vertexVisited = new Boolean[vertices];
        int vertex, src, dest, nextVertex;
        double leastCost;

        //Filling vertex visited array with "false"
        for(vertex = 0; vertex < vertices; vertex++){
            vertexVisited[vertex] = false;
        }

        //Start at "start" vertex
        nextVertex = start;
        //Loop until we have all vertices
        while(solution.path.size() < vertices-1){
            src = nextVertex;
            vertexVisited[src] = true;
            leastCost = Double.MAX_VALUE;
            //Check all vertices to see if we have visited and whether that path is smaller
            for(dest = 0; dest < vertices; dest++){
                if(!vertexVisited[dest] && edgeCosts[src][dest] < leastCost){
                    leastCost = edgeCosts[src][dest];
                    nextVertex = dest;
                }
            }

            //Add vertex with smallest path from source vertex to solution
            solution.path.add(nextVertex);
            vertexVisited[nextVertex] = true;
            solution.cost = solution.cost + leastCost;
        }

        //Add cost from last vertex to end to total cost
        solution.cost = solution.cost + edgeCosts[nextVertex][end];
        return solution;
    }

    //This function calls the helper function
    //Asks for path from 0 to 0, passing in a set of vertices
    public TSPSolution BruteForce(int start, int end, int vertices, double[][] edgeCosts){
        int[] toVisit = new int[vertices-1];
        for(int x = 0; x < vertices-1; x++){
            toVisit[x] = x + 1;
        }
        TSPSolution solution = RecursiveBruteForce(start, end, toVisit, edgeCosts);
        return solution;
    }

    //"Helper" recursive brute force algorithm
    TSPSolution RecursiveBruteForce(int start, int end, int[] toVisit, double[][] edgeCosts){
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
