import javax.swing.SwingUtilities;

/**
* This class is responsible to run the client
* 
* @author Lakshya Tulsyan 3036087389
* @version 1.0
* @since 9-12-2023
*/
public class LaunchClient 
{
	/**
	 * This runs a new board for the client.
	 * 
	 * @param args an array of command-line arguments for the application
	 */
	public static void main(String[] args) 
	{	
		SwingUtilities.invokeLater(new Runnable() 
		{
			@Override
			public void run() 
			{
				Board b = new Board();
				try 
				{
					b.start();
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		});
	}
}
