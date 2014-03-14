package superviseur;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import commun.Constantes;




public	class Carte extends JPanel {
	
		/**
		 * Carte representant les predateurs et leur trajectoire(au choix avec displayTraj)
		 */
		VirtualRobot[] robots  ;
		private int[] TabX = {-1,-1,-1,-1} ;
		private int[] TabY = {-1,-1,-1,-1} ;
		private boolean proche=false;
		private int clignot=0;
		private boolean isCaptured;
		private boolean displayTraj;
		private boolean displayConflict;
		
		public Carte(VirtualRobot[] robots)
		{
			this.robots=robots;
			this.isCaptured=false;
			this.displayTraj=false;
			this.displayConflict=false;
		}
		
		public void setRobots(VirtualRobot[] robots){
			this.robots=robots;
		}
		
		public void setIsCaptured(boolean captured)
		{
			this.isCaptured=captured;
		}
		
		public void displayTraj(boolean display){
			this.displayTraj=display;
		}
		
		public void displayConflict(boolean display){
			this.displayConflict=display;
		}

		/**
		 * Met a jour les tableaux de coordonnees et lance le repaint de la carte
		 */
		public void MAJ()
		{
			
			int[] tabX = new int[robots.length] ;
			int[] tabY = new int[robots.length] ;
			int[] xCentre = new int[robots.length] ;
			int[] yCentre = new int[robots.length] ;
			int[] rayon =  new int[robots.length] ;
			char[] type = new char[robots.length] ;

			for(int i=0;i<this.robots.length;i++)
			{
				tabX[i] = this.robots[i].getCoord().getX() ;
				tabY[i] = this.robots[i].getCoord().getY() ;
				type[i] = this.robots[i].getTrajectoire().getOrdreType();
				xCentre[i] = this.robots[i].getTrajectoire().getCentre()[0] ;
				yCentre[i] = this.robots[i].getTrajectoire().getCentre()[1] ;
				rayon[i] = this.robots[i].getTrajectoire().getRayon() ;
				if (robots[i].getCoord().getDistance()<=Strategie.distMin*2) this.proche=true;
				else this.proche=false;
 			}

			this.setTabX(tabX) ;
			this.setTabY(tabY) ;
			this.repaint();
			}
		
