package assignment4;
/* CRITTERS Critter.java
 * EE422C Project 4 submission by
 * Replace <...> with your actual data.
 * <Student1 Name>
 * <Student1 EID>
 * <Student1 5-digit Unique No.>
 * <Student2 Name>
 * <Student2 EID>
 * <Student2 5-digit Unique No.>
 * Slip days used: <0>
 * Fall 2016
 */


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.Class;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/* see the PDF for descriptions of the methods and fields in this class
 * you may add fields, methods or inner classes to Critter ONLY if you make your additions private
 * no new public, protected or default-package code or data can be added to Critter
 */


public abstract class Critter {
	private static String myPackage;
	private	static List<Critter> population = new java.util.ArrayList<Critter>();
	private static List<Critter> babies = new java.util.ArrayList<Critter>();

	// Gets the package name.  This assumes that Critter and its subclasses are all in the same package.
	static {
		myPackage = Critter.class.getPackage().toString().split(" ")[1];
	}
	
	private static java.util.Random rand = new java.util.Random();
	public static int getRandomInt(int max) {
		return rand.nextInt(max);
	}
	
	public static void setSeed(long new_seed) {
		rand = new java.util.Random(new_seed);
	}
	
	
	/* a one-character long string that visually depicts your critter in the ASCII interface */
	public String toString() { return ""; }
	
	private int energy = 0;
	protected int getEnergy() { return energy; }
	
	private int x_coord;
	private int y_coord;
	
	private boolean already_moved;
	
	protected final void walk(int direction) {
		if (!already_moved) {
			already_moved = true;
			move(direction, 1);
		}
		energy -= Params.walk_energy_cost;
	}
	
	protected final void run(int direction) {
		if (!already_moved) {
			already_moved = true;
			move(direction, 2);
		}
		energy -= Params.run_energy_cost;
	}
	
	protected final void move(int direction, int steps) {
		switch(direction) {
			case 0://right
				x_coord = (x_coord + steps) % Params.world_width;
			case 1://up-right
				x_coord = (x_coord + steps) % Params.world_width;
				y_coord = (y_coord - steps) % Params.world_height;
			case 2://up
				y_coord = (y_coord - steps) % Params.world_height;
			case 3://up-left
				x_coord = (x_coord - steps) % Params.world_width;
				y_coord = (y_coord - steps) % Params.world_height;
			case 4://left
				x_coord = (x_coord - steps) % Params.world_width;
			case 5://down-left
				x_coord = (x_coord - steps) % Params.world_width;
				y_coord = (y_coord + steps) % Params.world_height;
			case 6://down
				y_coord = (y_coord + steps) % Params.world_height;
			case 7://down-right
				x_coord = (x_coord + steps) % Params.world_width;
				y_coord = (y_coord + steps) % Params.world_height;
		}
		
	}
	
	protected final void reproduce(Critter offspring, int direction) {
		// check if parent has enough energy
		if(this.getEnergy() < Params.min_reproduce_energy) return;
		// set energy of parent/child
		offspring.energy = this.energy / 2;
		this.energy = this.energy/2 + this.energy%2;
		// place child in world
		offspring.x_coord = this.x_coord;
		offspring.y_coord = this.y_coord;
		offspring.move(direction, 1);
		// add child to babies list
		babies.add(offspring);
	}

	public abstract void doTimeStep();
	public abstract boolean fight(String oponent);
	
	/**
	 * create and initialize a Critter subclass.
	 * critter_class_name must be the unqualified name of a concrete subclass of Critter, if not,
	 * an InvalidCritterException must be thrown.
	 * (Java weirdness: Exception throwing does not work properly if the parameter has lower-case instead of
	 * upper. For example, if craig is supplied instead of Craig, an error is thrown instead of
	 * an Exception.)
	 * @param critter_class_name
	 * @throws InvalidCritterException
	 */
	public static void makeCritter(String critter_class_name) throws InvalidCritterException {
		Class<?> my_critter = null;
		Constructor<?> constructor = null;
		Object instance_of_my_critter = null;
		
		try {
			my_critter = Class.forName(critter_class_name);
		} catch (ClassNotFoundException e) {
			throw new InvalidCritterException(critter_class_name);
		}
		//check if subclass of Critter
		if (!Critter.class.isAssignableFrom(my_critter)) {
			throw new InvalidCritterException(critter_class_name);
		}
		try { 
			constructor = my_critter.getConstructor();
			instance_of_my_critter = constructor.newInstance();
		} catch (NoSuchMethodException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new InvalidCritterException(critter_class_name);
		}
		
		Critter c = (Critter) instance_of_my_critter;
		c.energy = Params.start_energy;
		c.x_coord = getRandomInt(Params.world_width);
		c.y_coord = getRandomInt(Params.world_height);
		c.already_moved = false;
		population.add(c);
	}
	
	/**
	 * Gets a list of critters of a specific type.
	 * @param critter_class_name What kind of Critter is to be listed.  Unqualified class name.
	 * @return List of Critters.
	 * @throws InvalidCritterException
	 */
	public static List<Critter> getInstances(String critter_class_name) throws InvalidCritterException {
		List<Critter> result = new java.util.ArrayList<Critter>();
		for(Critter current : population) {
			Class<?> type;
			try{
		    type = Class.forName(critter_class_name);
			} catch (ClassNotFoundException c) {
				throw new InvalidCritterException(critter_class_name);
			}
		    if(type.isAssignableFrom(current.getClass())) {
		    	result.add(current);
		    }
		}
		return result;
	}
	
