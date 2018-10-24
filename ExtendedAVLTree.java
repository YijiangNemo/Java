package net.datastructures;
import java.util.Comparator;
import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;




/**
 *Written by Yijiang wu
 */
public class ExtendedAVLTree<K, V> extends AVLTree<K, V>{


	public static void main(String[] args)
    { 
      String values1[]={"Sydney", "Beijing","Shanghai", "New York", "Tokyo", "Berlin",
     "Athens", "Paris", "London", "Cairo"}; 
      int keys1[]={20, 8, 5, 30, 22, 40, 12, 10, 3, 5};
      String values2[]={"Fox", "Lion", "Dog", "Sheep", "Rabbit", "Fish"}; 
      int keys2[]={40, 7, 5, 32, 20, 30};
         
      /* Create the first AVL tree with an external node as the root and the
     default comparator */ 
         
        AVLTree<Integer, String> tree1=new AVLTree<Integer, String>();

      // Insert 10 nodes into the first tree
         
        for ( int i=0; i<10; i++)
            tree1.insert(keys1[i], values1[i]);
       
      /* Create the second AVL tree with an external node as the root and the
     default comparator */
         
        AVLTree<Integer, String> tree2=new AVLTree<Integer, String>();
       
      // Insert 6 nodes into the tree
         
        for ( int i=0; i<6; i++)
            tree2.insert(keys2[i], values2[i]);
         
        ExtendedAVLTree.print(tree1);
        ExtendedAVLTree.print(tree2); 
        ExtendedAVLTree.print(ExtendedAVLTree.clone(tree1));
        ExtendedAVLTree.print(ExtendedAVLTree.clone(tree2));
        
        ExtendedAVLTree.print(ExtendedAVLTree.merge(ExtendedAVLTree.clone(tree1), 
        ExtendedAVLTree.clone(tree2)));
      }
    
	static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	static int screenW = screenSize.width-100;
	static int screenH = screenSize.height-100;
	public static <K, V> AVLTree<K, V> clone(AVLTree<K,V> tree){
		Comparator<K> compFunc = tree.C;
		AVLTree<K,V> clone = new AVLTree<K,V>(compFunc);
//create a new root node and then set the root of the clone tree
		AVLNode<K,V> cRoot = new AVLNode<K,V>(tree.root.element(),null,null,null);
		clone.root = cRoot;
//using the roots traversal the rest of the tree 
		copyTree(cRoot, tree.root, clone);
		clone.size = tree.size;
		clone.numEntries = tree.numEntries;
		
		return clone;
	}
	private static<K,V> void copyTree(BTPosition<Entry<K,V>> copy, BTPosition<Entry<K,V>> orig, AVLTree<K,V> tree) {

				if(orig.getLeft()!= null) {
					AVLNode<K,V> left = new AVLNode<K,V>(null,null,null,null);
					left.setElement(orig.getLeft().element());
					left.setParent(copy);
					copy.setLeft(left);
					copyTree(left, orig.getLeft(),tree);	
				} 
				
				if(orig.getRight() !=null) {
					AVLNode<K,V> right = new AVLNode<K,V>(null,null,null,null);
					right.setElement(orig.getRight().element());
					right.setParent(copy);
					copy.setRight(right);
					copyTree(right, orig.getRight(),tree);
				}
		
				if(copy.element()!=null) {
					tree.setHeight(copy);
				}
				
			}	
/*
 * Analysis of copyTree:
 * 
 * This function precisely goes through each element of the tree only once,
 * it will have different complexity for internal and external nodes
 * 
 * For internal node has 79 primitive operations 
 * For each external node has only 2 operations:
 * 
 * So the number of primitive operations for N nodes in a tree is:
 * N = 79I + 2E
 * 
 * The number of external nodes = I+1
 * So N = 79I + 2(I+1)
 *      = 81I + 2
 *      
 * so this function has a complexity of 81N + 2,big O is O(n)
 * 
 */

    public static <K, V> AVLTree<K, V> merge(AVLTree<K, V> tree1,
 			AVLTree<K, V> tree2) {
 		Comparator<K> comp = tree1.C;
 		AVLTree<K,V> merge = new AVLTree<K,V>(comp);
 		CustomArray<Entry<K,V>> tree1Nodes = new CustomArray<Entry<K,V>>(tree1.numEntries);
 		CustomArray<Entry<K,V>> tree2Nodes = new CustomArray<Entry<K,V>>(tree2.numEntries);
 		CustomArray<Entry<K,V>> mergeNodes = new CustomArray<Entry<K,V>>(tree1.numEntries+tree2.numEntries);
 		OrderList(tree1Nodes, tree1.root, 0);
 		OrderList(tree2Nodes, tree2.root, 0);
 //create a new array which is the size of both both arrays.
 		mergeInList(tree1Nodes, tree2Nodes, mergeNodes, comp);
 //use the ordered array to create an actual tree. 
 		merge.root = transferToTree(mergeNodes, 0, (mergeNodes.size()-1));
 		SetHeight(merge, merge.root);
 		merge.numEntries = tree1.numEntries+tree2.numEntries;
 		merge.size = (merge.numEntries*2)+1;
 		return merge;
 	}
 	
