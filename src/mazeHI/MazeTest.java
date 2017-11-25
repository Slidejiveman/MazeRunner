package mazeHI;
import mazePD.*;
import mazePD.Maze.Direction;
import mazePD.Maze.MazeMode;
import DroidHI.DroidMonitor;
import DroidPD.*;

public class MazeTest {

	public static void main(String[] args) {
		
		MazeTester(2,10);
		
		Droid droid = new Droid("R9-D4"); 
		DroidMonitor.altmain(args);
		Maze maze = new Maze(10, 2, MazeMode.TEST);	
		try {
			droid.enterMaze(maze);
		    droid.searchLevel();
			DroidPrint(droid, maze);
		} catch (Exception e) {
			System.err.println("I am Error.");
		}
		
	}
	/**MazeTester prints out the a test maze and was given to us by
	 * it's original author, Professor North. It prints all of the
	 * cells in the array.
	 * @param int levels - the depth of the maze in the z direction
	 * @param int dim - the length and width of the maze, which is square
	 * @author David North*/
	public static void MazeTester(int levels, int dim) {
		System.out.println("MazeTest");
		Maze maze = new Maze(dim, levels, MazeMode.TEST);
		System.out.println("Maze- " + maze.toString());
		
		for(int z = 0; z < levels; z++) {
			System.out.println("Level- " + z);
			String[] mazeArray = maze.toStringLevel(z);
			for (int y = 0; y < 10; y++) {
				System.out.println(mazeArray[y]);
			}
		}
	}
	
	/**DroidPrint calls an internal toString() function within the droid
	 * and returns a printout of all the cells and their contents that 
	 * the droid scanned on his journey.
	 * 
	 * @param Droid droid - the droid in the maze
	 * @param Maze maze - the maze holding the droid
	 * @author RyderDale */
	public static void DroidPrint(Droid droid, Maze maze) {
		System.out.println("Droid's Map");
		System.out.println("Maze- " + maze.toString());
		for(int z = 0; z < maze.getMazeDepth(); z++) {
			System.out.println("Level-" + z);
			String[] droidArray = droid.toStringLevel(z);
			for (int y = 0; y < 10; y++) {
				System.out.println(droidArray[y]);
			}
		}
	}
	
	
}

