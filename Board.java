import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
* This class is responsible for initiating the client
* It displays the client board, and performing all the functions of Client.
* This class sends all the functions performed by the client to the server.
* 
* @author Lakshya Tulsyan 
* @version 1.0
* @since 9-12-2023
* 
* @param font to store a font value
* @param player to store the player number
* @param ready to check whether both players are ready or not
* @param turn to check whether its the player's move or not
* @param board[] to store the update of the board in an array
* @param btn_name_status to store the status of whether the name has been submitted or not
* 
*/
public class Board implements ActionListener 
{
	
	Font font= new Font("ARIAL",Font.BOLD,34);
	private int player=1;
	private int ready=0;
	private boolean turn=false;
	String board[]=new String[9];
	
	JFrame frame;
	
	//Main Panel (Border Layout)
	JPanel MainPanel;
	
	//Message Display Panel (Flow Layout)
	JPanel MsgPanel;
	JLabel message;
	
	//Board Grid Panel (Grid Layout)
	JPanel ButtonPanel;
	JButton[] buttons= new JButton[9];
	
	//Name Panel (Flow Layout)
	JPanel NamePanel;
	JTextField name;
	JButton btn_name;
	boolean btn_name_status=false;
	
	//Menu Bar
	JMenuBar menuBar;
	JMenu controlMenu;
	JMenuItem exitMenuItem;
	JMenu helpMenu;
	JMenuItem instructionMenuItem;
	
