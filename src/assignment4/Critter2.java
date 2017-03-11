package assignment4;

import java.util.List;

public class Critter2 extends Critter { // "sloth"
	private int slowMove = 0; // moves every 6 turns
	@Override
	public void doTimeStep() {
		if(slowMove == 0) {
			slowMove = 5;
			walk(getRandomInt(8));
		} else slowMove--;
	}

	@Override
	public boolean fight(String opponent) {
		if (getEnergy() > 10) return true;
		return false;
	}
	
	public String toString() {
		return "1";
	}
	
}
