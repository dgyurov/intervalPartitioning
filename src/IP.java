import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class IP{
	
     /* 
     *
     * by Denitsa Mlechkova, Andreas Rangholm, Dimitar Gyurov and Lars Ommen - September 2015
	 * Dependencies: StdIn.java StdOut.java	MaxPQ.java (Robert Sedgewick and Kevin Wayne, Algorithms 4th edition)
	 *
	 */
	
	String line;
	int n;
	Interval[] initIntervals;
	HashMap<Integer,Integer> order;

	public IP() {

		this.parsefile();
		this.algorithm();

	}

	public void parsefile(){
		
		//Get the n from the first line
		n = Integer.valueOf(StdIn.readLine());

		//The number of intervals is n
		initIntervals = new Interval[n];
		
		//Init order HashMap
		order = new HashMap<Integer,Integer>();
		
		//Skip the empty line
		String emptyline = StdIn.readLine();
	
		for (int i=0; i<n; i++){
			line = StdIn.readLine();
			String[] parts = line.split(" ");
			
			//init a new Interval
			Interval current = new Interval();
			
			//Get the start and the end
			current.start = Integer.valueOf(parts[0]);
			current.end = Integer.valueOf(parts[1]);
			current.id = i;
			order.put(i, -1);
			initIntervals[i] = current;
		}
	}
	
	public void algorithm(){
		//Create a comparator, which will compare the keys
		Comparator<Partition> comparator = new Comparator<Partition>() {

			@Override
			public int compare(Partition p1, Partition p2) {
				return p1.Key - p2.Key;
			}
			
		};
		
		//Create a Priority Queue to store the future partitions
		MinPQ<Partition> pq = new MinPQ<Partition>(n,comparator);	
		
		//Duplicate the intervals
		Interval[] working = initIntervals.clone();

		//Sort intervals by starting time
		mergeSort(working);
		
		//Init the first Partition and d
		int d = 0;
		
		Partition first = new Partition();
		first.id=d;
		first.Key = 0;
		
		//Add the first Partition to the pq, so that it's not empty
		pq.insert(first);
		
		for (int j = 0; j < n; j++) {
			if(working[j].start>=pq.min().Key){
				//add to this partition
				working[j].partition = pq.delMin().id;
				
				//update the HashMap
				order.put(working[j].id, working[j].partition);
				
				//Update the Key prep
				Partition temp = new Partition();
				temp.id = working[j].partition;
				temp.Key = working[j].end;

				//update the Key to the new one
				pq.insert(temp);
			}else{
				//Create new partition
				Partition newPart = new Partition();
				newPart.id = ++d;
				newPart.Key = working[j].end;

				//Update the partition id
				working[j].partition = newPart.id;
				
				//Update the HashMap
				order.put(working[j].id, working[j].partition);
				
				pq.insert(newPart);
			}
		}
		
		//Print the result
		System.out.println(pq.size());
		System.out.println();
		System.out.println();
		for (Interval interval : initIntervals) {
			System.out.println(interval.start+" "+interval.end+" "+order.get(interval.id));
		}
	}

	public static void main(String[] args) {
		IP ip = new IP();
	}
	
	//Merge sort from cmu.edu
	public static void mergeSort(Comparable [ ] a)
	{
		Comparable[] tmp = new Comparable[a.length];
		mergeSort(a, tmp,  0,  a.length - 1);
	}


	private static void mergeSort(Comparable [ ] a, Comparable [ ] tmp, int left, int right)
	{
		if( left < right )
		{
			int center = (left + right) / 2;
			mergeSort(a, tmp, left, center);
			mergeSort(a, tmp, center + 1, right);
			merge(a, tmp, left, center + 1, right);
		}
	}


    private static void merge(Comparable[ ] a, Comparable[ ] tmp, int left, int right, int rightEnd )
    {
        int leftEnd = right - 1;
        int k = left;
        int num = rightEnd - left + 1;

        while(left <= leftEnd && right <= rightEnd)
            if(a[left].compareTo(a[right]) <= 0)
                tmp[k++] = a[left++];
            else
                tmp[k++] = a[right++];

        while(left <= leftEnd)    // Copy rest of first half
            tmp[k++] = a[left++];

        while(right <= rightEnd)  // Copy rest of right half
            tmp[k++] = a[right++];

        // Copy tmp back
        for(int i = 0; i < num; i++, rightEnd--)
            a[rightEnd] = tmp[rightEnd];
    }

}

class Interval implements Comparable{
	public int id;
	public int start;
	public int end;
	public int partition;
	
	public Interval(){
		this.id = -1;
		this.start = -1;
		this.end = -1;
		this.partition = -1;
	}

	//Used in merge sort
	@Override
	public int compareTo(Object anotherInterval) throws ClassCastException {
		 if (!(anotherInterval instanceof Interval))
		      throw new ClassCastException("An Interval object expected.");
		 int anotherIntervalStart = ((Interval) anotherInterval).start;
		return this.start - anotherIntervalStart;
	}
}

class Partition{
	public int id;
	public int Key;
	
	public Partition(){
		this.id = -1;
		this.Key = -1;
	}
}

