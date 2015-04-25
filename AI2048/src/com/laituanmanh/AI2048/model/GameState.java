package com.laituanmanh.AI2048.model;

import com.laituanmanh.commontools.Tool;

/**
 * 
 * @author manh
 *
 */
public class GameState {

	private int[][] board;
	private int colNum;
	private int rowNum;
	private static int goal = 2048;

	private int currentCol;
	private int currentRow;
	private int valueGen;

	private GameState parent;

	public GameState(int[][] board) {
		this.board = board;
		this.rowNum = board.length;
		this.colNum = board[0].length;
		valueGen = 2;
	}

	private GameState(int[][] board, GameState parent) {
		this(board);
		this.parent = parent;
	}

	/**
	 * generate a new state conform to direction.
	 * 
	 * @return newGameState : a new instance conform to new move.
	 */
	public GameState moveLeft() {
		return new GameState(this.left(), this);
	}

	public GameState moveRight() {
		return new GameState(this.right(), this);
	}

	public GameState moveUp() {
		return new GameState(this.up(), this);
	}

	public GameState moveDown() {
		return new GameState(this.down(), this);
	}

	/**
	 * Check if the current state reached the goal of game.
	 * 
	 * @return true if reach the goal.
	 */
	public boolean checkWin() {
		for (int i = 0; i < colNum; i++) {
			for (int j = 0; j < rowNum; j++) {
				if (board[j][i] == GameState.goal)
					return true;
			}
		}
		return false;
	}

	/**
	 * Print current state to console.
	 */
	public void PrintState() {
		System.out.println(this.toString());
	}

	/**
	 * Set goal for searching.
	 * 
	 * @param goal
	 *            : value that we expect to reach.
	 */
	public void setGoal(int goal) {
		GameState.goal = goal;
	}

	/**
	 * Return the goal that we have set.
	 * 
	 * @return goal.
	 */
	public int getGoal() {
		return GameState.goal;
	}

	/**
	 * Add a value to the board at index (row,col).
	 * 
	 * @param value
	 *            => value we wanna add.
	 * @param row
	 *            => row we wanna set.
	 * @param col
	 *            => column we wanna set.
	 * @return null => if the index is not equal to 0. GameState => if adding
	 *         task success.
	 */
	public GameState addNewValue(int value, int row, int col) {
		int[][] newBoard = this.add(value, row, col);
		if (newBoard == null)
			return null;
		return new GameState(newBoard, parent);
	}

	/**
	 * Return the number of column of the board game.
	 * 
	 * @return board[0].length;
	 */
	public int getColNum() {
		return colNum;
	}

	/**
	 * Return the number of row of the board game.
	 * 
	 * @return board.length;
	 */
	public int getRowNum() {
		return rowNum;
	}

	/**
	 * Getting value at index(row,col).
	 * 
	 * @param row
	 * @param col
	 * @return board[row,col]
	 */
	public int getValue(int row, int col) {
		return board[row][col];
	}

	/**
	 * Overriding the toString method of object
	 * 
	 * @return String
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < rowNum; i++) {
			for (int j = 0; j < colNum; j++) {
				builder.append(String.format("%-5d", board[i][j]));
			}
			builder.append("\n");
		}
		builder.append("\n");

		return builder.toString();
	}

	/**
	 * Check if the current state can generate the next state. *Only implemented
	 * in advance game state class.
	 * 
	 * @return true if has next state.
	 * @return false if not
	 */
	public boolean hasNextState() {
		return !(currentRow >= this.getColNum());
	}

	/**
	 * Return the next state of current state.
	 * 
	 * @return GameState => if generable.
	 * @return null => if non-generable
	 */
	public GameState getNextState() {
		// Return null if not generable.
		if (!hasNextState())
			return null;

		while (hasNextState()) {
			// Generate new state
			GameState state = this
					.addNewValue(valueGen, currentRow, currentCol);

			// Increase index value
			currentRow += (currentCol + 1) / this.getColNum();
			currentCol = (currentCol + 1) % this.getColNum();

			// If new state is not null, return it.
			if (null != state) {
				if (currentRow >= this.getRowNum() && 2 == valueGen) {
					// Reset index
					currentRow = 0;
					currentCol = 0;
					valueGen = 4;
				}

				return state;
			}
		}

		return null;
	}

	/**
	 * Return the parent of current state.
	 * 
	 * @return parent
	 */
	public GameState getParent() {
		return parent;
	}

	/**
	 * Set parent to current state.
	 * 
	 * @return itself
	 */
	public GameState setParent(GameState pState) {
		this.parent = pState;
		return this;
	}

	/**
	 * move all value on the board to the left.
	 * 
	 * @return newBoard;
	 */
	protected int[][] left() {
		int[][] newBoard = Tool.cloneArray(board);

		for (int i = 0; i < rowNum; i++) {
			for (int j = 0; j < colNum - 1; j++) {
				// if current cell !=0 find the next cell != 0
				if (newBoard[i][j] != 0)
					for (int k = j + 1; k < colNum; k++) {
						if (newBoard[i][j] == newBoard[i][k]) {
							// Get new value of 2 cell;
							newBoard[i][j] = newBoard[i][j] + newBoard[i][k];
							// Delete cell after merge.
							newBoard[i][k] = 0;

							// Check if 2048 value appears.

							if (newBoard[i][j] == GameState.goal)

								// Move value to the cell next to lastmove cell.
								j = k;
						}
					}
				if (j >= colNum - 1) {
					continue;
				}
			}

			// Fix 0 space
			int last = colNum;
			for (int a = 0; a < colNum; a++) {
				if (newBoard[i][a] == 0) {
					last = a;
					break;
				}

			}
			for (int a = last + 1; a < colNum; a++) {
				if (newBoard[i][a] != 0) {
					newBoard[i][last] = newBoard[i][a];
					newBoard[i][a] = 0;
					last += 1;
				}
			}
		}

		return newBoard;
	}

