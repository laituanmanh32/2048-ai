package com.laituanmanh.AI2048.gamesolver;

import com.laituanmanh.AI2048.model.GameState;
import com.laituanmanh.AI2048.model.SolutionPath;

public interface GameSolver {
	/**
	 * Find 2048 solution path from a starting state.
	 * 
	 * @param pState
	 * @return solution path if solution found.
	 * @return null if solution not found.
	 */
	public SolutionPath findSolution(GameState pState);
}
