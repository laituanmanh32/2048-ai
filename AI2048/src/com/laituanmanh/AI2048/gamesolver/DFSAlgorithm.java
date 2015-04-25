package com.laituanmanh.AI2048.gamesolver;

import com.laituanmanh.AI2048.model.GameState;
import com.laituanmanh.AI2048.model.SolutionPath;
import com.laituanmanh.commontools.MemoryMeasure;
import com.laituanmanh.commontools.TimeMeasure;

public class DFSAlgorithm implements GameSolver {

	@Override
	public SolutionPath findSolution(GameState state) {
		if (state == null)
			return null;
		MemoryMeasure.checkPoint();
		TimeMeasure.checkPoint();
		if (state.checkWin()) {
			SolutionPath solution = new SolutionPath();
			solution.setState(state);
			return solution;
		}

		SolutionPath sol = new SolutionPath();
		GameState gameSate;

		for (int i = 1; i <= 2; i++) {
			for (int col = 0; col < state.getColNum(); col++) {
				for (int row = 0; row < state.getRowNum(); row++) {
					gameSate = state.moveLeft();
					if ((sol = this.findSolution(gameSate.addNewValue(2 * i,
							row, col))) != null) {
						SolutionPath solution = new SolutionPath();
						solution.setAction("move left");
						solution.setState(state);
						solution.setNextState(sol);
						MemoryMeasure.checkPoint();
						TimeMeasure.checkPoint();
						return solution;
					}

					gameSate = state.moveRight();
					if ((sol = this.findSolution(gameSate.addNewValue(2 * i,
							row, col))) != null) {
						SolutionPath solution = new SolutionPath();
						solution.setAction("move right");
						solution.setState(state);
						solution.setNextState(sol);
						MemoryMeasure.checkPoint();
						TimeMeasure.checkPoint();
						return solution;
					}

					gameSate = state.moveUp();
					if ((sol = this.findSolution(gameSate.addNewValue(2 * i,
							row, col))) != null) {
						SolutionPath solution = new SolutionPath();
						solution.setAction("move up");
						solution.setState(state);
						solution.setNextState(sol);
						MemoryMeasure.checkPoint();
						TimeMeasure.checkPoint();
						return solution;
					}

					gameSate = state.moveDown();
					if ((sol = this.findSolution(gameSate.addNewValue(2 * i,
							row, col))) != null) {
						SolutionPath solution = new SolutionPath();
						solution.setAction("move down");
						solution.setState(state);
						solution.setNextState(sol);
						MemoryMeasure.checkPoint();
						TimeMeasure.checkPoint();
						return solution;
					}
				}
			}
		}

		return null;

	}

}
