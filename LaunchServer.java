import java.io.IOException;
import java.net.ServerSocket;
//import java.util.concurrent.Executors;

/**
* This class is responsible to launch the server.
* 
* @author Lakshya Tulsyan 3036087389
* @version 1.0
* @since 9-12-2023
*/
public class LaunchServer 
{
	/**
	 * This method sends the server socket which is available to the Server class
	 * 
	 * @param args an array of command-line arguments for the application
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException 
	{
		System.out.println("Server is Running...");
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() 
		{
			public void run() 
			{
				System.out.println("Server Stopped.");
			}
		}));
		try (var listener = new ServerSocket(58901)) 
		{
			Server myServer = new Server(listener);
			myServer.start();
		} 
		catch (Exception e) 
		{
			System.out.println(e.getMessage());
		}
	}
}