	//Socket, output writer and input reader of the client 
	private Socket socket;
	private Scanner in;
	private PrintWriter out;
	
	
	/**
	 * This method connects to the server and initiates the gui
	 * 
	 */
	public void start() 
	{
		try 
		{
			this.socket = new Socket("127.0.0.1", 58901);
			this.in = new Scanner(socket.getInputStream());
			this.out = new PrintWriter(socket.getOutputStream(), true);
		} 
		catch (UnknownHostException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		go();
		
		Thread handler = new ClientHandler(socket);
		handler.start();
	}
	
	/**
	 * This method sets the board array used to check for winner to blank spaces.
	 * 
	 */
	public void boardINIT()
	{
		for(int i=0;i<9;i++)
		{
			board[i]="";
		}
	}
	
	/**
	 * This method performs the reset function that is used to reset the turn variable of player 1 to true and player 2 to false. 
	 * It also reinitializes the board array, buttons display, and message text field.
	 * 
	 */
	public void reset()
	{
		if(player==1)
		{
			turn=true;
		}
		if(player==2)
		{
			turn=false;
		}
		for(int i=0;i<9;i++)
		{
			board[i]="";
			buttons[i].setText("");
		}
		message.setText("WELCOME "+name.getText()+" (Player "+player+")");
	}
	
	/**
	 * This method is used to display the JOptionPane when one of the player wins.
	 * 
	 */
	public void win()
	{
		if (JOptionPane.showConfirmDialog(frame, "Congratulations. You Win. Do you want to play again?", "WARNING",JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) 
		{
		    reset();
		} 
		else 
		{
		    System.exit(0);
		    out.println(-1);
		}
	}
	
	/**
	 * This method is used to display the JOptionPane when one of the player loses.
	 * 
	 */
	public void lose()
	{
		if (JOptionPane.showConfirmDialog(frame, "You Lose. Do you want to play again?", "WARNING",JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) 
		{
		    reset();
		} 
		else 
		{
		    System.exit(0);
		    out.println(-1);
		}
	}
	
	/**
	 * This method is used to display the JOptionPane when it is a draw between the players.
	 * 
	 */
	public void draw()
	{
		if (JOptionPane.showConfirmDialog(frame, "Draw. Do you want to play again?", "WARNING",JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) 
		{
		    reset();
		} 
		else 
		{
		    System.exit(0);
		    out.println(-1);
		}
	}
	
	/**
	 * This method is the display method that contains all the components of the GUI.
	 * The Main Panel is a BorderLayout to which all other panels are added.
	 * The Msg Pannel is added to the north
	 * The Board Panel with Grid Layout (3x3) with buttons is added in the centre, with the east and west sides set to zero dimension.
	 * The Name Panel is added at the south.
	 * There is also a menu bar with two options.
	 * 
	 */
	public void go() 
	{
		boardINIT();
		MainPanel=new JPanel();
		
		MsgPanel = new JPanel();
		MsgPanel.setLayout(new GridLayout(1,2));
		message=new JLabel();
		message.setText("Enter your player name...");
		message.setHorizontalAlignment(SwingConstants.LEFT);
		MsgPanel.add(message);
		
		ButtonPanel = new JPanel();	
		ButtonPanel.setLayout(new GridLayout(3,3));
		ButtonPanel.setBackground(Color.black);
		for(int i=0;i<9;i++) 
		{
			buttons[i] = new JButton();
			ButtonPanel.add(buttons[i]);
			buttons[i].setFocusable(false);
			buttons[i].addActionListener(this);
		}

		NamePanel = new JPanel();
		name = new JTextField(15);
		btn_name = new JButton("Submit");
		NamePanel.add(name);
		NamePanel.add(btn_name);
		btn_name.addActionListener(this);
				
		MainPanel.setLayout(new BorderLayout());
		JPanel right=new JPanel();
		JPanel left=new JPanel();
		MainPanel.add(right,"East");
		MainPanel.add(left,"West");
		right.setPreferredSize(new Dimension(0,0));
		left.setPreferredSize(new Dimension(0,0));
		MainPanel.add(MsgPanel,"North");
		MainPanel.add(ButtonPanel,"Center");
		MainPanel.add(NamePanel,"South");
		
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(MainPanel);
		frame.setTitle("Tic-Tac-Toe");
		frame.setSize(300,300);
		
		menuBar = new JMenuBar();
        controlMenu = new JMenu("Control");
        exitMenuItem = new JMenuItem("Quit");
        helpMenu = new JMenu("Help");
        instructionMenuItem = new JMenuItem("Instruction");
        controlMenu.add(exitMenuItem);
        helpMenu.add(instructionMenuItem);
        menuBar.add(controlMenu);
        menuBar.add(helpMenu);
        instructionMenuItem.addActionListener(this);
        exitMenuItem.addActionListener(this);
        
        	
        frame.setJMenuBar(menuBar);
        
        frame.setVisible(true);    
	}
	
	/**
	 * This method updates the other players move in this players board, after receiveing a message from the server.
	 * 
	 * @param pos it is the position received by the client through the server so that it can update the board accordingly
	 */
	private void update(int pos)
	{
		if(board[pos]=="")
		{
			if(player==1)
			{
				board[pos]="O";
				buttons[pos].setText(board[pos]);
				buttons[pos].setFont(new Font("ARIAL",Font.BOLD,34));	
				buttons[pos].setForeground(Color.green);
			}
			else if(player==2)
			{
				board[pos]="X";
				buttons[pos].setText(board[pos]);
				buttons[pos].setFont(new Font("ARIAL",Font.BOLD,34));	
				buttons[pos].setForeground(Color.red);
			}
			turn=true;
			message.setText("Your opponent has moved, now is your turn");
			check();
		}
	}
	
	/**
	 * This method checks for a win, loss, or tie 
	 * It is called after every moved performed by this player, and every move of the other player(when the board is updated).
	 * 
	 */
	public void check()
	{
		int count=0;
		for(int i=0;i<9;i++)
		{
			if(board[i]!="") 
			{
				count++;
			}
		}
		if(count!=9)
		{
			if(((board[0]==board[1])&&(board[1]==board[2])&&(board[2]=="X"))||
			  ((board[3]==board[4])&&(board[4]==board[5])&&(board[5]=="X"))||
			  ((board[6]==board[7])&&(board[7]==board[8])&&(board[8]=="X"))||
			  ((board[0]==board[3])&&(board[3]==board[6])&&(board[6]=="X"))||
			  ((board[1]==board[4])&&(board[4]==board[7])&&(board[7]=="X"))||
			  ((board[2]==board[5])&&(board[5]==board[8])&&(board[8]=="X"))||
			  ((board[0]==board[4])&&(board[4]==board[8])&&(board[8]=="X"))||
			  ((board[2]==board[4])&&(board[4]==board[6])&&(board[6]=="X")))
			{
				if(player==1)
				{
					win();
				}
				else
				{
					lose();
				}
			}
			if(((board[0]==board[1])&&(board[1]==board[2])&&(board[2]=="O"))||
			  ((board[3]==board[4])&&(board[4]==board[5])&&(board[5]=="O"))||
			  ((board[6]==board[7])&&(board[7]==board[8])&&(board[8]=="O"))||
			  ((board[0]==board[3])&&(board[3]==board[6])&&(board[6]=="O"))||
			  ((board[1]==board[4])&&(board[4]==board[7])&&(board[7]=="O"))||
			  ((board[2]==board[5])&&(board[5]==board[8])&&(board[8]=="O"))||
			  ((board[0]==board[4])&&(board[4]==board[8])&&(board[8]=="O"))||
			  ((board[2]==board[4])&&(board[4]==board[6])&&(board[6]=="O")))
			{
				if(player==2)
				{
					win();
				}
				else
				{
					lose();
				} 
			}
		}
		else
		{
			draw();
		}
	}
	
	/**
	 * This method displays the JOptionPane if the other player leaves the game.
	 * 
	 */
	public void end()
	{
		JOptionPane.showMessageDialog(frame, "Game Ends. One of the Players Left");
		System.exit(0);
	}
	
	/**
	 * This is the overload function which performs certain actions depending on the buttons being pressed.
	 * If the Quit option is pressed the window is closed and game exited.
	 * If the Instructions option is pressed a JOptionPane with the Instructions pops up.
	 * If the submit button is pressed the players name is submitted and subsequent message is sent to the server.
	 * If a board button is pressed and the move is valid the players move is made, and the position of the button is sent to the server.
	 * 
	 */
	public void actionPerformed(ActionEvent e) 
    {
        if (e.getActionCommand().equals("Quit")) 
        {
            System.exit(0);
            out.println(-1);
        }  
        if (e.getActionCommand().equals("Instruction")) 
        {
        	JOptionPane.showMessageDialog(frame,"Some information about the game:\n"
        			+ "\n"
        			+ "Criteria for a valid move:\n"
        			+ "\n"
        			+ "-The move is not occupied by any mark.\n"
        			+ "\n"
        			+ "-The move is made in the player's turn.\n"
        			+ "\n"
        			+ "-The move is made within the 3 x 3 board.\n"
        			+ "\n"
        			+ "The game would continue and switch among the opposite player until it reaches either one of the following conditions:\n"
        			+ "\n"
        			+ "-Player 1 wins.\n"
        			+ "\n"
        			+ "-Player 2 wins.\n"
        			+ "\n"
        			+ "-Draw");
        }  
        
        if(e.getSource()==btn_name)
        {
        	if((!name.getText().isEmpty())&&(btn_name_status==false))
        	{
	        	String n=name.getText();
	        	frame.setTitle("Tic Tac Toe - Player: "+n);
	        	message.setText("WELCOME "+n);
	        	name.setEditable(false);
	        	btn_name.setEnabled(false);
	        	btn_name_status=true;
	        	out.println("READY");
        	}
        }
        
        if((btn_name_status==true)&&(turn==true)&&(ready==2))
        {
	        for(int i=0;i<9;i++) 
	        {
				if((e.getSource()==buttons[i])&&(board[i]=="")) 
				{
					String s=""+i;
					if(player==1) 
					{
						buttons[i].setText("X");
						buttons[i].setFont(font);	
						buttons[i].setForeground(Color.red);
						board[i]="X";
						out.println(s);
					}
					else if(player==2)
					{
						buttons[i].setText("O");
						buttons[i].setFont(font);	
						buttons[i].setForeground(Color.green);
						buttons[i].setText("O");
						board[i]="O";
						out.println(s);
					}
					turn=false;
					message.setText("Valid Move, wait for your opponent");
					check();
				}	
	        } 
        }
    }
	
	/**
	* This is the inner class responsible for multi-threading and reading from the server
	* 
	* @author Lakshya Tulsyan 3036087389
	* @version 1.0
	* @since 9-12-2023
	* 
	*/
	class ClientHandler extends Thread 
	{
		private Socket socket;
		
		/**
		 * Constructor
		 * 
		 * @param socket to store the Socket of the Client.
		 */
		public ClientHandler(Socket socket) 
		{
			this.socket = socket;
		}

		@Override
		public void run() 
		{
			try 
			{
				readFromServer();
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}

		public void readFromServer() throws Exception 
		{
			try 
			{
				while (in.hasNextLine()) 
				{
					var command = in.nextLine();
					
					if(command.startsWith("READY"))
					{
						ready++;
						if(ready==2)
						{
							String s="WELCOME "+name.getText()+" (Player "+player+")";
							message.setText(s);
						}
					}
					if(command.startsWith("P1"))
					{
						player=1;
						turn=true;
					}
					if(command.startsWith("P2"))
					{
						player=2;
						turn=false;
					}
					if(command.startsWith("P2"))
					{
						player=2;
						turn=false;
					}
					if(command.startsWith("-1"))
					{
						end();
					}			
					if(command.startsWith("-2"))
					{
						boardINIT();
					}
					if(command.startsWith("0"))
					{
						update(0);
					}
					if(command.startsWith("1"))
					{
						update(1);
					}
					if(command.startsWith("2"))
					{
						update(2);
					}
					if(command.startsWith("3"))
					{
						update(3);
					}
					if(command.startsWith("4"))
					{
						update(4);
					}
					if(command.startsWith("5"))
					{
						update(5);
					}
					if(command.startsWith("6"))
					{
						update(6);
					}
					if(command.startsWith("7"))
					{
						update(7);
					}
					if(command.startsWith("8"))
					{
						update(8);
					}
				}
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			} 
			
			finally 
			{
				socket.close();
			}
		}
	}
}

