package robots;
import lejos.nxt.I2CPort;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.CompassHTSensor;
import lejos.robotics.navigation.DifferentialPilot;

public class Boussole extends CompassHTSensor
{	
	
	
	protected float degreeNorth;
	protected float degreeAxeSecondaire;
    DifferentialPilot robot;
	/*CONSTRUCTEUR*/
	public Boussole (DifferentialPilot pilot)
	{
		super(SensorPort.S2);
		
		this.robot = pilot;
		degreeNorth=0;
		degreeAxeSecondaire=0;
	}
	

	/******************************** Permet de trouver le nord via la boussole, l'angle 0 étant la direction du nord  *****************************/
	void Calibrer()
	{
	this.robot.setRotateSpeed(40);
    this.startCalibration();
	this.robot.rotate(720);
	this.robot.setRotateSpeed(0);
	this.stopCalibration();
	this.robot.setRotateSpeed(40);
	}
	
	
	/******************************** Retourne un angle entre 0 et 360 degres, 0 coresspondant au nord *********************************************/
	float Getcap()
	{	
		this.degreeNorth=this.getDegrees();
		return degreeNorth;
	}
	
	/******************************** Permet de definir un second axe, l'axe qui est defini fixe l'angle 0 vers l'avant du robot *******************/
	void FixerUnZero()
	{
		  this.resetCartesianZero();
		  this.degreeAxeSecondaire=this.getDegreesCartesian();
	
	}
	
	
	/******************************** Retourne un angle entre 0 et 360 degres ***********************************************************************/
	float AngleRelatifaZero()
	{
		this.degreeAxeSecondaire=this.getDegreesCartesian();
	    return degreeAxeSecondaire;
	}
}
	