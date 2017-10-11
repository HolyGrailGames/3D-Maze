package com.ru.tgra.shapes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MazeGenerator {
	private List<Node> nodes;
	private int width;
	private int height;
	private float blockSize;
	
	private Random random = new Random();
	
	public MazeGenerator(int width, int height, float blockSize) {
		this.nodes = new ArrayList<>();
		this.width = width;
		this.height = height;
		this.blockSize = blockSize;
	}
	
	public void init() {
		//Setup crucial nodes
		for (int i = 0; i < width; i++ )
		{
			for (int j = 0; j < height; j++ )
			{
				//Assume that all directions can be explored
				Node n = new Node(new Point3D(i, blockSize/2, j), blockSize, null, 4, false)
				if ( i * j % 2 == 0) 
				{
					//Add walls between nodes
					n.isWall = true;
				}
				nodes.add(n);
			}
		}
	}
	
	private Node link(Node n)
	{
		//Connects node to random neighbor (if possible) and returns
		//address of next node that should be visited

		int x, y;
		int dir;
		Node dest;
		
		//Nothing can be done if null pointer is given - return
		if ( n == null ) return null;
		
		//While there are directions still unexplored
		while (n.dirs > 0)
		{
			//Randomly pick one direction
			dir = random.nextInt(4)+1;
			dir = ( 1 << ( rand( ) % 4 ) );
			
			//If it has already been explored - try again
			if ( ~n->dirs & dir ) continue;
			
			//Mark direction as explored
			n->dirs &= ~dir;
			
			//Depending on chosen direction
			switch ( dir )
			{
				//Check if it's possible to go right
				case 1:
					if ( n->x + 2 < width )
					{
						x = n->x + 2;
						y = n->y;
					}
					else continue;
					break;
				
				//Check if it's possible to go down
				case 2:
					if ( n->y + 2 < height )
					{
						x = n->x;
						y = n->y + 2;
					}
					else continue;
					break;
				
				//Check if it's possible to go left	
				case 4:
					if ( n->x - 2 >= 0 )
					{
						x = n->x - 2;
						y = n->y;
					}
					else continue;
					break;
				
				//Check if it's possible to go up
				case 8:
					if ( n->y - 2 >= 0 )
					{
						x = n->x;
						y = n->y - 2;
					}
					else continue;
					break;
			}
			
			//Get destination node into pointer (makes things a tiny bit faster)
			dest = nodes + x + y * width;
			
			//Make sure that destination node is not a wall
			if ( dest->c == ' ' )
			{
				//If destination is a linked node already - abort
				if ( dest->parent != NULL ) continue;
				
				//Otherwise, adopt node
				dest->parent = n;
				
				//Remove wall between nodes
				nodes[n->x + ( x - n->x ) / 2 + ( n->y + ( y - n->y ) / 2 ) * width].c = ' ';
				
				//Return address of the child node
				return dest;
			}
		}
		
		//If nothing more can be done here - return parent's address
		return n->parent;
	}
}
