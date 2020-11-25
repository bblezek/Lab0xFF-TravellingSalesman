import org.junit.Test;
import org.junit.Assert;

import com.company.TSPSolution;
import com.company.FindShortPath;
import com.company.GenerateGraph;

public class FindShortPathTest {

    @Test
    public void testGreedy(){
        int radius = 100;
        GenerateGraph graph = new GenerateGraph();
        FindShortPath findShortPath = new FindShortPath();
        TSPSolution solution = new TSPSolution();
        double[][] costMatrix;
        double angle;
        //Print vertices around circle and coordinates
        for(int N = 5; N < 70; N=N+5) {
            System.out.printf("Testing greedy algorithm for graph of size %d \n", N);
            //Print vertices around circle and coordinates
            angle = 360 / N;
            angle = (double) Math.toRadians(angle);
            System.out.printf("%10s %10s %10s \n", "Vertex", "X", "Y");
            for (int vertex = 0; vertex < N; vertex++) {
                System.out.printf("%10d %10.2f %10.2f \n", vertex, radius*Math.cos(angle*vertex),
                        radius * Math.sin(angle * vertex));
            }
            //Print cost matrix
            costMatrix = graph.GenerateRandomCircularGraphCostMatrix(N, radius);
            graph.PrintMatrix(N, costMatrix);

            //Calculate solution and print
            solution = findShortPath.Greedy(0, 0, N, costMatrix);
            System.out.printf("Path: %s \n", solution.path.toString());
            System.out.printf("Expected cost: %.4f \nActual cost: %.4f \n", N*costMatrix[0][1], solution.cost);
            Assert.assertEquals(N*costMatrix[0][1], solution.cost, .00001);
        }
    }

    @Test
    public void testBruteForce(){
        int radius = 100;
        GenerateGraph graph = new GenerateGraph();
        FindShortPath findShortPath = new FindShortPath();
        TSPSolution solution = new TSPSolution();
        double[][] costMatrix;
        double angle;
        //Print vertices around circle and coordinates
        for(int N = 5; N < 70; N=N+5) {
            System.out.printf("Testing greedy algorithm for graph of size %d \n", N);
            //Print vertices around circle and coordinates
            angle = 360 / N;
            angle = (double) Math.toRadians(angle);
            System.out.printf("%10s %10s %10s \n", "Vertex", "X", "Y");
            for (int vertex = 0; vertex < N; vertex++) {
                System.out.printf("%10d %10.2f %10.2f \n", vertex, radius*Math.cos(angle*vertex),
                        radius * Math.sin(angle * vertex));
            }
            //Print cost matrix
            costMatrix = graph.GenerateRandomCircularGraphCostMatrix(N, radius);
            graph.PrintMatrix(N, costMatrix);

            //Calculate solution and print
            solution = findShortPath.BruteForce(0, 0, N, costMatrix);
            System.out.printf("Path: %s \n", solution.path.toString());
            System.out.printf("Expected cost: %.4f \nActual cost: %.4f \n", N*costMatrix[0][1], solution.cost);
            Assert.assertEquals(N*costMatrix[0][1], solution.cost, .00001);
        }
    }



}
