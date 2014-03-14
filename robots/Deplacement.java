package robots;
import lejos.nxt.Motor;
import lejos.robotics.navigation.DifferentialPilot;
import java.util.*;
import commun.*;

public class Deplacement {

	/**
	 * Gere le deplacement du robot 
	 */
	private DifferentialPilot motor;
	private int speed ;
	private int rotateSpeed;
	
	public  Deplacement()
	{
		 this.motor= new DifferentialPilot(5.4, 11.1, Motor.A, Motor.B, true);
		 this.speed=20;
		 this.rotateSpeed=100;
	}
	
	public DifferentialPilot getMotor()
	{
		return this.motor;
	}
	
	public void setSpeed(int speed){
		if( speed<this.motor.getMaxTravelSpeed() && speed>20){
			this.speed=speed;
		}else if(speed<20){
			this.speed=20;
		}else{
			this.speed=(int) this.motor.getMaxTravelSpeed();
		}
	}
	
	/**
	 * Interprete l'ordre donné par un message et l'execute 
	 * @param msg
	 */
	public void deplacer(Message msg)
	{
		char ordre;
		
		this.motor.setTravelSpeed(this.speed);
		this.motor.setRotateSpeed(this.rotateSpeed);
		if (msg.getTypeMessage()==MessageType.MESSAGE_DEPLACEMENT.getValue())
		{
			ordre=msg.getOrdre();
			if (ordre==OrdreDeplacement.DEP_GO_BACKWARD.getValue())
			{
				this.motor.forward();
			}
			else if (ordre==OrdreDeplacement.DEP_GO_FORWARD.getValue())
			{
				this.motor.backward();
			}
			else if (ordre==OrdreDeplacement.DEP_SET_TRAVEL_SPEED.getValue())
			{
				this.setSpeed(msg.getData());
			}
			else if (ordre==OrdreDeplacement.DEP_STOP.getValue())
			{
				this.motor.stop();
			}
			else if (ordre==OrdreDeplacement.DEP_TURN_ANGLE.getValue())
			{
				this.motor.rotate(msg.getData());
			}
			else if (ordre==OrdreDeplacement.DEP_TURN_LEFT.getValue())
			{
				this.motor.rotateLeft();
			}
			else if (ordre==OrdreDeplacement.DEP_TURN_RIGHT.getValue())
			{
				this.motor.rotateRight();
			}
			else if (ordre==OrdreDeplacement.DEP_ARC_BACKWARD.getValue())
			{
				this.motor.arcBackward(msg.getData());
			}
			else if (ordre==OrdreDeplacement.DEP_ARC_FORWARD.getValue())
			{
				this.motor.arcForward(msg.getData());
			}
			
		}
	
	}

}
