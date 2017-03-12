package assignment4;

public class Critter3 extends Critter {

	@Override
	public void doTimeStep() {
		// TODO Auto-generated method stub
		
	}

	@Override
	/**
	 * This critter is a carnivore that unconditionally fights non-Algae critters.
	 * Unfortunately it is allergic to plants and has a 50/50 chance of dying should 
	 * it encounter Algae and fail to escape from it.
	 * @param String representation of opponent
	 */
	public boolean fight(String oponent) {
		if (!oponent.equals("@")) {
			return true;
		}
		int dir = Critter.getRandomInt(8);
		run(dir);
		return false;
	}
	
	public String toString() {
		return "3";
	}
	
}