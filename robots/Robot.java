package robots;

import commun.*;

public abstract class Robot
{
	/**
	 * Pere des predateur et proie , concentre les fonctions et attributs communs
	 */
	protected static char id;
	protected Deplacement move;
	protected BluetoothManager com;
	
	public Robot (char id)
	{
		Robot.id=id;
		this.move=new Deplacement();
		this.com = new BluetoothManager(Thread.NORM_PRIORITY, this);
	}
	
	public abstract void onNewMessage(Message msg);
	
}