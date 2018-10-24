package net.datastructures;

import java.util.regex.*;


import java.io.*;
public class TaskScheduler {
	  public static void main(String[] args) throws Exception{

		    TaskScheduler.scheduler("samplefile1.txt", "feasibleschedule1", 4);
		   /** There is a feasible schedule on 4 cores */      
		    TaskScheduler.scheduler("samplefile1.txt", "feasibleschedule2", 3);
		   /** There is no feasible schedule on 3 cores */
		    TaskScheduler.scheduler("samplefile2.txt", "feasibleschedule3", 5);
		   /** There is a feasible scheduler on 5 cores */ 
		    TaskScheduler.scheduler("samplefile2.txt", "feasibleschedule4", 4);
		   /** There is no feasible schedule on 4 cores */

		   /** There is a feasible scheduler on 2 cores */ 
		    TaskScheduler.scheduler("samplefile3.txt", "feasibleschedule5", 2);
		    /** There is a feasible scheduler on 2 cores */ 
		    TaskScheduler.scheduler("samplefile4.txt", "feasibleschedule6", 2);

		  }


	
/*
 * Analysis of Taskscheduler:
 * 
 * Complexity of functions used in this scheduler
 * (1) getAllTasks         = 69n+11 = n
 * (2) createValidSchedule = 3nlogn
 *         releaseHeap = nlogn
 *         deadlineHeap = nlogn
 *         coreHeap = nlogn
 * (3) outputTofile        = 8n+6 
 *  
 * f(n) = (1)+(2)+(3)
 *  Big O  = O(nlogn)
 * 
 * 2 - creating  new linked list
 * 3 - creating  arrays and assigning value to num tasks
 * 1+(1) - calling the function get allTasks
 * 3 - numTasks value comes from the Linked list size and creating a new array
 * 1 - creating the schedule array
 * 2+(4) - calling the createValidSchedule function and using its value as a comparison
 * (5) - calling the output to file function
 * 
 * 
 */
	//This function takes the tasks located in file 1, and creates a schedule
	//where the number of tasks that are able to be handled at any one moment
	//are dependent on the integer m. This schedule is outputed to file2.
	public static void scheduler(String file1, String file2, int m) {
		LinkedList Tasks = new LinkedList();

		Task[] schedule = null;
		int numTasks = 0;
		//take all the arguments 
		if(!handleArgs(file1, file2, m)){
			return;
		}
		//get all the tasks in the file 
		getAllTasks(file1, Tasks);
		
		numTasks = Tasks.size;		
		schedule = new Task[numTasks];

		if(!createValidSchedule(schedule, Tasks, m, numTasks)) {
			System.out.println("No feasible schedule exists");
			return;
		} else {
			//if their is a valid file then output each task to the file
			outputToFile(schedule, file2);
		}
	}
/*
 * Analysis of handleArgs:
 * f(n) = 7
 * 
 */
	private static boolean handleArgs(String file1, String file2, int m) {
		//if file1 is null or the file provided in the string doesn't exist then exit the program
		if(file1==null){
			System.out.println("file1 does not exist");
			System.exit(0);
		}
		File input = new File(file1);
		if(!input.exists()) {
			System.out.println("file1  does  not exist");
			System.exit(0);
		}
		//file2 which is the output file must be a string. If its null then we cannot print to the output
		if(file2==null) {
			System.out.println("Not enough output file information");
			return false;
		}
		//if the amount of cores provided are less than or equal to 0 then its not possible to create a schedule
		if(m<=0) {
			System.out.println("No feasible schedule exists");
			return false;
		}
		
		return true;
	}
	
/*
 * Analysis of getAllTasks:
 * f(n) = 69n + 11
 * 
 * Note: this analysis assumes that for the worst case scenario each tasks attributes are on their own seperate line.
 *       each task can be reduced to 3 attributes which makes some of operations triple as a result but its still linear time.
 * 
 * (1)      
 * Start of the function has complexity of: 11 
 * 6 - assigning values to 6 variables
 * 2 - assigning a value to pattern and compiling the pattern
 * 3 - assigning a value to input file and calling two constructors
 * 
 * (2)
 * outer while loop(inputfile.readline): 13
 * 3 - the while condition which assigns a value from readLine
 * 2 - assigning a value from p matcher
 * 1 - for the while loop condition(which in this case only runs once because theres one element per line(see assumptions)
 * 3 - assigning a value to an array element from a function call to matcher
 * 2 - incrementing i
 * 1 - comparison of i>0
 * 
 * (3)
 * if condition(i == maxattributes): 30
 * 22 - for calling the valid attributes function and assigning the value to newNode
 * 1  - if comparison to null
 * 6  - adding the node to the linked list
 * 1  - making i = 0
 * 
 * 
 * Taking into account that each element of a task(3) is on its own line and there are n elements. There are 3n elements to process
 * Thus (2) has a complexity of 3n*13 = 39n (4)
 * 
 * The if condition will only run once per 3 elements. Thus it runs n times
 * Thus (3) has a complexity of 30n (5)
 * 
 * Total complexity of the function is (1)+(4)+(5) = 11+39n+30n = 69n+11
 */
	
