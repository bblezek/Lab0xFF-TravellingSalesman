package com.company;

public class Main {

    public static void main(String[] args) {
	// write your code here
        TimingTable table = new TimingTable();
        table.generateExactAlgorithmTable();
        table.generateHeuristicTable();
        table.generateSolutionQualityTable();
        table.generateAntParameterTable();
    }
}
