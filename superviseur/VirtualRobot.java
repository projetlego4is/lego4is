package superviseur;

import simulateur.Simulateur;
import commun.*;

public class VirtualRobot
{
	/**
	 * Concentre tout l'interface de communication avec les robots
	 * comme le deplacement, les coordonnees et l'arret 
	 * Le Superviseur se sert de cette interface pour communiquer de maniere totalement transparente
	 */
	private BluetoothManager com;
	private String nom;
	private Trajectoire trajectoire;
	private Coordonnees coord;
	private Simulateur sim;
	private boolean stopped;
	private char id;
	private boolean cibleNotVisible;
	private boolean isLeader;
	private int[] pointObjectif;
	private boolean simMode;
	

	public VirtualRobot(String nom, String addr)
	{
		this.nom = nom;
		this.stopped=false;
		this.coord= new Coordonnees();
		this.sim=new Simulateur(this,1);
		this.isLeader=false;
		this.pointObjectif= new int[2];
		this.pointObjectif [0]=0;
		this.pointObjectif [1]=0;
		if(addr!=Constantes.ADR_TEST){
			this.com = new BluetoothManager(this.nom, addr, this);
			this.simMode=false;
			this.cibleNotVisible=true;
			}else{
			this.simMode=true;
			this.cibleNotVisible=false;
			}
		this.trajectoire= new Trajectoire ();
		if (this.nom==Constantes.Bob)this.id=Constantes.ID_BOB;
		else if (this.nom==Constantes.NXT)this.id=Constantes.ID_NXT;
		else if (this.nom==Constantes.Robot1)this.id=Constantes.ID_ROBOT1;
		else if (this.nom==Constantes.Robot9)this.id=Constantes.ID_ROBOT9;
		else if (this.nom==Constantes.Robot10)this.id=Constantes.ID_ROBOT10;
		else if (this.nom==Constantes.Proie)this.id=Constantes.ID_PROIE;
		else this.id='0';
		
	}
	
	public void setObjectif(int x,int y){
		this.pointObjectif[0]=x;
		this.pointObjectif[1]=y;
	}
	
	public int[] getObjectif(){
		return this.pointObjectif;
	}
	
	public String getName(){
		return this.nom;
	}
	
	public void isProie(){
		this.id=Constantes.ID_PROIE;
	}
	
	public boolean getisLeader(){
		return this.isLeader;
	}
	public char getID()
	{
		return this.id;
	}
	
	public void MAJCompass(){
		if(!this.simMode){
			this.com.sendMessage(new Message(Constantes.ID_SUPERVISEUR,this.id,OrdreData.DATA_ANGLE,0));
		}
	}

	/**
	 * 
	 * @param msg
	 * 
	 * Traitement des message recu du robot (enregistrement des donées recues)
	 */
	public void onNewMessage(Message msg)
	{
		if (msg.getTypeMessage() == MessageType.MESSAGE_DATA.getValue()){
			if	(msg.getOrdre() == OrdreData.DATA_X.getValue()){
				this.coord.setX(msg.getData()) ;
				this.cibleNotVisible=false;
			} else
			if	(msg.getOrdre() == OrdreData.DATA_Y.getValue()){
				this.coord.setY(msg.getData()) ;
				this.cibleNotVisible=false;
			} else
			if	(msg.getOrdre() == OrdreData.DATA_ANGLE.getValue()){
				this.coord.setCap(360-msg.getData()) ;
				this.cibleNotVisible=false;
			} else
			if (msg.getOrdre() == OrdreData.DATA_DISTANCE.getValue()){
				this.coord.setDistance(msg.getData()) ;
				this.cibleNotVisible=false;
			}
			else if(msg.getOrdre() == OrdreData.DATA_CIBLE_IS_NOT_VISIBLE.getValue()){
				this.cibleNotVisible=true;
				this.Tourner(45);
			}
		}

	}



	public void setLeader(boolean isLeader)
	{
		if(!this.simMode){
			if (isLeader)
				this.com.sendMessage(new Message(Constantes.ID_SUPERVISEUR, this.id, OrdreRole.ROLE_IS_LEADER));
		}
		this.isLeader=isLeader;
	}

	public void Stop(){
		if(!this.simMode){
			this.com.sendMessage(new Message(Constantes.ID_SUPERVISEUR, this.id, OrdreDeplacement.DEP_STOP, 0));
			System.out.println("Stop "+this.nom);
		}else{
			this.stopped=true;
		}
	}
	
