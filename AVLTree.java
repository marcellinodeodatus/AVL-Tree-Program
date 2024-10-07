// AvlTree class
//
// CONSTRUCTION: with no initializer
//
// ******************PUBLIC OPERATIONS*********************
// void insert( x )       --> Insert x
// void remove( x )       --> Remove x (unimplemented)
// boolean contains( x )  --> Return true if x is present
// Comparable findMin( )  --> Return smallest item
// Comparable findMax( )  --> Return largest item
// boolean isEmpty( )     --> Return true if empty; else false
// void makeEmpty( )      --> Remove all items
// void printTree( )      --> Print tree in sorted order
// ******************ERRORS********************************
// Throws UnderflowException as appropriate

/**
 * Implements an AVL tree.
 * Note that all "matching" is based on the compareTo method.
 * Only changes: The print method indicates where each value is structurally
 * stored in the tree. Also, instead of throwing an exception from findMin
 * and findMax, null is returned in that case.
 */
public class AvlTree<AnyType extends Comparable<? super AnyType>>
{
    /**
     * Construct the tree.
     */
    public AvlTree( )
    {
        root = null;
    }

    /**
     * Insert into the tree; duplicates are ignored.
     * @param x the item to insert.
     */
    public void insert( AnyType x )
    {
        root = insert( x, root );
    }



    /**
     * Remove from the tree. Nothing is done if x is not found.
     * @param x the item to remove.
     */
    public void remove( AnyType x )
    {
        root = remove( x, root );
    }

    /**
     * Internal method to remove from a subtree.
     * @param x the item to remove.
     * @param t the node that roots the subtree.
     * @return the new root of the subtree.
     */
    private AvlNode<AnyType> remove( AnyType x, AvlNode<AnyType> t )
    {
        if( t == null )
            return t;   // Item not found; do nothing

        int compareResult = x.compareTo( t.element );

        if( compareResult < 0 )
            t.left = remove( x, t.left );
        else if( compareResult > 0 )
            t.right = remove( x, t.right );
        else if( t.left != null && t.right != null ) // Two children
        {
            t.element = findMin( t.right ).element;
            t.right = remove( t.element, t.right );
        }
        else
            t = ( t.left != null ) ? t.left : t.right;

        return balance( t );
    }

    /**
     * Internal method to balance the subtree.
     * @param t the node that roots the subtree.
     * @return the new root of the subtree.
     */
    private AvlNode<AnyType> balance( AvlNode<AnyType> t )
    {
        if( t == null )
            return t;

        if( height( t.left ) - height( t.right ) > 1 )
            if( height( t.left.left ) >= height( t.left.right ) )
                t = rotateWithLeftChild( t );
            else
                t = doubleWithLeftChild( t );
        else if( height( t.right ) - height( t.left ) > 1 )
            if( height( t.right.right ) >= height( t.right.left ) )
                t = rotateWithRightChild( t );
            else
                t = doubleWithRightChild( t );

        t.height = Math.max( height( t.left ), height( t.right ) ) + 1;
        return t;
    }


    /**
     * Find the smallest item in the tree.
     * @return smallest item or null if empty.
     */
    public AnyType findMin( )
    {
		if( isEmpty( ) )
			return null;
        return findMin( root ).element;
    }

    /**
     * Find the largest item in the tree.
     * @return the largest item of null if empty.
     */
    public AnyType findMax( )
    {
		if( isEmpty( ) )
			return null;
        return findMax( root ).element;
    }

    /**
     * Find an item in the tree.
     * @param x the item to search for.
     * @return true if x is found.
     */
    public boolean contains( AnyType x )
    {
        return contains( x, root );
    }

    /**
     * Make the tree logically empty.
     */
    public void makeEmpty( )
    {
        root = null;
    }

    /**
     * Test if the tree is logically empty.
     * @return true if empty, false otherwise.
     */
    public boolean isEmpty( )
    {
        return root == null;
    }

    /**
     * Print the tree contents in sorted order.
     */
    public void printTree( )
    {
        if( isEmpty( ) )
            System.out.println( "Empty tree" );
        else {
        	printTreeBreadthFirst( root, 1 );
        	System.out.println("");

        }
    }
	
