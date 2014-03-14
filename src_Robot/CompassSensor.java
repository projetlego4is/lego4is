import lejos.nxt.Button;
import lejos.nxt.I2CPort;
import lejos.nxt.addon.CompassHTSensor;


public class CompassSensor extends CompassHTSensor {	
	
	/*CONSTRUCTEUR*/
	public CompassSensor (I2CPort port){
		super(port);
	}
	
	
	public static void calibrageBoussole(WallE robotWallE){
		robotWallE.robot.setRotateSpeed(20);
		robotWallE.robot.rotateLeft();
		robotWallE.boussole.startCalibration();
		System.out.println("Appuyer un bouton quand le robot aura fait deux tours");	
		Button.waitForAnyPress();
		robotWallE.robot.stop();
		robotWallE.boussole.stopCalibration();
	}

	//calibrage de la boussole // Attendre deux tours du robot et appuyer sur un bouton sans déplacer le robot
	public static void main(String[] args) {	
		WallE rob= new WallE();
		calibrageBoussole(rob);		
	}

}

