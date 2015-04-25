package com.laituanmanh.AI2048.model;

public class SolutionPath {
	private String action;
	private GameState state;
	private SolutionPath nextState;

	public SolutionPath() {
		action = null;
		state = null;
		nextState = null;
	};

	public SolutionPath(GameState state, String action) {
		this.state = state;
		this.action = action;
	}

	public SolutionPath(GameState state, String action, SolutionPath next) {
		this.state = state;
		this.action = action;
		this.nextState = next;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public GameState getState() {
		return state;
	}

	public void setState(GameState state) {
		this.state = state;
	}

	public SolutionPath getNextState() {
		return nextState;
	}

	public void setNextState(SolutionPath nextState) {
		this.nextState = nextState;
	}

}
