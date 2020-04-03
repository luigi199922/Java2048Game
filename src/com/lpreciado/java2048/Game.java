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

	private boolean validateValue(int val, int maxPowerOfTwo) {
		if (val >= 0 && (val & val - 1) == 0 && val <= maxPowerOfTwo) {
			return true;
		} else {
			return false;
		}
	}

	private boolean validateRow(int[] row) {
		int max = 2048;
		for (int i = 0; i < row.length; i++) {
			if (!validateValue(row[i], max)) {
				return false;
			}
		}
		return true;
	}

	private boolean moveLeft(int[] row) {
		if (!validateRow(row)) {
			return false;
		}
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
		return true;
	}
	//NEW
	private int[] combineLeft(int[] row) {

		if (!moveLeft(row)) {
			return row;
		}

		int length = row.length;

		for (int i = 0; i < length; i++) {
			int second = i + 1; //second is the index of the next number in array
			if ((length - 1) >= second) { // if we have reached the end of array
				if (row[i] == row[second] && row[i] != 0) { //
					int sum = row[i] * 2;
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
		moveLeft(row);
		return row;
	}
	//NEW
	private int[][] combineLeft(int[][] b) {
		int[][] result = new int[b.length][b[0].length];
		for (int row = 0; row < result.length; row++) {
			result[row] = combineLeft(b[row]);
		}
		return result;
	}
	//NEW
	private int[][] rotateLeft(int[][] b) {
		int rowLengthOld = b[0].length;

		int[][] result = new int[b[0].length][b.length];
		for (int row = 0; row < result.length; row++) {
			for (int col = 0; col < result[row].length; col++) {
				result[row][col] = b[col][rowLengthOld - 1 - row];
			}
		}
		return result;
	}
	//NEW
	public void left() {
		board = combineLeft(board);
	}
	//NEW
	public void right() {
		int[][] result = rotateLeft(rotateLeft(combineLeft(rotateLeft(rotateLeft(board)))));
		board =  result;
	}
	//NEW
	public void up() {
		int[][] result = rotateLeft(rotateLeft(rotateLeft(combineLeft(rotateLeft(board)))));
		board =  result;
	}
	//NEW
	public void down() {
		int[][] result = rotateLeft(combineLeft(rotateLeft(rotateLeft(rotateLeft(board)))));
		board =  result;
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
