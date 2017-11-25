package DroidPD;
import mazePD.*;
import mazePD.Maze.Content;
import mazePD.Maze.Direction;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.SwingUtilities;

public class Droid implements DroidInterface {
    
	//Location: remember content, whether visited, coordinates
	//Potential variables
	public enum Status {LASTSEARCHSUCCESS, LASTSEARCHFAILED};
	Maze maze;
	int mazeDepth = 0;
	int mazeDim = 0;
	String name = "";
	Coordinates startCoordinates;
	Coordinates currentCoordinates;
	Coordinates[] adjCellCoordinates;
	LinkedStack pathStack = new LinkedStack();
	Cell[][][] cellArray;
	Content startCellContent;
	Content currentCellContent;
	Content[] adjCellContents;
	Cell startCell;
	Cell currentCell;
	Cell[] adjCells;
	Cell backUp;
	Direction nextDirection;
	int moves;
	Status status = Status.LASTSEARCHFAILED;
	private ArrayList<ActionListener> actionListeners;
	
public class Cell {
		
		private Coordinates coordinates;			// coordinates of this cell
		private Content cellContent;				// contents of this cell 
		private boolean genVisit = false;				// has this cell be visited during generation
		private int moveFromX,moveFromY;			// cell moved from during creation
		
		protected Cell(int x, int y, int z, Content cellContent)
		{
			coordinates = new Coordinates(x,y,z);
			setCellContent(cellContent);
		}

		protected int getLocX() {
			return getCoordinates().getX();
		}

		protected void setLocX(int locX) {
			getCoordinates().setX(locX);
		}
		
		protected int getLocY() {
			return getCoordinates().getY();
		}

		protected void setLocY(int locY) {
			getCoordinates().setY(locY);;
		}

		protected int getLocZ() {
			return getCoordinates().getZ();
		}
		
		protected void setLocZ(int locZ) {
			this.getCoordinates().setZ(locZ);
		}
		
		protected Content getCellContent() {
			return cellContent;
		}
	
		protected void setCellContent(Content cellContent) {
			this.cellContent = cellContent;
		}

		protected boolean isGenVisit() {
			return genVisit;
		}
		
		protected void setGenVisit(boolean genVisit) {
			this.genVisit = genVisit;
		}
		
		protected String locString()
		{
			return "["+new Integer(getLocX()).toString()+","+new Integer(getLocY()).toString()+","+
					new Integer(getLocZ()).toString()+"]";
		}
		
		public String toString()
		{
			switch(cellContent)
			{
			case EMPTY:
				return "[ ]";
			case BLOCK:
				return "[*]";
			case PORTAL_DN:
				return "[P]";
			case PORTAL_UP:
				return "[P]";
			case COIN:
				return "[C]";
			case END:
				return "[E]";
			default:
				return "[X]";
			}
		}

		protected Coordinates getCoordinates() {
			return coordinates;
		}

		protected void setCoordinates(Coordinates coordinates) {
			this.coordinates = coordinates;
		}

	}
	
	/**Constructor*/
	public Droid(String name) {
	    this.setName(name);
	    actionListeners = new ArrayList<ActionListener>();
    }
    /**robot enters the maze and becomes aware of its information*/
    public void enterMaze(Maze maze) {
    	
    	maze.enterMaze(this);
    	this.setMaze(maze);
    	this.setMazeSize(maze);
    	this.setMazeDepth(maze);
    	this.startCoordinates = maze.getMazeStartCoord();
    	this.currentCoordinates = maze.getMazeStartCoord();
    	this.startCellContent = scanCurrentCell();
    	this.startCell = new Cell(startCoordinates.getX(),startCoordinates.getY(),
    			          startCoordinates.getZ(),startCellContent);
    	startCell.setGenVisit(true);
    	this.currentCell = startCell;
    	pathStack.push(startCell);
    	this.cellArray = new Cell[this.mazeDim][this.mazeDim][this.mazeDepth];
    	this.cellArray[startCoordinates.getX()][startCoordinates.getY()][startCoordinates.getZ()] = 
    			startCell;
    }
    