 /*
  * Analysis of OrderList List
  * 
  * This function is approximately f(N) = 12N
  * 
  * 6N - for determining if the element is null and calling the function 
  * 5N - for putting the element in the array
  * 1N - to return the value of i
  */
 	private static<K,V> int OrderList(CustomArray<Entry<K,V>> list, BTPosition<Entry<K,V>> current, int i) {
 		if(current.getLeft().element()!=null) {
 			i = OrderList(list, current.getLeft(), i);
 		}
 		list.setEle(i, current.element());
 		i = i+1;
 		if(current.getRight().element()!= null) {
 			i = OrderList(list, current.getRight(), i);
 		}
 		return i;
 	}
 	
 /*
  * Analysis of mergeInList:
  * This function takes in two list of size n and m
  * f(n+m) = 26(n+m) 
  * 3(n+m)  - assignment indexes
  * 5(n+m)  - while condition 
  * 11(n+m) - using the comparison function and setting 
  * 7(n+m)  - assigning the remaining values to array
  */
 	private static<K,V> void mergeInList(CustomArray<Entry<K,V>> list1, CustomArray<Entry<K,V>> list2, CustomArray<Entry<K,V>> list3, Comparator <K> C) {
 		int i1 = 0;
 		int i2 = 0;
 		int iR = 0;
 		int comp;	
 		while((i1 < list1.size()) && (i2< list2.size())) {
 			comp = C.compare(list1.getEle(i1).getKey(), list2.getEle(i2).getKey());
 			if(comp <= 0) {
 				list3.setEle(iR, list1.getEle(i1));
 				i1++;
 			} else {
 				list3.setEle(iR, list2.getEle(i2));
 				i2++;
 			}
 			iR++;
 		}
 		//we might be left with elements from the larger array. So just insert them at the end of the result list.
 		while(i1 < list1.size()) {
 			list3.setEle(iR, list1.getEle(i1));
 			i1++;
 			iR++;
 		}
 		while(i2 < list2.size()) {
 			list3.setEle(iR, list2.getEle(i2));
 			i2++;
 			iR++;
 		}
 	}
 /*
  * Analysis of transferToTree:
  * This function is approximately F(N) = 49N 
  *  4N - for calculation of the current node
  *  13N - for the creation of the AVLNode
  *  22N - for the creation of the left and right children
  *  4N - for setting the left and right child's parent
  *  4N - for setting the parents left and right children
  *  2N - for calling the function on the value and returning the node
  */
 	private static<K,V> AVLNode<K,V> transferToTree(CustomArray<Entry<K,V>> list, int start, int end) {
 		int current = (start+((end-start)/2));
 		AVLNode<K,V> node = new AVLNode<K,V>(list.getEle(current),null,null,null);
 		AVLNode<K,V> left = new AVLNode<K,V>(null,null,null,null);
 		AVLNode<K,V> right = new AVLNode<K,V>(null,null,null,null);
 		//when the start equals the end it means you've reached an internal node that only has
 		//external nodes as children
 		if(start == end) {
 			left.setParent(node);
 			right.setParent(node);
 			
 			node.setLeft(left);
 			node.setRight(right);
 			return node;
 		}
 		if(current != start) {
 			left = transferToTree(list, start, current-1);
 			node.setLeft(left);
 			left.setParent(node);
 		} else {
 			left.setParent(node);
 			node.setLeft(left);
 		}
 		right = transferToTree(list, current+1, end);
 		right.setParent(node);
 		node.setRight(right);
 		return node;
 	}

 /*
  * Analysis of SetfHeight:
  * This function is approximately F(N) = 33N. 
  * 
  * Setting the height of a node takes 33 operations
  */
 	public static<K,V> void SetHeight(AVLTree<K,V> tree, BTPosition<Entry<K,V>> node) {
 		if(node.getLeft().element()!=null) {
 			SetHeight(tree, node.getLeft());
 		}
 		if(node.getRight().element()!=null) {
 			SetHeight(tree, node.getRight());
 		}
 		tree.setHeight(node);
 	}

