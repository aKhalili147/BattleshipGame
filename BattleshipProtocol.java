import java.net.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.*;

public class BattleshipProtocol {
	
	private static int play=0,wait=1,
			x=-1,y=-1;//last shot
	private Game[] users=new Game[2];	
	//Timer[] timesup=new Timer(10000,new LostTurn());	
	
	public BattleshipProtocol(BattleshipThread w, BattleshipThread z)
	{
		users[0]=new Game(w);
		users[1]=new Game(z); 				
	}					
							 
    public boolean play () 
	{
		users[play].getThread().playNow();
		users[wait].getThread().sendMessage("getmove");
		while (users[play].getThread().turnOver())
			{}		
		System.out.println("Behave");		
		if (users[wait].getShipsLeft()!=0)
		{
		  if (x!=-1)
			users[wait].getThread().sendResults(x,y);
		  else
			users[wait].getThread().sendResults();
		  users[wait].getThread().sendMessage("keepplaying");
		  users[play].getThread().sendMessage("keepplaying");
		  if (play==0)
		  {
				play=1;
				wait=0;			
		  }
		  else
		  {
				play=0;
				wait=1;					
		  }
			
			x=-1;
			y=-1;
			//catch (Exception x){}
			//wait();
			return false;
		}
		else
		{
		  	users[wait].getThread().sendResults(x,y);
		  	users[wait].getThread().sendMessage("gameover");
			users[play].getThread().sendMessage("gameover");
			return true;
		}
	}
	
	public String results(int x,int y)
	{
		
		this.x=x;
		this.y=y;
		return users[wait].whatHappened(x,y);					
	}
	
	public Game getCurrentUser()
	{
		return users[play];
	}

	public class Game
	{
		private BattleshipThread myThread;
		private Timer myTimer;
		private String[][] c=new String[10][10];		
		private Ship rafts[]=new Ship[5];		
		private int shipsleft;
		private int i,j;
		
		
		public Game(BattleshipThread w)
		{
			myThread=w;
			c=w.sendBoard();
			shipsleft=5;
			mapShips(c);
			myTimer=new Timer(10000,new LostTurn());			
		}
		
		public String[][] getBoard()
		{
			return c;
		}
		
		public class LostTurn implements ActionListener
		{
			public void actionPerformed(ActionEvent v)
			{	
				users[wait].getThread().setTurnOver();
			}				
		}
		
		public String whatHappened(int x, int y)
		{
			if (!this.c[x][y].equals(" "))
			{
				for (i=0;i<5;i++)
				{
					if (this.c[x][y].equals(rafts[i].getName()))
					{
						rafts[i].setHitsLeft();
						break;						
					}
				}
				if (rafts[i].getHitsLeft()==0)
				{  
					shipsleft-=1;
					if (rafts[i].getDirect()==0)
						return ("hit shipsunk "+rafts[i].getName()+" "+rafts[i].getX()+" "+rafts[i].getY()+" "+rafts[i].getDirect()+" "+rafts[i].getEndX());
					else
						return ("hit shipsunk "+rafts[i].getName()+" "+rafts[i].getX()+" "+rafts[i].getY()+" "+rafts[i].getDirect()+" "+rafts[i].getEndY());
				}
				else			
					return ("hit");
			}
			else
				return "miss";			
		}
		
		public int getShipsLeft()
		{
			return this.shipsleft;			
		}
		
		public String whatWasHit(int x, int y)
		{
			return this.c[x][y];			
		}
		
		public BattleshipThread getThread()
		{
			return this.myThread;
		}
		
		private void mapShips(String[][] c)
		{
			int g,i,j,k,//counters
				x=-1,y=-1,//coordinates
				length=0;
			
			for (i=0;i<5;i++)
			{
				g=0;
				switch (i)
				{
					case 0:		length=5;
					break;
					case 1:		length=4;
					break;
					case 2:		length=3;	
					break;
					case 3:		length=3;
					break;
					case 4:		length=2;
					break;							
				}
				good:
				for (j=0;j<10;j++)
				{			
					for (k=0;k<10;k++)
					{
						if (c[j][k].equals(Battleship.getShips(i)))
						{
							if (g==0)
							{
								x=j;
								y=k;								
							}
							g+=1;
							if (g==length)
							{
								if (x==j)
									rafts[i]=new Ship(c[j][k],1,length,x,y
								,j,k);
								else
									rafts[i]=new Ship(c[j][k],0,length,x,y
									,j,k);
								break good;	
							}
						}		
					}
				}
			}		
		}
	}		
}