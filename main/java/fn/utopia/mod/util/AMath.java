package fn.utopia.mod.util;

// Some static methods for simple arithmetic, geometry and related things.
public class AMath {
	
	public enum Direction {
		
		NORTH("NORTH","N"),
		NORTHEAST("NORTHEAST","NE"),
		EAST("EAST","E"),
		SOUTHEAST("SOUTHEAST","SE"),
		SOUTH("SOUTH","S"),
		SOUTHWEST("SOUTHWEST","SW"),
		WEST("WEST","W"),
		NORTHWEST("NORTHWEST","NW");		
		
		private final String upper,lower;
		private final String initial;
		
		private Direction(String upper, String initial){
			this.upper = upper;
			this.lower = upper.toLowerCase();
			this.initial = initial;
		}
		
		public String getUpperCase(){
			return upper;
		}
		
		public String getLowerCase(){
			return lower;
		}
		
		public String getInitial(){
			return initial;
		}
	}
	
	private static double P_8  = Math.PI/8.0;
	private static double P3_8 = 2.0*Math.PI/8.0;
	private static double P5_8 = 3.0*Math.PI/8.0;
	private static double P7_8 = 4.0*Math.PI/8.0;
	private static double P9_8 = 5.0*Math.PI/8.0;
	private static double P11_8 = 6.0*Math.PI/8.0;
	private static double P13_8 = 7.0*Math.PI/8.0;
	private static double P15_8 = 7.0*Math.PI/8.0;
	
	// Get the direction of the vector P0 -> P1
	public static Direction getDirection(double x0, double z0, double x1, double z1){
		double angle = Math.atan2(x1 - x0, z1 - z0);
		if(angle < 0 ){
			angle += Math.PI*2;
		}
		// east to south positive rotation
		if(angle >= P15_8 || angle < P_8){
			return Direction.EAST;
		} else if(angle >= P_8 || angle < P3_8){
			return Direction.SOUTHEAST;
		} else if(angle >= P3_8 || angle < P5_8){
			return Direction.SOUTH;
		} else if(angle >= P5_8 || angle < P7_8){
			return Direction.SOUTHWEST;
		} else if(angle >= P7_8 || angle < P9_8){
			return Direction.WEST;
		} else if(angle >= P9_8 || angle < P11_8){
			return Direction.NORTHWEST;
		} else if(angle >= P11_8 || angle < P13_8){
			return Direction.NORTH;
		} else {
			return Direction.NORTHEAST;
		}
	}
	
	// Are the disks (x0,y0,r0) and (x1,y1,r1) completely disjoint.
	// Assuming no degenerate circles (r = 0).
	public static boolean circlesDisjoint(double x0, double z0, double r0, double x1, double z1, double r1){
		return (x0 - x1)*(x0 - x1) + (z0 - z1)*(z0 - z1) < (r0 + r1)*(r0 + r1);
	}
	
	// Are the spheres (x0,y0,z0,r0) and (x1,y1,z1,r1) completely disjoint. 
	// Assuming no degenerate spheres (r = 0).
	public static boolean spheresDisjoint(double x0, double y0, double z0, double r0, double x1, double y1, double z1, double r1){
		return (x0 - x1)*(x0 - x1) + (y0 - y1)*(y0 - y1) + (z0 - z1)*(z0 - z1) < (r0 + r1)*(r0 + r1);
	}
	
	// Is the point (x,z) inside the circle with center (Px,Py) and radius r.
	public static boolean isInsideCicle(double x, double z, double Px, double Pz, double r){
		return (x - Px)*(x - Px) + (z - Pz)*(z - Pz) < r*r;
	}
	
	// Is the point (x,y,z) inside the sphere with center (Px,Py,Pz) and radius r.
	public static boolean isInsideSphere(double x, double y, double z, double Px, double Py, double Pz, double r){
		return (x - Px)*(x - Px) + (y - Py)*(y - Py) + (z - Pz)*(z - Pz) < r*r;
	}
}