    /**
     * Internal method to insert into a subtree.
     * @param x the item to insert.
     * @param t the node that roots the subtree.
     * @return the new root of the subtree.
     */
    private AvlNode<AnyType> insert( AnyType x, AvlNode<AnyType> t )
    {
        if( t == null )
            return new AvlNode<AnyType>( x, null, null );
		
		int compareResult = x.compareTo( t.element );
		
        if( compareResult < 0 )
        {
            t.left = insert( x, t.left );
            if( height( t.left ) - height( t.right ) == 2 )
                if( x.compareTo( t.left.element ) < 0 )
                    t = rotateWithLeftChild( t );
                else
                    t = doubleWithLeftChild( t );
        }
        else if( compareResult > 0 )
        {
            t.right = insert( x, t.right );
            if( height( t.right ) - height( t.left ) == 2 )
                if( x.compareTo( t.right.element ) > 0 )
                    t = rotateWithRightChild( t );
                else
                    t = doubleWithRightChild( t );
        }
        else
            ;  // Duplicate; do nothing
        t.height = Math.max( height( t.left ), height( t.right ) ) + 1;
        return t;
    }


    /**
     * Internal method to find the smallest item in a subtree.
     * @param t the node that roots the tree.
     * @return node containing the smallest item.
     */
    private AvlNode<AnyType> findMin( AvlNode<AnyType> t )
    {
        if( t == null )
            return t;

        while( t.left != null )
            t = t.left;
        return t;
    }

    /**
     * Internal method to find the largest item in a subtree.
     * @param t the node that roots the tree.
     * @return node containing the largest item.
     */
    private AvlNode<AnyType> findMax( AvlNode<AnyType> t )
    {
        if( t == null )
            return t;

        while( t.right != null )
            t = t.right;
        return t;
    }

    /**
     * Internal method to find an item in a subtree.
     * @param x is item to search for.
     * @param t the node that roots the tree.
     * @return true if x is found in subtree.
     */
    private boolean contains( AnyType x, AvlNode<AnyType> t )
    {
        while( t != null )
		{
			int compareResult = x.compareTo( t.element );
			
            if( compareResult < 0 )
                t = t.left;
            else if( compareResult > 0 )
                t = t.right;
            else
                return true;    // Match
		}

        return false;   // No match
    }

    /**
     * Internal method to print a subtree in a preorder fashion.
     * Each element is associated with an integer which specifies
     * where that node is structurally. In particular, the root node
     * is node #1. All left children are 2 times their parent and all
     * right children are 2 times their parent plus one. This system
     * mirrors how a binary heap is stored in an array.
     * @param t the node that roots the tree.
     */
    private void printTree( AvlNode<AnyType> t, int nodenum )
    {
        if( t != null )
        {
        	System.out.println("Element #"+nodenum+". "+t.element );
            printTree( t.left, 2*nodenum );
            printTree( t.right, 2*nodenum+1 );
        }
    }
    
    /**
     * Breadth-first traversal and printout:
     * This method prints out all the values of the current object using
     * a breadth first search. Thus, The root node prints first, followed
     * by the left child, then the right child, etc.
     */
    private void printTreeBreadthFirst( AvlNode<AnyType> t, int nodenum )
    {
    	// These queues mirror each other and basically store the upcoming
    	// nodes in the BFS.
    	Queue queueNodes = null;
    	Queue queueNodeNums = null;
    	
    	if (t.element.equals(null))
    	{
    		return;
    	}
    	
    	queueNodes = new LinkedList<AvlNode<AnyType>>(); // Actually a queue
    	queueNodeNums = new LinkedList<Integer>();
    	
    	// Start the algorithm by adding the root node to the queue, our 
    	// starting point.
    	queueNodes.add(t);
    	queueNodeNums.add((Integer)nodenum);
    	
    	// Loop as long as there are more nodes left.
    	while (!queueNodes.isEmpty())
    	{
    		// This is the element to print out next!
    		AvlNode<AnyType> trav = (AvlNode<AnyType>)queueNodes.remove();
    		Integer travNodeNum = (Integer)queueNodeNums.remove();
    		
    		// Print out the current element and its height.
    		System.out.println("Element "+travNodeNum+" containing "+trav.element+" at height "+trav.height);
    		
    		// Once you print an element out, you need to add its children to 
    		// the back of the queue. We choose to go left then right.
    		if (trav.left != null)
    		{
    			queueNodes.add(trav.left);
    			queueNodeNums.add(2*travNodeNum);
    		}
    		
    		if (trav.right != null)
    		{
    			queueNodes.add(trav.right);
    			queueNodeNums.add(2*travNodeNum+1);
    		}
    	}
    }



