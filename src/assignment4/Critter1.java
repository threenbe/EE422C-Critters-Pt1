package assignment4;

public class Critter1 extends Critter { // "runner"

	@Override
	public void doTimeStep() {
		if(getEnergy() > 250) {
			Critter1 child = new Critter1();
			reproduce(child, Critter.getRandomInt(8));
		}
		run(getRandomInt(8));
	}

	@Override
	public boolean fight(String opponent) { // will run as characteristic unless it is exceedingly likely he will win,or he is low on energy ("hungry")
		if ((getEnergy() > 100) || (getEnergy() < Params.run_energy_cost * 3)) return true;
		run(getRandomInt(8));
		return false;
	}
	
	public String toString() {
		return "1";
	}
	
}
