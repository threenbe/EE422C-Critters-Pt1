package assignment4;
/* CRITTERS Main.java
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

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.*;


/*
 * Usage: java <pkgname>.Main <input file> test
 * input file is optional.  If input file is specified, the word 'test' is optional.
 * May not use 'test' argument without specifying input file.
 */
public class Main {

    static Scanner kb;	// scanner connected to keyboard input, or input file
    private static String inputFile;	// input file, used instead of keyboard input if specified
    static ByteArrayOutputStream testOutputString;	// if test specified, holds all console output
    private static String myPackage;	// package of Critter file.  Critter cannot be in default pkg.
    private static boolean DEBUG = false; // Use it or not, as you wish!
    static PrintStream old = System.out;	// if you want to restore output to console


    // Gets the package name.  The usage assumes that Critter and its subclasses are all in the same package.
    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }

    /**
     * Main method.
     * @param args args can be empty.  If not empty, provide two parameters -- the first is a file name, 
     * and the second is test (for test output, where all output to be directed to a String), or nothing.
     */
    public static void main(String[] args) { 
        if (args.length != 0) {
            try {
                inputFile = args[0];
                kb = new Scanner(new File(inputFile));			
            } catch (FileNotFoundException e) {
                System.out.println("USAGE: java Main OR java Main <input file> <test output>");
                e.printStackTrace();
            } catch (NullPointerException e) {
                System.out.println("USAGE: java Main OR java Main <input file>  <test output>");
            }
            if (args.length >= 2) {
                if (args[1].equals("test")) { // if the word "test" is the second argument to java
                    // Create a stream to hold the output
                    testOutputString = new ByteArrayOutputStream();
                    PrintStream ps = new PrintStream(testOutputString);
                    // Save the old System.out.
                    old = System.out;
                    // Tell Java to use the special stream; all console output will be redirected here from now
                    System.setOut(ps);
                }
            }
        } else { // if no arguments to main
            kb = new Scanner(System.in); // use keyboard and console
        }

        /* Do not alter the code above for your submission. */
        /* Write your code below. */
        boolean continueSim= true;
        while(continueSim) {
        	ArrayList<String> tokens = parse(kb);
        	if(tokens.size() > 0) {
	        	try {
	        		continueSim = execute(tokens);
	        	} catch (Exception e) {
	        		System.out.print("error processing:");
	        		for(String token : tokens) {
	        			System.out.print(" " + token);
	        		}
	        		System.out.println();
	        	}
        	}
        }
        // System.out.println("GLHF");
        
        /* Write your code above */
        System.out.flush();

    }
    
    public static ArrayList<String> parse(Scanner kb) {
    	String token = kb.nextLine();
    	Scanner sc = new Scanner(token);
    	ArrayList<String> tokens = new ArrayList<String>();
    	while(sc.hasNext()) {
    		tokens.add(sc.next());
    	}
    	sc.close();
    	return tokens;
    }
    
    
    public static void checkCommandLength(ArrayList<String> tokens, int length) throws Exception {
    	if(tokens.size() != length) throw new Exception();
    }
    
    
    public static boolean execute(ArrayList<String> tokens) throws Exception {
    	// quit
    	if(tokens.get(0).equals("quit")) {
    		try {
    			checkCommandLength(tokens, 1);
    			return false;
    		} catch (Exception e){
    			throw e;
    		}
    	}
    	// show
    	if(tokens.get(0).equals("show")) {
    		try {
    			checkCommandLength(tokens, 1);
    			Critter.displayWorld();
    		} catch (Exception e){
    			throw e;
    		}
    		return true;
    	}
    	// step
    	if(tokens.get(0).equals("step")) {
    		int steps = 1;
    		if(tokens.size() > 1) {
    			try {
    				checkCommandLength(tokens, 2);
    				steps = Integer.parseInt(tokens.get(1));
    			} catch (Exception e){ // next token isnt an int
    				throw new Exception();
    			}
    		}
    		while (steps > 0) {
    			Critter.worldTimeStep();
    			steps--;
    		}
    		return true;
    	}
    	// seed
    	if(tokens.get(0).equals("seed")) {
    		try {
    			checkCommandLength(tokens, 2);
    			Critter.setSeed(Integer.parseInt(tokens.get(1)));
    		} catch (Exception e){ // next token isnt an int
				throw new Exception();
			}
    		return true;
    	}
    	// make
    	if(tokens.get(0).equals("make")) {
    		// records type of critter
    		/*Class<?> type; 
    		try {
    		    type = Class.forName(tokens.get(1));
    		} catch (Exception c) {
    				throw c;
    		}*/
    		
    		// checks if number specified, or if not then 1
    		int num = 1;
    		if(tokens.size() > 2) {
    			try {
    				checkCommandLength(tokens, 3);
    				num = Integer.parseInt(tokens.get(2));
    			} catch (Exception e){ // next token isn't an int or too many tokens
    				throw new Exception();
    			}
    		}
    		try {
    			String critter_class_name = myPackage + "." + tokens.get(1);
    			for (int i = 0; i < num; i++) {
    				Critter.makeCritter(critter_class_name);
    			}
    		} catch (Exception e) {
    			throw new Exception();
    		}
    		
    		return true;
    	}
    	// stats
    	if(tokens.get(0).equals("stats")) {
    		try {
    			checkCommandLength(tokens, 2);
    			String critter_class_name = myPackage + "." + tokens.get(1);
    			List<Critter> list_of_instances = Critter.getInstances(critter_class_name);
    			Critter.runStats(list_of_instances);
    		}
    		catch (Exception e) {
    			throw new Exception();
    		}
    		return true;
    	}
    	//unknown command
    	throw new Exception();
    }
}