	public void Avancer(boolean enAvant)
	{
		int vitesseSimu=2;
		
		if(!this.simMode){
			if(enAvant)
				this.com.sendMessage(new Message(Constantes.ID_SUPERVISEUR, this.id, OrdreDeplacement.DEP_GO_FORWARD, 0));
			else
				this.com.sendMessage(new Message(Constantes.ID_SUPERVISEUR, this.id, OrdreDeplacement.DEP_GO_BACKWARD, 0));
		}else{
			int Cap=this.coord.getCap();
			if(enAvant){
				this.coord.setX((int) (this.coord.getX()+vitesseSimu*Math.cos(Cap*Math.PI/180)));
				this.coord.setY((int) (this.coord.getY()+vitesseSimu*Math.sin(Cap*Math.PI/180)));
			}else{
				this.coord.setX((int) (this.coord.getX()-vitesseSimu*Math.cos(Cap*Math.PI/180)));
				this.coord.setY((int) (this.coord.getY()-vitesseSimu*Math.sin(Cap*Math.PI/180)));
			}
			this.stopped=false;
		}
	}

	public void TournerInfini(boolean sensHoraire)
	{
		if(!this.simMode){
			if(sensHoraire)
				this.com.sendMessage(new Message(Constantes.ID_SUPERVISEUR, this.id, OrdreDeplacement.DEP_TURN_RIGHT, 0));
			else
				this.com.sendMessage(new Message(Constantes.ID_SUPERVISEUR, this.id, OrdreDeplacement.DEP_TURN_LEFT, 0));
		}else{
			this.stopped=false;
		}
	}

	public void setSpeed(int speed)
	{
		if(!this.simMode){
		this.com.sendMessage(new Message(Constantes.ID_SUPERVISEUR, this.id, OrdreDeplacement.DEP_SET_TRAVEL_SPEED, speed));
		}
	}

	public void Tourner(int angle)
	{
		if(!this.simMode){
		this.com.sendMessage(new Message(Constantes.ID_SUPERVISEUR, this.id, OrdreDeplacement.DEP_TURN_ANGLE, angle));
		} else {
			this.coord.setCap(this.coord.getCap()+angle);
			this.stopped=false;
		}
	}


	public void arcForward(int rayon)
	{
		if(!this.simMode){
		this.com.sendMessage(new Message(Constantes.ID_SUPERVISEUR, this.id, OrdreDeplacement.DEP_ARC_FORWARD, rayon));
		}else{
			this.stopped=false;
		}
	}
	
	public void arcBackward(int rayon)
	{
		if(!this.simMode){
		this.com.sendMessage(new Message(Constantes.ID_SUPERVISEUR, this.id, OrdreDeplacement.DEP_ARC_BACKWARD, rayon));
		}else{
			this.stopped=false;
		}
	}
	public void calcTrajectoire(){
		this.trajectoire.calculArc(this.getCoord(),this.pointObjectif[0], this.pointObjectif[1]);
	}
	
	/**
	 * Envoi la trajectoire courante au robot concerne
	 */
	public void sendTrajectoire()
	{
		if(!this.cibleNotVisible && !this.isLeader && this.getCoord().getDistance()>Strategie.distMin){
			if (this.trajectoire.getOrdreType()==OrdreDeplacement.DEP_ARC_BACKWARD.getValue()){
				if(this.trajectoire.getAngle()>180 && this.simMode){
					this.Tourner(180);
					this.calcTrajectoire();
				}
				else 
					this.arcBackward(this.trajectoire.getRayon());
			}
			else if (this.trajectoire.getOrdreType()==OrdreDeplacement.DEP_TURN_ANGLE.getValue()&& this.simMode)
				{
				this.Tourner(this.trajectoire.getAngle());
				this.Avancer(true);
				}
		}else if (!this.isLeader){
			this.Stop();
		}
		
		if( this.simMode && this.isLeader){
			this.trajectoire.calculAngleDirect(this.coord.getX(), this.coord.getY(), 0, 0, this.coord.getCap());
			this.Tourner(this.trajectoire.getAngle());
		}
		
		if(this.simMode)
			this.stopped=false;
		
	}

	public Trajectoire getTrajectoire()
	{
		return this.trajectoire;
	}

	public Coordonnees getCoord()
	{
		return this.coord;
	}
	
	public void setDistanceMin(int distance){
		if(!this.simMode)
			this.com.sendMessage(new Message(Constantes.ID_SUPERVISEUR,this.id,OrdreData.DATA_DISTANCE,distance));
	}
	
	public void MAJCoord(){
		if(!this.simMode){
			this.com.sendMessage(new Message(Constantes.ID_SUPERVISEUR,this.id,OrdreData.DATA_X,0));
			this.com.sendMessage(new Message(Constantes.ID_SUPERVISEUR,this.id,OrdreData.DATA_Y,0));
			this.com.sendMessage(new Message(Constantes.ID_SUPERVISEUR,this.id,OrdreData.DATA_ANGLE,0));
		}else{
			if(!this.stopped)
				this.sim.calcSimulatedPostion();
		}
	}
	
	public void closeConnection(){
		if(!this.simMode){
			this.com.sendMessage(new Message(Constantes.ID_SUPERVISEUR,this.id,OrdreRole.ROLE_SHUTDOWN));
			this.com.close();
		}
	}
	
}
