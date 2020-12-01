package com.company;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

public class TimingTable {

    public void generateExactAlgorithmTable(){
        long beforeTime, afterTime, bruteTime, lastBruteTime, dynamicTime, lastDynamicTime;
        lastBruteTime = 0;
        lastDynamicTime = 0;
        GenerateGraph graph = new GenerateGraph();
        FindShortPath findShortPath = new FindShortPath();
        double[][] edgeCosts;
        TSPSolution solution;
        System.out.printf("Brute Force: %68s \n", "Dynamic Programming:");
        System.out.printf("%10s %15s %15s %15s %10s %15s %15s %15s", "Vertices", "Time", "Actual Ratio", "+1 Ratio",
                "Vertices", "Time", "Actual Ratio", "+1 Ratio");
        System.out.printf("\n");

        for(int N = 4; N < 30; N++){
            edgeCosts = graph.GenerateRandomCostMatrix(N, 100);
            if(lastBruteTime < 180000000000L) {
                beforeTime = getCpuTime();
                solution = findShortPath.BruteForce(0, 0, N, edgeCosts);
                afterTime = getCpuTime();
                bruteTime = afterTime - beforeTime;
                if(N != 4) {
                    System.out.printf("%10d %15d %15.2f %15s ", N, bruteTime, (float) bruteTime / lastBruteTime, "");
                } else {
                    System.out.printf("%10d %15d %15s %15s ", N, bruteTime, "na", "na");
                }
                lastBruteTime = bruteTime;
            } else {
                System.out.printf("%58s ", "");
            }

            beforeTime = getCpuTime();
            solution = findShortPath.BruteForce(0, 0, N, edgeCosts);
            afterTime = getCpuTime();
            dynamicTime = afterTime - beforeTime;

            if(N != 4) {
                System.out.printf("%10d %15d %15.2f %15s ", N, dynamicTime, (float) dynamicTime / lastDynamicTime, "");
            } else {
                System.out.printf("%10d %15d %15s %15s ", N, dynamicTime, "na", "na");
            }
            lastDynamicTime = dynamicTime;
            System.out.printf("\n");
        }
    }

    //Function to retrieve CPU time
    public static long getCpuTime() {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        return bean.isCurrentThreadCpuTimeSupported() ?
                bean.getCurrentThreadCpuTime() : 0L;
    }

    int factorial(int num){
        int result = 1;
        if(num <= 1){
            return 1;
        }
        for(int x = 2; x <= num; x++){
            result = result * x;
        }
        return result;
    }
}