	private static void getAllTasks(String f, LinkedList t) {
		//3 atrributeds per task
		String[] attributes = new String[4];
		int i = 0;
		int maxAttributes = 4;
		String input;
		BufferedReader inputFile = null;
		Tnode newNode = null;
		//each variable in a task is made of of alphanumeric characters
		//there is the possibility of negative numbers so thats included for later
		//validation purposes
		String taskPatt = "\\s*([a-zA-Z0-9-]+)\\s*";
		Pattern p = Pattern.compile(taskPatt);
		
		try {
			inputFile = new BufferedReader(new FileReader(f));
		} catch (FileNotFoundException e) {
			System.out.print("file1 does not exist");
			System.exit(0);
		}
		
		try {
			//read each line of the file
			while((input=inputFile.readLine())!=null) {
				//match each element of a task
				Matcher m = p.matcher(input);
				while(m.find()) {
					attributes[i] = m.group(1);
					i++;
					//if 3 valid attributes have been found combine them into 1 task
					if(i==maxAttributes) {
						//check to see if the attributes are valid
						newNode = validAttributes(attributes);
						if(newNode!=null) {
							//add them into the linkedlist
							t.add(newNode);
							i = 0;
						} else {
							System.out.format("input error when reading the attributes of the task %s", attributes[0]);
							System.exit(0);
						}
					}
				}
				//if there are any attributes leftover then that means the file has not been written correctly
				if(i>0) {
					System.out.format("input  error when  reading  the  attributes  of  the  task %s", attributes[0]);
					System.exit(0);
				}
			}
		} catch (IOException e1) {
			System.out.println("Cannot read the file");
			System.exit(0);
		}
		
		
		try {
			//close the file to make sure we have no memory leaks
			inputFile.close();
		} catch (IOException e) {
			System.out.println("Cannot close file");
			System.exit(0);
		}
		return;
	}
/*
 * Analysis of validAttributes:
 * f(n) = 21
 * 
 */
	public static Tnode validAttributes(String[] a) {
		Tnode newNode = null;
		int execution;
		int release;
		int deadline;
		//a valid name only has numbers and characters
		if(!Pattern.matches("[A-Za-z]+[A-Za-z0-9]*", a[0])) {
			return newNode;
		}
		//a valid release time only has numbers and is >=0
		if(!Pattern.matches("\\d+", a[1])) {
			return newNode;
		} else {
			execution = Integer.parseInt(a[1]);
		}
		if(!Pattern.matches("\\d+", a[2])) {
			return newNode;
		} else {
			release = Integer.parseInt(a[2]);
		}
		//a valid deadline only has numbers and is more than 0
		if(!Pattern.matches("\\d+", a[3])) {
			return newNode;
		} else {
			deadline = Integer.parseInt(a[3]);
			if(deadline<1) {
				return newNode;
			}
				
		}
		//release must less than a deadline
		if(release>=deadline) {
			return newNode;
		}
		
		newNode = new Tnode(a[0],execution, release, deadline);
		
		return newNode;
	}

/*
 * Analysis of createValidSchedule:
 * big O : nlog(n)
 * 
 * createValidSchedule = 3nlog(n)
 *  while loop for releaseHeap = nlog(n)
 *  while loop for deadlineHeap = nlog(n)
 *  while loop for coreHeap = nlog(n)
 */

	
	private static boolean createValidSchedule(Task[] s, LinkedList tasks, int cores, int maxTasks) {
		//create 3 new heap that is ordered by deadline times
		HeapPriorityQueue<Integer, Tnode> releaseHeap = new HeapPriorityQueue<Integer, Tnode>();
		HeapPriorityQueue<Integer, Tnode> readyHeap = new HeapPriorityQueue<Integer, Tnode>();
		HeapPriorityQueue<Integer, Tnode> coreHeap = new HeapPriorityQueue<Integer, Tnode>();
		int numTasks = 0;
		int c= 0;
        // at coreheap,corex.value.deadline is task release time,corex.value.execution is core name
			for(c = 0;c<cores;c++){
				Tnode corex = new Tnode();
				corex.value = new Task();
				corex.value.release = 0;
				corex.value.deadline = 0;
				corex.value.execution = c+1;
				
				coreHeap.insert(corex.value.deadline,corex);
			}
	
			
			Tnode node = tasks.head;
			while(node!=null) {
				
				releaseHeap.insert(node.value.release,node);
				node = node.next;
			}			
			//while the current tasks release time is equal to current start time put it into the deadline heap

			
			while((releaseHeap.isEmpty()==false)||(readyHeap.isEmpty()==false)){
	
			Tnode coreTask = coreHeap.min().getValue();
			
			
			//Tnode Task1 = releaseHeap.removeMin();	
		//	Task1.key = Task1.value.deadline;
			//readyHeap.insert(Task1);
			while(releaseHeap.isEmpty()==false){
				
				Tnode Task2 = releaseHeap.min().getValue();
				

				//if(Task2.value.release==0){return false;}
				
				if(coreTask.value.deadline >= Task2.value.release){
					
					readyHeap.insert(Task2.value.deadline,Task2);
					releaseHeap.removeMin();
				}
				else{
					
					
					break;}
				
			}
			if(readyHeap.isEmpty() != false){
				
				Tnode Task1 = releaseHeap.min().getValue();
				releaseHeap.removeMin();
				
				readyHeap.insert(Task1.value.deadline,Task1);
				while(releaseHeap.isEmpty()==false){
					Tnode Task3 = releaseHeap.min().getValue();
					if(Task3.value.release==Task1.value.release){

						readyHeap.insert(Task3.value.deadline,Task3);
						releaseHeap.removeMin();
					}
					else{
						
						break;
					}
				}
				}
			//the number of cores dictates how many tasks we can pull out of the deadline heap at particular time
			
				//System.out.println("Rd"+readyHeap.entries);
				Tnode rTask = readyHeap.min().getValue();
				readyHeap.removeMin();
				//System.out.println(rTask.value.name);
				Tnode cTask = coreHeap.min().getValue();
				coreHeap.removeMin();
				
				
				//If the current time is more than the tasks deadline, then there is no possible
				//schedule
				Task sTask = new Task();
				sTask.name = rTask.value.name;
                   if(rTask.value.release >= cTask.value.deadline){
					
					sTask.startTime = rTask.value.release;
					
					}
					else{
						
						sTask.startTime = cTask.value.deadline;
						
						}
				
				if(rTask.value.deadline < (sTask.startTime + rTask.value.execution)) {	
					//System.out.println(rTask.value.execution);
					//System.out.println(sTask.name);
								return false;
							}
				else{
					
					
					cTask.value.deadline = sTask.startTime +rTask.value.execution;
					
					sTask.execution = cTask.value.execution;
					coreHeap.insert(cTask.value.deadline,cTask);					
					s[numTasks] = sTask;
					//System.out.println(s[numTasks]);
					numTasks ++;
					}
						
				
				//increment the number of cores used			
		}
			
		return true;
	}
/*
 * Analysis of outputToFile: f(n)=8n+6
 * 
 * 2 - creating the File and FileWriter objects
 * 2 - calling the file exists function and using its value for comparison
 * 1 - creating a new fileWriter object
 * 8n - For loop for printing the string to a file
 *    - 1n - calling the write function
 *    - 4n - calling variables from an element in an array twice
 *    - 2n - creating strings with spaces in them
 *    - 1n - combining the string together
 * 1 - calling the function for the file to close
 * 
 * 
 */
	//take the tasks in s and output them to a file with the name
	//provided by the outputFile string
	private static void outputToFile(Task[] s, String outputFile) {
		FileWriter out = null;
		File file = new File(outputFile + ".txt");
		
		//if the file exists make sure not append to the file but to rewrite it
		//else create a new file with the name provided
		try {
			if(file.exists()){
				out = new FileWriter(outputFile + ".txt", false);
			} else {
				out = new FileWriter(outputFile + ".txt");
			}
		} catch (IOException e) {
			System.out.println("Cannot create or find output file");
			System.exit(0);
		}
		
		try {
			//write the name of each task and the time it starts to the file
			for(int i=0; i<s.length; i++) {
				
				out.write(s[i].name+" "+ "core" +s[i].execution + " " + s[i].startTime+" ");
			}
		} catch (IOException e) {
			System.out.println("Cannot write to file");
			System.exit(0);
		}
		
		try {
			//close the file to prevent memory leaks
			out.close();
		} catch (IOException e) {
			System.out.println("Cannot close the file stream");
			System.exit(0);
		}
	}
}

