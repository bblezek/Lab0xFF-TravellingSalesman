import org.junit.Test;
import org.junit.Assert;

import com.company.TSPSolution;
import com.company.FindShortPath;
import com.company.GenerateGraph;

import java.util.List;

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
            /*angle = 2 * Math.PI / N;
            System.out.printf("%10s %10s %10s \n", "Vertex", "X", "Y");
            for (int vertex = 0; vertex < N; vertex++) {
                System.out.printf("%10d %10.2f %10.2f \n", vertex, radius*Math.cos(angle*vertex),
                        radius * Math.sin(angle * vertex));
            }*/
            //Print cost matrix
            costMatrix = graph.GenerateRandomCircularGraphCostMatrix(N, radius);
            //graph.PrintMatrix(N, costMatrix);

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
        for(int N = 3; N < 13; N++) {
            System.out.printf("Testing brute force algorithm for graph of size %d \n", N);
            //Print vertices around circle and coordinates
            /*angle = 2 * Math.PI / N;
            angle = (double) Math.toRadians(angle);
            System.out.printf("%10s %10s %10s \n", "Vertex", "X", "Y");
            for (int vertex = 0; vertex < N; vertex++) {
                System.out.printf("%10d %10.2f %10.2f \n", vertex, radius*Math.cos(angle*vertex),
                        radius * Math.sin(angle * vertex));
            }*/
            //Print cost matrix
            costMatrix = graph.GenerateRandomCircularGraphCostMatrix(N, radius);
            //graph.PrintMatrix(N, costMatrix);

            //Calculate solution and print
            solution = findShortPath.BruteForce(0, 0, N, costMatrix);
            System.out.printf("Path: %s \n", solution.path.toString());
            System.out.printf("Expected cost: %.4f \nActual cost: %.4f \n", N*costMatrix[0][1], solution.cost);
            Assert.assertEquals(N*costMatrix[0][1], solution.cost, .00001);
        }
    }

    //Tests exact dynamic programming algorithm
    @Test
    public void testDynamicProgramming(){
        int radius = 100;
        GenerateGraph graph = new GenerateGraph();
        FindShortPath findShortPath = new FindShortPath();
        TSPSolution solution = new TSPSolution();
        double[][] costMatrix;
        double angle;
        //Print vertices around circle and coordinates
        for(int N = 3; N < 15; N++) {
            System.out.printf("Testing dynamic programming algorithm for graph of size %d \n", N);
            //Print vertices around circle and coordinates
            /*angle = 2*Math.PI / N;
            System.out.printf("%10s %10s %10s \n", "Vertex", "X", "Y");
            for (int vertex = 0; vertex < N; vertex++) {
                System.out.printf("%10d %10.2f %10.2f \n", vertex, radius*Math.cos(angle*vertex),
                        radius * Math.sin(angle * vertex));
            }*/
            //Print cost matrix
            costMatrix = graph.GenerateRandomCircularGraphCostMatrix(N, radius);
            //graph.PrintMatrix(N, costMatrix);

            //Calculate solution and print
            solution = findShortPath.DynamicProgramming(0, 0, N, costMatrix);
            System.out.printf("Path: %s \n", solution.path.toString());
            System.out.printf("Expected cost: %.4f \nActual cost: %.4f \n", N*costMatrix[0][1], solution.cost);
            Assert.assertEquals(N*costMatrix[0][1], solution.cost, .00001);
        }
    }

    //Compare results from brute force and dynamic programming algorithms
    @Test
    public void compareExactTest(){
        GenerateGraph graph = new GenerateGraph();
        FindShortPath findShortPath = new FindShortPath();
        TSPSolution bruteSolution = new TSPSolution();
        TSPSolution dynamicSolution = new TSPSolution();
        double[][] costMatrix;
        for(int N = 4; N < 13; N++){
            costMatrix = graph.GenerateRandomCostMatrix(N, 100);
            bruteSolution = findShortPath.BruteForce(0, 0, N, costMatrix);
            dynamicSolution = findShortPath.DynamicProgramming(0, 0, N, costMatrix);
            System.out.printf("Brute force path: %s \n", bruteSolution.path.toString());
            System.out.printf("Dynamic programming path: %s \n", dynamicSolution.path.toString());
            System.out.printf("\n");
            Assert.assertEquals(bruteSolution.path, dynamicSolution.path);
        }
    }

    //Tests ant colony algorithm using circular Euclidean algorithm
    @Test
    public void testAntColony(){
        GenerateGraph graph = new GenerateGraph();
        FindShortPath findShortPath = new FindShortPath();
        TSPSolution solution = new TSPSolution();
        double[][] costMatrix;
        double radius = 100;
        double angle;
        for(int N = 5; N < 65; N=N+5){
            System.out.printf("Testing ant colony algorithm for graph of size %d \n", N);
            //Print coordinates
            /*angle = 2*Math.PI / N;
            System.out.printf("%10s %10s %10s \n", "Vertex", "X", "Y");
            for (int vertex = 0; vertex < N; vertex++) {
                System.out.printf("%10d %10.2f %10.2f \n", vertex, radius*Math.cos(angle*vertex),
                        radius * Math.sin(angle * vertex));
            }*/
            //Generate and print cost matrix
            costMatrix = graph.GenerateRandomCircularGraphCostMatrix(N, radius);
            //graph.PrintMatrix(N, costMatrix);

            System.out.printf("Doing %d vertex matrix \n", N);
            solution = findShortPath.AntColony(N, costMatrix, 50, .45, .99, 100);
            System.out.printf("Actual: %.2f \n", solution.cost);
            System.out.printf("Expected: %.2f \n", N*costMatrix[0][1]);
            System.out.printf("%s \n", solution.path.toString());
            Assert.assertEquals(N*costMatrix[0][1], solution.cost, .01);
        }
    }

    //Compares paths returned by different algorithms - counts path and its backward counterpart as the same
    public boolean comparePaths(int vertices, List<Integer> greedySolution, List<Integer> antSolution) {
        for(int x = 0; x < vertices-1; x++){
            //Testing to see if elements in the same position in the paths match or if elements in opposite positions match
            if(greedySolution.get(x) != antSolution.get(vertices-2-x) && greedySolution.get(x) != antSolution.get(x)){
                return false;
            }
        }
        return true;
    }

    //Test that simply compares result paths from greedy and ant colony algorithms
    @Test
    public void compareHeuristicTest(){
        GenerateGraph graph = new GenerateGraph();
        FindShortPath findShortPath = new FindShortPath();
        TSPSolution greedySolution, antSolution;
        double[][] edgeCosts;
        for(int N = 5; N < 65; N=N+5){
            edgeCosts = graph.GenerateRandomCircularGraphCostMatrix(N, 100);
            greedySolution = findShortPath.Greedy(0, 0, N, edgeCosts);
            antSolution = findShortPath.AntColony(N, edgeCosts, 50, .45, .99, 100);
            if(comparePaths(N, greedySolution.path, antSolution.path)){
                System.out.printf("Paths for greedy and ant colony solutions for graph of size %d match! \n", N);
            } else {
                System.out.printf("Paths for greedy and any colony solutions for graph of size %d do not match! \n", N);
            }
        }
    }
}
