package com.company;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

public class TimingTable {

    public void generateExactAlgorithmTable() {
        long beforeTime, afterTime, time, lastBruteTime, lastDynamicTime, lastGreedyTime, lastAntTime, antTimePerStep;
        lastBruteTime = 0;
        lastDynamicTime = 0;
        lastGreedyTime = 0;
        lastAntTime = 0;
        antTimePerStep = 0;
        GenerateGraph graph = new GenerateGraph();
        FindShortPath findShortPath = new FindShortPath();
        double[][] edgeCosts;
        TSPSolution solution;
        System.out.printf("%10s %15s %50s %44s %47s\n", "", "Brute Force", "Dynamic Programming", "Greedy", "Ant Colony");
        System.out.printf("%10s %15s %15s %15s %15s %15s %15s %15s %15s %15s %15s %15s %15s", "Vertices", "Time", "Actual Ratio", "+1 Ratio",
                "Time", "Actual Ratio", "+1 Ratio", "Time", "Actual Ratio", "+1 Ratio", "Time", "Actual Ratio", "+1 Ratio");
        System.out.printf("\n");

        for (int N = 4; N < 25; N++) {
            edgeCosts = graph.GenerateRandomCostMatrix(N, 100);
            System.out.printf("%10d ", N);
            //Brute force quits after running for a while
            if (lastBruteTime < 180000000000L) {
                beforeTime = getCpuTime();
                solution = findShortPath.BruteForce(0, 0, N, edgeCosts);
                afterTime = getCpuTime();
                time = afterTime - beforeTime;
                if (N != 4) {
                    //Time complexity is (N-1)!
                    System.out.printf("%15d %15.2f %15.2f ", time, (float) time / lastBruteTime,
                            (float) factorial(N-1)/factorial(N-2));
                } else {
                    System.out.printf("%15d %15s %15s ", time, "na", "na");
                }
                lastBruteTime = time;
            } else {
                System.out.printf("%48s", "");
            }

                //Dynamic portion of the table
                beforeTime = getCpuTime();
                solution = findShortPath.DynamicProgramming(0, 0, N, edgeCosts);
                afterTime = getCpuTime();
                time = afterTime - beforeTime;

                if (N != 4) {
                    //Time complexity is 2^N * N^2
                    System.out.printf("%15d %15.2f %15.2f ", time, (float) time / lastDynamicTime,
                            (float) (Math.pow(2, N) * Math.pow(N, 2)) / (Math.pow(2, N - 1) * Math.pow(N - 1, 2)));
                } else {
                    System.out.printf("%15d %15s %15s ", time, "na", "na");
                }
                lastDynamicTime = time;

            //Greedy portion of the table
            beforeTime = getCpuTime();
            solution = findShortPath.Greedy(0, 0, N, edgeCosts);
            afterTime = getCpuTime();
            time = afterTime - beforeTime;

            if (N != 4) {
                System.out.printf("%15d %15.2f %15.2f ", time, (float) time / lastGreedyTime,
                        Math.pow(N, 2)/Math.pow(N-1, 2));
            } else {
                System.out.printf("%15d %15s %15s ", time, "na", "na");
            }
            lastGreedyTime = time;

            //Ant Colony portion of the table
            beforeTime = getCpuTime();
            solution = findShortPath.AntColony(N, edgeCosts, 50, .45, .99, 100);
            afterTime = getCpuTime();
            time = afterTime - beforeTime;

            if (N != 4) {
                System.out.printf("%15d %15.2f %15.2f ", time, (float) time / lastAntTime,
                        (float) (time/solution.steps)/antTimePerStep);
            } else {
                System.out.printf("%15d %15s %15s ", time, "na", "na");
            }
            lastAntTime = time;
            antTimePerStep = time/solution.steps;
            System.out.printf("\n");

        }
    }

