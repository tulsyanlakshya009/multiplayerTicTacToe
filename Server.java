import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
* This class is responsible to run the server, accept clients.
* It is also responsible for sending the messages to the clients based on what it receives from other clients.
* 
* @author Lakshya Tulsyan 3036087389
* @version 1.0
* @since 9-12-2023
* 
* @param clients stores the client sockets in an arraylist
* @param ServerSocket stores the clients server sokcet
* @param writers stores the print writers for all clients
* @param playernum stores whether the player number has been sent to the clients or not
*/
public class Server 
{
	private ServerSocket serverSocket;
	private ArrayList<Socket> clients = new ArrayList<Socket>();
	private boolean playernum=false;
	private ArrayList<PrintWriter> writers = new ArrayList<PrintWriter>();

	public Server(ServerSocket serverSocket) 
	{
		this.serverSocket = serverSocket;
	}

	public void start() 
	{
		var pool = Executors.newFixedThreadPool(200);
		while (true) 
		{
			try 
			{
				Socket socket = serverSocket.accept();
				if(clients.size()<2)
				{
					clients.add(socket);
					pool.execute(new Handler(socket));
					System.out.println("Connected to client");
				}
				else
				{
					System.out.println("Cannot connect to client");
				}
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	* This is an inner class that is responsible for reading client messages and sending messages to all clients
	* 
	* @author Lakshya Tulsyan 3036087389
	* @version 1.0
	* @since 9-12-2023
	*/
	public class Handler implements Runnable 
	{
		private Socket socket;
		private Scanner input;
		private PrintWriter output;
		
		/**
		 * Constructor
		 * 
		 * @param socket Stores the socket value used to initialize clients print writer and scanner
		 */
		public Handler(Socket socket) 
		{
			this.socket = socket;
		}
		
		/**
		 * It will read msg from clients and convey it to other clients
		 * 
		 */
		@Override
		public void run() 
		{
			try 
			{
				input = new Scanner(socket.getInputStream());
				output = new PrintWriter(socket.getOutputStream(), true);
				writers.add(output);
				while (input.hasNextLine()) 
				{
					if(playernum==false)
					{
						writers.get(0).println("P1");
						writers.get(1).println("P2");
						playernum=true;
					}
					var command = input.nextLine();
					
					if (command.startsWith("READY")) 
					{
						for (PrintWriter writer : writers) 
						{
							writer.println("READY");
						}
					}
					if (command.startsWith("-1")) 
					{
						for (PrintWriter writer : writers) 
						{
							writer.println("-1");
						}
						System.out.println("Client Left");
					}
					if (command.startsWith("-2")) 
					{
						for (PrintWriter writer : writers) 
						{
							writer.println("-2");
						}
					}
					if (command.startsWith("0")) 
					{
						for (PrintWriter writer : writers) 
						{
							writer.println(0);
						}
					}
					
					if (command.startsWith("1")) 
					{
						for (PrintWriter writer : writers) 
						{
							writer.println(1);
						}
					} 
					
					if (command.startsWith("2")) 
					{
						for (PrintWriter writer : writers) 
						{
							writer.println(2);
						}
					}
					
					if (command.startsWith("3")) 
					{
						for (PrintWriter writer : writers) 
						{
							writer.println(3);
						}
					}
					
					if (command.startsWith("4")) 
					{
						for (PrintWriter writer : writers) 
						{
							writer.println(4);
						}
					}
					
					if (command.startsWith("5")) 
					{
						for (PrintWriter writer : writers) 
						{
							writer.println(5);
						}
					}
					
					if (command.startsWith("6")) 
					{
						for (PrintWriter writer : writers) 
						{
							writer.println(6);
						}
					}
					
					if (command.startsWith("7")) 
					{
						for (PrintWriter writer : writers) 
						{
							writer.println(7);
						}
					}
					
					if (command.startsWith("8")) 
					{
						for (PrintWriter writer : writers) 
						{
							writer.println(8);
						}
					}
				}
				
			} 
			
			catch (Exception e) 
			{
				System.out.println(e.getMessage());
			} 
			
			finally 
			{
				// client disconnected
				if (output != null) 
				{
					writers.remove(output);
					for (PrintWriter writer : writers) 
					{
						writer.println(-1);
					}
				}
			}
		}
	}
}
