# 3D-Maze
3D Maze using libgdx

## Students
Guðjón Steinar Sverrisson (gudjonss12)

Hlynur Stefánsson (hlynurs15)

## Maze Generation

Our maze is randomly generated (Code adopted from  https://en.wikipedia.org/wiki/Maze_generation_algorithm c algorithm, thanks Hilmar & Christian for pointing this out to us!)

### Custom maps
By default the maps are generated randomly and every time you run the program the resulting maze is output to myMaze.txt under the assets/ folder. The first line is the size of the maze and then a bunch of characters that represent different objects in our game.

    # : Wall
    P : Player (only one allowed)
    E : Elevator
    c : coin

Currently our program can only handle custom maps that have the same width and height so the size of the maze has to be size*size large. Feel free to play around and make your own custom maps!

### Loading a custom map
Loading a custom map is easy, you just have to open the DesktopLauncher.java file and send in the file name of the custom map you want to load into the Maze3D constructor. By default it's set to null which means that the program will generate a random 25x25 maze for you instead:

    new LwjglApplication(new Maze3D(null), config);

But you can set it like so:

    new LwjglApplication(new Maze3D("7x7.txt"), config);

And then the program will load the file assets/7x7.txt !

## Lights
Our scene has 4 light sources, all of which use the vertex shader.
These lights are placed one in every quadrant of our maze.
None of these lights have any ambient values, although there is a global ambience factor of (0.2, 0.2, 0.2, 1.0)
- Each light has a position
- Each light has a diffuse value
- Each light has a specular value

## Materials
- Materials can have diffuse values
- Materials can have specular values
- Materials can have emission
- Materials can have shininess 

## Controls
    W: Forward
    A: Strafe left
    S: Backward
    D: Strafe right
    SPACE: Jump
    
    Mouse or arrow keys to look around(left/right)

## Objects
We have a random numbner of elevators in the maze, and, believe it or not, they elevates you! They spawn all over the map and move at varying speeds. There are also rotating "Coins" randomly distributed around the map that the player can pick up (can't do anything else with them unfortunately).

## Collisions:
We figure out which cell we are in, and use that to check only against adjacent walls. If no collision is found, we check for diagonal collisions against the corners using a slightly different formula.
