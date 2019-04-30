import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.File;
import java.io.*;
import java.util.Vector;
import java.lang.Integer;
import java.text.NumberFormat;	

public class Player
{
	private int hits;
	private int i,j;
	private Integer n;
	private int r,c;//row and column for comp attack	
	private Ship boats[] = new Ship[5];	
	private String user;//user name
	//private JPanel board;//panel to store game board
	private int shipsleft;
	private int shots;// shots taken
	private boolean[][] hitormiss=new boolean[10][10];
	private boolean chit=false;//checks if computer hit ship or not		
	private JButton[][] bboard = new JButton [10][10];
						//gbutton=new JButton [10][10];
	private int[][] mhs=new int[10][10];//used by computer to track miss(0)
	//, hit(1), or sunk(2); default is (3)
	private boolean move;
	private JPanel gboard,myboard;
	private Vector rows=new Vector();
	private Vector cols=new Vector();	
	private Timer timeleft;//
	private String[][] whatship=new String[10][10];//stores name of ships or " "
	private int go=2;//direction for the computer to look for ships
	private int fr,fc;//first hits made by computer					
	private int lastship;//length of the last ship left
	private NumberFormat nf = NumberFormat.getPercentInstance();
	//private Board games
		
	public Player(String name)
	{			
		user=name;
		shipsleft=5;
		lastship=0;	
				
		if
		((user.equals("Computer"))||(user.equals("CPU1"))||(user.equals("CPU2"))||(Battleship.isAutoSet())||(Battleship.isLocal()))
			for (i=0;i<5;i++)
				boats[i]=new Ship(Battleship.getShips(i),0,0,0,0);		
		if((user.equals("Computer"))||(user.equals("CPU1"))||(user.equals("CPU2")))
		{
			for (i=0;i<10;i++)
				for (j=0;j<10;j++)
					mhs[i][j]=3;						
			
			timeleft= new Timer(1000,new CompAttack());
		}
		else
			timeleft= new Timer(10000,new AttackListener());				
		move=false;
		shots=0;
		hits=0;
		for (i=0;i<10;i++)
		{			
			for (j=0;j<10;j++)
			{
				this.bboard[i][j]=new JButton();
				this.bboard[i][j].setBackground(null);				
				hitormiss[i][j]=false;
				this.whatship[i][j]=" ";				
			}
		}			
	}
	
	public void setUser(String m)
	{
		this.user=m;	
	}
	
	//returns player's game board with ap
	public JPanel getMyBoard()
	{
		return this.myboard;	
	}	
	
	//returns player's game board with ap
	public JPanel getGBoard()
	{
		return this.gboard;	
	}	
	
	public void setMyBoard(JPanel r)
	{
		this.myboard=r;		
	}

	public void setGBoard(JPanel r)
	{
		this.gboard=r;		
	}		
		
	public void setBoats(int i, Ship r)
	{
		this.boats[i]=r;		
	}	
	
	/*public void setGames(Board k)
	{
		this.games=k;
	}	
	
	public Board getGames()
	{
		return this.games;
	}*/
	
	public Ship getBoats(int x)
	{
		return this.boats[x];
	}
		
	public void setShots()
	{
		this.shots+=1;	
	}		
		
	public void setHits()
	{
		this.hits+=1;	
	}		
	
	public int getShots()
	{
		return this.shots;	
	}		
	
	public int getHits()
	{
		return this.hits;	
	}		
		
	public String getAcc()
	{
		if (this.getShots()>0)
			return nf.format(((double)(this.getHits())/(double)(this.getShots())));
		else
			return "";
	}		
		
	public Timer getTimer()
	{
		return timeleft;
	}
	
	public JButton getBboard(int i,int j)
	{
		return this.bboard[i][j];	
	}	
	
	public void setBboard(int i,int j, Color k)
	{
		this.bboard[i][j].setBackground(k);	
	}		
		
	public void setMove(boolean x)
	{
		this.move=x;
	}
		
	public boolean getMove()
	{
		return this.move;
	}
		
	//returns user name
	public String getUser()
	{
		return user;				
	}
	
