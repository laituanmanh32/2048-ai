package com.laituanmanh.AI2048.gamesolver;

import java.util.LinkedList;
import java.util.Queue;

import com.laituanmanh.AI2048.model.GameState;
import com.laituanmanh.AI2048.model.SolutionPath;
import com.laituanmanh.commontools.MemoryMeasure;
import com.laituanmanh.commontools.TimeMeasure;

public class BFSAlgorithm implements GameSolver {
	@Override
	public SolutionPath findSolution(GameState pState) {
		// Or null
		if (null == pState)
			return null;

		MemoryMeasure.checkPoint();
		TimeMeasure.checkPoint();
		pState.PrintState();

		// Check if input state is already reached goal.
		if (pState.checkWin()) {
			SolutionPath sol = new SolutionPath();
			sol.setState(pState);
			return sol;
		}

		// Create a queue.
		Queue<GameState> stateQueue = new LinkedList<GameState>();
		// Add first state to queue
		stateQueue.add(pState.moveLeft());
		stateQueue.add(pState.moveUp());
		stateQueue.add(pState.moveRight());
		stateQueue.add(pState.moveDown());

		// start BFS
		while (!stateQueue.isEmpty()) {
			GameState currentState = stateQueue.poll();

			// Else, create new state and add to queue.
			for (GameState state = currentState.getNextState(); state != null
					|| currentState.hasNextState(); state = currentState
					.getNextState()) {
				MemoryMeasure.checkPoint();
				// Check if solution found
				if (state.checkWin()) {

					TimeMeasure.checkPoint();
					SolutionPath sol = new SolutionPath();
					sol.setState(state);
					return sol;
				}

				stateQueue.add(state.moveLeft());
				stateQueue.add(state.moveUp());
				stateQueue.add(state.moveRight());
				stateQueue.add(state.moveDown());
			}
		}

		return null;
	}

}
