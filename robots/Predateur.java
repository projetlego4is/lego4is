package robots;
import java.util.ArrayList;

import commun.Message;
import commun.MessageType;
import commun.OrdreData;
import commun.OrdreDeplacement;
import commun.OrdreRole;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.IRSeekerV2;
import lejos.nxt.addon.IRSeekerV2.Mode;

public class Predateur extends Robot
{
	/**
	 * Programme principal gerant les predateur, cad track la proie si demandé (isLeader=true) execute les ordres
	 * recu du superviseur, envoi les donnees demandées, calcul ses coordonnées
	 */
	private boolean isLeader;
	private Tracker track;
	private IRSeekerV2 ir;
	private Coordonnes coord;
	private double distanceMin;
	private Boussole compas;
	private boolean shutdown;
	public Predateur(char id)
	{
		super(id);
		this.distanceMin=Double.MAX_VALUE;
		this.isLeader=false;
		this.shutdown=false;
		this.compas=new Boussole(this.move.getMotor());
		this.coord=new Coordonnes();
		this.ir= new IRSeekerV2(SensorPort.S1,Mode.AC);
		this.track=new Tracker(this.move.getMotor(),this.ir,20);
	}
	
	public boolean getisLeader()
	{
		return this.isLeader;
	}
	
	public void setisLeader(boolean isLeader)
	{
		this.isLeader=isLeader;
	}
	
	public void onNewMessage(Message msg)
	{
		
		int [] sensors_values= this.ir.getSensorValues();
		int i=0;
		int max=0;
		int data=0;
		
		//-----------------------------------
		// traitement de la communication
		if (msg.getTypeMessage()==MessageType.MESSAGE_DEPLACEMENT.getValue())
		{
			LCD.clearDisplay();
			System.out.print("Slaves");
			this.setisLeader(false);
			this.track.stopTracking();
			this.move.deplacer(msg);
		}
		else if(msg.getTypeMessage()==MessageType.MESSAGE_ROLE.getValue())
		{
			if (msg.getOrdre()==OrdreRole.ROLE_IS_LEADER.getValue())
				this.setisLeader(true);
			else if (msg.getOrdre()==OrdreRole.ROLE_SHUTDOWN.getValue()){
				this.com.close();
				this.shutdown=true;
				}
			else if (msg.getOrdre()==OrdreRole.ROLE_IS_SLAVE.getValue())
				this.setisLeader(false);
		}
		else if (msg.getTypeMessage()==MessageType.MESSAGE_DATA.getValue())
		{
			//-----------------------------------
			// calcul de la distance cible robot
			for(i=0;i<sensors_values.length;i++){
				if(sensors_values[i]>=max){
					max=sensors_values[i];
				}
			}
			
			if (msg.getOrdre()==OrdreData.DATA_DISTANCE.getValue())
			{	
				//data=max;
				//this.com.sendMessage(new Message(msg.getReceiver(),msg.getSender(),OrdreData.DATA_DISTANCE,data));
				this.distanceMin=msg.getData();
			}
			
			if (max<10)
			{
				this.com.sendMessage(new Message(msg.getReceiver(),msg.getSender(),OrdreData.DATA_CIBLE_IS_NOT_VISIBLE,data));
			}
			else if (msg.getOrdre()==OrdreData.DATA_ANGLE.getValue())
			{
				data=(int)(this.compas.Getcap());
				this.com.sendMessage(new Message(msg.getReceiver(),msg.getSender(),OrdreData.DATA_ANGLE,data));
			
			}
			else if (msg.getOrdre()==OrdreData.DATA_X.getValue())
			{
				data=(int)(this.coord.getX());
				//attention on ne peux envoyer que un octet de data
				this.com.sendMessage(new Message(msg.getReceiver(),msg.getSender(),OrdreData.DATA_X,data));
				
			}
			else if (msg.getOrdre()==OrdreData.DATA_Y.getValue())
			{
				data=(int)(this.coord.getY());
				//attention on ne peux envoyer que un octet de data
				this.com.sendMessage(new Message(msg.getReceiver(),msg.getSender(),OrdreData.DATA_Y,data));
				
			}
			
		}
	}
	
	public static void main(String[] args) {
		Predateur pred=new Predateur('1');
	
		int i=0;
		int max=0;
		
		pred.compas.Calibrer();
		while (!pred.shutdown)
		{
			try {
			    Thread.currentThread().sleep(10) ;
			} catch (InterruptedException e) {
			}
			
			int [] sensors_values= pred.ir.getSensorValues();
	
		
		
			//--------------------------------
			//reinitialisation
			i=0;
			max=0;
			
			//-----------------------------------
			// calcul de la distance cible robot
			
			
			for(i=0;i<sensors_values.length;i++){
				if(sensors_values[i]>=max){
					max=sensors_values[i];
				}
			}
			
			if(max>10){
					pred.coord.calculCoordonnees(max, pred.ir.getAngle(), pred.compas.Getcap());
				
				if (pred.getisLeader()==true && pred.distanceMin<pred.coord.getDistance())
				{
					pred.track.restartTracking();
					pred.track.tracker();
					LCD.clearDisplay();
					System.out.print("Leader");
					
				} else if(pred.distanceMin>=pred.coord.getDistance()){
					pred.move.getMotor().stop();
				}
			}
		}
		 
	}
}