	//checks if Statistics frame is open
	public static void isStatsOpen()
	{
		if (Battleship.getStats().isShowing())
		{	
			Battleship.getStats().removeAll();
			Battleship.getStats().setLayout(new GridLayout(6,3));					
			Battleship.setData(new JLabel(""));
			Battleship.getStats().add(Battleship.getData());
			Battleship.setData(new JLabel("Player 1",SwingConstants.CENTER));
			Battleship.getStats().add(Battleship.getData());
			Battleship.setData(new JLabel("Player 2",SwingConstants.CENTER));
			Battleship.getStats().add(Battleship.getData());				
			Battleship.setData(new JLabel("Names"));
			Battleship.getStats().add(Battleship.getData());
			if (Battleship.getYou() == 0)			
				resetStats(Battleship.getYou(),Battleship.getEnemy());			
			else 
				resetStats(Battleship.getEnemy(),Battleship.getYou());			
			Battleship.getStatistics().getContentPane().add(Battleship.getStats());
			Battleship.getStatistics().pack();
			Battleship.getStatistics().repaint();
		}				
	}	
			
	public static void resetStats(int x,int y)
	{
		Battleship.setData(new JLabel(Battleship.getPlayers(x).getUser(),SwingConstants.CENTER));
		Battleship.getStats().add(Battleship.getData());
		Battleship.setData(new JLabel(Battleship.getPlayers(y).getUser(),SwingConstants.CENTER));
		Battleship.getStats().add(Battleship.getData());
		Battleship.setData(new JLabel("Shots Taken"));
		Battleship.getStats().add(Battleship.getData());
		Battleship.setData(new JLabel(Integer.toString(Battleship.getPlayers(x).getShots()),SwingConstants.CENTER));
		Battleship.getStats().add(Battleship.getData());
		Battleship.setData(new JLabel(Integer.toString(Battleship.getPlayers(y).getShots()),SwingConstants.CENTER));
		Battleship.getStats().add(Battleship.getData());
		Battleship.setData(new JLabel("Hits"));
		Battleship.getStats().add(Battleship.getData());
		Battleship.setData(new JLabel(Integer.toString(Battleship.getPlayers(x).getHits()),SwingConstants.CENTER));
		Battleship.getStats().add(Battleship.getData());
		Battleship.setData(new JLabel(Integer.toString(Battleship.getPlayers(y).getHits()),SwingConstants.CENTER));
		Battleship.getStats().add(Battleship.getData());
		Battleship.setData(new JLabel("Shot Accuracy"));
		Battleship.getStats().add(Battleship.getData());
		Battleship.setData(new JLabel(Battleship.getPlayers(x).getAcc(),SwingConstants.CENTER));
		Battleship.getStats().add(Battleship.getData());
		Battleship.setData(new JLabel(Battleship.getPlayers(y).getAcc(),SwingConstants.CENTER));
		Battleship.getStats().add(Battleship.getData());
		Battleship.setData(new JLabel("Ships Left"));
		Battleship.getStats().add(Battleship.getData());
		Battleship.setData(new JLabel(Integer.toString(Battleship.getPlayers(x).getShipsLeft()),SwingConstants.CENTER));
		Battleship.getStats().add(Battleship.getData());
		Battleship.setData(new JLabel(Integer.toString(Battleship.getPlayers(y).getShipsLeft()),SwingConstants.CENTER));
		Battleship.getStats().add(Battleship.getData());	
	}
	
	public String getWhatShip(int x,int y)
	{
		return this.whatship[x][y];			
	}		
	
	public boolean getChit()
	{
		return this.chit;	
	}
	
	public void setChit(boolean x)
	{
		this.chit=x;	
	}
	
	public void setFC(int x)
	{
		this.fc=x;	
	}
	
	public void setFR(int x)
	{
		this.fr=x;	
	}
	
	public void setC(int x)
	{
		this.c=x;	
	}
	
	public void setR(int x)
	{
		this.r=x;	
	}
	
	public int getGo()
	{
		return this.go;	
	}		
	
	//sets direction for comp to look(2=anywhere,1=horizontal,0=vertical)
	public void setGo(int x)
	{
		this.go=x;	
	}	
	
