package robots;


import commun.Constantes;
import commun.Message;
import commun.MessageType;
import commun.OrdreData;
import commun.OrdreDeplacement;
import commun.OrdreRole;


public class Proie extends Robot
{
	
	private Boussole compas;
	private boolean shutdown;
	public Proie(char id)
	{
		super(id);
		this.shutdown=false;
		this.compas=new Boussole(this.move.getMotor());
	}
	
	
	public void onNewMessage(Message msg)
	{
		
		//-----------------------------------
		// traitement de la communication
		
		msg.afficher();
	
		if (msg.getTypeMessage()==MessageType.MESSAGE_DEPLACEMENT.getValue())
		{
			this.move.deplacer(msg);
		}
		else if (msg.getTypeMessage()==MessageType.MESSAGE_DATA.getValue())
		{
			if (msg.getOrdre()==OrdreData.DATA_ANGLE.getValue()) 
				this.com.sendMessage(new Message(msg.getReceiver(),msg.getSender(),OrdreData.DATA_ANGLE,(int)this.compas.Getcap()));
		}
		else if (msg.getTypeMessage()==MessageType.MESSAGE_ROLE.getValue())
		{
			if (msg.getOrdre()==OrdreRole.ROLE_CIBLE_IS_CAPTURED.getValue())
			{
				this.move.deplacer(new Message ('a','b',OrdreDeplacement.DEP_STOP,0));
			}
			else if (msg.getOrdre()==OrdreRole.ROLE_SHUTDOWN.getValue()){
				this.com.close();
				this.shutdown=true;
			}
		}
		
	}
	
	/**
	 * Programme principal de la proie
	 */
	public static void main(String[] args) {
		Proie proie=new Proie('1');
	
		
		
		proie.compas.Calibrer();
		
		
		while (!proie.shutdown)
		{
			try {
			    Thread.currentThread().sleep(100) ;
			} catch (InterruptedException e) {
			}
			
			
			
		}
		System.out.println(proie.shutdown);
	}
}