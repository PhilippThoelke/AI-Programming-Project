package frame;

import java.util.HashSet;

import optimization.Optimizers;
import optimization.Loss;

import frontend.Window;

public class Warehouse {

	private static PSU[] psus;
	private static int[] order;

	public static void main(String[] args) {
		Window window = new Window();
	}

	public static void readWarehouseFile(String path) {
		psus = Parser.parseWarehouse("problem_files\\problem1.txt");
	}

	public static void readOrderFile(String path) {
		order = Parser.parseOrder("problem_files\\order11.txt");
	}

	public static HashSet<Integer> maskedItems(boolean[] mask) {
		HashSet<Integer> items = new HashSet<>();
		for (int i = 0; i < psus.length; i++) {
			if (mask[i]) {
				items.addAll(psus[i].getItems());
			}
		}
		return items;
	}

	public static int numItemsCarried(boolean[] mask) {
		int numItemsCarried = 0;
		for (int i = 0; i < psus.length; i++) {
			if (mask[i]) {
				numItemsCarried += psus[i].itemCount();
			}
		}
		return numItemsCarried;
	}

	public static PSU getPSU(int index) {
		return psus[index];
	}

	public static int[] currentOrder() {
		return order;
	}

	public static int orderSize() {
		return order.length;
	}

	public static int psuCount() {
		return psus.length;
	}

}