	//returns column of first hit
	public int getFC()
	{
		return this.fc;	
	}		
	
	//column 
	public int getC()
	{
		return this.c;	
	}	
	
	//returns row of first hit
	public int getFR()
	{
		return this.fr;	
	}		
	
	//row 
	public int getR()
	{
		return this.r;	
	}		
	
	public void setLastShip(int x)
	{
		this.lastship=x;	
	}
	
	public int getLastShip()
	{
		return this.lastship;	
	}
	
	public int getShipsLeft()
	{
		return this.shipsleft;	
	}
	
	public void setShipsLeft()
	{
		this.shipsleft-=1;			
	}	
	
	public void setWhatShip(int x,int y,String u)
	{
		this.whatship[x][y]=u;			
	}	
	
	public void setMHS(int x,int y,int z)
	{
		this.mhs[x][y]=z;			
	}
		
	public int getMHS(int x, int y)
	{
		return this.mhs[x][y];				
	}	
	
	//method that determines if hit ship is sunk or not
	public boolean isSunk(int x, int y)
	{
		int f=0;			
		
		//finds which ship was sunk
		while (!this.boats[f].getName().equals(this.getWhatShip(x,y)))
			f++;
		this.boats[f].setHitsLeft();		
		if (this.boats[f].getHitsLeft()==0)
		{
			Battleship.getPlayers(Battleship.getEnemy()).setShipsLeft();
			if
			((Battleship.getPlayers(Battleship.getYou()).getUser().equals("Computer"))||(Battleship.getPlayers(Battleship.getYou()).getUser().equals("CPU1"))||(Battleship.getPlayers(Battleship.getYou()).getUser().equals("CPU2")))
			{	
				for (int k=0;k<10;k++)
					for (int m=0;m<10;m++)
						if
				(this.boats[f].getName().equals(this.getWhatShip(k
							,m)))
						{
							Battleship.getPlayers(Battleship.getYou()).setMHS(k,m,2);
							this.setBboard(k,m,Color.black);
						}
				Battleship.getPlayers(Battleship.getYou()).setGo(2);				
				Battleship.getPlayers(Battleship.getYou()).setChit(false);
				if ((!this.getUser().equals("CPU1"))
					&&(!this.getUser().equals("CPU2")))
					JOptionPane.showMessageDialog(null,"You just lost your "+
					this.boats[f].getName()+"!","Ship Destroyed",
					JOptionPane.WARNING_MESSAGE);
			}
			else
			{
				JOptionPane.showMessageDialog(null,"You sank the "+
				this.boats[f].getName()+"!","Good Job!",
				JOptionPane.INFORMATION_MESSAGE);
				for (int k=0;k<10;k++)
					for (int m=0;m<10;m++)
						if(this.boats[f].getName().equals(this.getWhatShip(k
							,m)))
						{								
							this.setBboard(k,m,Color.black);
						}					
			}
			return true;
		}
		else
			return false;
	}
	
	//method that determines if hit ship is sunk or not
	public boolean isSunk(int x, int y, String z)
	{
		int f=0;			
		
		while (!z.equals(this.boats[f].getName()))
			f++;
		this.boats[f].setHitsLeft();
		System.out.println(z+":  "+this.boats[f].getHitsLeft());
		if (this.boats[f].getHitsLeft()==0)
		{
			this.setShipsLeft();
			JOptionPane.showMessageDialog(null,"You just lost your "+
			this.boats[f].getName()+"!","Ship Destroyed",
					JOptionPane.WARNING_MESSAGE);
			for (int k=0;k<10;k++)
				for (int m=0;m<10;m++)
					if(z.equals(this.getWhatShip(k,m)))
						this.setBboard(k,m,Color.black);											
			return true;
		}
		else
			return false;
	}

	//sets hitormiss[x][y] to k 				
	public void setHitOrMiss (int x, int y, boolean k)
	{
		this.hitormiss[x][y]=k;
	}
			
	public boolean getHitOrMiss (int x, int y)
	{
		return this.hitormiss[x][y];
	}
			
