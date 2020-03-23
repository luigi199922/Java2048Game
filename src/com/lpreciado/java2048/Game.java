package com.lpreciado.java2048;

import java.util.*;

public class Game {
	private final int rowLength;
	private final int colLength;
	private int[][] board;
	private int Score;
	private GameState state;

	public Game(int row, int col) {
		this.board = new int[row][col];
		this.rowLength = row;
		this.colLength = col;
		SpawnNumber();
		SpawnNumber();
		SpawnNumber();
		this.Score = 0;
		this.state = GameState.CONTINUE;
	}
	
	public void resetGame() {
		this.board = new int[this.rowLength][this.colLength];
		SpawnNumber();
		SpawnNumber();
		SpawnNumber();
		this.Score = 0;
		this.state = GameState.CONTINUE;
	}
	public int getRows() {
		return this.rowLength;
	}
	public int getColumns() {
		return this.colLength;
	}

	public void setBoard(int[][] board) {
		this.board = board;
	}

	public int getScore() {
		return this.Score;
	}

	public GameState getGameState() {
		return this.state;
	}

	public int[][] getGameBoard() {
		return this.board;
	}

	public void printBoard() {
		for (int row = 0; row < this.board.length; row++) {
			for (int col = 0; col < this.board[row].length; col++) {
				System.out.print(this.board[row][col] + " ");
			}
			System.out.println();
		}
	}

	private boolean checkFor2048() {
		for (int row = 0; row < this.board.length; row++) {
			for (int col = 0; col < this.board[row].length; col++) {
				if (this.board[row][col] == 2048) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean checkBoardFull() {
		for (int row = 0; row < this.board.length; row++) {
			for (int col = 0; col < this.board[row].length; col++) {
				if (this.board[row][col] == 0) {
					return false;
				}
			}
		}
		return true;
	}

	private boolean checkHasMoves() {
		for (int row = 0; row < this.board.length; row++) {
			for (int col = 0; col < this.board[row].length; col++) {
				
					if (row == 0) {
						if (col != 0) {
							if (this.board[row][col] == this.board[row][col - 1]) {
								return true;
							}
						}
					} else {
						if (col != 0) {
							if (this.board[row][col] == this.board[row][col - 1]
									|| this.board[row - 1][col] == this.board[row][col]) {
								return true;
							}
						}
					
				}
			}
		}
		return false;
	}

	public void checkState() {
		if (checkFor2048()) {
			state = GameState.WIN;
		} else if (checkBoardFull()) {
			if (!checkHasMoves()) {
				state = GameState.LOSE;
			} else {
				state = GameState.CONTINUE;
			}

		}
	}

	private int getNumberOfEmptySpaces() {
		int counter = 0;
		for (int row = 0; row < this.board.length; row++) {
			for (int col = 0; col < this.board[row].length; col++) {
				if (this.board[row][col] == 0) {
					++counter;
				}
			}
		}
		return counter;
	}

	private void insertNumberToBoard(int num, int index) {
		int counter = 0;
		for (int row = 0; row < this.board.length; row++) {
			for (int col = 0; col < this.board[row].length; col++) {
				if (counter > index) {
					break;
				}
				if (this.board[row][col] == 0 && counter == index) {
					this.board[row][col] = num;
					++counter;
					break;
				} else if (this.board[row][col] == 0 && counter != index) {
					++counter;
				}
			}
		}
	}

	private static boolean validateValue(int val, int maxPowerOfTwo) {
		if (val >= 0 && (val & val - 1) == 0 && val <= maxPowerOfTwo) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean validateRow(int[] row) {
		int max = 2048;
		for (int i = 0; i < row.length; i++) {
			if (!validateValue(row[i], max)) {
				return false;
			}
		}
		return true;
	}

	private int[] ColumnToArray(int columnNumber) {
		int[] arr = new int[this.board.length];
		for (int row = 0; row < this.board.length; row++) {
			arr[row] = this.board[row][columnNumber];
		}

		return arr;
	}

	private void ArrayToColumn(int[] arr, int columnNumber) {

		for (int row = 0; row < this.board.length; row++) {
			for (int col = 0; col < this.board[row].length; col++) {
				if (col == columnNumber) {
					this.board[row][col] = arr[row];
				}
			}
		}

	}

	private boolean Shift(int[] row, boolean Left) {
		if (!validateRow(row)) {
			return false;
		}
		if (Left) {
			for (int i = 0; i < row.length; i++) {
				if (row[i] != 0) {
					for (int j = 0; j < i; j++) {
						if (row[j] == 0) {
							int temp = row[i];
							row[i] = row[j];
							row[j] = temp;
						}
					}
				}
			}
		} else {
			for (int i = row.length - 1; i > 0; i--) {
				if (row[i] == 0) {
					for (int j = i; j >= 0; j--) {
						if (row[j] != 0) {
							int temp = row[i];
							row[i] = row[j];
							row[j] = temp;
							break;
						}
					}
				}
			}

		}

		return true;
	}

	private boolean combine(int[] row, boolean Left) {

		if (!Shift(row, Left)) {
			return false;
		}

		int length = row.length;

		if (Left) {
			for (int i = 0; i < length; i++) {
				int second = i + 1;
				if ((length - 1) >= second) {
					if (row[i] == row[second] && row[i] != 0) {
						int sum = row[i] * 2;
						this.Score += row[i];
						row[i] = sum;
						row[second] = 0;
						++i;
					} else if (row[i] == 0) {
						break;
					}

				} else {
					break;
				}
			}
		} else {
			for (int i = length - 1; i > 0; i--) {
				int second = i - 1;
				if (0 <= second) {
					if (row[i] == row[second] && row[i] != 0) {
						int sum = row[i] * 2;
						this.Score += row[i];
						row[i] = sum;
						row[second] = 0;
						--i;
					} else if (row[i] == 0) {
						break;
					}

				} else {
					break;
				}
			}

		}
		Shift(row, Left);
		return true;
	}

	public boolean combineGrid(boolean Left) {
		for (int row = 0; row < this.board.length; row++) {
			if (!combine(this.board[row], Left)) {
				return false;
			}
		}
		return true;
	}

	public boolean combineGridVertical(boolean Up) {
		for (int col = 0; col < this.board.length; col++) {
			// convert to array
			// Shift Accordingly
			// convert to board
			int[] arr = ColumnToArray(col);
			if (Up) {
				combine(arr, true);
			} else {
				combine(arr, false);
			}
			ArrayToColumn(arr, col);
		}

		return true;
	}

	public void SpawnNumber() {
		if (checkBoardFull()) {

		} else {
			Random rand = new Random();
			int index = rand.nextInt(getNumberOfEmptySpaces());
			int num = rand.nextInt(5);
			if (num == 1) {
				++num;
			}
			if (num == 0 || num == 3 || num == 1) {

			} else {
				insertNumberToBoard(num, index);
			}
		}

	}



}