		/**
		 * appeler lorsqu'on appel repaint
		 */
		public void paintComponent(Graphics g)
		{
		//On dessine le fond
	    Image img;
		try {
			if (this.isCaptured) img = ImageIO.read(new File(Fond.getpath()+"/imagetut/carte3.jpg"));
			else if (!this.proche)img = ImageIO.read(new File(Fond.getpath()+"/imagetut/carte.jpg"));
			else 
				{
				
				if (this.clignot<10) img = ImageIO.read(new File(Fond.getpath()+"/imagetut/carte1.jpg"));
				else img = ImageIO.read(new File(Fond.getpath()+"/imagetut/carte2.jpg"));
				
				if (this.clignot<20)this.clignot++;
				else this.clignot=0;
				}
			this.setPreferredSize(new Dimension(img.getHeight(this),img.getWidth(this)));
			g.drawImage(img, 0,0,this);
		 } catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	   

	    //On dessine les prédateurs par un triangle orienté en fonction de la boussole
	    try {
			Image img1= ImageIO.read(new File(Fond.getpath()+"/imagetut/proie.gif"));
			int xpix=0,ypix=0;
			for (int i = 0 ; i < TabX.length ; i++)
			{
				int cotetrig;
				if(this.robots[i].getisLeader()){
					cotetrig=30;
				}else{
					cotetrig=20;
				}
				
				//changement pour le repere graphique 
				xpix=this.TabX[i]+this.getWidth()/2;
				ypix=-this.TabY[i]+this.getHeight()/2;
				
				//calcul du triangle
				int x[]={(int) (xpix+Math.cos((double)-this.robots[i].getCoord().getCap()*3.14/180)*cotetrig),(int) (xpix+Math.cos((double)((90-this.robots[i].getCoord().getCap())*3.14/180))*cotetrig),(int) (xpix+Math.cos((double)((270-this.robots[i].getCoord().getCap())*3.14/180))*cotetrig)};
				int y[]={(int) (ypix+Math.sin((double)-this.robots[i].getCoord().getCap()*3.14/180)*cotetrig),(int) (ypix+Math.sin((double)((90-this.robots[i].getCoord().getCap())*3.14/180))*cotetrig),(int) (ypix+Math.sin((double)((270-this.robots[i].getCoord().getCap())*3.14/180))*cotetrig)};
				
				//affichage du triangle
				g.setColor(Color.decode("9903401"));
				g.fillPolygon(x,y,3);
				
				g.setColor(Color.decode("10807276"));
	  			Font font= new Font(Font.SERIF,Font.BOLD,24);
	  			g.setFont(font);
	  			String s;
	  			s=String.valueOf(this.robots[i].getID());
	  			g.drawString(s, xpix, ypix);
	  			
	  			//on dessine la trajectoire (arc)
	  			if(this.displayTraj){
	  				if(!this.robots[i].getisLeader()){
		  				if(this.robots[i].getTrajectoire().getDirection()==Constantes.DROITE)
		  					g.drawArc(this.robots[i].getTrajectoire().getCentre()[0]-Math.abs(robots[i].getTrajectoire().getRayon())+this.getWidth()/2, -this.robots[i].getTrajectoire().getCentre()[1]-Math.abs(robots[i].getTrajectoire().getRayon())+this.getHeight()/2, 2*Math.abs(robots[i].getTrajectoire().getRayon()), 2*Math.abs(robots[i].getTrajectoire().getRayon()),(int) (180/Math.PI*Math.atan2(robots[i].getCoord().getY()-robots[i].getTrajectoire().getCentre()[1], robots[i].getCoord().getX()-robots[i].getTrajectoire().getCentre()[0])),-robots[i].getTrajectoire().getAngleParcourir());
		  				else
		  					g.drawArc(this.robots[i].getTrajectoire().getCentre()[0]-Math.abs(robots[i].getTrajectoire().getRayon())+this.getWidth()/2, -this.robots[i].getTrajectoire().getCentre()[1]-Math.abs(robots[i].getTrajectoire().getRayon())+this.getHeight()/2, 2*Math.abs(robots[i].getTrajectoire().getRayon()), 2*Math.abs(robots[i].getTrajectoire().getRayon()),(int) (180/Math.PI*Math.atan2(robots[i].getCoord().getY()-robots[i].getTrajectoire().getCentre()[1], robots[i].getCoord().getX()-robots[i].getTrajectoire().getCentre()[0])),robots[i].getTrajectoire().getAngleParcourir());
		  			}else{
		  				int xligne[]={xpix,this.getWidth()/2};
		  				int yligne[]={ypix,this.getHeight()/2};
		  				g.drawPolyline(xligne, yligne, 2);
		  			}
	  			}
			}
			if(this.displayConflict){
  				
	  				for(int j=0; j<robots.length-1;j++){
	  					if(!this.robots[j].getisLeader()){
		  					for(int k=j+1; k<robots.length;k++){
		  						if(!this.robots[k].getisLeader()){
			  						if(robots [j].getTrajectoire().conflitTrajectoire(robots [j], robots[k])){
			  							xpix=(int) (robots[j].getTrajectoire().getPtConflict()[0]+this.getWidth()/2);
			  							ypix=(int) (-robots[j].getTrajectoire().getPtConflict()[1]+this.getHeight()/2);
			  							g.drawOval(xpix, ypix, 4, 4);
			  							xpix=(int) (robots[j].getTrajectoire().getPtConflict()[2]+this.getWidth()/2);
			  							ypix=(int) (-robots[j].getTrajectoire().getPtConflict()[3]+this.getHeight()/2);
			  							g.drawOval(xpix, ypix, 4, 4);
			  						}
		  						}
		  					}
	  					}
  					}
  			}
	    } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		    
		}

		public void setTabX(int[] tab)
		{
			this.TabX = tab ;
		}

		public void setTabY(int[] tab)
		{
			this.TabY = tab ;
		}

    }


