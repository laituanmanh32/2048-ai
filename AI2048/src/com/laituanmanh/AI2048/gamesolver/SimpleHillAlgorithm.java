package com.laituanmanh.AI2048.gamesolver;

import java.util.Stack;

import com.laituanmanh.AI2048.model.GameState;
import com.laituanmanh.AI2048.model.SolutionPath;
import com.laituanmanh.commontools.TimeMeasure;
import com.laituanmanh.commontools.MemoryMeasure;

public class SimpleHillAlgorithm implements GameSolver {

	@Override
	public SolutionPath findSolution(final GameState pState) {
		if (null == pState)
			return null;

		MemoryMeasure.checkPoint();
		TimeMeasure.checkPoint();
		if (pState.checkWin()) {
			SolutionPath sol = new SolutionPath();
			sol.setState(pState);
			return sol;
		}

		Stack<SolutionPath> solStack = new Stack<SolutionPath>();
		solStack.add(new SolutionPath(pState, "Start!"));
		GameState currentState = pState;
		GameState moveState;

		// Get estimate value of current state;
		EstimateValue currentValue = estimate(currentState);

		boolean nomove = false;
		while (!currentState.checkWin()) {

			if (nomove)
				break;
			// Checkpoint to break to
			newscan: {
				MemoryMeasure.checkPoint();
				TimeMeasure.checkPoint();
				for (int value = 2; value >= 1; value--) {
					for (int col = 0; col < currentState.getColNum(); col++) {
						for (int row = 0; row < currentState.getRowNum(); row++) {

							moveState = currentState.moveRight();
							MemoryMeasure.checkPoint();
							TimeMeasure.checkPoint();
							if (moveState.addNewValue(value, row, col) != null) {
								EstimateValue nextValue = estimate(moveState
										.addNewValue(value, row, col));
								if (currentValue.compareTo(nextValue) == -1) {
									currentState = moveState.addNewValue(value,
											row, col);
									currentValue = nextValue;

									solStack.add(new SolutionPath(currentState,
											"move right"));
									currentState.PrintState();
									// start new scan.
									break newscan;
								}
							}

							MemoryMeasure.checkPoint();
							TimeMeasure.checkPoint();
							moveState = currentState.moveDown();
							if (moveState.addNewValue(value, row, col) != null) {
								EstimateValue nextValue = estimate(moveState
										.addNewValue(value, row, col));
								if (currentValue.compareTo(nextValue) == -1) {
									currentState = moveState.addNewValue(value,
											row, col);
									currentValue = nextValue;

									solStack.add(new SolutionPath(currentState,
											"move down"));
									currentState.PrintState();
									// start new scan.
									break newscan;
								}
							}

							MemoryMeasure.checkPoint();
							TimeMeasure.checkPoint();
							moveState = currentState.moveLeft();
							if (moveState.addNewValue(value, row, col) != null) {
								EstimateValue nextValue = estimate(moveState
										.addNewValue(value, row, col));
								if (currentValue.compareTo(nextValue) == -1) {
									currentState = moveState.addNewValue(value,
											row, col);
									currentValue = nextValue;

									solStack.add(new SolutionPath(currentState,
											"move left"));
									currentState.PrintState();
									// start new scan.
									break newscan;
								}
							}

							MemoryMeasure.checkPoint();
							TimeMeasure.checkPoint();
							moveState = currentState.moveUp();
							if (moveState.addNewValue(value, row, col) != null) {
								EstimateValue nextValue = estimate(moveState
										.addNewValue(value, row, col));
								if (currentValue.compareTo(nextValue) == -1) {
									currentState = moveState.addNewValue(value,
											row, col);
									currentValue = nextValue;

									solStack.add(new SolutionPath(currentState,
											"move up"));
									currentState.PrintState();
									// start new scan.
									break newscan;
								}
							}

						}
					}
				}
				nomove = true;
			}
		}

		return createPath(solStack);
	}

