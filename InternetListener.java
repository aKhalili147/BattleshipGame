import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

//Listener for the buttons on the board	while playing game
	public class InternetListener implements ActionListener
	{	
		int i,j;
		
		public void actionPerformed(ActionEvent v)
		{						
			
			System.out.println(Battleship.getPlayers(0).getMove());
			if
			(Battleship.getPlayers(0).getMove())			
			{				
				System.out.println("WooHoo");
				Object source = v.getSource();
				outer:						
				for (i=0;i<10;i++)
				{				
					for (j=0;j<10;j++)
					{					
						if (source==Battleship.getPlayers(1).getBboard(i,j))
						{								
							if ((Battleship.getPlayers(1).getBboard(i,j).getBackground()==Color.black)||
								(Battleship.getPlayers(1).getBboard(i,j).getBackground()==Color.orange)||
								(Battleship.getPlayers(1).getBboard(i,j).getBackground()==Color.blue))
							{
								JOptionPane.showMessageDialog(null,"You tri"
								+"ed that spot already.","Wasted Shot",
								JOptionPane.ERROR_MESSAGE);
								Battleship.getClient().fireShot();								
							}
							else
							{
								Battleship.getClient().fireShot(i,j);							
							}
							break outer;						
						}
						else if (source==(Battleship.getPlayers(0).getBboard(i
							,j)))
						{
							JOptionPane.showMessageDialog(null,"You are not suppose"
							+"d to fire on your own board!","Lost Turn",
							JOptionPane.WARNING_MESSAGE);
							Battleship.getClient().fireShot();		
							break outer;							
						}			
					}
				}						
				Player.isStatsOpen();
				Battleship.getPlayers(0).setMove(false);													
			}
			else
			{
				if (!Battleship.getGameOver())
				{
					JOptionPane.showMessageDialog(null,"You cannot not play yet.",
					"Wait",JOptionPane.WARNING_MESSAGE);				
				}
				else
				{
					for (i=0;i<10;i++)
					{				
						for (j=0;j<10;j++)
						{
							Battleship.getPlayers(0).getBboard(i
							,j).setEnabled(false);
							Battleship.getPlayers(0).getBboard(i,j).setEnabled(false);
						}
					}
				}
			}								
		}
	}	