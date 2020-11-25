package com.company;

public class Main {

    public static void main(String[] args) {
	// write your code here
        GenerateGraph graph = new GenerateGraph();
        float[][] matrix;
        matrix = graph.GenerateRandomCostMatrix(5, 100);
        graph.PrintMatrix(5, matrix);

        System.out.printf("\n");
        matrix = graph.GenerateRandomEuclideanCostMatrix(5, 20, 20);
        graph.PrintMatrix(5, matrix);

        System.out.printf("\n");
        matrix = graph.GenerateRandomCircularGraphCostMatrix(5, 50);
        graph.PrintMatrix(5, matrix);

        FindShortPath findShortPath = new FindShortPath();
        TSPSolution solution = new TSPSolution();
        solution = findShortPath.BruteForce(5, matrix);
    }
}
