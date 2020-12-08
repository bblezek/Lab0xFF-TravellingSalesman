package com.company;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

public class TimingTable {

    public void generateExactAlgorithmTable() {
        long beforeTime, afterTime, time;
        long[] bruteTimes = new long[20];
        long[] dynamicTimes = new long[20];
        GenerateGraph graph = new GenerateGraph();
        FindShortPath findShortPath = new FindShortPath();
        double[][] edgeCosts;
        TSPSolution solution;
        System.out.printf("%10s %15s %50s \n", "", "Brute Force", "Dynamic Programming");
        System.out.printf("%10s %15s %15s %15s %15s %15s %15s \n", "Vertices", "Time", "Doubling", "Expected",
                "Time", "Doubling", "Expected");
        System.out.printf("%42s %15s %31s %15s \n", "Ratio", "Doubling", "Ratio", "Doubling");
        System.out.printf("%58s %47s \n", "Ratio", "Ratio");

        for (int N = 4; N < 24; N++) {
            edgeCosts = graph.GenerateRandomCostMatrix(N, 100);
            System.out.printf("%10d ", N);
            //Brute force quits after running for a while
            if (N <= 14) {
                beforeTime = getCpuTime();
                solution = findShortPath.BruteForce(0, 0, N, edgeCosts);
                afterTime = getCpuTime();
                time = afterTime - beforeTime;
                if (N >= 8 && N%2==0) {
                    //Time complexity is (N-1)!
                    System.out.printf("%15d %15.2f %15.2f ", time, (float) time / bruteTimes[(N/2)-4],
                            (float) factorial(N-1)/factorial(N/2 - 1));
                } else {
                    System.out.printf("%15d %15s %15s ", time, "na", "na");
                }
                bruteTimes[N-4] = time;
            } else {
                System.out.printf("%48s", "");
            }

                //Dynamic portion of the table
                beforeTime = getCpuTime();
                solution = findShortPath.DynamicProgramming(0, 0, N, edgeCosts);
                afterTime = getCpuTime();
                time = afterTime - beforeTime;

                if (N >= 8 && N%2==0) {
                    //Time complexity is 2^N * N^2
                    System.out.printf("%15d %15.2f %15.2f ", time, (float) time / dynamicTimes[(N/2)-4],
                            (float) (Math.pow(2, N) * Math.pow(N, 2)) / (Math.pow(2, N/2) * Math.pow(N/2, 2)));
                } else {
                    System.out.printf("%15d %15s %15s ", time, "na", "na");
                }
                dynamicTimes[N-4] = time;
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
        System.out.printf("%10s %47s %47s\n", "", "Greedy", "Ant Colony");
        System.out.printf("%10s %15s %15s %15s %15s %15s %15s \n", "Vertices", "Time", "Doubling", "Expected",
                "Time", "Doubling", "Expected");
        System.out.printf("%42s %15s %31s %15s \n", "Ratio", "Doubling", "Ratio", "Doubling");
        System.out.printf("%58s %47s \n", "Ratio", "Ratio");
        for(int N = 4; N < 10000000; N=N*2) {
            System.out.printf("%10d ", N);
            edgeCosts = graph.GenerateRandomCostMatrix(N, 100);

            if (lastGreedyTime < 180000000000L) {
                //Greedy portion of the table
                beforeTime = getCpuTime();
                solution = findShortPath.Greedy(0, 0, N, edgeCosts);
                afterTime = getCpuTime();
                time = afterTime - beforeTime;


                if (N != 4) {
                    System.out.printf("%15d %15.2f %15.2f ", time, (float) time / lastGreedyTime,
                            Math.pow(N, 2) / Math.pow(N / 2, 2));
                } else {
                    System.out.printf("%15d %15s %15s ", time, "na", "na");
                }
                lastGreedyTime = time;
            } else {
                System.out.printf("%48s", "");
            }

            if (lastAntTime < 180000000000L) {
                //Ant Colony portion of the table
                beforeTime = getCpuTime();
                solution = findShortPath.AntColony(N, edgeCosts, 50, .45, .99, 100);
                afterTime = getCpuTime();
                time = afterTime - beforeTime;

                if (N != 4) {
                    System.out.printf("%15d %15.2f %15.2f ", time, (float) time / lastAntTime,
                            (float) (time / solution.steps) / antTimePerStep);
                } else {
                    System.out.printf("%15d %15s %15s ", time, "na", "na");
                }
                lastAntTime = time;
                antTimePerStep = time / solution.steps;
            }
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

    //Function to generate a table comparing different parameters for ant colony
    public void generateAntParameterTable(){
        GenerateGraph generateGraph = new GenerateGraph();
        TSPSolution dynamicSolution, antColonySolution;
        FindShortPath findShortPath = new FindShortPath();
        double[][] edgeCosts;
        System.out.printf("%10s %5s %5s %5s %5s %5s %5s %5s %5s %5s %5s %5s %5s \n", "Vertices", "Ants", "Pher", "Decay", "SQR",
                "Decay", "SQR", "Decay", "SQR", "Decay", "SQR", "Decay", "SQR");
        System.out.printf("%22s\n", "Factor");
        for(int N = 4; N < 20; N++){
            System.out.printf("%10d ", N);
            edgeCosts = generateGraph.GenerateRandomCostMatrix(N, 100);
            dynamicSolution = findShortPath.DynamicProgramming(0, 0, N, edgeCosts);
            for(int ants = 25; ants <=100; ants=ants+25){
                for(double pherFactor = .10; pherFactor < 1; pherFactor=pherFactor+.1){
                    if(ants == 25 && pherFactor == .1) {
                        System.out.printf("%5d %5.2f ", ants, pherFactor);
                    } else {
                        System.out.printf("%10s %5d %5.2f ", "", ants, pherFactor);
                    }
                    for(double decayFactor = .90; decayFactor < 1; decayFactor=decayFactor+.02){
                        antColonySolution = findShortPath.AntColony(N, edgeCosts, ants, pherFactor,
                                decayFactor, 25);
                        System.out.printf("%5.2f %5.2f ", decayFactor,
                                (float) dynamicSolution.cost/antColonySolution.cost);
                    }
                    System.out.printf("\n");
                }
            }
        }
    }

    //Function to retrieve CPU time
    public static long getCpuTime() {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        return bean.isCurrentThreadCpuTimeSupported() ?
                bean.getCurrentThreadCpuTime() : 0L;
    }

    //Function to calculate factorial, given a number
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
