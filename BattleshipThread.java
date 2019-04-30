import java.net.*;
import java.io.*;

public class BattleshipThread extends Thread {
    private Socket whitesox = null;			
	PrintWriter out;
	BufferedReader in;
	String letter,results;
	String me;
	String[][] pboard=new String [10][10];
			 
	int i,j,//counter
		x,y;//coordinate
	boolean gameover=false, myturn=false;
	
	public BattleshipThread(Socket socket) throws IOException{
		super("BattleshipThread");
		this.whitesox = socket;
		this.out = new PrintWriter(socket.getOutputStream(), true);
		this.in = new BufferedReader(new InputStreamReader(
				    socket.getInputStream()));
		me=in.readLine();
		for (i=0;i<10;i++)
			for (j=0;j<10;j++)
			{
				pboard[i][j]=in.readLine();
				System.out.println(pboard[i][j]);
			}					
	}

    public void run()
	{	
		while (!gameover)
		{	
			while (!this.myturn)
			{ }				
			this.out.println("play");
			try
			{				
				while ((letter=in.readLine())==null)
				{ }
				if (!myturn)
					letter="wastedturn";
			  	if (!letter.equals("wastedturn"))
				{				  
					x=Integer.parseInt(letter);
					y=Integer.parseInt(in.readLine());
					System.out.println("shot received: "+x+","+y);
					results=BattleshipServer.getProtocol().results(x,y);
					this.out.println("results");
					System.out.println("results:  "+results);
					this.out.println(results);
					System.out.println("results sent");
				}				 
			}
			catch(IOException e){}			
			myturn=false;			
		}
		/*
		while (!gameover)
		{
			
			
		}*/		
	}	
	
	public String[][] sendBoard()
	{
		return pboard;
	}
		
	public void getShot()
	{
		try
			{
				x=Integer.parseInt(in.readLine());
				y=Integer.parseInt(in.readLine());
			}
			catch(IOException e){}
			//notifyAll();
			out.println(BattleshipServer.getProtocol().results(x,y));
	}
		
	
	public void sendResults()  
	{
	  	this.out.println("results");
		out.println("lostturn");		
	}
	
	public void sendResults(int x, int y)  
	{
		this.out.println("results");
		out.println("theirshot "+x+" "+y);		
	}
		
	public String getMyName()
	{
		return me;	
	}
		
	public void playNow()
	{
		this.myturn=true;	
	}

	//notifies player that their turn is over;	
	public boolean turnOver()
	{
		return this.myturn;	
	}
	
	//notifies player that their turn is over;	
	public void setTurnOver()
	{
		this.myturn=false;
		out.println("lostturn");
	}
	
	public void sendMessage(String x)
	{
		if (x.equals("gameover"))
		  gameover=true;
		out.println(x);	
	}
		
		
	    /*out.close();
	    in.close();
	    whitesox.close();

		} catch (IOException e) {
			e.printStackTrace();
	}*/
	/*try {	
			
		/*while ()
		{
				
			
		}*/
		
		
	    /*out.close();
	    in.close();
	    whitesox.close();

		} catch (IOException e) {
			e.printStackTrace();
	}*/
    
}