    /**main searching function that calls move() and others
     */
    public void searchLevel() throws InterruptedException {
    	
    	scanAdjacentCells();
    	while(!pathStack.isEmpty() && peek().getCellContent() != Content.END) { 
	    		    	
	    	if((adjCellContents[0] != Content.BLOCK && adjCellContents[0] != Content.NA)&& 
	    			!hasBeenVisited(Direction.D00)) {
	    		
	    		move(Direction.D00); 
	    		//scanCurrentCell();			//should be redundant
	    		checkForPortal();
	    	    scanAdjacentCells();
	    	    
	    	} else if (adjCellContents[1] != Content.BLOCK && adjCellContents[1] != Content.NA &&
	    			!hasBeenVisited(Direction.D90)) {
	    		
	    		move(Direction.D90);
	    		//scanCurrentCell();		//should be redundant
	    		checkForPortal();
	    		scanAdjacentCells();
	    		
	    	} else if (adjCellContents[2] != Content.BLOCK && adjCellContents[2] != Content.NA &&
	    			!hasBeenVisited(Direction.D180)) {
	    		
	    		move(Direction.D180);
	    		//scanCurrentCell();		//should be redundant
	    		checkForPortal();
	    		scanAdjacentCells();
	    		
	    	} else if (adjCellContents[3] != Content.BLOCK && adjCellContents[3] != Content.NA &&
	    			!hasBeenVisited(Direction.D270)) {
	    		
	    		move(Direction.D270);
	    		//scanCurrentCell();		//should be redundant
	    		checkForPortal();
	    		scanAdjacentCells();
	    		
	    	} else {
	    		
	    		backUp = pop();
	    		backUp = peek();
	    		
	    		nextDirection = chooseNextDirection();
	    		
	    		move(nextDirection);
	    		
	    		//scanCurrentCell();		//should be redundant
	    		checkForPortal();
	    		scanAdjacentCells();
	    	}
	    	//Update GUI
    		notifyListeners();
    		Thread.sleep(250);
    	}
    	if(pathStack.isEmpty()) {
    		System.out.println("I am out of moves.");
    	} else {
    		status = Status.LASTSEARCHSUCCESS;
    		notifyListeners();
    		System.out.println("End reached.");	
    	}
    	
    }
    
    /**moves the robot into an open cell*/
    public void move(Direction direction) {
    	
        Coordinates newCoordinates = this.maze.move(this, direction);
        if(!currentCoordinates.equals(newCoordinates)) {
		    currentCoordinates = newCoordinates;
		    int x = getCurrentCoordinates().getX();
	        int y = getCurrentCoordinates().getY();
	        int z = getCurrentCoordinates().getZ();
	   	    currentCell = cellArray[x][y][z];
        	currentCell.setGenVisit(true);
		    if(((Cell)pathStack.top()).getCoordinates() != currentCell.getCoordinates()) {
		    	pathStack.push(currentCell);
		    	moves++;
		    }
        }
    }
    
    /**moves the robot down to the next level*/
    // Use portal does need to be like this or it will break!
    public void usePortal(Direction direction) {
    	Coordinates newCoordinates = this.maze.usePortal(this, direction);
    	if(!currentCoordinates.equals(newCoordinates)) {
		    currentCoordinates = newCoordinates;
        	currentCell = new Cell(currentCoordinates.getX(), currentCoordinates.getY(), 
				   currentCoordinates.getZ(), scanCurrentCell());
        	currentCell.setGenVisit(true);
		    pathStack.push(currentCell);
		    int x = getCurrentCoordinates().getX();
	        int y = getCurrentCoordinates().getY();
	        int z = getCurrentCoordinates().getZ();
	   	    cellArray[x][y][z] = currentCell;
        }
    }
    
    public void checkForPortal() {
    	if(currentCell.getCellContent() == Content.PORTAL_DN) {
	    	usePortal(Direction.DN);
	    } 
    }
    
    public Direction chooseNextDirection() {
    	if(this.currentCell.getLocX() < this.backUp.getLocX()) {
    		return Direction.D90;
    	} else if(this.currentCell.getLocX() > this.backUp.getLocX()) {
    		return Direction.D270;
    	} else if(this.currentCell.getLocY() < this.backUp.getLocY()) {
    		return Direction.D180;
    	} else if(this.currentCell.getLocY() > this.backUp.getLocY()){
    		return Direction.D00;
    	} else
    		return null;
    }
    
    /** Fills up all of his local memory with maze data*/
    public Content scanCurrentCell() {
       return currentCellContent = maze.scanCurLoc(this);
    }
    
