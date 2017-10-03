import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Game extends JFrame implements KeyListener
{
        //declare variables
        private BufferedImage image;
        private DrawArea drawArea;
        private JPanel canvas;
        private Card[][] board;
        private Graphics g;
        Player player1;
        Player player2;
        private javax.swing.Timer timer;
        public String supposedSet;
        int[] possibleSet;
        Card[] Set;
        boolean setInput;
        ArrayList<Card> deckOfCards;
        private int numHints;
        public boolean hintUsed;
        private int possibleHint;
        private boolean updateBoard;

        //default constructor
        public Game() {}

        //overloaded constructor- this isn't default because I need to call a default constructor
        //without resetting the values of each variable
        public Game(int input)
        {
                //read in spreadsheet of cards
                try {
                        image = ImageIO.read(new File("./src/set.jpg"));
                } catch (IOException e) {
                        e.printStackTrace();
                }

                //initialize variables
                drawArea = new DrawArea();
                canvas = new JPanel();
                canvas.add(drawArea);
                drawArea.requestFocus();
                board = new Card[3][4];
                supposedSet = "";
                possibleSet = new int[3];
                setInput = false;
                Set = new Card[3];
                deckOfCards = new ArrayList<Card>();
                numHints = 3;
                hintUsed = false;
                possibleHint = 0;
                updateBoard = false;

                //Get player's names and set scores to 0
                if (input == 1) {
                        player1 = new Player();
                        player2 = new Player();
                        String p1name, p2name;
                        p1name = JOptionPane.showInputDialog(null, "Enter player 1's name: ");
                        while (p1name.equalsIgnoreCase(""))
                                p1name = JOptionPane.showInputDialog(null, "That name is empty.  Enter a valid name: ");
                        p2name = JOptionPane.showInputDialog(null, "Enter player 2's name: ");
                        while (p2name.equalsIgnoreCase(""))
                                p2name = JOptionPane.showInputDialog(null,"That name is empty.  Enter a valid name: ");
                        while (p1name.equals(p2name))
                                p2name = JOptionPane.showInputDialog(null,"Enter a name for player 2 that is different than "
                                        + p1name + " : ");

                        player1.setName(p1name);
                        player1.setScore(0);

                        player2.setName(p2name);
                        player2.setScore(0);

                        JOptionPane.showMessageDialog(null,player1.getName()+ " will use the 'Q' key to enter a set \n"
                                +player2.getName() + " will use the 'P' key\nThe 'H' key gives you a hint\n"
                                                + "Pressing okay will begin the game");
                }

                //Configure canvas to draw graphics and keylistener to take input
                this.setContentPane(canvas);
                this.addKeyListener(this);
                buildDecks();   //creates deck of 81 cards

                drawOriginalBoard();    //draws first board

                //starts timer necessary for key listener
                timer = new Timer(40, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent ae) {
                        }
                });
                timer.start();
        }

        public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_Q) {
                        //determines that it's player 1's turn
                        player1.setMyTurn(true);
                        player2.setMyTurn(false);
                        //new Reminder(3);
                        setSupposedSet(JOptionPane.showInputDialog(player1.getName()
                                        + ", enter your set (ex. 1 5 11)"));
                }

                if (e.getKeyCode() == KeyEvent.VK_P) {
                        //determines that it's player 2's turn
                        player2.setMyTurn(true);
                        player1.setMyTurn(false);
                        //new Reminder(3);
                        setSupposedSet(JOptionPane.showInputDialog(player2.getName()
                                        + ", enter your set (ex. 1 5 11)"));
                }

                //determines if user wants a int
                if (e.getKeyCode() == KeyEvent.VK_H){
                        getHint();
                        setHintUsed(true);
                }
        }

        public void keyReleased(KeyEvent e) {}   //override

        public void keyTyped(KeyEvent e) {}     //override

        public class TimerAction implements ActionListener      //repaints
        {
                public void actionPerformed(ActionEvent ae)
                {
                        drawArea.repaint();
                }
        }

        public void setHintUsed(boolean input){ //determines if a user has already used a
                hintUsed = input;                                       //hint on one board
        }

        //This is the basis of a timer that cuts a player off for taking too much time to input a set

        /*public class Reminder{
                public Reminder(int seconds) {
                timer2.schedule(new RemindTask(), seconds*1000);
                }

            class RemindTask extends TimerTask {
                public void run() {
                    //timer2.cancel(); //Terminate the timer thread
                    System.out.println(boardHasBeenUpdated());
                    if(boardHasBeenUpdated())
                        JOptionPane.showMessageDialog(null, "Time's up! Input a blank line in the next box");
                }
            }
        }*/

        public boolean boardHasBeenUpdated(){return updateBoard;}       //determines if cards on the board
                                                                                                                                //have been changed
        public class DrawArea extends JPanel
        {
                //set height and width of the canvas/board
                private final int HEIGHT = 700;
                private final int WIDTH = 1110;
                public DrawArea()
                {
                        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
                        this.setMaximumSize(new Dimension(WIDTH, HEIGHT));
                        this.setBackground(Color.BLACK);
                }

                //redraws the board every time repaint is called
                public void paintComponent(Graphics g)
                {
                        super.paintComponent(g);
                        Card c = new Card();
                        for (int i = 0; i < 3; i++)
                                for (int m = 0; m < 4; m++)
                                {
                                        c = getCardFromBoard(i, m);
                                        if (c != null) {
                                        			int 	xc = 138;
                                        			int yc = 195;
                                                //finds coordinates of corners of the card on the spreadsheet
                                        			int xcoordBeginCrop = ((c.getNum() - 1) * xc + (c
                                                                .getShape() - 1) * (xc * 3));
                                                int ycoordBeginCrop = ((c.getShade() - 1) * yc + (c
                                                                .getColor() - 1) * (yc * 3));
                                                int xcoordEndCrop = 135;
                                                int ycoordEndCrop = yc;

                                                //determines where to print the card on the board
                                                int xcoordToPrint = m * 200;
                                                int ycoordToPrint = i * 215;

                                                // (getSubimage (x, y , width, height) x and y is coordinate of left corner
                                                // drawImage(image, x, y, null) x and y are coordinates where image will be drawn
                                                if (!(xcoordBeginCrop + xc <= image.getWidth()))
                                                        xcoordEndCrop = image.getWidth() - xcoordBeginCrop;
                                                if (!(ycoordBeginCrop + yc <= image.getHeight()))
                                                        ycoordEndCrop = image.getHeight() - ycoordBeginCrop;
                                                g.drawImage(image.getSubimage(xcoordBeginCrop,ycoordBeginCrop,
                                                        xcoordEndCrop, ycoordEndCrop),xcoordToPrint + 50, ycoordToPrint + 40, null);
                                        }

                                        //Paint numbers 1-12 on board
                                        int counter = 1;
                                        for (int p = 0; p <= 2; p++)
                                                for (int b = 0; b <= 3; b++)
                                                {
                                                        Font f = new Font("default", Font.BOLD, 14);
                                                        g.setFont(f);
                                                        g.setColor(Color.WHITE);
                                                        g.drawString("" + counter, 200 * b + 115,
                                                                        215 * p + 35);
                                                        counter++;
                                                }

                                        //Paint scores, cards remaining and hints remaining
                                        g.drawString(player1.getName() + "'s Score: " + player1.getScore(), 850, 315);
                                        g.drawString(player2.getName() + "'s Score: " + player2.getScore(), 850, 340);
                                        g.drawString("Number of cards remaining: " + deckOfCards.size() + "", 850, 365);
                                        g.drawString("Number of hints: " + numHints, 850, 390);
                                }
                }

        }

        public void buildDecks() {
                for (int i = 1; i < 4; i++)             // i=number
                        for (int k = 1; k < 4; k++)             // k=color
                                for (int m = 1; m < 4; m++)             //m=shape
                                        for (int p = 1; p < 4; p++)             //p=shade
                                                deckOfCards.add(new Card(i, k, m, p));
                //this is a set that has no moves if you want to check if there is a move available
                /*
                 * deckOfCards.add(new Card(2, 2, 3, 1)); deckOfCards.add(new Card(1, 1,
                 * 2, 3)); deckOfCards.add(new Card(3, 1, 1, 1)); deckOfCards.add(new
                 * Card(3, 3, 3, 2)); deckOfCards.add(new Card(2, 1, 2, 2));
                 * deckOfCards.add(new Card(1, 3, 1, 3)); deckOfCards.add(new Card(2, 1,
                 * 2, 1)); deckOfCards.add(new Card(2, 1, 1, 3)); deckOfCards.add(new
                 * Card(1, 3, 3, 2)); deckOfCards.add(new Card(3, 3, 1, 3));
                 * deckOfCards.add(new Card(2, 3, 3, 1)); deckOfCards.add(new Card(3, 1,
                 * 2, 3));
                 */
        }

        public void drawOriginalBoard() {
                //prints a random board
                for (int i = 0; i < 3; i++)
                        for (int m = 0; m < 4; m++)
                                board[i][m] = deckOfCards.remove((int) (Math.random() * deckOfCards.size()));
                //changes the board if no move exists
                if (!moveAvailable())
                        updateIfNoMove();

        }

        //accessor card method
        public Card getCardFromBoard(int row, int column) {return board[row][column];}

        private void setSupposedSet(String input) {
                supposedSet = input;
                //declares invalid move if the user inputs nothing
                if (supposedSet.length() == 0)
                        updateBoard(0);
                else {
                        //prompts user to reenter if they accidentally put a space at the front (won't work with integer parse)
                        while(supposedSet.charAt(0)==' ')
                                supposedSet = JOptionPane.showInputDialog(null, "Retype your entry without a space in front.");

                        for (int i = 0; i < 3; i++) {
                                //if there is still a space that means there is more than one int to be parsed
                                if (supposedSet.indexOf(" ") != -1) {
                                        possibleSet[i] = Integer.parseInt(supposedSet.substring(0,
                                                        supposedSet.indexOf(" ")));
                                        supposedSet = supposedSet.substring(supposedSet.indexOf(" ") + 1);
                                }
                                //only one int left to be parsed
                                else
                                        possibleSet[i] = Integer.parseInt(supposedSet);
                        }

                        for (int k = 0; k < 3; k++) {
                                //conversion between array numbers and board numbers
                                int row = (possibleSet[k] - 1) / 4;
                                int col = (possibleSet[k] - 1) % 4;
                                //Checks that a user input numbers between 1 and 12
                                if(row>=0 && row<=2 && col>=0 && col<=3)
                                        Set[k] = getCardFromBoard(row, col);
                        }

                        //3 cards have to exist in order for the program to check a valid set
                        if(Set[0]!=null && Set[1]!=null && Set[2]!=null)
                                playGame();
                        else    //this will happen if a player didn't input numbers between 1 and 12
                                updateBoard(0);
                }
        }

        public void playGame() {
                //creates new cards to be checked- can't use Set because of pointers
                Card c1 = new Card(); Card c2 = new Card(); Card c3 = new Card();
                c1 = new Card(Set[0].getNum(), Set[0].getColor(), Set[0].getShape(),Set[0].getShade());
                c2 = new Card(Set[1].getNum(), Set[1].getColor(), Set[1].getShape(),Set[1].getShade());
                c3 = new Card(Set[2].getNum(), Set[2].getColor(), Set[2].getShape(),Set[2].getShade());

                //updates board based on validity of set input
                if (checkValidSet(c1, c2, c3))
                        updateBoard(1);
                else
                        updateBoard(0);

                //swaps out one card for another if there is no possible move but cards remain
                if (!moveAvailable() && deckOfCards.size() != 0) {
                        JOptionPane.showMessageDialog(null,"No sets found, cards are being rearranged.");
                        updateIfNoMove();
                }
                //ends game if there are no possible moves and no cards to swap out
                if (!moveAvailable() && deckOfCards.size() == 0)
                        gameOver();

        }

        public boolean checkValidSet(Card card1, Card card2, Card card3) {
                //invalid set if the user chose any blank spots
                if (card1 == null || card2 == null || card3 == null)
                        return false;
                //invalid set if the user input the same card three times
                if((card1.getNum()==card2.getNum() && card1.getNum()==card3.getNum() && card2.getNum()==card3.getNum()) &&
                                (card1.getColor()==card2.getColor() && card1.getColor()==card3.getColor() && card2.getColor()==card3.getColor()) &&
                                (card1.getShape()==card2.getShape() && card1.getShape()==card3.getShape() && card2.getShape()==card3.getShape()) &&
                                (card1.getShade()==card2.getShade() && card1.getShade()==card3.getShade() && card2.getShade()==card3.getShade()))
                        return false;

                //checks num same of different
                if ((card1.getNum() != card2.getNum()
                                && card2.getNum() != card3.getNum() && card1.getNum() != card3
                                .getNum()) ||(card1.getNum() == card2.getNum() && card2.getNum() == card3.getNum()))

                        //checks color same or different
                        if (((card1.getColor()) != (card2.getColor())&& (card2.getColor()) != (card3.getColor()) && card1
                                        .getColor() != card3.getColor())||((card1.getColor()) == (card2.getColor()) && (card2
                                                        .getColor()) == (card3.getColor())))

                                //checks shape same or different
                                if (((card1.getShape()) != (card2.getShape()) && (card2.getShape()) != (card3.getShape()) && card1
                                                .getShape() != card3.getShape()) ||((card1.getShape()) == (card2.getShape()) && (card2
                                                                .getShape()) == (card3.getShape())))

                                        //checks shade same or different
                                        if ((card1.getShade() != card2.getShade() && card2.getShade() != card3.getShade() && card1
                                                        .getShade() != card3.getShade()) || (card1.getShade() == card2.getShade() && card2
                                                                        .getShade() == card3.getShade()))
                                                return true; //if num, color, shape, shade are all the same or all different

                return false; //if one of the parameters is not met

        }

        public void getHint() {
                boolean setFound = false;

                if (numHints == 0){     //only allows three hints per game
                        JOptionPane.showMessageDialog(null, "Sorry, no more hints available :(");
                        setFound = true;
                }
                if(hintUsed){           //only allows one hint per deal
                        JOptionPane.showMessageDialog(null, "You already used a hint on this board. \n"
                                        + "It includes card "+ possibleHint);
                        setFound = true;
                }

                Card one = new Card(); Card two = new Card(); Card three = new Card();
                while (!setFound) {             //looks for a hint until one is found using moveAvailable algorithm
                        for (int i = 1; i <= 12; i++) {
                                int row1 = (i - 1) / 4;
                                int col1 = (i - 1) % 4;
                                one = getCardFromBoard(row1, col1);

                                for (int m = i + 1; m <= 12; m++) {
                                        int row2 = (m - 1) / 4;
                                        int col2 = (m - 1) % 4;
                                        two = getCardFromBoard(row2, col2);

                                        for (int r = m + 1; r <= 12; r++) {
                                                int row3 = (r - 1) / 4;
                                                int col3 = (r - 1) % 4;
                                                three = getCardFromBoard(row3, col3);

                                                if (checkValidSet(one, two, three)) {
                                                        if(!hintUsed){  //checks that a hint hasn't been used
                                                                possibleHint = (col1 + 1) + (row1 * 4);
                                                                JOptionPane.showMessageDialog(null,
                                                                                "A possible set inculdes card " + possibleHint);
                                                                setFound = true;        //indicates that a hint was found
                                                                numHints--;
                                                                drawArea.repaint();
                                                                hintUsed=true;
                                                        }
                                                }
                                                if (setFound)
                                                        break;          //I KNOW THIS IS BAD CODE
                                        }
                                        if (setFound)
                                                break;
                                }
                                if (setFound)
                                        break;
                        }
                }
        }

        public void updateBoard(int num) {
                if (num == 1) { // valid set was entered
                        JOptionPane.showMessageDialog(null, "That set is VALID!!");
                        //determines who input the set
                        if (player1.getMyTurn())
                                player1.setScore(1);
                        if (player2.getMyTurn())
                                player2.setScore(1);

                        //replaces three cards from valid set with 3 random cards from the deck
                        int row = 0;
                        int col = 0;
                        for (int x = 0; x < 3; x++) {
                                row = (possibleSet[x] - 1) / 4;
                                col = (possibleSet[x] - 1) % 4;
                                if (deckOfCards.size() != 0)
                                        board[row][col] = deckOfCards.remove((int) (Math.random() * deckOfCards.size()));
                                else
                                        board[row][col] = null;
                        }
                }

                if (num == 0) { // invalid set was entered
                        JOptionPane.showMessageDialog(null, "That set is invalid.");
                        //determines who input the set
                        if (player1.getMyTurn())
                                player1.setScore(-1);
                        if (player2.getMyTurn())
                                player2.setScore(-1);
                }

                hintUsed=false;         //allows for another hint because the board has been updated
                updateBoard = true;     //board has been updated
                drawArea.repaint();

        }

        //Swaps out a random card if there is no move availble but extra cards in the deck
        public void updateIfNoMove() {
                int row = (int) (Math.random() * 3);
                int col = (int) (Math.random() * 4);
                board[row][col] = deckOfCards.remove((int) (Math.random() * deckOfCards
                                .size()));
                Card temp = getCardFromBoard(row, col);
                deckOfCards.add(new Card(temp.getNum(), temp.getColor(), temp
                                .getShape(), temp.getShade()));
                drawArea.repaint();
                hintUsed=false;
        }

        public boolean wasHintUsed(){return hintUsed;}

        public boolean moveAvailable() {
                Card C1 = new Card(); Card C2 = new Card(); Card C3 = new Card();
                for (int i = 1; i <= 12; i++) {
                        int row1 = (i - 1) / 4;
                        int col1 = (i - 1) % 4;
                        C1 = getCardFromBoard(row1, col1);

                        for (int m = i + 1; m <= 12; m++) {
                                int row2 = (m - 1) / 4;
                                int col2 = (m - 1) % 4;
                                C2 = getCardFromBoard(row2, col2);

                                for (int r = m + 1; r <= 12; r++) {
                                        int row3 = (r - 1) / 4;
                                        int col3 = (r - 1) % 4;
                                        C3 = getCardFromBoard(row3, col3);

                                        if (checkValidSet(C1, C2, C3))
                                                return true;    //returns true once it hits the first possible set
                                }
                        }
                }
                return false;   //if no sets are found
        }

        public void gameOver() {
                String name = "";

                if (player1.getScore() == player2.getScore())           //tie
                        JOptionPane.showMessageDialog(null, "The game is over.  It was a tie!");
                else {
                        if (player1.getScore() > player2.getScore())    //player1 won
                                name = player1.getName();
                        if (player1.getScore() < player2.getScore())    //player2 won
                                name = player2.getName();
                        JOptionPane.showMessageDialog(null, "The game is over. " + name + " won!!");
                }
                System.exit(0);         //closes when the game ends
        }
}