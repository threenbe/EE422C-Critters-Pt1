package assignment4;

import java.util.List;

public class Critter2 extends Critter {

	@Override
	public void doTimeStep() {
		run(getRandomInt(8));
	}

	@Override
	public boolean fight(String opponent) {
		if (getEnergy() > 10) return true;
		return false;
	}
	
	public String toString() {
		return "1";
	}
	
	public void test (List<Critter> l) {
		
	}
}
