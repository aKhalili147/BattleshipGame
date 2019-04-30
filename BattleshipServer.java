


import java.net.*;
import java.io.*;
import java.util.Vector;

public class BattleshipServer
{	
	private static BattleshipProtocol p;
	private static Socket sockets=null;
		
	public static void main(String[] args) throws IOException 
	{
		boolean gameover=false;
        int i,j, num = 0;//number of players accessing the server
		ServerSocket serverSocket = null;	
		Vector threads=new Vector();		
	   
	   	try {
            serverSocket = new ServerSocket(4444);
        } 
		catch (IOException e) 
		{
            System.err.println("Could not listen on port: 4444.");
            System.exit(1);
        }     
		while (num!=2)
		{
			if (num==0)
			{
				sockets=serverSocket.accept();				
				threads.add(new BattleshipThread(sockets));
				System.out.println(num);
				((Thread)threads.elementAt(num)).start();				
			}
			else
			{
				sockets=serverSocket.accept();				
				threads.add(new BattleshipThread(sockets));
				System.out.println(num);
				((Thread)threads.elementAt(num)).start();					
			}
			//System.out.println(num);
			num++;			
			//if (num%2==0)
				//new BattleshipProtocol(((Thread)threads.elementAt(num-1))
			//,((Thread)threads.elementAt(num)));
		}
		((BattleshipThread)threads.elementAt(num-2)).sendMessage("opponent");
		((BattleshipThread)threads.elementAt(num-2)).sendMessage(((BattleshipThread)threads.elementAt(num-1)).getMyName());
		((BattleshipThread)threads.elementAt(num-1)).sendMessage("opponent");
		((BattleshipThread)threads.elementAt(num-1)).sendMessage(((BattleshipThread)threads.elementAt(num-2)).getMyName());
		p=new BattleshipProtocol(((BattleshipThread)threads.elementAt(num-2))
			,((BattleshipThread)threads.elementAt(num-1)));	
		while (!gameover)
		{
			gameover=p.play();			
		}			
	 }	 
	 
	 public static BattleshipProtocol getProtocol()
	 {
		return p;	 
	 }
}