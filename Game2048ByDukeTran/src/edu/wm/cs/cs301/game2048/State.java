package edu.wm.cs.cs301.game2048;

import java.util.Arrays;
import java.util.Stack;

public class State implements GameState {
	
	private int[] board;

	public State() {
		setEmptyBoard();
	}
	
	public State(State currentState) {
		fillBoard(currentState.board);
	}
	
	private void fillBoard(int[] newBoard) {
		board = new int[16];
		int idx = 0;
		for (int num: newBoard) {
			board[idx] = num;
			idx++;
		}
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
				if (tempValue == 0) continue;
				else if (tempValue == compare) return true;
				else compare = tempValue;
			}
		}
		
		for (int y = 0; y < 4; y++) {
			int compare = getValue(0, y);
			for (int x = 1; x < 4; x++) {
				int tempValue = getValue(x, y); 
				if (tempValue == 0) continue;
				else if (tempValue == compare) return true;
				else compare = tempValue;
			}
		}
		
		return false;
	}

	@Override
	public boolean reachedThreshold() {
		int max = 0;
		for (int num: board) if (num > max) max = num;
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
	
	public double evaluateState(String direction) {
		final double SCORE_COEFFICIENT = 1.25;
		final double ORDERING_COEFFICIENT = 2;
		final double SPACES_COEFFICIENT = 7;
		final double MERGE_COEFFICIENT = 3;
		
		double penalty = 0;
		
		/* New score heuristic */
		State newState = new State((State)this);
		double newScore = 0;
		double tempScore = 0;
		switch (direction) {
			case "left":
				tempScore = newState.left() * 1.1;
				break;
			case "right":
				tempScore = newState.right() * 1.25; 
				break;
			case "up":
				tempScore = newState.up();
				break;
			case "down":
				tempScore = newState.down() * 1.5;
				break;
		}
		int[] newBoard = newState.board;
		int max = 0;
		for (int num: newBoard) if (num > max) max = num;
		newScore = Math.log(2048) / Math.log(tempScore) * 1.5;

		/* Empty spaces heuristic. */
		int emptySpaces = 0;
		for (int num: newBoard) emptySpaces += num == 0 ? 1 : 0;
		
		/* Positioning heuristic. 
		 * Reward weighted points if tiles are present in the right column
		 * and second right column. 
		 * Weighting graph: 
		 * 0 2 5 7
		 * 0 2 4 7
		 * 1 2 3 7
		 * 0 1 2 10
		 */
		final int[] WEIGHT_GRAPH = new int[] {0,2,5,7,0,2,4,7,1,2,3,7,0,1,2,10};
		int positioning = 0;
		for (int idx = 0; idx < newBoard.length; idx++) 
			if (newBoard[idx] != 0) positioning += WEIGHT_GRAPH[idx];
		
		/** Ordering heuristic.
		 *  Check if max is in the bottom-right corner.
		 *  Reward points if right most column is full and 
		 *  in ascending order from top to bottom.
		 *  Reward points if second to the right column is in 
		 *  descending order from top to bottom. 
		 */
		int ordering = 0;
		int orderedMax = 0;
		if (newState.getValue(3, 3) == max) orderedMax = 60;
		else penalty += 120;
		for (int y = 0; y < 3; y++) {
			if (newState.getValue(3, y) <= newState.getValue(3, y + 1)) ordering += 7;
			else penalty += 9 * (4 - (y + 1)) * ORDERING_COEFFICIENT;
			if (newState.getValue(2, y) >= newState.getValue(2, y + 1)) ordering += 3;
			else penalty += 5 * (4 - (y + 1)) * ORDERING_COEFFICIENT;
			if (y == 0 && newState.getValue(2, y) != 0 && 
				newState.getValue(2, y) <= newState.getValue(3, y)) ordering += 5;
			else penalty += 7 * ORDERING_COEFFICIENT;
		}
		
		/** Potential merges heuristic
		 * Reward points for creating potential merges.
		 */
		int potentialMerges = 0;
		for (int x = 0; x < 4; x++) {
			int compare = newState.getValue(x, 0);
			for (int y = 1; y < 4; y++) {
				int tempValue = newState.getValue(x, y); 
				if (tempValue == 0) continue;
				else if (tempValue == compare) {
					potentialMerges += 1;
					compare = 0;
				}
				else compare = tempValue;
			}
		}
		for (int y = 0; y < 4; y++) {
			int compare = newState.getValue(0, y);
			for (int x = 1; x < 4; x++) {
				int tempValue = newState.getValue(x, y); 
				if (tempValue == 0) continue;
				else if (tempValue == compare) {
					potentialMerges += 1;
					compare = 0;
				}
				else compare = tempValue;
			}
		}
		
		System.out.printf("%s %s %s %s\n", positioning,
				ordering * ORDERING_COEFFICIENT, orderedMax, 
				emptySpaces * SPACES_COEFFICIENT);
		/* Sum the weighted heuristics. */
		return newScore + ordering * ORDERING_COEFFICIENT + orderedMax + 
			(emptySpaces > 8 ? emptySpaces * (SPACES_COEFFICIENT / 2) 
			: emptySpaces * SPACES_COEFFICIENT) - penalty;
	}

	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		
		State compState = (State) obj;
		if (!Arrays.equals(board, compState.board)) return false;
		return true;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		return prime * result + Arrays.hashCode(board);
	}
}
