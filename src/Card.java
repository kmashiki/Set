public class Card {
	private int num;			//1,2,3
	private int color;		//red, green, blue
	private int shape;		//rectangle, triangle, oval	
	private int shade;		//empty, half, full
	
	public Card(){}	//default constructor
	
	public Card (int n, int c, int shape , int shade){	//overloaded constructor
		num=n;
		color=c;
		this.shape=shape;
		this.shade=shade;
	}
	
	//accessor methods
	public int getNum(){return num;}
	public int getColor(){return color;}
	public int getShape(){return shape;}
	public int getShade(){return shade;}	
}