package com.laituanmanh.AI2048.gamesolver;

import static org.junit.Assert.fail;

import java.io.BufferedWriter;

import org.junit.Test;

import com.laituanmanh.AI2048.model.GameState;
import com.laituanmanh.AI2048.model.SolutionPath;
import com.laituanmanh.commontools.MemoryMeasure;
import com.laituanmanh.commontools.MemoryMeasure.NoEndPointChecked;

public class Test_DFS {

	@Test
	public void test() throws NoEndPointChecked {
		GameSolver solver = new DFSAlgorithm();
		// GameState state = new SimpleGameState(new int[][] { { 2, 2, 2, 0 },
		// { 0, 0, 0, 0 }, { 0, 0, 2, 0 }, { 0, 0, 0, 0 } });
		GameState state = new GameState(new int[][] { { 0, 2, 0, 0 },
				{ 0, 0, 0, 0 }, { 0, 2, 0, 0 }, { 0, 0, 0, 0 } });
		state.setGoal(2048);
		// set start point
		long start = System.currentTimeMillis();
		MemoryMeasure.reset();
		// Start finding solution
		SolutionPath solution = solver.findSolution(state);
		if (solution == null)
			fail("solution not found.");
		// Get elapsed time
		long elapsedTimeMillis = System.currentTimeMillis() - start;

		printSol(solution, 1);
		System.out.println("It takes " + elapsedTimeMillis / 1000F
				+ "s to find out solution.");
		System.out.println("Memory taked: "
				+ MemoryMeasure.getUsageMemoryMeasureInMB() + "Mb");
	}

	public void printSol(SolutionPath path, int step) {
		if (path.getNextState() == null) {
			path.getState().PrintState();
			System.out.println("Now, win!");
			return;
		}
		path.getState().PrintState();
		// System.out.print("step " + step + " ");
		System.out.print(path.getAction() + "\n-----------\n");
		printSol(path.getNextState(), step + 1);

	}

	public void printToFile(SolutionPath path, BufferedWriter writer) {
		for (SolutionPath p = path; p != null; p = p.getNextState()) {

		}
	}

}