	/**
	 * move all value on the board to the right.
	 * 
	 * @return newBoard;
	 */
	protected int[][] right() {
		int[][] newBoard = Tool.cloneArray(board);

		for (int i = 0; i < rowNum; i++) {
			for (int j = colNum - 1; j > 0; j--) {
				// if current cell !=0 find the next cell != 0
				if (newBoard[i][j] != 0)
					for (int k = j - 1; k >= 0; k--) {
						if (newBoard[i][j] == newBoard[i][k]) {
							// Get new value of 2 cell;
							newBoard[i][j] = newBoard[i][j] + newBoard[i][k];
							// Delete cell after merge.
							newBoard[i][k] = 0;

							// Check if 2048 value appears.
							if (newBoard[i][j] == GameState.goal)

								// Move value to the cell next to lastmove cell.
								j = k;
						}
					}
				if (j <= 0) {
					continue;
				}
			}

			// Fix 0 space
			int last = -1;
			for (int a = colNum - 1; a >= 0; a--) {
				if (newBoard[i][a] == 0) {
					last = a;
					break;
				}

			}
			for (int a = last - 1; a >= 0; a--) {
				if (newBoard[i][a] != 0) {
					newBoard[i][last] = newBoard[i][a];
					newBoard[i][a] = 0;
					last -= 1;
				}
			}
		}
		return newBoard;
	}

	/**
	 * move all value on the board to the top.
	 * 
	 * @return newBoard;
	 */
	protected int[][] up() {
		int[][] newBoard = Tool.cloneArray(board);

		for (int j = 0; j < colNum; j++) {
			for (int i = 0; i < rowNum - 1; i++) {
				// if current cell !=0 find the next cell != 0
				if (newBoard[i][j] != 0)
					for (int k = i + 1; k < rowNum; k++) {
						if (newBoard[i][j] == newBoard[k][j]) {
							// Get new value of 2 cell;
							newBoard[i][j] = newBoard[i][j] + newBoard[k][j];
							// Delete cell after merge.
							newBoard[k][j] = 0;
							// Check if 2048 value appears.
							if (newBoard[i][j] == GameState.goal)

								// Move value to the cell next to lastmove cell.
								i = k;
						}
					}
				if (i >= rowNum - 1) {
					continue;
				}
			}

			// Fix 0 space
			int last = rowNum;
			for (int a = 0; a < rowNum; a++) {
				if (newBoard[a][j] == 0) {
					last = a;
					break;
				}

			}
			for (int a = last + 1; a < colNum; a++) {
				if (newBoard[a][j] != 0) {
					newBoard[last][j] = newBoard[a][j];
					newBoard[a][j] = 0;
					last += 1;
				}
			}
		}
		return newBoard;
	}

	/**
	 * move all value on the board to the bottom.
	 * 
	 * @return newBoard;
	 */
	protected int[][] down() {
		int[][] newBoard = Tool.cloneArray(board);

		for (int j = 0; j < colNum; j++) {
			for (int i = rowNum - 1; i > 0; i--) {
				// if current cell !=0 find the next cell != 0
				if (newBoard[i][j] != 0)
					for (int k = i - 1; k >= 0; k--) {
						if (newBoard[i][j] == newBoard[k][j]) {
							// Get new value of 2 cell;
							newBoard[i][j] = newBoard[i][j] + newBoard[k][j];
							// Delete cell after merge.
							newBoard[k][j] = 0;
							// Check if 2048 value appears.
							if (newBoard[i][j] == GameState.goal)

								// Move value to the cell next to lastmove cell.
								i = k;
						}
					}
				if (i <= 0) {
					continue;
				}
			}

			// Fix 0 space
			int last = -1;
			for (int a = rowNum - 1; a > 0; a--) {
				if (newBoard[a][j] == 0) {
					last = a;
					break;
				}

			}
			for (int a = last - 1; a >= 0; a--) {
				if (newBoard[a][j] != 0) {
					newBoard[last][j] = newBoard[a][j];
					newBoard[a][j] = 0;
					last -= 1;
				}
			}
		}
		return newBoard;
	}

	protected int[][] add(int value, int row, int col) {
		try {
			if (board[row][col] != 0)
				return null;
		} catch (ArrayIndexOutOfBoundsException ex) {
			System.out.println(row + " " + col);
		}

		int[][] newBoard = Tool.cloneArray(this.board);
		newBoard[row][col] = value;
		return newBoard;
	}

	/**
	 * 
	 * @return
	 */
	public int[] flat() {
		int[] flatboard = new int[this.getColNum() * this.getRowNum()];
		for (int i = 0; i < getRowNum(); i++) {
			for (int j = 0; j < getColNum(); j++) {
				flatboard[getColNum() * i + j] = board[i][j];
			}
		}
		return flatboard;
	}
}