	//checks if any of the surrounding points are plausible
	public boolean isSurrounded(int x, int y)
	{
		if (this.isPlausible(x+1,y))				
			return false;		
		else if (this.isPlausible(x-1,y))
			return false;				
		else if (this.isPlausible(x,y+1))
			return false;	
		else if (this.isPlausible(x,y-1))
			return false;	
		else 
			return true;
	}
	
	//checks if shot is possible and hasn't been tried before
	public boolean isPlausible(int x, int y)
	{
		if ((isValid(x,y))&&(this.getMHS(x,y)==3))
			return true;
		else
			return false;
	}
	
	//checks if selected position is a plausible location for the remaining	ships
	public boolean rshipsv(int x,int y)
	{
		int u=0;
		int g=0;
					
	if (((isValid(x+1,y))&&((Battleship.getPlayers(Battleship.getYou()).getMHS(x+1,y)==3)||
							(Battleship.getPlayers(Battleship.getYou()).getMHS(x+1,y)==1)))||
			((isValid(x-1,y))&&((Battleship.getPlayers(Battleship.getYou()).getMHS(x-1,y)==3)||
							(Battleship.getPlayers(Battleship.getYou()).getMHS(x-1,y)==1))))		
			u=0;			
		else
			u=5;			
		found:			
		while (u<5)				
		{
			g=0;						
			if (this.boats[u].getHitsLeft()!=0)
			{						
				daloop:
				for (i=(x-(this.boats[u].getLength()));i<(x+(this.boats[u].getLength()));i++)
				{							
					if ((isValid(i,y))&&((Battleship.getPlayers(Battleship.getYou()).getMHS(i,y)==3)||
						(Battleship.getPlayers(Battleship.getYou()).getMHS(i,y)==1)))
					{
						g+=1;
						if (g==(this.boats[u].getLength()))
								
							break daloop;					
					}	
					else	
						g=0;
				}						
				if (g==(this.boats[u].getLength()))
					break found;
				else
				{
					u++;
					if (u==5)
						g=0;
				}											
			}
			else
				u++;				
		}				
		if (u!=5)
		{						
			return true;			
		}			
		else			
			return false;			
	}
		
	//checks if selected position is a plausible location for the remaining	ships
	public boolean rshipsh(int x,int y)
	{
		int u=0;
		int g=0;
					
		if (((isValid(x,y+1))&&((Battleship.getPlayers(Battleship.getYou()).getMHS(x,y+1)==3)||
							(Battleship.getPlayers(Battleship.getYou()).getMHS(x,y+1)==1)))||
			((isValid(x,y-1))&&((Battleship.getPlayers(Battleship.getYou()).getMHS(x,y-1)==3)||
							(Battleship.getPlayers(Battleship.getYou()).getMHS(x,y-1)==1))))		
			u=0;			
		else
			u=5;			
		alright:
		while (u<5)
		{
			g=0;			
			if (this.boats[u].getHitsLeft()!=0) 
			{	
				daloop:
				for (i=(y-this.boats[u].getLength());i<(y+this.boats[u].getLength());i++)
				{												
					if ((isValid(x,i))&&((Battleship.getPlayers(Battleship.getYou()).getMHS(x,i)==3)||
						(Battleship.getPlayers(Battleship.getYou()).getMHS(x,i)==1)))
					{
						g+=1;
						if (g==this.boats[u].getLength())
							break daloop;
					}	
					else
						g=0;
				}						
				if (g==(this.boats[u].getLength()))
					break alright;
				else
				{
					u++;
					if (u==5)
						g=0;
				}										
			}
			else
				u++;				
		}			
		if (u!=5)					
		{						
			return true;			
		}		
		else			
			return false;			
	}		
	