 	public static<K,V> void print(AVLTree<K,V> tree) {
 		TWindow window = new TWindow();
 		if(tree!=null) {
 			if(tree.numEntries>0) {
 				Canvas<K,V> trees = new Canvas<K,V>(tree);
 				JScrollPane scroll = new JScrollPane(trees,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, 
 											 ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
 				window.add(scroll);
 			}
 		}
 			window.setVisible(true);
 		
 	}
	//class for the top level container of the swing graphics
	//used to contain the drawing of the AVLTree
 	private static class TWindow extends JFrame {

 		private static final long serialVersionUID = 1L;

 		public TWindow() {
 			initUI();
 		}
 		
 		private void initUI() {
 			setTitle("Assignment 2 - Print AVLTrees");
 			setSize(screenW,screenH);
 			setLocationRelativeTo(null);
 			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 			setResizable(false);
 			
 		}
 	}
 	
 	public static class Canvas<K,V> extends JPanel {
 		
 		private static final long serialVersionUID = -8436502373026255497L;
 		
 		private AVLTree<K,V> aTree;
 		private int width;
 		private int height;
 		private int nodes;
 		private int treeHeight;
 		private int ySpace;
 		private int xoff = 40;
 		public Font font = new Font("Times New Roman", Font.BOLD, 20);

 		public Canvas(AVLTree<K,V> tree) {
 			aTree = tree;
 			nodes = tree.size+1;
 			treeHeight = tree.height(tree.root())+1;
 			width = 80;
 			height = 50;
 			ySpace = (height*2);
 		
 		}
 		//The size depend on the size of the tree 		
 	    public Dimension getPreferredSize() {

 	        return new Dimension((nodes*width),ySpace*(treeHeight+1));
 	    }

 		public void paintComponent(Graphics g) {
 			super.paintComponent(g);
 			g.setFont(font);
 			doDrawing(g);
 		}
 		
 		public void doDrawing(Graphics g) {
 			
 			BTPosition<Entry<K,V>> node = aTree.root;
 			GraphicNode pos = new GraphicNode();
 			drawAVLTree(node, pos, 0, g);
 		}
 	
 		private GraphicNode drawAVLTree(BTPosition<Entry<K,V>> node, GraphicNode pos, int height, Graphics g) {
 			GraphicNode left = null;
 			GraphicNode current = null;
 			GraphicNode right = null;
 			if(node.getLeft()!=null) {
 				left = drawAVLTree(node.getLeft(), pos, height+1, g);
 			}
 			
 			if(node.element() == null) {
 				current = new Rectangle(height,pos.accumulatePosition,g);
 			} else {
 				current = new Circle(height,pos.accumulatePosition, node.element().getKey().toString(), g);
 			}
 			pos.accumulatePosition+=1;

 			if(node.getRight()!=null) {
 				right = drawAVLTree(node.getRight(), pos, height+1, g);
 				Line rightLine = new Line((current.x+current.w),(current.y+(current.h/2)),right.x+(right.w/2),right.y,g);
 			}
 			if(node.getLeft()!=null){
 				Line leftLine = new Line(current.x,(current.y+(current.h/2)),left.x+(left.w/2),left.y,g);
 			}
 			return current;
 			
 		}

 		private class GraphicNode {
 			public int x;
 			public int y;
 			public int w;
 			public int h;
 			public int position;
 			public int accumulatePosition;
 			public String key;
 			
 			public GraphicNode() {
 				this.x = 0;
 				this.y = 0;
 				this.w = 0;
 				this.h = 0;
 				this.position = 0;
 				this.accumulatePosition = -1;
 				this.key = null;
 			}
 			
 		}
 		private class Circle extends GraphicNode {
 		
 		    public Circle(int height, int position, String key, Graphics g) {
 		    	this.position = position+1;
 		    	this.x = (Canvas.this.width*this.position)+Canvas.this.xoff;
 		    	this.y = (Canvas.this.ySpace*height)+Canvas.this.xoff;
 		    	this.w = 80;
 		    	this.h = w;
 		    	this.key = key;
 		    	this.paintCircle(g);
 		    }
 		
 		    public void paintCircle(Graphics g){
 		        g.setColor(Color.YELLOW);
 		        g.drawOval(x,y,w,h);
 		        g.setColor(Color.YELLOW);
 		        g.fillOval(x,y,w,h);
 		        g.setColor(Color.BLACK);
 		        g.drawString(this.key, x+(w/4), y+(w/2));
 		    }
 		}

 		private class Rectangle extends GraphicNode {

 			public Rectangle(int height, int position, Graphics g) {
 		    	this.position = position+1;
 		    	this.x = (Canvas.this.width*this.position)+Canvas.this.xoff;
 		    	this.y = (Canvas.this.ySpace*height)+Canvas.this.xoff;
 		    	this.w = 80;
 		    	this.h = 50;
 		    	this.paintRectangle(g);
 			}
 			
 			public void paintRectangle(Graphics g) {
 		        g.setColor(Color.RED);
 		        g.drawRect(x,y,width,height);
 		        g.setColor(Color.RED);
 		        g.fillRect(x,y,width,height);
 			}
 		}
 		private class Line extends GraphicNode {
 			public int x1;
 			public int x2;
 			public int y1;
 			public int y2;
 			public Line(int p1, int h1, int p2, int h2, Graphics g) {
 				this.x1 = p1;
 				this.y1 = h1;
 				this.x2 = p2;
 				this.y2 = h2;
 				this.paintLine(g);

 			}
 			public void paintLine(Graphics g) {
 				g.setColor(Color.BLACK);
 				g.drawLine(x1, y1, x2, y2);
 			}
 		}
 	}

 	public static class CustomArray<E> {
 		public E[] anArray;
 		public int size;
 		
 		@SuppressWarnings("unchecked")
		public CustomArray(int size) {
 			anArray = (E[]) new Object[size];
 		}
 		
 		public int size() {
 			return this.anArray.length;
 		}
 		public void setEle(int i, E current) {
 			this.anArray[i] = current;
 		}
 		public E getEle(int i) {
 			return this.anArray[i];
 		}
 		
 	}

 }
