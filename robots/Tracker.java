package robots;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.addon.IRSeekerV2;
import lejos.nxt.addon.IRSeekerV2.Mode;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.Delay;
import commun.*;

// La classe IR permet de controler un detecteur infrarouge

public class Tracker {
	
	
	
		
	/**
	 * @param args
	 * Permet une traque automatique de la proie  ayant pour but
	 * d'atteindre la cible directement (pas de strategie)
	 */
	//-----------------------------------
	// ATTRIBUTS
	//-----------------------------------
	private DifferentialPilot robot; //parametres des moteurs
	private IRSeekerV2 capteur;		//capteur IR
	private int speed;
	private boolean stopTracking;
	
	
	//------------------------------------
	// CONSTRUCTEUR
	//-----------------------------------
	
	public Tracker(DifferentialPilot pilot,IRSeekerV2 seeker,int vitesse)
	{
		this.robot=pilot;
		this.capteur=seeker;
		this.speed=vitesse;
		this.stopTracking=false;
	

	}
	
	//------------------------------------
	// METHODES
	//-----------------------------------
	
	
	// stop le tracking
	public void stopTracking ()
	{
		this.stopTracking=true;
		this.robot.stop();
	}
	
	//reprend le tracking
	public void restartTracking ()
	{
		this.stopTracking=false;
	}
	
	//change la vitesse des roues (déconseillé si en plein milieu d'une traque)
	public void changeSpeed (int speed)
	{
		this.speed=speed;
	}
	
	
	/**
	 * permet de controler les moteurs du robot de facon a ce qu'il suive 
	 * la balle infrarouge
	 *
	 * ATTENTION: CETTE FONCTION N'EST PAS PERIODIQUE IL FAUT DONC L'INTEGRER
	 * DANS UNE BOUCLE TANT QUE L'ON VEUT SUIVRE LA BALLE
	 *
	 * pour l'arreter il faut ABSOLUMENT passer par stopTracking sinon le robot 
	 *continue d'avancer sur la dernière commande qu'il a recu
	 *on peut donc faire plusieurs autre tâches dans la boucle du programme appelant
	 */
	
	public void tracker()
	{

		int max=0;
		int i;
		double K= 20000;
		int[] sensors_values;
		double rotation_command;
		double entree=0;
		
		//si l'arret n'est pas demander
		if (this.stopTracking!=true){
			
			//on récupere les valeur des capteurs
			sensors_values=capteur.getSensorValues();
			max=0;
			
			//on cherche le max
			for(i=0;i<sensors_values.length;i++){
				if(sensors_values[i]>=max){
					max=sensors_values[i];
				}
			}
			
			//si la balle n'est pas trop loin ou pas trop pret
			if(max>10 && max<130){
				
				//on recupere l'angle percu
				rotation_command=capteur.getAngle();
				
				// on calcul la commande a envoyer aux moteurs
				entree=K/(max*rotation_command);
				
				//met a jour la vitesse moyenne de déplacement 
				robot.setTravelSpeed(20);
				
				// si l'angle percu n'est pas aberrant ou nul (ce qui implique entrée =Nan)
				if (rotation_command!=0 && rotation_command<=120 && rotation_command>=-120)
				{
					
					robot.arcBackward(entree);
					
				}
				
				//si entree=Nan (rayon infini) on va tout droit
				else
					robot.backward();
				
			}else{
				//sinon on arrete le robot
				robot.stop();
			}
		}
		else 
		{
			robot.stop();
		}
	}
	
	
}
