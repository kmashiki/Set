import javax.swing.JFrame;

public class SetKM {
	public static void main(String[] args) {
		Game tool = new Game(1);	//starts the game
		tool.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		tool.setTitle("Set");	//labels the board window
		tool.pack();
		tool.show();
	}
}