class Task {
//each task has a name, release time, deadline time and if its schedulable then a starttime
	public String name;
	public int execution;
	public int release;
	public int deadline;
	public int startTime;
/*
 * Each of these constructors is f(n) = 3
 */
	public Task() {
		this.name = null;
		this.execution = -1;
		this.release = -1;
		this.deadline = -1;
	}
	
	public Task(String name,int execution, int release, int deadline) {
		this.name = name;
		this.execution = execution;
		this.release = release;
		this.deadline = deadline;
	}
	
}
//Nodes that are used for both linked list and heap data structures
class Tnode {
	
	public Task value;
	public Tnode next;
	
	public Tnode() {
		
		this.value = null;
		this.next = null;
	}
	public Tnode(Task element) {
		
		this.value = element;
		this.next = null;
	}
/*
 * Constructor has f(n) = 6
 * 2 - assigning values to key and next
 * 4 - creating a task object and assigning it to the value
 */
	public Tnode(String name,int execution, int release, int deadline) {
		
		this.value = new Task(name, execution, release, deadline);
		this.next = null;
	}

}

//the linked list is used because at the beginning of the program we don't know how many tasks
//there are. So we create a linked list, find out the resulting size and use that size to inform
//the heap how big it should make its internal array
class LinkedList {
	public Tnode head;
	public int size;
	
/*
 * Each of these constructors have f(n) = 2
 * 
 */
	public LinkedList() {
		this.head = null;
		this.size = 0;
	}
	public LinkedList(Tnode head) {
		this.head = head;
		this.size = 1;
	}
/*
 * Analysis of add:
 * f(n) = 5
 * 1 - comparison of head
 * 1 - assigning head to a temporary node
 * 2 - accessing the new node next and assigning the temporary node to it
 * 1 - assigning a new head
 */
	public void add(Tnode newNode) {
		if(head == null) {
			head = newNode;
			size++;
		} else {
			Tnode prevHead = this.head;
			newNode.next = prevHead;
			this.head = newNode;
			size++;
		}
	}
}


