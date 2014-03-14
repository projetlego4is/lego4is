package superviseur;


import simulateur.Simulateur;
import lejos.nxt.Button;
import commun.Constantes;
import commun.Message;


public class Superviseur {
	
	/**
	 * Gere l'intelligence et l'IHM sur l'ordinateur , il collecte les donnees des robots
	 * en tire les trajectoire, les envoi, et gere l'IHM
	 */

	private GUI gui;
	private VirtualRobot [] robots;
	private boolean endOfGame;
	private boolean simMode;
	
	public Superviseur ()
	{

		this.robots=new VirtualRobot[0];
		this.endOfGame=false;
		this.gui= new GUI(this);
		this.simMode=false;
	}
	
	/**
	 * 
	 * @param robot
	 * Ajoute un robot a la liste de robot
	 */
	public void addVirtualRobot(String name,String addr,boolean isPred)
	{
		if (this.simMode) 
			addr=Constantes.ADR_TEST;
		
		VirtualRobot robot= new VirtualRobot(name,addr);
		
		if(!isPred) robot.isProie();
		VirtualRobot [] aux= new VirtualRobot [this.robots.length+1];
		for (int i =0; i<this.robots.length;i++) aux[i]=this.robots[i];
		aux[this.robots.length]=robot;
		this.robots=aux;
	}
	
	public VirtualRobot[] getRobots()
	{
		return this.robots;
	}
	
	public VirtualRobot getProie()
	{
		VirtualRobot result=null;
		for (int i=0;i<this.robots.length;i++)
		{
			if (robots[i].getID()==Constantes.ID_PROIE) result=robots[i];
		}
		return result;
	}
	
	public VirtualRobot getPredateur(char id)
	{
		VirtualRobot result=null;
		for (int i=0;i<this.robots.length;i++)
		{
			if (robots[i].getID()==id) result=robots[i];
		}
		return result;
	}
	
	public boolean getSimMode(){
		return this.simMode;
	}
	public void endOfGame()
	{
		this.endOfGame=true;
		this.gui.dispose();
	}
	
	public VirtualRobot[] getPredateurs()
	{
		VirtualRobot[] result= new VirtualRobot[0];
		if(this.robots.length-1>=0){
			result=new VirtualRobot [this.robots.length-1];
		}
		int j=-1;
		for (int i=0;i<this.robots.length;i++)
		{
			if (robots[i].getID()!=Constantes.ID_PROIE) {
				j++;
				result[j]=robots[i];
			}
			
		}
		return result;
		
	}
	
	public boolean isCaptured()
	{
		boolean isCaptured=true;
		for (int j=0; j<this.getPredateurs().length;j++)
		{
			
			if(this.getPredateurs()[j].getCoord().getDistance()>Strategie.distMin) isCaptured=false;
				
		}
		return isCaptured;
	}
	
	public void MAJRobots(){
		for(int j=0; j<this.robots.length;j++)
		{
			this.robots[j].MAJCoord();
		}
	}
	
	public void stopRobots(){
		for(int j=0; j<this.robots.length;j++)
		{   
			this.robots[j].Stop();
		}
	}
	
	public void setInitialPostion(int mode, int x0, int y0, int cap0){
		//initialisation arbitraires des positions et orientations
		if(this.simMode){
			switch(mode){
			case Constantes.MOD_CIRC:	
				for(int j=0;j<this.getPredateurs().length;j++)
				{
					this.getPredateurs()[j].getCoord().setX((int) ((Math.sqrt(x0*x0+y0*y0))*Math.cos(Trajectoire.angle(x0, y0)*Math.PI/180+j*2*3.14/this.getPredateurs().length)));
					this.getPredateurs()[j].getCoord().setY((int) ((Math.sqrt(x0*x0+y0*y0))*Math.sin(Trajectoire.angle(x0, y0)*Math.PI/180+j*2*3.14/this.getPredateurs().length)));
					this.getPredateurs()[j].getCoord().setCap((int) (cap0+360*j/this.getPredateurs().length));
					this.getPredateurs()[j].calcTrajectoire();
				}
				break;
			case Constantes.MOD_SAME_X:
				for(int j=0;j<this.getPredateurs().length;j++)
				{
					this.getPredateurs()[j].getCoord().setX((int) (x0));
					this.getPredateurs()[j].getCoord().setY((int) (y0+Math.pow(-1, j)*50*j));
					this.getPredateurs()[j].getCoord().setCap((int) (cap0+360*j/this.getPredateurs().length));
					this.getPredateurs()[j].calcTrajectoire();
				}
				break;
			case Constantes.MOD_SAME_Y:
				for(int j=0;j<this.getPredateurs().length;j++)
				{
					this.getPredateurs()[j].getCoord().setX((int) (x0+Math.pow(-1, j)*50*j));
					this.getPredateurs()[j].getCoord().setY((int) (y0));
					this.getPredateurs()[j].getCoord().setCap((int) (cap0+360*j/this.getPredateurs().length));
					this.getPredateurs()[j].calcTrajectoire();
				}
				break;
				
				default: break;
			}
		}
	}
	
	public static void main(String[] args)
	{
		  Superviseur s = new Superviseur();
		  boolean isCaptured=false;
		  int i=0;
		  
		  //mode simulation
		  s.simMode=true;
		  
		  //attente du choix des robots
		  while(!s.gui.getLancer()&&!s.endOfGame)
		  {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		  }
		  
		  	
			s.setInitialPostion(Constantes.MOD_CIRC, 200, -100, 30);
			Strategie strat= new Strategie (s.getPredateurs(),s.simMode);
			for(int j=0; j<s.getPredateurs().length;j++){
				s.getPredateurs()[j].setDistanceMin(Strategie.distMin);
			}
		  i=0;
		  //debut du jeu
		  while(!s.endOfGame)
		  {
			i++;
			try {
				
				//calcul des trajectoires et envoi
				if (s.isCaptured()){
					s.gui.cibleCaptured();
					s.stopRobots();
					}
				else
				{
					s.MAJRobots();
					strat.calculerStrategie();
					if(i%4==0){
						for(int j=0;j<s.getPredateurs().length;j++)
						{	
							s.getPredateurs()[j].calcTrajectoire();
							s.getPredateurs()[j].sendTrajectoire();
						}
					}
					//affichage des trajectoires et points de conflit
					 s.gui.getCarte().displayTraj(true);
				     s.gui.getCarte().displayConflict(true);
				}
				
				//mise a jour de la carte
				s.gui.getCarte().MAJ();
				if(!s.simMode)
					Thread.sleep(500);
				else
					Thread.sleep(50);
				System.out.flush();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  } 
		  
		  //fin des communication et du jeu
		 
		  for(int k=0; k<s.getRobots().length;k++){
			  s.getRobots()[k].closeConnection();
		  }
		  
		  System.out.println("fin");
	}
	

}
