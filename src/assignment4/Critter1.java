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

public class Critter1 extends Critter {
/**
 * The Runner must always run. It will reprocude only with a significant 
 * amount of energy headroom.
 */
	@Override
	public void doTimeStep() {
		if(getEnergy() > 250) {
			Critter1 child = new Critter1();
			reproduce(child, Critter.getRandomInt(8));
		}
		run(getRandomInt(8));
	}
/**
 * The critter will always try to run, except in the cases where it is hungry 
 * and desperate to eat (will die in <3 turns of running) or where it is likely 
 * to win a fight because of its energy count.
 */
	@Override
	public boolean fight(String opponent) {
		if ((getEnergy() > 100) || (getEnergy() < Params.run_energy_cost * 3)) {
			return true;
		}
		run(getRandomInt(8));
		return false;
	}
	
	public String toString() {
		return "1";
	}
	
}