    /**
     * Return the height of node t, or -1, if null.
     */
    private int height( AvlNode<AnyType> t )
    {
        return t == null ? -1 : t.height;
    }

    /**
     * Rotate binary tree node with left child.
     * For AVL trees, this is a single rotation for case 1.
     * Update heights, then return new root.
     */
    private AvlNode<AnyType> rotateWithLeftChild( AvlNode<AnyType> k2 )
    {
        AvlNode<AnyType> k1 = k2.left;
        k2.left = k1.right;
        k1.right = k2;
        k2.height = Math.max( height( k2.left ), height( k2.right ) ) + 1;
        k1.height = Math.max( height( k1.left ), k2.height ) + 1;
        return k1;
    }

    /**
     * Rotate binary tree node with right child.
     * For AVL trees, this is a single rotation for case 4.
     * Update heights, then return new root.
     */
    private AvlNode<AnyType> rotateWithRightChild( AvlNode<AnyType> k1 )
    {
        AvlNode<AnyType> k2 = k1.right;
        k1.right = k2.left;
        k2.left = k1;
        k1.height = Math.max( height( k1.left ), height( k1.right ) ) + 1;
        k2.height = Math.max( height( k2.right ), k1.height ) + 1;
        return k2;
    }

    /**
     * Double rotate binary tree node: first left child
     * with its right child; then node k3 with new left child.
     * For AVL trees, this is a double rotation for case 2.
     * Update heights, then return new root.
     */
    private AvlNode<AnyType> doubleWithLeftChild( AvlNode<AnyType> k3 )
    {
        k3.left = rotateWithRightChild( k3.left );
        return rotateWithLeftChild( k3 );
    }

    /**
     * Double rotate binary tree node: first right child
     * with its left child; then node k1 with new right child.
     * For AVL trees, this is a double rotation for case 3.
     * Update heights, then return new root.
     */
    private AvlNode<AnyType> doubleWithRightChild( AvlNode<AnyType> k1 )
    {
        k1.right = rotateWithLeftChild( k1.right );
        return rotateWithRightChild( k1 );
    }

    private static class AvlNode<AnyType>
    {
            // Constructors
        AvlNode( AnyType theElement )
        {
            this( theElement, null, null );
        }

        AvlNode( AnyType theElement, AvlNode<AnyType> lt, AvlNode<AnyType> rt )
        {
            element  = theElement;
            left     = lt;
            right    = rt;
            height   = 0;
        }

        AnyType           element;      // The data in the node
        AvlNode<AnyType>  left;         // Left child
        AvlNode<AnyType>  right;        // Right child
        int               height;       // Height
    }

      /** The tree root. */
    private AvlNode<AnyType> root;


    // Test program
    public static void main( String [ ] args )
    {
        AvlTree<Integer> t = new AvlTree<Integer>( );
        final int NUMS = 40;
        final int GAP  = 37;

        System.out.println( "Checking... (no more output means success)" );

        for( int i = GAP; i != 0; i = ( i + GAP ) % NUMS )
            t.insert( i );
          
        t.printTree();

        if( t.findMin( ) != 1 || t.findMax( ) != NUMS - 1 )
            System.out.println( "FindMin or FindMax error!" );

        for( int i = 1; i < NUMS; i++ )
            if( !t.contains( i ) )
               System.out.println( "Find error!" );
        
        // Test the remove method
        t.remove( 10 );
        t.remove( 37 );

        t.printTree();
    }
}