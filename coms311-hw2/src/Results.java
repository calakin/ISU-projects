import java.util.Random;

public class Results {
	public static void main(String[] args) {
		
		int arr3000[] = new int[3000];
		int arr30000[] = new int[30000];
		int arr300000[] = new int[300000];
		double x, y;
		SelectionSort s = new SelectionSort();
		BubbleSort b = new BubbleSort();
		InsertionSort i = new InsertionSort();
		
		
/*		//selection sort
		//sorted
		//3000
		makeSorted(arr3000);
		x = System.currentTimeMillis();
		s.sort(arr3000);
		y = System.currentTimeMillis();
		System.out.println("Operation took " + (y-x)/1000 + "seconds.");
		//30000
		makeSorted(arr30000);
		x = System.currentTimeMillis();
		s.sort(arr30000);
		y = System.currentTimeMillis();
		System.out.println("Operation took " + (y-x)/1000 + "seconds.");
		//300000
		makeSorted(arr300000);
		x = System.currentTimeMillis();
		s.sort(arr300000);
		y = System.currentTimeMillis();
		System.out.println("Operation took " + (y-x)/1000 + "seconds.");
		
		//reverse sorted
		//3000
		makeReverseSorted(arr3000);
		x = System.currentTimeMillis();
		s.sort(arr3000);
		y = System.currentTimeMillis();
		System.out.println("Operation took " + (y-x)/1000 + "seconds.");
		//30000
		makeReverseSorted(arr30000);
		x = System.currentTimeMillis();
		s.sort(arr30000);
		y = System.currentTimeMillis();
		System.out.println("Operation took " + (y-x)/1000 + "seconds.");
		//300000
		makeReverseSorted(arr300000);
		x = System.currentTimeMillis();
		s.sort(arr300000);
		y = System.currentTimeMillis();
		System.out.println("Operation took " + (y-x)/1000 + "seconds.");
		
		//random
		//3000
		makeRandom(arr3000);
		x = System.currentTimeMillis();
		s.sort(arr3000);
		y = System.currentTimeMillis();
		System.out.println("Operation took " + (y-x)/1000 + "seconds.");
		//30000
		makeRandom(arr30000);
		x = System.currentTimeMillis();
		s.sort(arr30000);
		y = System.currentTimeMillis();
		System.out.println("Operation took " + (y-x)/1000 + "seconds.");
		//300000
		makeRandom(arr300000);
		x = System.currentTimeMillis();
		s.sort(arr300000);
		y = System.currentTimeMillis();
		System.out.println("Operation took " + (y-x)/1000 + "seconds.");
		
		
		//bubble sort
		//sorted
		//3000
		makeSorted(arr3000);
		x = System.currentTimeMillis();
		b.sort(arr3000);
		y = System.currentTimeMillis();
		System.out.println("Operation took " + (y-x)/1000 + "seconds.");
		//30000
		makeSorted(arr30000);
		x = System.currentTimeMillis();
		b.sort(arr30000);
		y = System.currentTimeMillis();
		System.out.println("Operation took " + (y-x)/1000 + "seconds.");
		//300000
		makeSorted(arr300000);
		x = System.currentTimeMillis();
		b.sort(arr300000);
		y = System.currentTimeMillis();
		System.out.println("Operation took " + (y-x)/1000 + "seconds.");
		
		//reverse sorted
		//3000
		makeReverseSorted(arr3000);
		x = System.currentTimeMillis();
		b.sort(arr3000);
		y = System.currentTimeMillis();
		System.out.println("Operation took " + (y-x)/1000 + "seconds.");
		//30000
		makeReverseSorted(arr30000);
		x = System.currentTimeMillis();
		b.sort(arr30000);
		y = System.currentTimeMillis();
		System.out.println("Operation took " + (y-x)/1000 + "seconds.");
		//300000
		makeReverseSorted(arr300000);
		x = System.currentTimeMillis();
		b.sort(arr300000);
		y = System.currentTimeMillis();
		System.out.println("Operation took " + (y-x)/1000 + "seconds.");
		
		//random
		//3000
		makeRandom(arr3000);
		x = System.currentTimeMillis();
		b.sort(arr3000);
		y = System.currentTimeMillis();
		System.out.println("Operation took " + (y-x)/1000 + "seconds.");
		//30000
		makeRandom(arr30000);
		x = System.currentTimeMillis();
		b.sort(arr30000);
		y = System.currentTimeMillis();
		System.out.println("Operation took " + (y-x)/1000 + "seconds.");
		//300000
		makeRandom(arr300000);
		x = System.currentTimeMillis();
		b.sort(arr300000);
		y = System.currentTimeMillis();
		System.out.println("Operation took " + (y-x)/1000 + "seconds.");*/
		
		
		//insertion sort
		//sorted
		//3000
		makeSorted(arr3000);
		x = System.currentTimeMillis();
		i.sort(arr3000);
		y = System.currentTimeMillis();
		System.out.println("Operation took " + (y-x)/1000 + "seconds.");
		//30000
		makeSorted(arr30000);
		x = System.currentTimeMillis();
		i.sort(arr30000);
		y = System.currentTimeMillis();
		System.out.println("Operation took " + (y-x)/1000 + "seconds.");
		//300000
		makeSorted(arr300000);
		x = System.currentTimeMillis();
		i.sort(arr300000);
		y = System.currentTimeMillis();
		System.out.println("Operation took " + (y-x)/1000 + "seconds.");
		
		//reverse sorted
		//3000
		makeReverseSorted(arr3000);
		x = System.currentTimeMillis();
		i.sort(arr3000);
		y = System.currentTimeMillis();
		System.out.println("Operation took " + (y-x)/1000 + "seconds.");
		//30000
		makeReverseSorted(arr30000);
		x = System.currentTimeMillis();
		i.sort(arr30000);
		y = System.currentTimeMillis();
		System.out.println("Operation took " + (y-x)/1000 + "seconds.");
		//300000
		makeReverseSorted(arr300000);
		x = System.currentTimeMillis();
		i.sort(arr300000);
		y = System.currentTimeMillis();
		System.out.println("Operation took " + (y-x)/1000 + "seconds.");
		
		//random
		//3000
		makeRandom(arr3000);
		x = System.currentTimeMillis();
		i.sort(arr3000);
		y = System.currentTimeMillis();
		System.out.println("Operation took " + (y-x)/1000 + "seconds.");
		//30000
		makeRandom(arr30000);
		x = System.currentTimeMillis();
		i.sort(arr30000);
		y = System.currentTimeMillis();
		System.out.println("Operation took " + (y-x)/1000 + "seconds.");
		//300000
		makeRandom(arr300000);
		x = System.currentTimeMillis();
		i.sort(arr300000);
		y = System.currentTimeMillis();
		System.out.println("Operation took " + (y-x)/1000 + "seconds.");
	}
	
	private static void makeSorted(int arr[]) {
		int length = arr.length;
		int[] arr2 = new int[length];
		
		for(int i = 0; i < length; i++) {
			arr2[i] = i;
		}
		
		arr = arr2;
	}
	
	private static void makeReverseSorted(int arr[]) {
		int length = arr.length;
		int[] arr2 = new int[length];
		
		for(int i = 0; i < length; i++) {
			arr2[length - 1 - i] = i;
		}
		
		arr = arr2;
	}
	
	private static void makeRandom(int arr[]) {
		int length = arr.length;
		int[] arr2 = new int[length];
		
		Random r = new Random();
		
		for(int i = 0; i < length; i++) {
			arr2[i] = r.nextInt();
		}
		
		arr = arr2;
	}
}
