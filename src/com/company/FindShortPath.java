package com.company;

import javax.xml.crypto.dsig.keyinfo.KeyValue;
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

    //This function generates a "toVisit" list and then calls the recursive helper function
    public TSPSolution DynamicProgramming(int start, int end, int vertices, double[][] edgeCosts){
        int[] toVisit = new int[vertices-1];
        int x;
        //Assuming graph starts and ends at 0
        for(x = 0; x < vertices-1; x++){
            toVisit[x] = x+1;
        }
        TSPSolution solution = new TSPSolution();
        //Set to 1 because we need a row for empty set
        int size = 1;
        //Determine size necessary for sub solution table
        /*for(int setSize = 1; setSize <= vertices; setSize++){
            size = size + factorial(vertices)/(factorial(setSize) * factorial(vertices-setSize));
        }*/
        SubSolution[][] subSolutionTable = new SubSolution[(int) Math.pow(2, vertices-1)][vertices];

        SubSolution finalSubSolution = RecursiveDynamicProgramming(start, end, toVisit, edgeCosts, subSolutionTable);
        int nextVertex = start;
        int remainingNodes = (int) (Math.pow(2, vertices-1)-1);
        for(x = 0; x < vertices-1; x++){
            nextVertex = subSolutionTable[remainingNodes][nextVertex].firstStep;
            remainingNodes = (int) (remainingNodes - Math.pow(2, nextVertex-1));
            solution.path.add(nextVertex);
        }
        solution.cost = finalSubSolution.cost;
        return solution;
    }

    int factorial(int num){
        if(num <= 1){
            return 1;
        }
        int result = 1;
        for(int x = 2; x <= num; x++){
            result = result * x;
        }
        return result;
    }

    SubSolution RecursiveDynamicProgramming(int start, int end, int[] toVisit, double[][] edgeCosts,
                                                SubSolution[][] subSolutionTable){
        double leastCost = 0;
        int setIdx;
        SubSolution subSolution = new SubSolution();
        //If there is already a solution in the table, simply return it
        if(subSolutionTable[convertSetToIndex(toVisit)][start] != null){
            return subSolutionTable[convertSetToIndex(toVisit)][start];
            //If we only have one vertex to visit
        } else if(toVisit.length == 1){
            setIdx = convertSetToIndex(toVisit);
            //Save cost in table as cost from vertex to visit to end
            leastCost = edgeCosts[start][toVisit[0]] + edgeCosts[toVisit[0]][end];
            subSolution.cost = leastCost;
            //Save first step as the only vertex in the set
            subSolution.firstStep = toVisit[0];
            subSolutionTable[setIdx][start] = subSolution;
        } else {
            SubSolution potentialSubSolution = new SubSolution();
            int vertex;
            int[] leftToVisit = new int[toVisit.length-1];
            for(int idx = 0; idx < toVisit.length; idx++){
                //Get actual integer value of vertex at index
                vertex = toVisit[idx];
                //Get set of vertices left to visit (not including current vertex)
                for(int leftIdx = 0; leftIdx < toVisit.length-1; leftIdx++) {
                    //Skip element at index in array
                    if (leftIdx >= idx) {
                        leftToVisit[leftIdx] = toVisit[leftIdx + 1];
                    } else if (leftIdx < idx) {
                        leftToVisit[leftIdx] = toVisit[leftIdx];
                    }
                }

                    potentialSubSolution = RecursiveDynamicProgramming(vertex, end, leftToVisit, edgeCosts, subSolutionTable);
                    //Set initial values for potential solution or find a better solution
                    if(idx == 0 || potentialSubSolution.cost + edgeCosts[start][vertex] < subSolution.cost){
                        subSolution.cost = potentialSubSolution.cost + edgeCosts[start][vertex];
                        subSolution.firstStep = vertex;
                    }

            }
            setIdx = convertSetToIndex(toVisit);
            subSolutionTable[setIdx][start] = subSolution;
        }
        return subSolution;
    }

    int convertSetToIndex(int[] vertexSet){
        int result = 0;
        for(int x = 0; x < vertexSet.length; x++){
            result = (int) (result + Math.pow(2, vertexSet[x] - 1));
        }
        return result;
    }
}
