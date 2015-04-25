package com.laituanmanh.AI2048.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class Test_GameState {

	@Test
	public void testMove() {
		GameState state = new GameState(new int[][] { { 2, 2, 2, 2 },
				{ 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 } });
		state.moveLeft().PrintState();
		state.moveRight().PrintState();
		state.moveUp().PrintState();
		state.moveDown().PrintState();
	}

	@Test
	public void testSetGetParent() {
		GameState state = new GameState(new int[][] { { 2, 0, 0, 0 },
				{ 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 } });
		GameState state2 = new GameState(new int[][] { { 2, 0, 0, 0 },
				{ 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 } });
		state.setParent(state2);
		assertNotNull(state.getParent());
		state.moveDown().PrintState();
		state.moveDown().getNextState().moveDown().getParent().PrintState();
	}

	@Test
	public void testStateMove() {
		GameState state = new GameState(new int[][] { { 2, 0, 0, 0 },
				{ 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 } });
		assertNotNull(state.moveLeft());
	}

	@Test
	public void testHasNextState() {
		GameState state = new GameState(new int[][] { { 2, 0, 0, 0 },
				{ 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 } });
		assertTrue(state.hasNextState());
	}

	@Test
	public void testNewStateGenerated() {
		GameState state = new GameState(new int[][] { { 0, 0, 0, 0 },
				{ 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 } });
		assertNotNull(state.getNextState());
	}

	@Test
	public void testAllLeftMove() {
		GameState state = new GameState(new int[][] { { 0, 0, 0, 0 },
				{ 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 } });
		List<GameState> stateList = new ArrayList<GameState>();

		while (state.hasNextState()) {
			GameState newState = state.getNextState();
			if (newState != null)
				stateList.add(newState);
		}

		while (stateList.contains(null)) {

			stateList.remove(null);
		}

		System.out.println(stateList.size());
		assertEquals(stateList.size(), 32);
	}

	@Test
	public void test() {
		GameState state = new GameState(new int[][] { { 0, 0, 0, 0 },
				{ 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 } });
		List<GameState> stateList = new ArrayList<GameState>();

		for (int i = 0; i < state.getRowNum(); i++) {
			for (int j = 0; j < state.getColNum(); j++) {
				stateList.add(state.moveLeft().addNewValue(2, i, j));
			}
		}
		for (int i = 0; i < state.getRowNum(); i++) {
			for (int j = 0; j < state.getColNum(); j++) {
				stateList.add(state.moveLeft().addNewValue(4, i, j));
			}
		}

		while (stateList.contains(null)) {

			stateList.remove(null);
		}
		assertEquals(stateList.size(), 32);
	}

	@Test
	public void testAllStateGenerated() {
		GameState state = new GameState(new int[][] { { 0, 0, 0, 0 },
				{ 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 } });

		List<GameState> stateList = new ArrayList<GameState>();

		GameState left = state.moveLeft();
		GameState right = state.moveRight();
		GameState up = state.moveUp();
		GameState down = state.moveDown();

		// while(left.hasNextState()) left.getNextState().PrintState();

		while (left.hasNextState() || right.hasNextState() || up.hasNextState()
				|| down.hasNextState()) {
			stateList.add((left.getNextState()));
			stateList.add((right.getNextState()));
			stateList.add((up.getNextState()));
			stateList.add((down.getNextState()));

		}

		while (stateList.contains(null)) {
			stateList.remove(null);
		}

		System.out.println(stateList.size());
		assertEquals(stateList.size(), 128);
	}
}
