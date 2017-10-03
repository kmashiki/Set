public class Player {
	
	//variable declaration
     private String name;
     private int score;
     private boolean isMyTurn;

     //default constructor
     public Player () {
          isMyTurn =false;
     }

     //name accessor and mutator methods
     public String getName(){return name;}
     public void setName(String n){name =n;}
     
     //score accessor and mutator methods
     public int getScore(){return score;}
     public void setScore(int s){score += s;}

     //turn accessor and mutator methods
     public boolean getMyTurn(){return isMyTurn;}
     public void setMyTurn(boolean input){isMyTurn = input;}
}