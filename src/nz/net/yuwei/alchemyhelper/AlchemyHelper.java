package nz.net.yuwei.alchemyhelper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

public class AlchemyHelper {
	private String elemFileName;
	private String prodFileName;
	private HashMap<String, String> products;
	private LinkedList<String> elements, newElems;

	private int currIdx;

	public AlchemyHelper(String elemFile, String prodFile) {
		products = new HashMap<String, String>();
		elements = new LinkedList<String>();
		newElems = new LinkedList<String>();

		elemFileName = new String(elemFile);
		prodFileName = new String(prodFile);

		currIdx = 0;
	}

	public void readDB() {
		// read elements
		try {
			Scanner sc = new Scanner(new File(elemFileName));
			String elem;
			while(sc.hasNext()) {
				elem = sc.next();
				elements.add(elem);
				newElems.add(elem);
			}
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		// read product
		try {
			Scanner sc = new Scanner(new File(prodFileName));
			String elems, prod;
			while(sc.hasNext()) {
				elems = sc.next();
				if(sc.hasNext()) {
					prod = sc.next();
					products.put(elems, prod);
				} else {
					System.err.println("product DB is broken");
					System.exit(1);
				}
			}
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void writeDB() {
		// write element
		try {	
			BufferedWriter out = new BufferedWriter(new FileWriter(elemFileName));
			for(String i : elements) {
				out.write(i + "\n");
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// write product
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(prodFileName));
			for(String i : products.keySet()) {
				out.write(i + " " + products.get(i) + "\n");
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String genKey(String a, String b) {
		if(a.compareTo(b) > 0) {
			return a+"+"+b;
		}

		return b+"+"+a;
	}

	public void printAllElem() {
		System.out.println();
		for(String x : elements) {
			System.out.print(x + " ");
		}
		System.out.println("\n");
	}

	public void printAllProd() {
		System.out.println();
		for(String i : products.keySet()) {
			if(!products.get(i).equals("--"))
				System.out.println(i + " " + products.get(i));
		}
		System.out.println();
	}

	public void getNextTest() {
		if(currIdx == elements.size()) {
			newElems.poll();
			currIdx = 0;
		}

		if(newElems.isEmpty()) {
			System.out.println("That's all");
			return;
		}

		if(newElems.get(0).equals(elements.get(currIdx))) {
			currIdx++;
			getNextTest();
			return;
		}

		String key = genKey(newElems.element(), elements.get(currIdx));
		String input, newProd;
		if(products.containsKey(key)) {
			currIdx++;
			getNextTest();
		} else {
			System.out.println("TRY: " + key);
			System.out.print("Does it produce new element? [Y/N] ");
			Scanner sc = new Scanner(System.in);
			input = sc.next().toLowerCase();
			if(input.equals("Y") || input.equals("y")) {
				System.out.print("Input the name of product: ");
				newProd = sc.next();
				products.put(key, newProd);
				if(!elements.contains(newElems)) {
					elements.add(newProd);
					newElems.add(newProd);
				}
				currIdx++;
			} else {
				products.put(key, "--");
				currIdx++;
				getNextTest();
			}
			
		}
	}
	
	public void manualInput() {
		
	}

	public static void printMenu() {


		System.out.println("    Alchemy Helper Menu		");
		System.out.println("============================");
		System.out.println(" 1 print all elements ");
		System.out.println(" 2 print all products ");
		System.out.println(" 3 suggest next test  ");
		System.out.println(" 4 manual input  (Not supported yet) ");
		System.out.println(" 5 write to database  ");
		System.out.println(" 6 quit				  ");
		System.out.println("============================");
		System.out.print(" Enter the number: ");
	}

	public static int readInput() {
		Scanner sc = new Scanner(System.in);
		String tmp = sc.next();
		try {
			int ret = Integer.parseInt(tmp);
			return ret;
		} catch (Exception e) {
			//e.printStackTrace();
			return 0;
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int choice;
		AlchemyHelper ah = new AlchemyHelper(args[0],args[1]);
		ah.readDB();

		String ESC = "\033[";
		System.out.print(ESC + "2J"); 
		System.out.flush();
		printMenu();
		do {
			choice = readInput();
			switch (choice) {
			case 1:
				ah.printAllElem();
				break;
			case 2:
				ah.printAllProd();
				break;
			case 3:
				ah.getNextTest();
				break;
			case 4:
				ah.manualInput();
				break;
			case 5:
				ah.writeDB();
				break;
			case 6:
				System.out.println();
				ah.writeDB();
				break;
			default:
				System.err.println("not a valid choice");
				break;
			}
			printMenu();
		} while(choice != 5);
	}





}
