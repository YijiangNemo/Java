// written by Yijiang wu

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class MyDlist extends DList {
//Q1
    public MyDlist() {
        super();
    }

//Q2
    public MyDlist(String f) {
		if(f.equals("stdin")) {
			readStdin();
		} else {
			readFile(f);
		}
	}

	private void readStdin() {
		String input = null;
		Scanner rs = new Scanner(System.in);
//input 
		while((input=rs.nextLine()).isEmpty()==false) {
//create a new node with the users input
			DNode newNode = new DNode(input, null, null);
			this.addLast(newNode);
		}
		rs.close();
	}
	
	private void readFile(String file) {
		String[] elements;
		String input = null;
		BufferedReader inputFile = null;
	
		try {
			//open the text file
			inputFile = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			System.out.println("Can not find");
			return;
		}
		try {
			//read each line until there are no more lines
			while((input = inputFile.readLine())!=null){
		//split up the lines into words and store the words in an array
				
				elements = input.split(" ");
		//create a new Node for each word
				for(int x=0; x<elements.length; x++){
					if(elements[x].isEmpty()==false){
					DNode newNode = new DNode(elements[x],null,null);
					this.addLast(newNode);
					}
				}
			}
		}
		catch (IOException e) {
			System.out.println("Can not read");
			return;
		}

		}


//Q3
  /*gets the header of the list and traverse each Node 
   * in the list and prints the element  */
    public static void printList(MyDlist u) {
		DNode current = u.header.next;
		
		while (current != u.trailer) { 
			System.out.println(current.element);
			current = current.next; 
		}
	}


//Q4
    public static MyDlist cloneList(MyDlist u) {
        DNode currentOld = u.getFirst();
        MyDlist ret = new MyDlist();
        DNode currentNew = null;
//while node isn't  the trailer create a new DNode with the same element
//as the current node. pass this DNode to the tail of the clone list.
        while (u.hasNext(currentOld)) {
            currentNew = new DNode(currentOld.getElement(), null, null);
            ret.addLast(currentNew);
            currentOld = currentOld.getNext();
        }
        ret.trailer = currentNew.getNext();
        ret.size = u.size;
        return ret;
    }
//Q5
	public static MyDlist union(MyDlist u, MyDlist v) {
		//create empty MyDlist 
		MyDlist aUnion = new MyDlist();
		//create nodes
		DNode uNode = u.header.next;
		DNode vNode = v.header.next;
		DNode newNode = null;
		//Analysis: 6
		//4 for creating new elements and 2 for following references
		while(uNode!=u.trailer) {
			newNode = new DNode(uNode.getElement(),null, null);
			aUnion.addLast(newNode);
			uNode = uNode.next;
		}
		//Analysis: 5N
		
		while(vNode!=v.trailer) {
			newNode = new DNode(vNode.getElement(),null, null);
			aUnion.addLast(newNode);
			vNode = vNode.next;
		}
		//Analysis: 5N
		
		//Analysis: N^2 
		deleteDuplicates(aUnion);
		
		return aUnion;
		//Analysis: 1.
		//Big O: O(n^2).
	}
	

	private static void deleteDuplicates(MyDlist u) {
		if(u.size() != 0 ) {
			//set the uNode to first node of the list u
			DNode uNode = u.header.next;
			DNode uNodeNext = null;
			DNode previous = null;
			DNode next = null;
			//a node created to make sure nodes are handled by the duplicate.
			DNode duplicate = null;
			//Analysis: 7
		
			//go through the list element by element
			while(uNode != u.trailer) {
				uNodeNext = uNode.next;
			//Analysis: 2N. a reference and assignment
				//compare the all elements after the current 
				while(uNodeNext != u.trailer) {
					if(uNode.getElement().equals(uNodeNext.getElement())) {
						previous = uNodeNext.prev;
						next = uNodeNext.next;
						previous.setNext(next);
						next.setPrev(previous);
						u.size--;
						duplicate = uNodeNext;
					}
					//Analysis: N(N+1)/2 
					uNodeNext = uNodeNext.next;
					if(duplicate != null) {
						duplicate.setPrev(null);
						duplicate.setNext(null);
						duplicate = null;
					}
 
				}
				uNode = uNode.next;
				//Analysis: 2N 
			// Big O:O(n^2).	
			}
		}
	}

//Q6

	public static MyDlist intersection(MyDlist u, MyDlist v) {
		MyDlist intersection = null; 
		//Analysis: 1 
		
		//finds the smaller list 
		if(u.size() <= v.size()) {
			intersection = MyIntersection(u,v);
		} else {
			intersection = MyIntersection(v,u);
		}
		return intersection;
		//Analysis: 6 
	}
	


	private static MyDlist MyIntersection(MyDlist x, MyDlist y) {
		MyDlist intersection = new MyDlist();
		DNode sNode = x.header.next;
		DNode bNode = null;
		//Analysis: 4
		while(sNode != x.trailer) {
			//for each node of the smaller list go through the bigger list
			//and compare each element to see if they're the same.
			bNode = y.header.next;
			while(bNode != y.trailer) {
               /*create a new DNode and add it to the tail of the new MyDlist
				 if the elements are the same  */
				if(sNode.getElement().equals(bNode.getElement())) {
					DNode newNode = new DNode(sNode.getElement(), null, null);
					intersection.addLast(newNode);
				}
				bNode = bNode.next;
				//Analysis : 8(N^2)

			}
			sNode = sNode.next;
			//Analysis:4N.
		}
		
		return intersection;
		//Analysis = 1
		/*
		 * Analysis: 8(N^2) + 4N + 4
		 */
	}
	//Big O: O(N^2)
	

public static void main(String[] args) throws Exception{
	 
	   System.out.println("please type some strings, one string each line and an empty line for the end of input:");
	    /** Create the first doubly linked list
	    by reading all the strings from the standard input. */
	    MyDlist firstList = new MyDlist("stdin");
	    
	   /** Print all elememts in firstList */
	    printList(firstList);
	   
	   /** Create the second doubly linked list                         
	    by reading all the strings from the file myfile that contains some strings. */
	  
	   /** Replace the argument by the full path name of the text file */  
	    MyDlist secondList=new MyDlist("C:/Users/Hui Wu/Documents/NetBeansProjects/MyDlist/myfile.txt");

	   /** Print all elememts in secondList */                     
	    printList(secondList);

	   /** Clone firstList */
	    MyDlist thirdList = cloneList(firstList);

	   /** Print all elements in thirdList. */
	    printList(thirdList);

	  /** Clone secondList */
	    MyDlist fourthList = cloneList(secondList);

	   /** Print all elements in fourthList. */
	    printList(fourthList);
	    
	   /** Compute the union of firstList and secondList */
	    MyDlist fifthList = union(firstList, secondList);

	   /** Print all elements in thirdList. */ 
	    printList(fifthList); 

	   /** Compute the intersection of thirdList and fourthList */
	    MyDlist sixthList = intersection(thirdList, fourthList);

	   /** Print all elements in fourthList. */
	    printList(sixthList);

}
}
