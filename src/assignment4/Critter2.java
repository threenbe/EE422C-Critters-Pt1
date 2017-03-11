package assignment4;

public class Critter2 extends Critter { // "sloth"
	private int slowMove = 0; // moves every 6 turns
	@Override
	public void doTimeStep() {
		if(getEnergy() == 40) {
			Critter2 child = new Critter2();
			reproduce(child, Critter.getRandomInt(8));
		}
		if(slowMove == 0) {
			slowMove = 5;
			walk(getRandomInt(8));
		} else slowMove--;
	}

	@Override
	public boolean fight(String opponent) { // only "fights" plants
		if (opponent.equals("@")) return true;
		return false;
	}
	
	public String toString() {
		return "2";
	}
	
}