	//checks if point (x,y) is valid		
	public boolean isValid(int x, int y)
	{			
		if ((x<0)||(y<0)||(x>9)||(y>9))
			return false;	
		else
			return true;		
	}

	
	//used by computer to scan area around last hit			
	public void scanArea(int x, int y)
	{
		if (this.getGo()==2)
		{
			if (this.isPlausible(x,y+1))
				{
					if (!Battleship.getPlayers(Battleship.getEnemy()).rshipsh(x,y+1))
					{	
						this.setMHS(x,y+1,0);
						this.scanArea(x,y);
					}								
					else
						this.fireShot(x,y+1,1);					
				}			
			else if (this.isPlausible(x+1,y))
				{
					if (!Battleship.getPlayers(Battleship.getEnemy()).rshipsv(x+1,y))
					{	
						this.setMHS(x+1,y,0);
						this.scanArea(x,y);
					}							
					else
						this.fireShot(x+1,y,0);									
				}
			else if (this.isPlausible(x,y-1))
				{						
					if (!Battleship.getPlayers(Battleship.getEnemy()).rshipsh(x,y-1))
					{	
						this.setMHS(x,y-1,0);
						this.scanArea(x,y);
					}						
					else
						this.fireShot(x,y-1,1);											
				}
			else if (this.isPlausible(x-1,y))
				{						
					if (!Battleship.getPlayers(Battleship.getEnemy()).rshipsv(x-1,y))
					{	
						this.setMHS(x-1,y,0);
						this.scanArea(x,y);
					}						
					else
						this.fireShot(x-1,y,0);					
				}
		}
		else if (this.getGo()==1)//means that ship is horizontal
		{
			if (this.getChit())
			{
				if (this.isPlausible(x,y+1))
					this.fireShot(x,y+1);
				else if (this.isPlausible(x,y-1))						
					this.fireShot(x,y-1);
				else if (this.isPlausible(x,this.getFC()+1))
					this.fireShot(x,this.getFC()+1);
				else if (this.isPlausible(x,this.getFC()-1))
					this.fireShot(x,this.getFC()-1);
				else
				{
					this.setGo(2);
					this.scanArea(this.getFR(),this.getFC());
				}					
			}
			else 
			{
				if (this.isPlausible(x,y+1))
					this.fireShot(x,y+1);
				else if (this.isPlausible(x,y-1))
				{
					this.fireShot(x,y-1);
					if (!this.getChit())
						this.setGo(2);			
				}
				else if (this.isPlausible(x,this.getFC()+1))
				{
					this.fireShot(x,this.getFC()+1);
					if (!this.getChit())
						this.setGo(2);			
				}
				else if (this.isPlausible(x,this.getFC()-1))
				{
					this.fireShot(x,this.getFC()-1);
					if (!this.getChit())
						this.setGo(2);							
				}
				else
				{
					this.setGo(2);
					this.scanArea(this.getFR(),this.getFC());
				}						
			}
		}
		else if (go==0) //means that ship is vertical
		{
			if (this.getChit())
			{
				if (this.isPlausible(x+1,y))
					this.fireShot(x+1,y);					
				else if (this.isPlausible(x-1,y))
					this.fireShot(x-1,y);					
				else if (this.isPlausible(this.getFR()+1,y))						
					this.fireShot(this.getFR()+1,y);					
				else if (this.isPlausible(this.getFR()-1,y))						
					this.fireShot(this.getFR()-1,y);					
				else
				{
					this.setGo(2);
					this.scanArea(this.getFR(),this.getFC());
				}
			}
			else
			{
				if (this.isPlausible(x+1,y))
					this.fireShot(x+1,y);				
				else if (this.isPlausible(x-1,y))
				{
					this.fireShot(x-1,y);	
					if (!this.getChit())
						this.setGo(2);					
				}
				else if (this.isPlausible(this.getFR()+1,y))						
				{
					this.fireShot(this.getFR()+1,y);	
					if (!this.getChit())
						this.setGo(2);								
				}
				else if (this.isPlausible(this.getFR()-1,y))	
				{
					this.fireShot(this.getFR()-1,y);	
					if (!this.getChit())
						this.setGo(2);			
				}
				else
				{
					this.setGo(2);
					this.scanArea(this.getFR(),this.getFC());
				}
			}
		}
	}
	
	private void fireShot(int x, int y, int z)
	{
		this.takeShot(x,y);
		if (this.getChit())
		{
			this.setGo(z);
			this.setR(x);
			this.setC(y);							
		}	
	}
	
