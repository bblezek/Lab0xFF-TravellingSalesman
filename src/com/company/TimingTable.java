package com.company;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

public class TimingTable {

    public void generateExactAlgorithmTable() {
        long beforeTime, afterTime, time, lastBruteTime, lastDynamicTime, lastGreedyTime, lastAntTime;
        lastBruteTime = 0;
        lastDynamicTime = 0;
        lastGreedyTime = 0;
        lastAntTime = 0;
        GenerateGraph graph = new GenerateGraph();
        FindShortPath findShortPath = new FindShortPath();
        double[][] edgeCosts;
        TSPSolution solution;
        System.out.printf("%10s %15s %50s %44s %47s\n", "", "Brute Force", "Dynamic Programming", "Greedy", "Ant Colony");
        System.out.printf("%10s %15s %15s %15s %15s %15s %15s %15s %15s %15s %15s %15s %15s", "Vertices", "Time", "Actual Ratio", "+1 Ratio",
                "Time", "Actual Ratio", "+1 Ratio", "Time", "Actual Ratio", "+1 Ratio", "Time", "Actual Ratio", "+1 Ratio");
        System.out.printf("\n");

        for (int N = 4; N < 30; N++) {
            edgeCosts = graph.GenerateRandomCostMatrix(N, 100);
            //Brute force quits after running for a while
            if (lastBruteTime < 180000000000L) {
                beforeTime = getCpuTime();
                solution = findShortPath.BruteForce(0, 0, N, edgeCosts);
                afterTime = getCpuTime();
                time = afterTime - beforeTime;
                if (N != 4) {
                    System.out.printf("%10d %15d %15.2f %15s ", N, time, (float) time / lastBruteTime, "");
                } else {
                    System.out.printf("%10d %15d %15s %15s ", N, time, "na", "na");
                }
                lastBruteTime = time;
            } else {
                System.out.printf("%58s ", "");
            }

            //Dynamic portion of the table
            beforeTime = getCpuTime();
            solution = findShortPath.DynamicProgramming(0, 0, N, edgeCosts);
            afterTime = getCpuTime();
            time = afterTime - beforeTime;

            if (N != 4) {
                System.out.printf("%15d %15.2f %15s ", time, (float) time / lastDynamicTime, "");
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
                System.out.printf("%15d %15.2f %15s ", time, (float) time / lastGreedyTime, "");
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
                System.out.printf("%15d %15.2f %15s ", time, (float) time / lastAntTime, "");
            } else {
                System.out.printf("%15d %15s %15s ", time, "na", "na");
            }
            lastAntTime = time;
            System.out.printf("\n");

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
