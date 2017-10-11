package com.ru.tgra.shapes;

import java.util.Random;

public class MazeGenerator {

	Node nodes[]; //Nodes array
	int width, height; //Maze dimensions
	Random rand = new Random();
	
	public MazeGenerator(int width, int height ) {
		this.width = width;
		this.height = height;
	}
	
	int init( )
	{
		nodes = new Node[width * height];
		for (int i = 0; i < nodes.length; i++)
		{
			Node node = new Node();
			nodes[i] = node;
		}
		
		System.out.println(nodes[3]);
		
		Node n;
		
		//Allocate memory for maze
		
		if ( nodes == null ) {
			return 1;			
		}
			
		//Setup crucial nodes
		for (int i = 0; i < width; i++ )
		{
			for (int j = 0; j < height; j++ )
			{
				n = nodes[i + j * width];
				if (i * j % 2 != 0) 
				{
					n.x = i;
					n.y = j;
					n.dirs = 15; //Assume that all directions can be explored (4 youngest bits set)
					n.c = ' '; 
				}
				else n.c = '#'; //Add walls between nodes
			}
		}
		return 0;
	}

	Node link(Node n)
	{
		//Connects node to random neighbor (if possible) and returns
		//address of next node that should be visited

		int x = 0, y = 0;
		char dir;
		Node dest;
		
		//Nothing can be done if null pointer is given - return
		if ( n == null ) {
			return null;
		}
		
		//While there are directions still unexplored
		while (n.dirs != 0)
		{
			//Randomly pick one direction
			dir = ((char)(1 << (rand.nextInt(Integer.MAX_VALUE) % 4 )));
			
			//If it has already been explored - try again
			if ((~n.dirs & dir )!= 0) {
				continue;
			}
			
			//Mark direction as explored
			n.dirs &= ~dir;
			
			//Depending on chosen direction
			switch (dir)
			{
				//Check if it's possible to go right
				case 1:
					if (n.x + 2 < width)
					{
						x = n.x + 2;
						y = n.y;
					}
					else continue;
					break;
				
				//Check if it's possible to go down
				case 2:
					if (n.y + 2 < height)
					{
						x = n.x;
						y = n.y + 2;
					}
					else continue;
					break;
				
				//Check if it's possible to go left	
				case 4:
					if (n.x - 2 >= 0 )
					{
						x = n.x - 2;
						y = n.y;
					}
					else continue;
					break;
				
				//Check if it's possible to go up
				case 8:
					if ( n.y - 2 >= 0 )
					{
						x = n.x;
						y = n.y - 2;
					}
					else continue;
					break;
			}
			
			//Get destination node into pointer (makes things a tiny bit faster)
			dest = nodes[x + y * width];
			
			//Make sure that destination node is not a wall
			if ( dest.c == ' ' )
			{
				//If destination is a linked node already - abort
				if ( dest.parent != null) continue;
				
				//Otherwise, adopt node
				dest.parent = n;
				
				//Remove wall between nodes
				nodes[n.x + ( x - n.x ) / 2 + ( n.y + ( y - n.y ) / 2 ) * width].c = ' ';
				
				//Return address of the child node
				return dest;
			}
		}
		
		//If nothing more can be done here - return parent's address
		return n.parent;
	}

	void draw( )
	{

		//Outputs maze to terminal - nothing special
		for (int i = 0; i < height; i++ )
		{
			for (int j = 0; j < width; j++ )
			{
				System.out.print(nodes[j + i * width].c + " ");
			}
			System.out.println();
		}
	}
	
	void generate() {

		//Setup start node
		Node start = nodes[1 + width];
		start.parent = start;
		Node last= start;
		
		//Connect nodes until start node is reached and can't be left
		while ( ( last = link( last ) ) != start );
		draw( );
	}
}