	private void fireShot(int x, int y)
	{
		this.takeShot(x,y);
		if (this.getChit())
		{			
			this.setR(x);
			this.setC(y);							
		}	
	}

		
	public void takeShot(int x,int y)
	{				
		this.setShots();
		if (Battleship.getPlayers(Battleship.getEnemy()).getHitOrMiss(x,y))
		{
			this.setHits();
			if (!Battleship.getPlayers(Battleship.getEnemy()).isSunk(x,y))
			{
				Battleship.getPlayers(Battleship.getEnemy()).setBboard(x,y,Color.orange);
				if ((this.getUser().equals("Computer"))||(this.getUser().equals("CPU1"))||(this.getUser().equals("CPU2")))
				{	
					this.setMHS(x,y,1);
					this.setChit(true);
				}										
			}											
		}
		else	
		{	
			Battleship.getPlayers(Battleship.getEnemy()).setBboard(x,y,Color.blue);
			if ((this.getUser().equals("Computer"))||(this.getUser().equals("CPU1"))||(this.getUser().equals("CPU2")))
			{	
				this.setMHS(x,y,0);
				this.setChit(false);
			}
		}					
	}		
	
	public void humanAttack(ActionEvent v)
	{
		if (this.getMove())
		{				
			Object source = v.getSource();
			outer:						
			for (i=0;i<10;i++)
			{				
				for (j=0;j<10;j++)
				{					
					if (source==Battleship.getPlayers(Battleship.getEnemy()).getBboard(i,j))
					{								
						if ((Battleship.getPlayers(Battleship.getEnemy()).getBboard(i,j).getBackground()==Color.black)||
							(Battleship.getPlayers(Battleship.getEnemy()).getBboard(i,j).getBackground()==Color.orange)||
							(Battleship.getPlayers(Battleship.getEnemy()).getBboard(i,j).getBackground()==Color.blue))
						{
							JOptionPane.showMessageDialog(null,"You tri"
							+"ed that spot already.","Wasted Shot",
							JOptionPane.ERROR_MESSAGE);								
						}
						else
							this.takeShot(i,j);								
						break outer;						
					}
					else if (source==this.getBboard(i,j))
					{
						JOptionPane.showMessageDialog(null,"You are not suppose"
						+"d to fire on your own board!","Lost Turn",
						JOptionPane.WARNING_MESSAGE);
						break outer;							
					}						
				}
			}
			
			if ((i==10)&&(j==10))
				JOptionPane.showMessageDialog(null,"You took too long!",
				"Lost Turn",JOptionPane.INFORMATION_MESSAGE);				
			Player.isStatsOpen();
			this.setMove(false);
			this.getTimer().stop();				
			if (Battleship.getPlayers(Battleship.getEnemy()).getShipsLeft()!=0)
			{						
				if (!Battleship.getPlayers(Battleship.getEnemy()).getUser().equals("Computer"))
					Battleship.getPlayers(Battleship.getEnemy()).setMove(true);					
				Battleship.getPlayers(Battleship.getEnemy()).getTimer().start();
				Battleship.flipYou();
			}
			else
			{
				if (Battleship.getPlayers(Battleship.getEnemy()).getUser().equals("Computer"))//change once
					//menu options work
				{
					JOptionPane.showMessageDialog(null,"YOU WON!",
					"It's A Celebration!",JOptionPane.INFORMATION_MESSAGE);
					if (this.getUser().equals("Stupid"))
						JOptionPane.showMessageDialog(null,"Maybe you're no"
						+"t that stupid after all!","",JOptionPane.INFORMATION_MESSAGE);
				}
				else
				{
					JOptionPane.showMessageDialog(null,
					this.getUser()+" won!!!","It's A Celebration"
					+"!",JOptionPane.INFORMATION_MESSAGE);
					if (this.getUser().equals("Stupid"))
						JOptionPane.showMessageDialog(null,"Maybe you're no"
						+"t that stupid after all!","",JOptionPane.INFORMATION_MESSAGE);									
				}						
			}									
		}						
	}
	//Listener for the buttons on the board	while playing game		
	public class CompAttack implements ActionListener
	{	
		public void actionPerformed(ActionEvent v)
		{				
			Battleship.getPlayers(Battleship.getYou()).compattack();			
		}
	}
	
