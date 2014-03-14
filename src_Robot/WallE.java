import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.DifferentialPilot;
import java.io.*;

import lejos.nxt.*;
import lejos.nxt.comm.*;


public class WallE {
	
	//cnostantes qui doivent être identiques à celles de la classe du pc "fonctionMain"
	
			//constante pour savoir s'il y a eu un obstacle
			private static final int PRESENCE_OBSTACLE = 1;		
			private static final int PAS_OBSTACLE = 2;
			
			//constantes pour le choix des parties du code 
			private static final int DEBUT = 1;
			private static final int NEXT_POINT =2 ;
			private static final int NEXT_POINT_IF =21 ;
			//private static final int NEXT_POINT_ELSE =22 ;
			private static final int OBSTACLE =3 ;		
			private static final int OBJ_ATTEINT = 4;
			private static final int OBJ_NON_ATTEIGNABLE = 5;
			private static final int FIN = 6;	
	
			
			
	
	private float distanceRoule = 0;
	
	protected DifferentialPilot robot;	
	protected UltrasonicSensor sonar;	
	protected CompassSensor boussole;
	
	 public WallE() {
		this.robot = new DifferentialPilot(5.4, 11.1, Motor.B, Motor.C, true);
		this.sonar =new UltrasonicSensor(SensorPort.S2);
		this.boussole = new CompassSensor(SensorPort.S1);
	 }
	 
	 
	//************************************************************************************************//
	//*****************fonction qui s'occupe du déplacement du robot**********************************//
	//************************************************************************************************//	 
	 public int deplacerWallE(float distance, float angle){
			
			int obstacle=PAS_OBSTACLE;
			
			this.robot.reset();//reset de tacho count	
			this.robot.setRotateSpeed(20);			
			this.robot.rotate(angle);//rotation pour que le robot se place sur le bon angle avant d'attaquer le point d'arrivée			
			this.robot.setTravelSpeed(10);			

			this.robot.backward(); //moteur monté à l'envers donc backward => robot avance
			
			while(this.robot.getMovement().getDistanceTraveled()>-distance){
						
				if(sonar.getDistance()<=8){
					obstacle=PRESENCE_OBSTACLE;
					this.distanceRoule=this.robot.getMovement().getDistanceTraveled();
					this.robot.stop();
					System.out.println(obstacle+" obstacle ");
					break;
					}
				
		    	obstacle=PAS_OBSTACLE;
				
			}
			this.robot.stop();
			return obstacle;
		}
	 
	 
//*******************************************************************************************//
//********************************|\  /|   /\   | |\  |**************************************//
//********************************| \/ |  /--\  | | \ |**************************************//
//********************************|    | /    \ | |  \|**************************************//
//*******************************************************************************************//
	 
	 
	 public static void main(String[] args) {
		
		float distance = 0;
		float angle = 0 ;
		
		int obstacle=PAS_OBSTACLE;
		int toDo=DEBUT;
		boolean on_continue = true;	
		float direction=0;
		
		WallE robotWallE = new WallE();
		robotWallE.boussole.resetCartesianZero(); //le robot doit être suivant l'axe des x pour mettre le zero
		
		/** * Etablissement de la connection bluetooth **/	 
	    LCD.drawString("waiting connection",0, 0);	    
	    NXTConnection connection = null;	     
	    connection = Bluetooth.waitForConnection();		   
	     
	    DataInputStream dataIn = connection.openDataInputStream();
	    DataOutputStream dataOut = connection.openDataOutputStream() ;
	
	    
	    
	    try {
	    	
	    	while(on_continue==true){
	    		
	    		//on lit ce que le PC nous dit de faire
	    		toDo=dataIn.readInt();
	    		System.out.println("Je dois faire "+ toDo) ;
	    		
	    		switch (toDo) {
	    		//****************DEBUT*******************************//
				case DEBUT:
					//il attend que l'ordi calcule le prochain obj					
					break;
					
				//****************NEXT_POINT**************************//
				case NEXT_POINT:
					int partie_du_if=dataIn.readInt();
					
					if ( partie_du_if==NEXT_POINT_IF){
					//on regarde la direction du robot
					direction = robotWallE.boussole.getDegreesCartesian() ;
			    	//System.out.println("direction : "+ direction) ;
					//Envoi de la direction du robot
			    	dataOut.writeFloat(direction) ;
			    	dataOut.flush() ;
			    	//System.out.println("J'ai envoyé ma direction");
			    	//Reception de la distance à parcourir
			    	distance = dataIn.readFloat();
			    	//System.out.println("J'ai recu la distance");
			    	//Reception de l'angle
			    	angle = dataIn.readFloat(); 
			    	//System.out.println("J'ai recu l'angle");
			    	
			    	//on le fait avancer et on regarde s'il y a un obstacle
			    	obstacle=robotWallE.deplacerWallE(distance, angle) ;
			    	
			    	//Envoi s'il y a un obstacle ou pas
			    	dataOut.writeInt(obstacle) ;
					dataOut.flush() ;
					}
					
					break;
					
					
				//****************OBSTACLE****************************//
				case OBSTACLE:
					//System.out.println("Obstacle! Je fais quoi?");
					//on regarde la direction du robot
					direction = robotWallE.boussole.getDegreesCartesian() ;
			    	//System.out.println("direction : "+ direction) ;
					
					//Envoi de la direction du robot
			    	dataOut.writeFloat(direction) ;
			    	dataOut.flush() ;
			    	//Envoi la distance parcourue depius le début
					dataOut.writeFloat(robotWallE.distanceRoule) ;
					dataOut.flush() ;
					//Reception de la distance à parcourir
					distance = dataIn.readFloat();//distance reçu négative 
			    	//System.out.println("J'ai recu la distance à reculer");
			    	robotWallE.robot.travel(Math.abs(distance));//distance positive pour pouvoir reculer
					break;
					
					
				//****************OBJ_ATTEINT*************************//
				case OBJ_ATTEINT:
					Sound.beepSequenceUp();
					
					/*File Atteint= new File("objAtteint.wav");
					Sound.playSample(Atteint);*/
					//on allume les LEDs
					
					break;
										
					
				//****************OBJ_NON_ATTEIGNABLE*****************//
				case OBJ_NON_ATTEIGNABLE:
					
					Sound.buzz();
					
					break;
							
					
				//***************FIN**********************************//
				case FIN:		
					//on allume les LEDs				
			    	Motor.A.forward();
			    	try {
			    		Thread.sleep(100);
					}catch (InterruptedException e) {
						System.out.println("problème du thread.sleep");
						e.printStackTrace();
					}
			    	Motor.A.stop();
			    	try {
			    		Thread.sleep(50);
					}catch (InterruptedException e) {
						System.out.println("problème du thread.sleep");
						e.printStackTrace();
					}
			    	
			    	Sound.playNote(Sound.XYLOPHONE, 262, 100);
			    	Sound.playNote(Sound.XYLOPHONE, 294, 100);
			    	Sound.playNote(Sound.XYLOPHONE, 330, 100);
			    	Sound.playNote(Sound.XYLOPHONE, 349, 100);
			    	Sound.playNote(Sound.XYLOPHONE, 392, 100);
			    	Sound.playNote(Sound.XYLOPHONE, 440, 100);
			    	Sound.playNote(Sound.XYLOPHONE, 494, 100);
			    	Sound.playNote(Sound.XYLOPHONE, 524, 100);
			    	
			    	//fermeture connexion
			    	dataOut.close() ;
			    	dataIn.close() ;
			    	connection.close() ;
			    	
			    	//c'est fini
					on_continue=true;
					break;
					
					
					
				default:
					break;
				}
	    		
	    		
	    	}   	
	    	
	    
		} catch (IOException e ) {
		      System.out.println(" write error "+e); 
		}
	   
	}
	
}
