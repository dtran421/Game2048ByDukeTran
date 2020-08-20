package edu.wm.cs.cs301.game2048;

public class State implements GameState {

	public State() {
		// TODO Auto-generated constructor stub
	}
	
	public State(State currentState) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getValue(int x, int y) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setValue(int x, int y, int value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setEmptyBoard() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean addTile() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isFull() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canMerge() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean reachedThreshold() {
		// TODO Auto-generated method stub
		return false;
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
