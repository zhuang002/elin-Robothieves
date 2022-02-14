import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {

	static char[][] graph = null;
	static int[][] state = null;
	static Coordinate start = null;
	static int step = 0;
	static ArrayList<Coordinate> cameras=new ArrayList<>();
	
	public static void main(String[] args) throws InputErrorException {
		// TODO Auto-generated method stub
		
		// input the data
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		int m = sc.nextInt();
		
		graph = new char[n][m];
		state = new int[n][m];
		sc.nextLine();
		for (int i=0;i<n;i++) {
			String line = sc.nextLine();
			for (int j=0;j<line.length();j++) {
				graph[i][j] = line.charAt(j);
				if (graph[i][j] == 'S') {
					start = new Coordinate(i,j);
				} else if (graph[i][j] == 'C') {
					cameras.add(new Coordinate(i,j));
				}
			}
		}
		
		for (int i=0;i<n;i++) {
			for (int j=0;j<m;j++) {
				state[i][j] = -1; // -1 means this coordinate has not been visited. 
				//-2 or non-negative value means the coordinate has been visited. 
				//-3 means this coordinate is visible to a camera
			}
		}
		
		preprocessCameras();
		
		if (isVisibleByCamera(start)) {
			printOut();
			return;
		}
		
		Set<Coordinate> current = new HashSet<>();
		current.add(start);
		Set<Coordinate> next = new HashSet<>();
		
		while (!current.isEmpty()) {
			for (Coordinate coord:current) {
				state[coord.row][coord.col] = step;
				next.addAll(getNeighbours(coord));
			}
			current = next;
			next = new HashSet<>();
		}
		
		printOut();
		
	}

	private static Set<Coordinate> getNeighbours(Coordinate coord) {
		// TODO Auto-generated method stub
		
		// get neighbours that 1. can be stopped(continue to go if meet conveyer) 
		// and 2. is not visible by camera. and 3. is not a wall.
		
		Set<Coordinate> neighbours = new HashSet<>();
		
		// go up
		Coordinate loc = moveUntilStop(coord, -1, 0);
		if (loc != null) 
			neighbours.add(loc);
		
		// go down
		loc = moveUntilStop(coord, 1, 0);
		if (loc != null) 
			neighbours.add(loc);
		
		// go left
		loc = moveUntilStop(coord, 0, -1);
		if (loc != null) 
			neighbours.add(loc);
		
		// go right
		loc = moveUntilStop(coord, -1, 0);
		if (loc != null) 
			neighbours.add(loc);
		
		return neighbours;
	}

	private static Coordinate moveUntilStop(Coordinate coord, int i, int j) {
		// TODO Auto-generated method stub
		int row = coord.row;
		int col = coord.col;
		
		
		row += i;
		col += j;
		
		if (row<0 || row>=graph.length || col<0 || col>graph[0].length) {
			return null;
		}
		
		if (state[row][col] == -2 || state[row][col] >=0) {
			// already visited.
			return null;
		}
		
		switch(graph[row][col]) {
		case 'W':
			return null;
		case '.':
			if (state[row][col] == -3) {
				return null;
			} 
			//state[row][col] = -2;
			return new Coordinate(row,col);
		case 'C':
			return null;
		case 'L':
			state[row][col] = -2;
			return moveUntilStop(new Coordinate(row,col), 0, -1);
		case 'U':
			state[row][col] = -2;
			return moveUntilStop(new Coordinate(row,col), -1, 0);
		case 'D':
			state[row][col] = -2;
			return moveUntilStop(new Coordinate(row,col), 1, 0);
		case 'R':
			state[row][col] = -2;
			return moveUntilStop(new Coordinate(row,col), 0, 1);
		default:
			throw new InputErrorException();
		}

	}

	private static void printOut() {
		// TODO Auto-generated method stub
		for (int i=0;i<state.length;i++) {
			for (int j=0;j<state[0].length;j++) {
				if (graph[i][j] == '.') {
					if (state[i][j]>=0) {
						System.out.println(state[i][j]);
					} else {
						System.out.println(-1);
					}
				}
			}
		}
	}

	private static boolean isVisibleByCamera(Coordinate coord) {
		// TODO Auto-generated method stub
		return state[coord.row][coord.col]!=-3;
	}

	private static void preprocessCameras() throws InputErrorException {
		// TODO Auto-generated method stub
		for (Coordinate coord: cameras) {
			markVisibleCoordinate(coord);
		}
	}

	private static void markVisibleCoordinate(Coordinate camera) throws InputErrorException {
		// TODO Auto-generated method stub
		int row = camera.row;
		int col = camera.col;
		
		
		// from camera go right
		markVisibleCoordinate(row,col,0,1);
		
		
		
		// from camera go left
		row = camera.row;
		col = camera.col;
		markVisibleCoordinate(row,col,0,-1);
		
		// from camera go down
		row = camera.row;
		col = camera.col;
		markVisibleCoordinate(row,col,1,0);
		
		// from camera go up
		row = camera.row;
		col = camera.col;
		markVisibleCoordinate(row,col,-1,0);
	}

	private static void markVisibleCoordinate(int row, int col, int i, int j {
		// TODO Auto-generated method stub
		boolean end = false;
		while (!end) {
			row += i;
			col += j; 
			if (col>=graph[0].length || col<0 || row<0 || row>graph.length) {
				// exceed the boundary
				break;
			}
			
			switch(graph[row][col]) {
			case 'W':
				end = true;
				break;
			case '.':
				state[row][col] = -3;
				break;
			case 'L':
			case 'R':
			case 'U':
			case 'D':
				break;
			default:
				throw new InputErrorException();
			}
		}
	}

}

class Coordinate {
	int row;
	int col;
	
	public Coordinate(int r, int c) {
		this.row = r;
		this.col = c;
	}
}

class InputErrorException extends Exception {
	
}