	private SolutionPath createPath(Stack<SolutionPath> stack) {
		SolutionPath path = stack.pop();
		while (!stack.isEmpty()) {
			SolutionPath newPath = stack.pop();
			newPath.setNextState(path);
			path = newPath;
		}

		return path;
	}

	public EstimateValue estimate(GameState state) {
		EstimateValue value = new EstimateValue();

		int max = 0;
		int blank = 0;
		int pair = 0;
		int total = 0;

		// count total blank cell and find max value
		for (int i = 0; i < state.getRowNum(); i++) {
			for (int j = 0; j < state.getColNum(); j++) {
				if (0 == state.getValue(i, j))
					blank++;
				if (max < state.getValue(i, j))
					max = state.getValue(i, j);
				total += state.getValue(i, j);
			}
		}

		// count pair for up,down move.
		for (int j = 0; j < state.getColNum(); j++) {
			for (int i = state.getRowNum() - 1; i > 0; i--) {
				// if current cell !=0 find the next cell != 0
				if (state.getValue(i, j) != 0)
					for (int k = i - 1; k >= 0; k--) {
						if (state.getValue(i, j) == state.getValue(k, j)) {
							pair++;
							// Move value to the cell next to lastmove cell.
							i = k - 1;
							k = k - 1;
						}
					}
				if (i <= 0) {
					continue;
				}
			}
		}

		// count pair for left,right move.
		for (int i = 0; i < state.getRowNum(); i++) {
			for (int j = 0; j < state.getColNum() - 1; j++) {
				// if current cell !=0 find the next cell != 0
				if (state.getValue(i, j) != 0)
					for (int k = j + 1; k < state.getColNum(); k++) {
						if (state.getValue(i, j) == state.getValue(i, k)) {
							pair++;
							// Move value to the cell next to lastmove cell.
							j = k + 1;
							k = k + 1;
						}
					}
				if (j >= state.getColNum() - 1) {
					continue;
				}
			}
		}

		// Check if the largest value is in the corner.
		if (state.getValue(0, 0) == max
				|| state.getValue(0, state.getColNum() - 1) == max
				|| state.getValue(state.getRowNum() - 1, state.getColNum() - 1) == max
				|| state.getValue(state.getRowNum() - 1, 0) == max)
			value.setCornered(true);

		value.setMaxValue(max);
		value.setTotalBlank(blank);
		value.setTotalPair(pair);
		value.setTotalValue(total);
		return value;
	}

	public class EstimateValue implements Comparable<EstimateValue> {

		int maxValue;
		int totalValue;
		int totalBlank;
		int totalPair;
		boolean isCornered;

		/**
		 * @return -1 if other bigger
		 * @return 0 if equals or equals.
		 */
		@Override
		public int compareTo(EstimateValue o) {

			/*
			 * Max value of next state absolutely bigger or equals to this
			 * state.
			 */

			if (o.isCornered) {
				if (this.maxValue < o.maxValue)
					return -1;
				if (totalBlank < o.totalBlank)
					return -1;
				if (totalPair < o.totalPair)
					return -1;

				// else compare total black cell.
				if ((totalBlank) < (o.totalBlank * 1.5))
					return -1;
			}

			// else compare total pair.

			return 0;
		}

		public int getMaxValue() {
			return maxValue;
		}

		public void setMaxValue(int maxValue) {
			this.maxValue = maxValue;
		}

		public int getTotalValue() {
			return totalValue;
		}

		public void setTotalValue(int totalValue) {
			this.totalValue = totalValue;
		}

		public int getTotalBlank() {
			return totalBlank;
		}

		public void setTotalBlank(int totalBlank) {
			this.totalBlank = totalBlank;
		}

		public int getTotalPair() {
			return totalPair;
		}

		public void setTotalPair(int totalPair) {
			this.totalPair = totalPair;
		}

		public boolean isCornered() {
			return isCornered;
		}

		public void setCornered(boolean isCornered) {
			this.isCornered = isCornered;
		}

	}
}
