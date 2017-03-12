package assignment4;
/* CRITTERS Main.java
 * EE422C Project 4 submission by
 * Timberlon Gray
 * tg22698
 * 16235
 * Raiyan Chowdhury
 * rac4444
 * 16235
 * Slip days used: <0>
 * Fall 2016
 */

public class Critter2 extends Critter {
	private int slowMove = 0; // moves every 6 turns
/**
 * The Sloth moves very infrequently, and reproduces at a 
 * fairly low energy count.
 */
	@Override
	public void doTimeStep() {
		if(getEnergy() > 40) {
			Critter2 child = new Critter2();
			reproduce(child, Critter.getRandomInt(8));
		}
		if(slowMove == 0) {
			slowMove = 5;
			walk(getRandomInt(8));
		} else slowMove--;
	}

/**
 * Because the sloth is vegetarian, it will try to run from any 
 * fight that is not with algae.
 */
	@Override
	public boolean fight(String opponent) {
		if (opponent.equals("@")) return true;
		return false;
	}
	
	public String toString() {
		return "2";
	}
	
}
