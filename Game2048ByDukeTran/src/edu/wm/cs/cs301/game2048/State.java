package edu.wm.cs.cs301.game2048;

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
		return board[(x * 4) + y];
	}

	@Override
	public void setValue(int x, int y, int value) {
		board[(x * 4) + y] = value;
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
		for (int row = 0; row < 4; row++) {
			int compare = board[(row * 4)];
			for (int col = 1; col < 4; col++) {
				if (board[(row * 4) + col] == compare) {
					return true;
				} else if (board[(row * 4) + col] == 0) {
					continue;
				} else {	
					compare = board[(row * 4) + col];
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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int right() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int down() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int up() {
		// TODO Auto-generated method stub
		return 0;
	}

}
