package edu.wm.cs.cs301.game2048;

import java.util.Stack;

public class State implements GameState {
	
	private int[] board;

	public State() {
		setEmptyBoard();
	}
	
	public State(State currentState) {
		board = currentState.getBoard();
	}
	
	public int[] getBoard() {
		return board;
	}

	@Override
	public int getValue(int x, int y) {
		return board[(y * 4) + x];
	}

	@Override
	public void setValue(int x, int y, int value) {
		board[(y * 4) + x] = value;
	}

	@Override
	public void setEmptyBoard() {
		board = new int[16];
		for (int i = 0; i < board.length; i++) {
			board[i] = 0;
		}
	}

	@Override
	public boolean addTile() {
		if (isFull()) return false;
		
		int randomIdx;
		do {
			randomIdx = (int)(Math.random() * 16);
		} while (board[randomIdx] != 0);
		
		double randomNum = Math.random();
		board[randomIdx] = randomNum > 0.5 ? 2 : 4;
		return true;
	}

	@Override
	public boolean isFull() {
		for (int num: board) {
			if (num == 0) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean canMerge() {
		for (int x = 0; x < 4; x++) {
			int compare = getValue(x, 0);
			for (int y = 1; y < 4; y++) {
				int tempValue = getValue(x, y); 
				if (tempValue == compare) {
					return true;
				} else if (tempValue == 0) {
					continue;
				} else {	
					compare = tempValue;
				}
			}
		}
		
		for (int y = 0; y < 4; y++) {
			int compare = getValue(0, y);
			for (int x = 1; x < 4; x++) {
				int tempValue = getValue(x, y); 
				if (tempValue == compare) {
					return true;
				} else if (tempValue == 0) {
					continue;
				} else {	
					compare = tempValue;
				}
			}
		}
		
		return false;
	}

	@Override
	public boolean reachedThreshold() {
		int max = 0;
		for (int num: board) {
			if (num > max) {
				max = num;
			}
		}
		return max >= 2048 ? true : false;
	}

	@Override
	public int left() {
		Stack<Integer> tileStack = new Stack<Integer>();
		int score = 0;
		
		for (int y = 0; y < 4; y++) {
			for (int x = 3; x >= 0; x--) {
				int num = getValue(x, y);
				if (num != 0) {
					tileStack.push(num);
					setValue(x, y, 0);
				}
			}
			
			int tempX = 0;
			while (!tileStack.empty()) {
				int tempNum = tileStack.pop();
				if (!tileStack.empty() && tempNum == tileStack.peek()) {
					tempNum += tileStack.pop(); 
					score += tempNum;
				}
				setValue(tempX, y, tempNum);
				tempX++;
			}
		}
		
		return score;
	}

	@Override
	public int right() {
		Stack<Integer> tileStack = new Stack<Integer>();
		int score = 0;
		
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				int num = getValue(x, y);
				if (num != 0) {
					tileStack.push(num);
					setValue(x, y, 0);
				}
			}
			
			int tempX = 3;
			while (!tileStack.empty()) {
				int tempNum = tileStack.pop();
				if (!tileStack.empty() && tempNum == tileStack.peek()) {
					tempNum += tileStack.pop(); 
					score += tempNum;
				}
				setValue(tempX, y, tempNum);
				tempX--;
			}
		}
		
		return score;
	}

	@Override
	public int down() {
		Stack<Integer> tileStack = new Stack<Integer>();
		int score = 0;
		
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {
				int num = getValue(x, y);
				if (num != 0) {
					tileStack.push(num);
					setValue(x, y, 0);
				}
			}
			
			int tempY = 3;
			while (!tileStack.empty()) {
				int tempNum = tileStack.pop();
				if (!tileStack.empty() && tempNum == tileStack.peek()) {
					tempNum += tileStack.pop();
					score += tempNum;
				}
				setValue(x, tempY, tempNum);
				tempY--;
			}
		}
		
		return score;
	}

	@Override
	public int up() {
		Stack<Integer> tileStack = new Stack<Integer>();
		int score = 0;
		
		for (int x = 0; x < 4; x++) {
			for (int y = 3; y >= 0; y--) {
				int num = getValue(x, y);
				if (num != 0) {
					tileStack.push(num);
					setValue(x, y, 0);
				}
			}
			
			int tempY = 0;
			while (!tileStack.empty()) {
				int tempNum = tileStack.pop();
				if (!tileStack.empty() && tempNum == tileStack.peek()) {
					tempNum += tileStack.pop(); 
					score += tempNum;
				}
				setValue(x, tempY, tempNum);
				tempY++;
			}
		}
		
		return score;
	}

	public boolean equals(State compState) {
		// TODO: implement equals method
		return false;
	}
	
	public int hashCode() {
		// TODO: implement hashcode method
		return 0;
	}
}