    //Generates table for heuristic algorithms
    public void generateHeuristicTable(){
        long beforeTime, afterTime, time, lastGreedyTime, lastAntTime, antTimePerStep = 0;
        lastGreedyTime = 0;
        lastAntTime = 0;
        TSPSolution solution;
        FindShortPath findShortPath = new FindShortPath();
        GenerateGraph graph = new GenerateGraph();
        double[][] edgeCosts;
        System.out.printf("%10s %48s %48s\n", "", "Greedy", "Ant Colony");
        System.out.printf("%10s %15s %15s %15s %15s %15s %15s ", "Vertices", "Time", "Actual Ratio", "+1 Ratio",
                "Time", "Actual Ratio", "+1 Ratio");
        System.out.printf("\n");
        for(int N = 50; N < 10000000; N=N*2){
            edgeCosts = graph.GenerateRandomCostMatrix(N, 100);
            //Greedy portion of the table
            beforeTime = getCpuTime();
            solution = findShortPath.Greedy(0, 0, N, edgeCosts);
            afterTime = getCpuTime();
            time = afterTime - beforeTime;

            if (N != 4) {
                System.out.printf("%15d %15.2f %15.2f ", time, (float) time / lastGreedyTime,
                        Math.pow(N, 2)/Math.pow(N-1, 2));
            } else {
                System.out.printf("%15d %15s %15s ", time, "na", "na");
            }
            lastGreedyTime = time;

            //Ant Colony portion of the table
            beforeTime = getCpuTime();
            solution = findShortPath.AntColony(N, edgeCosts, 50, .45, .99, 100);
            afterTime = getCpuTime();
            time = afterTime - beforeTime;

            if (N != 4) {
                System.out.printf("%15d %15.2f %15.2f ", time, (float) time / lastAntTime,
                        (float) (time/solution.steps)/antTimePerStep);
            } else {
                System.out.printf("%15d %15s %15s ", time, "na", "na");
            }
            lastAntTime = time;
            antTimePerStep = time/solution.steps;
            System.out.printf("\n");
        }
    }

    //Heuristic solution quality testing
    public void generateSolutionQualityTable(){
        GenerateGraph generateGraph = new GenerateGraph();
        TSPSolution dynamicSolution, greedySolution, antColonySolution;
        FindShortPath findShortPath = new FindShortPath();
        double[][] edgeCosts;
        double dynTotal, greedyTotal, antTotal;
        System.out.printf("%10s %10s %10s %10s %10s %10s \n", "Vertices", "Dynamic", "Greedy", "Ant Colony", "SQR for", "SQR for");
        System.out.printf("%43s %10s %10s \n", "", "Greedy/Dyn", "Ant/Dyn");
        int runs = 10;
        for(int N = 4; N < 21; N++){
            dynTotal = 0;
            greedyTotal = 0;
            antTotal = 0;
            for(int run = 0; run < runs; run++) {
                edgeCosts = generateGraph.GenerateRandomEuclideanCostMatrix(N, 100, 100);
                dynamicSolution = findShortPath.DynamicProgramming(0, 0, N, edgeCosts);
                greedySolution = findShortPath.Greedy(0, 0, N, edgeCosts);
                antColonySolution = findShortPath.AntColony(N, edgeCosts, 50, .45, .99, 100);
                dynTotal = dynTotal + dynamicSolution.cost;
                greedyTotal = greedyTotal + greedySolution.cost;
                antTotal = antTotal + antColonySolution.cost;
            }
            greedyTotal = greedyTotal/runs;
            antTotal = antTotal/runs;
            dynTotal = dynTotal/runs;
            System.out.printf("%10d %10.2f %10.2f %10.2f %10.2f %10.2f \n", N, dynTotal, greedyTotal, antTotal,
                    greedyTotal/dynTotal, antTotal/dynTotal);
        }
    }

    //Function to retrieve CPU time
    public static long getCpuTime() {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        return bean.isCurrentThreadCpuTimeSupported() ?
                bean.getCurrentThreadCpuTime() : 0L;
    }

    int factorial(int num) {
        int result = 1;
        if (num <= 1) {
            return 1;
        }
        for (int x = 2; x <= num; x++) {
            result = result * x;
        }
        return result;
    }
}
