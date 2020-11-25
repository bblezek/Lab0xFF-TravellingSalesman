package com.company;

import java.util.*;

public class GenerateGraph {

    //Generates a simple random cost matrix given number of vertices
    float[][] GenerateRandomCostMatrix(int vertices, float maxCost){
        float[][] costMatrix = new float[vertices][vertices];
        Random rd = new Random();
        float cost;
        for(int vertex = 0; vertex < vertices; vertex++){
            for(int dest = vertex; dest < vertices; dest++){
                //Initializing spots in diagonal to 0
                if(dest == vertex){
                    costMatrix[vertex][dest] = 0;
                } else {
                    cost = (rd.nextInt((int) maxCost - 1) + rd.nextFloat());
                    costMatrix[vertex][dest] = cost;
                    costMatrix[dest][vertex] = cost;
                }
            }
        }
        return costMatrix;
    }

    //Generates random coordinates for a given number of vertices in a plane
    //Then creates random costs for vertices
    float[][] GenerateRandomEuclideanCostMatrix(int vertices, int maxX, int maxY){
        float[][] costMatrix = new float[vertices][vertices];
        float[][] coordinates = new float[vertices][2];
        Random rand = new Random();
        int vertex;
        double distance, xCoordinate, yCoordinate;
        //Generate random coordinates for each vertex
        for(vertex = 0; vertex < vertices; vertex++){
            coordinates[vertex][0] = rand.nextInt(maxX - 1) + rand.nextFloat();
            coordinates[vertex][1] = rand.nextInt(maxY - 1) + rand.nextFloat();
        }

        //Calculate cost matrix using coordinates generated earlier
        //Picking "source" node
        for(vertex = 0; vertex < vertices; vertex++){
            //Picking "dest" node
            for(int dest = vertex + 1; dest < vertices; dest++){
                //Calculates distance using distance formula
                distance = Math.sqrt(Math.pow(coordinates[dest][0] - coordinates[vertex][0], 2)
                        + Math.pow(coordinates[dest][1] - coordinates[vertex][1], 2));
                costMatrix[vertex][dest] = (float) distance;
                costMatrix[dest][vertex] = (float) distance;
            }
        }
        return costMatrix;
    }

    float[][] GenerateRandomCircularGraphCostMatrix(int vertices, float radius){
        float[][] costMatrix = new float[vertices][vertices];
        double[][] coordinates = new double[vertices][2];
        int vertex, src;
        List<Integer> vertexList = new ArrayList<Integer>();

        float angle = 360 / vertices;
        angle = (float) Math.toRadians(angle);
        double distance;
        for(int x = 0; x < vertices; x++){
            vertexList.add(x);
        }

        //Calculate coordinates for points evenly spaced around a circle
        for(vertex = 0; vertex < vertices; vertex++){
            coordinates[vertex][0] = radius * Math.cos(angle * vertex);
            coordinates[vertex][1] = radius * Math.sin(angle * vertex);
        }

            for(vertex = 0; vertex < vertices; vertex++){
                //Mix up remaining vertices
                Collections.shuffle(vertexList);
                //Select a vertex, then remove from list
                src = vertexList.get(0);
                vertexList.remove(0);
                //Loop over remaining vertices, generating random distances
                for(int dest = src + 1; dest < vertices; dest++){
                    distance = Math.sqrt(Math.pow(coordinates[dest][0] - coordinates[src][0], 2)
                            + Math.pow(coordinates[dest][1] - coordinates[src][1], 2));
                    costMatrix[src][dest] = (float) distance;
                    costMatrix[dest][src] = (float) distance;
                }
            }

        return costMatrix;
    }

    void PrintMatrix(int vertices, float[][] matrix){
        for(int x = 0; x < vertices; x++){
            for(int y = 0; y < vertices; y++){
                System.out.printf("%6.2f ", matrix[x][y]);
            }
            System.out.printf("\n");
        }
    }
}
