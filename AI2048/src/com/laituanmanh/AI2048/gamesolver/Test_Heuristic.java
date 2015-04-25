package com.laituanmanh.AI2048.gamesolver;

import org.junit.Test;

import com.laituanmanh.AI2048.model.GameState;
import com.laituanmanh.AI2048.model.SolutionPath;

public class Test_Heuristic {

	@Test
	public void test() {
		SimpleHillAlgorithm solver = new SimpleHillAlgorithm();
		// GameState state = new SimpleGameState(new int[][] { { 2, 2, 2, 0 },
		// { 0, 0, 0, 0 }, { 0, 0, 2, 0 }, { 0, 0, 0, 0 } });
		GameState state = new GameState(new int[][] { { 0, 0, 0, 0 },
				{ 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 } });
		state.setGoal(2048);
		long start = System.currentTimeMillis();
		SolutionPath sol = solver.findSolution(state);

		printSol(sol, 1);
		long elapsedTimeMillis = System.currentTimeMillis() - start;
		System.out.println("It takes " + elapsedTimeMillis / 1000F
				+ "s to find out solution.");
	}

	public void printSol(SolutionPath path, int step) {
		if (path.getNextState() == null) {
			path.getState().PrintState();
			System.out.println("Now, win!");
			return;
		}
		path.getState().PrintState();
		System.out.print("step " + step + " ");
		System.out.print(path.getAction() + "\n-----------\n");
		printSol(path.getNextState(), step + 1);

	}
}