    public void scanAdjacentCells() {
    	this.adjCellContents = maze.scanAdjLoc(this);
    	int x = currentCell.getLocX();
    	int y = currentCell.getLocY();
    	int z = currentCell.getLocZ();
    	// either makes a new cell or updates content if it exists
    	if(adjCellContents[0] != Content.NA) {
	    	if(cellArray[x][y-1][z] == null){
	    		cellArray[x][y-1][z] = new Cell(x, y-1, z, adjCellContents[0]);
	    	} else {
	    		cellArray[x][y-1][z].setCellContent(adjCellContents[0]);
	    	}
    	}
    	
    	if(adjCellContents[1] != Content.NA) {
	    	if(cellArray[x+1][y][z] == null){
	    		cellArray[x+1][y][z] = new Cell(x+1, y, z, adjCellContents[1]);
	    	} else {
	    		cellArray[x+1][y][z].setCellContent(adjCellContents[1]);
	    	}
    	}
    	
    	if(adjCellContents[2] != Content.NA) {
	    	if(cellArray[x][y+1][z] == null){
	    		cellArray[x][y+1][z] = new Cell(x, y+1, z, adjCellContents[2]);
	    	} else {
	    		cellArray[x][y+1][z].setCellContent(adjCellContents[2]);
	    	}
    	}
    	
    	if(adjCellContents[3] != Content.NA) {
	    	if(cellArray[x-1][y][z] == null){
	    		cellArray[x-1][y][z] = new Cell(x-1, y, z, adjCellContents[3]);
	    	} else {
	    		cellArray[x-1][y][z].setCellContent(adjCellContents[3]);
	    	}
    	}	
    }
    
    public boolean hasBeenVisited(Direction direction) {
    	int x = getCurrentCoordinates().getX();
    	int y = getCurrentCoordinates().getY();
    	int z = getCurrentCoordinates().getZ();
    	switch (direction) {
    	case D00 :
	        if (currentCell.getLocY()-1 >=0)
	        	y--;
        	break; 		
    	case D90 :
    		if (currentCell.getLocX()+1 < getMazeSize())
    			x++;
    		break;
    	case D180:
    		if (currentCell.getLocY()+1 < getMazeSize())
    			y++;
    		break;
    	case D270:
    		if (currentCell.getLocX()-1 >= 0)
    			x--;
    		break;
    	case DN:
    		if (currentCell.getLocZ()+1 < getMazeDepth())
    			z++;
    		break;
    	case UP:
    		if (currentCell.getLocX()-1 >= 0)
    			z--;
    		break;
    	default:	
    		//There is no default case code
    	}
    	if(!cellArray[x][y][z].isGenVisit()) {
    		return false;
    	} else {
    		return true;
    	}
    }	
    
    /** gets the coordinates of current cell*/
    public Coordinates getCurrentCoordinates() {
    	return currentCoordinates;
    }
    /**gets max x and y coordinates*/
    public int getMazeSize() {
    	return this.mazeDim;
    }
    public void setMazeSize(Maze maze) {
    	this.mazeDim = maze.getMazeDim();
    }
    /**gets max z coordinate */
    public int getMazeDepth() {
    	return this.mazeDepth;
    }
    public void setMazeDepth(Maze maze) {
    	this.mazeDepth = maze.getMazeDepth();
    }
    
    public String getName() {
    	return this.name;
    }
    public void setName(String name) {
    	this.name = name;
    }
    public void setMaze(Maze maze) {
    	this.maze = maze;
    }
    public Content getStartCellContent() {
    	return this.startCellContent;
    }
    public Cell pop() {
    	return (Cell)pathStack.pop();
    }
    public Cell peek() {
    	return (Cell)pathStack.top();
    }
    
    // Not sure that this is the right function at the moment.
    // Might just need to pop off the whole stack
    public int getMoves() {
    	return moves;
    }
    
    public String[] toStringLevel(int level)
	{
		String[] droidLevel = new String[getMazeSize()];
		for (int y=0;y<getMazeSize();y++)
			{
				droidLevel[y] ="";
				for (int x=0;x<getMazeSize();x++)
				{
					if(cellArray[x][y][level] != null) {
						droidLevel[y] += cellArray[x][y][level].toString();
					} else {
						droidLevel[y] += "[?]";
					}
				}
			}
				
		return droidLevel;	
	}
	public Maze getMaze() {
		
		return this.maze;
	}

	public String getPositionContent(int x, int y, int z) {
		if(cellArray[x][y][z] != null) {
			return cellArray[x][y][z].toString();
		} else {
			return "[?]";
		}
	}
	
	public Status getStatus() {
		return this.status;
	}

	public void addActionListener(ActionListener actionListner)
	{
		actionListeners.add(actionListner);
	}
	
	// call this after a move is made in the maze and possibly sleep afterwards
	public void notifyListeners() {
	  for (ActionListener actionListner : actionListeners) {	
		  SwingUtilities.invokeLater(new Runnable() {
		   public void run() {
		   actionListner.actionPerformed(new ActionEvent(this,0,""));
		   }
		  });
	  }
	}
	
}