	/**
	 * Prints out how many Critters of each type there are on the board.
	 * @param critters List of Critters.
	 */
	public static void runStats(List<Critter> critters) {
		System.out.print("" + critters.size() + " critters as follows -- ");
		java.util.Map<String, Integer> critter_count = new java.util.HashMap<String, Integer>();
		for (Critter crit : critters) {
			String crit_string = crit.toString();
			Integer old_count = critter_count.get(crit_string);
			if (old_count == null) {
				critter_count.put(crit_string,  1);
			} else {
				critter_count.put(crit_string, old_count.intValue() + 1);
			}
		}
		String prefix = "";
		for (String s : critter_count.keySet()) {
			System.out.print(prefix + s + ":" + critter_count.get(s));
			prefix = ", ";
		}
		System.out.println();		
	}
	
	public static void doEncounters() {
		List<Critter> shared = new ArrayList<Critter>();
		for (int x = 0; x < Params.world_width; x++) {
			for (int y = 0; y < Params.world_height; y++) {
				//add all critters in this position to list
				for (Critter c : population) {
					if (c.x_coord == x && c.y_coord == y && c.energy > 0) {
						shared.add(c);
					}
				}
				//take care of encounters until there are 0-1 critters left
				while (shared.size() > 1) {
					Critter a = shared.get(0);
					Critter b = shared.get(1);
					//see if the critters want to fight
					boolean fight_a = a.fight(b.toString());
					boolean fight_b = a.fight(b.toString());
					
					//critters fight if these conditions are met
					if (a.energy > 0 && b.energy > 0 
							&& a.x_coord == x && a.y_coord == y
							&& b.x_coord == x && b.y_coord == y) {
						
						int rand_a, rand_b;
						
						if (fight_a)
							rand_a = getRandomInt(a.energy);
						else
							rand_a = 0;
						
						if (fight_b)
							rand_b = getRandomInt(b.energy);
						else
							rand_b = 0;
							
						if (rand_a > rand_b) {
							a.energy += (b.energy/2);
							b.energy = 0;
						} else {
							b.energy += (a.energy/2);
							a.energy = 0;
						}
					}
					
					//dead critters removed from list of critters at this position
					if (a.energy <= 0)
						shared.remove(a);
					if (b.energy <= 0)
						shared.remove(b);	
				}
				//"shared" list cleared so that the next position can be handled
				shared.clear();
			}
		}
	}
	
	/**
	 * Removes "dead" critters i.e. critters with no energy
	 */
	public static void removeDeadCritters() {
		Iterator<Critter> i = population.iterator();
		while (i.hasNext()) {
			Critter c = i.next();
			if (c.energy <= 0) {
				i.remove();
			}
		}
	}
	
	/* the TestCritter class allows some critters to "cheat". If you want to 
	 * create tests of your Critter model, you can create subclasses of this class
	 * and then use the setter functions contained here. 
	 * 
	 * NOTE: you must make sure that the setter functions work with your implementation
	 * of Critter. That means, if you're recording the positions of your critters
	 * using some sort of external grid or some other data structure in addition
	 * to the x_coord and y_coord functions, then you MUST update these setter functions
	 * so that they correctly update your grid/data structure.
	 */
	static abstract class TestCritter extends Critter {
		protected void setEnergy(int new_energy_value) {
			super.energy = new_energy_value;
		}
		
		protected void setX_coord(int new_x_coord) {
			super.x_coord = new_x_coord;
		}
		
		protected void setY_coord(int new_y_coord) {
			super.y_coord = new_y_coord;
		}
		
		protected int getX_coord() {
			return super.x_coord;
		}
		
		protected int getY_coord() {
			return super.y_coord;
		}
		

		/*
		 * This method getPopulation has to be modified by you if you are not using the population
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.
		 */
		protected static List<Critter> getPopulation() {
			return population;
		}
		
		/*
		 * This method getBabies has to be modified by you if you are not using the babies
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.  Babies should be added to the general population 
		 * at either the beginning OR the end of every timestep.
		 */
		protected static List<Critter> getBabies() {
			return babies;
		}
	}

	/**
	 * Clear the world of all critters, dead and alive
	 */
	public static void clearWorld() {
		population.clear();
		babies.clear();
	}
	
	public static void worldTimeStep() {
		// TODO Complete this method.
		
		//do time steps
		for (Critter c : population) {
			c.doTimeStep();
		}
		
		//resolve encounters
		doEncounters();
		
		//update rest energy
		for (Critter c : population) {
			c.energy -= Params.rest_energy_cost;
		}
		
		//generate algae
		for (int i = 0; i < Params.refresh_algae_count; i++) {
			Critter a = new Algae();
			a.energy = Params.start_energy;
			a.x_coord = getRandomInt(Params.world_width);
			a.y_coord = getRandomInt(Params.world_height);
			population.add(a);
		}
		
		//add babies to population
		population.addAll(babies);
		babies.clear();
		
		//set already_moved variables to false for next time step
		for (Critter c: population) {
			c.already_moved = false;
		}
		
		//remove dead critters
		removeDeadCritters();
	}
	
	public static void displayWorld() {
		// creates and populates 2D array representation of all the critters
		char[][] world = new char[Params.world_width][Params.world_height];
		for(Critter current : population) {
			world[current.x_coord][current.y_coord] = current.toString().charAt(0);
		}
		// prints the 2D array + border out
		// top border
		System.out.print("+");
		for(int i = 0; i < Params.world_width; i++) {
			System.out.print("-");
		}
		System.out.println("+");
		// each row of the map (with borders on left/right)
		for(int j = 0; j < Params.world_height; j++) {
			System.out.print("|");
			for(int k = 0; k < Params.world_width; k++) {
				System.out.print(world[k][j]);
			}
			System.out.println("|");
		}
		// bottom border
		System.out.print("+");
		for(int k = 0; k < Params.world_width; k++) {
			System.out.print("-");
		}
		System.out.println("+");
	}
}
