# 3D-Maze
3D Maze using libgdx

Our maze is randomly generated (Code adopted from  https://en.wikipedia.org/wiki/Maze_generation_algorithm c algorithm, thanks Hilmar & Christian for pointing this out to us!)

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
    
    Mouse to look around(left/right) 

## Objects
We have an elevator in the maze, and, believe it or not, it elevates you!

## Collisions:
    We figure out which cell we are in, and use that to check only against adjacent walls. If no collision is found, we check for diagonal collisions against the corners using a slightly different fornmula. 