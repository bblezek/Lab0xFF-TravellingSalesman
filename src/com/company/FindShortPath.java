package com.company;

import javax.xml.crypto.dsig.keyinfo.KeyValue;
import java.util.List;
import java.util.Random;

public class FindShortPath {

    //Greedy algorithm - takes shortest path from start node, then shortest path from next node, etc.
    public TSPSolution Greedy(int start, int end, int vertices, double[][] edgeCosts) {
        TSPSolution solution = new TSPSolution();
        //Stores whether we have already "visited" the vertex before
        Boolean[] vertexVisited = new Boolean[vertices];
        int vertex, src, dest, nextVertex;
        double leastCost;

        //Filling vertex visited array with "false"
        for (vertex = 0; vertex < vertices; vertex++) {
            vertexVisited[vertex] = false;
        }

        //Start at "start" vertex
        nextVertex = start;
        //Loop until we have all vertices
        while (solution.path.size() < vertices - 1) {
            src = nextVertex;
            vertexVisited[src] = true;
            leastCost = Double.MAX_VALUE;
            //Check all vertices to see if we have visited and whether that path is smaller
            for (dest = 0; dest < vertices; dest++) {
                if (!vertexVisited[dest] && edgeCosts[src][dest] < leastCost) {
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
    public TSPSolution BruteForce(int start, int end, int vertices, double[][] edgeCosts) {
        int[] toVisit = new int[vertices - 1];
        for (int x = 0; x < vertices - 1; x++) {
            toVisit[x] = x + 1;
        }
        TSPSolution solution = RecursiveBruteForce(start, end, toVisit, edgeCosts);
        return solution;
    }

    //"Helper" recursive brute force algorithm
    TSPSolution RecursiveBruteForce(int start, int end, int[] toVisit, double[][] edgeCosts) {
        TSPSolution solution = new TSPSolution();
        int[] verticesLeft = new int[toVisit.length - 1];

        //If we only have one vertex left, create path from that to end
        //Return vertex and cost from start to vertex to end
        if (toVisit.length == 1) {
            //Calculate path from start to toVisit node to end
            solution.cost = edgeCosts[start][toVisit[0]] + edgeCosts[toVisit[0]][end];
            solution.path.add(toVisit[0]);
            //If we have more than one node left
        } else {
            TSPSolution potentialSolution = new TSPSolution();
            int vertex;
            //Loop through each vertex in our "toVisit" array
            for (int idx = 0; idx < toVisit.length; idx++) {
                //Get actual integer value of vertex at index
                vertex = toVisit[idx];
                //Get set of vertices left to visit (not including current vertex)
                for (int leftIdx = 0; leftIdx < toVisit.length - 1; leftIdx++) {
                    if (leftIdx >= idx) {
                        verticesLeft[leftIdx] = toVisit[leftIdx + 1];
                    } else if (leftIdx < idx) {
                        verticesLeft[leftIdx] = toVisit[leftIdx];
                    }
                }
                //Calling brute force algorithm on the vertices left
                potentialSolution = RecursiveBruteForce(vertex, end, verticesLeft, edgeCosts);

                //Set initial values for potential solution or found a better solution
                if (idx == 0 || potentialSolution.cost + edgeCosts[start][vertex] < solution.cost) {
                    solution.cost = potentialSolution.cost + edgeCosts[start][vertex];
                    solution.path.clear();
                    //Transferring potential solution path to solution path
                    for (int x = 0; x < potentialSolution.path.size(); x++) {
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
    public TSPSolution DynamicProgramming(int start, int end, int vertices, double[][] edgeCosts) {
        int[] toVisit = new int[vertices - 1];
        int x;
        //Assuming graph starts and ends at 0
        //Creates list of vertices
        for (x = 0; x < vertices - 1; x++) {
            toVisit[x] = x + 1;
        }
        TSPSolution solution = new TSPSolution();
        //Create empty solution table
        SubSolution[][] subSolutionTable = new SubSolution[(int) Math.pow(2, vertices - 1)][vertices];

        //Call helper program
        SubSolution finalSubSolution = RecursiveDynamicProgramming(start, end, toVisit, edgeCosts, subSolutionTable);
        int nextVertex = start;
        int remainingNodes = (int) (Math.pow(2, vertices - 1) - 1);
        //Figuring out final solution using first steps from each step
        for (x = 0; x < vertices - 1; x++) {
            nextVertex = subSolutionTable[remainingNodes][nextVertex].firstStep;
            remainingNodes = (int) (remainingNodes - Math.pow(2, nextVertex - 1));
            solution.path.add(nextVertex);
        }

        //Cost is cost returned from recursive program
        solution.cost = finalSubSolution.cost;
        return solution;
    }

    //Helper dynamic programming function that returns the cost and first step of best solution
    SubSolution RecursiveDynamicProgramming(int start, int end, int[] toVisit, double[][] edgeCosts,
                                            SubSolution[][] subSolutionTable) {
        double leastCost = 0;
        int setIdx;
        SubSolution subSolution = new SubSolution();
        //If there is already a solution in the table, simply return it
        if (subSolutionTable[convertSetToIndex(toVisit)][start] != null) {
            return subSolutionTable[convertSetToIndex(toVisit)][start];
            //If we only have one vertex to visit
        } else if (toVisit.length == 1) {
            setIdx = convertSetToIndex(toVisit);
            //Save cost in table as cost from vertex to visit to end
            leastCost = edgeCosts[start][toVisit[0]] + edgeCosts[toVisit[0]][end];
            subSolution.cost = leastCost;
            //Save first step as the only vertex in the set
            subSolution.firstStep = toVisit[0];
            subSolutionTable[setIdx][start] = subSolution;
        } else {
            //Stores intermediate solutions to compare to get best solution to return
            SubSolution potentialSubSolution;
            int vertex;
            int[] leftToVisit = new int[toVisit.length - 1];
            for (int idx = 0; idx < toVisit.length; idx++) {
                //Get actual integer value of vertex at index
                vertex = toVisit[idx];
                //Get set of vertices left to visit (not including current vertex)
                for (int leftIdx = 0; leftIdx < toVisit.length - 1; leftIdx++) {
                    //Skip element at index in array
                    if (leftIdx >= idx) {
                        leftToVisit[leftIdx] = toVisit[leftIdx + 1];
                    } else if (leftIdx < idx) {
                        leftToVisit[leftIdx] = toVisit[leftIdx];
                    }
                }
                potentialSubSolution = RecursiveDynamicProgramming(vertex, end, leftToVisit, edgeCosts, subSolutionTable);
                //Set initial values for potential solution or put better solution in "subSolution"
                if (idx == 0 || potentialSubSolution.cost + edgeCosts[start][vertex] < subSolution.cost) {
                    subSolution.cost = potentialSubSolution.cost + edgeCosts[start][vertex];
                    subSolution.firstStep = vertex;
                }

            }
            //Add our final "best" solution to table
            setIdx = convertSetToIndex(toVisit);
            subSolutionTable[setIdx][start] = subSolution;
        }
        return subSolution;
    }

    //Simple function that takes a set and converts it to the index of the sub solution table
    int convertSetToIndex(int[] vertexSet) {
        int result = 0;
        for (int x = 0; x < vertexSet.length; x++) {
            result = (int) (result + Math.pow(2, vertexSet[x] - 1));
        }
        return result;
    }

    double attraction(int k, int h, double[][] pheroMatrix, double[][] dist) {
        return (pheroMatrix[k][h] / dist[k][h]);
    }

    //Ant colony algorithm
    //Assumes that we are going from 0 to 0
    //pherFactor is for pheromone factor
    //M is for the number of ants
    public TSPSolution AntColony(int N, double[][] edgeCost, int M, double pherFactor, double decayFactor, int maxUnchangedTimeSteps) {
        double[][] pheroMatrix = new double[N][N];
        double[][] newPheroMatrix = new double[N][N];
        boolean[] curAntVisited = new boolean[N];
        int[] path = new int[N];
        int r, c, k, h, step;
        int unchangedTimeSteps = 0;
        double pathCost, totalA, Q, cumProb, edgeSelectionProbability;
        TSPSolution solution = new TSPSolution();
        Random rand = new Random();
        h = 0;

        //Setting initial attractiveness so that attraction function returns something other than 0
        for(r = 0; r < N; r++){
            for(c = 0; c < N; c++){
                pheroMatrix[r][c] = (double) edgeCost[r][c]/pherFactor;
            }
        }

        //Loops until we get the same path several times in a row
        while (unchangedTimeSteps < maxUnchangedTimeSteps) {
            //Loop through each ant at each time step
            for (int ant = 0; ant < M; ant++) {
                //Resetting path cost to 0 and setting first stop in path to home node (0)
                pathCost = 0;
                path[0] = 0;
                totalA = 0;

                //Clear new phero matrix and reset curAntVisited array
                for (r = 0; r < N; r++) {
                    curAntVisited[r] = false;
                    for (c = 0; c < N; c++) {
                        newPheroMatrix[r][c] = 0;
                    }
                }

                //We visited node 0
                curAntVisited[0] = true;
                //Loop through each node
                for (step = 1; step < N; step++) {
                    k = path[step - 1];
                    totalA = 0;
                    //Accumulating total attraction for all possible paths
                    // Then calculate relative probability for the next step
                    for (h = 0; h < N; h++) {
                        //If our destination has not been visited and is not equal to our source
                        if (curAntVisited[h] == false && h != k) {
                            totalA = totalA + attraction(k, h, pheroMatrix, edgeCost);
                        }
                    }
                    Q = rand.nextDouble();
                    cumProb = 0;
                    for (h = 0; h < N; h++) {
                        if (curAntVisited[h] == false && h != k) {
                            edgeSelectionProbability = attraction(k, h, pheroMatrix, edgeCost) / totalA;
                            cumProb = cumProb + edgeSelectionProbability;
                            //Next node h is found
                            if (Q < cumProb) {
                                break;
                            }
                        }
                    }
                    path[step] = h;
                    curAntVisited[h] = true;
                    pathCost = pathCost + edgeCost[k][h];
                }

                //Adding final step to node
                pathCost = pathCost + edgeCost[h][0];
                //If we found a new "min" solution, put in our solution to return
                if(pathCost < solution.cost || solution.cost == 0){
                    solution.cost = pathCost;
                    solution.path.clear();
                    //Don't include 0 in our path, so only need N-1 vertices in our path
                    for(int x = 1; x < N; x++){
                        solution.path.add(path[x]);
                    }
                    unchangedTimeSteps = -1;
                }

                //Laying down new pheromones
                for(step = 0; step < N; step++){
                    k = path[step];
                    h = path[(step+1) % N];
                    newPheroMatrix[k][h] = newPheroMatrix[k][h] + pherFactor/pathCost;
                }
            }

            //Updating pheromones for next time step
            for(k = 0; k < N; k++){
                for(h = 0; h < N; h++){
                    //Factor in decay factor to old pheromone
                    pheroMatrix[k][h] = pheroMatrix[k][h] * decayFactor;
                    //Adding new pheromones to old
                    pheroMatrix[k][h] = pheroMatrix[k][h] + newPheroMatrix[k][h];
                }
            }
            unchangedTimeSteps++;
        }
        return solution;
    }
}