	public void compattack()
	{					
		if (this.getChit())
			this.scanArea(this.getR(),this.getC());					
		else
		{
			if (this.getGo()!=2)
				this.scanArea(this.getFR(),this.getFC());			
			else
			{
				blah:
				for (i=0;i<10;i++)
				{
					for (j=0;j<10;j++)
						if (this.getMHS(i,j)==1)
						{
							if (this.getMHS(this.getFR(),this.getFC())==2)
							{
								if ((this.isPlausible(i+1,j))||(this.isPlausible(i,j+1))
								||(this.isPlausible(i-1,j))||(this.isPlausible(i,j-1)))
								{
									this.scanArea(i,j);
									this.setFR(i);
									this.setFC(j);										
									break blah;
								}
							}
							else 
							{
								this.scanArea(this.getFR(),this.getFC());									
								break blah;																		
							}								
						}
				}
				if (i==10)
				{							
					do
					{									
						for (i=0;i<10;i++)
						{
							for (j=0;j<10;j++)
								if (this.getMHS(i,j)==3)
								{										
									rows.add(new Integer(i));
									break;
								}
						}									
						do
						{
							r=(int)(Math.random()*10);
						}
						while(r>=rows.size());												
						r=((Integer)rows.elementAt(r)).intValue();						
						for (i=0;i<10;i++)
						{
							if (this.getMHS(r,i)==3)
								cols.add(new Integer(i));														
						}											
						do
						{
							c=(int)(Math.random()*10);
						}
						while(c>=cols.size());											
						c=((Integer)cols.elementAt(c)).intValue();										
						if (this.isSurrounded(r,c))							
							this.setMHS(r,c,0);							
						else if ((!Battleship.getPlayers(Battleship.getEnemy()).rshipsh(r,c))
							&&(!Battleship.getPlayers(Battleship.getEnemy()).rshipsv(r,c)))
						{							
							this.setMHS(r,c,0);
						}															
						else if ((Battleship.getPlayers(Battleship.getEnemy()).getShipsLeft()==1)
							&&(this.getLastShip()==0))
						{								
							for (int i=0;i<5;i++)
								if (Battleship.getPlayers(Battleship.getEnemy()).boats[i].getHitsLeft()!=0)
									this.setLastShip(Battleship.getPlayers(Battleship.getEnemy()).boats[i].getLength());								
						}							
						rows.clear();
						cols.clear();
					}
					while(this.getMHS(r,c)!=3);						
					this.takeShot(r,c);
					if (this.getChit())
					{	
						this.setFR(r);	
						this.setFC(c);
					}					
				}
			}
		}
		isStatsOpen();
		this.getTimer().stop();
		if (Battleship.getPlayers(Battleship.getEnemy()).getShipsLeft()>0)
		{
			if ((!Battleship.getPlayers(Battleship.getEnemy()).getUser().equals("CPU1"))
				&&(!this.getUser().equals("CPU2")))
				Battleship.getPlayers(Battleship.getEnemy()).setMove(true);	
			Battleship.getPlayers(Battleship.getEnemy()).getTimer().start();
			Battleship.flipYou();
		}
		else
		{
			if (this.getUser().equals("Computer"))
			{
				JOptionPane.showMessageDialog(null,"You Lost!","Sorry!",
				JOptionPane.INFORMATION_MESSAGE);
				if (Battleship.getPlayers(Battleship.getEnemy()).getUser().equals("Stupid"))
					JOptionPane.showMessageDialog(null,"Stupid!","Sorry!",
					JOptionPane.INFORMATION_MESSAGE);
				for (i=0;i<10;i++)
				{
					for (j=0;j<10;j++)
					{
						if ((!this.getWhatShip(i,j).equals(" "))
							&&(((this.getBboard(i,j
						).getBackground())!=Color.black)&&
									((this.getBboard(i,j
								).getBackground())!=Color.orange)))							
							{
								this.setBboard(i,j,Battleship.getColor());
							}
					}
				}									
			}
			else
				JOptionPane.showMessageDialog(null,this.getUser()+
				" won!!!","It's A Celebration!",JOptionPane.INFORMATION_MESSAGE);			
		}					
	}	
}