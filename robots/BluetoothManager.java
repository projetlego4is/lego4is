package robots;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.bluetooth.RemoteDevice;

import commun.Message;
import commun.OrdreRole;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTCommDevice;
import lejos.nxt.comm.NXTConnection;
import lejos.nxt.comm.USB;


public class BluetoothManager extends Thread
{
	/**
	 * Gere la communication cote robot
	 */
	private DataInputStream dis;
	private DataOutputStream dos;
	private NXTConnection connection;
	private Robot robot;
	private boolean shutdown;
	
	public BluetoothManager(int priority, Robot robot)
	{
		super();
		this.shutdown=false;
		this.setPriority(priority);
		this.robot = robot;
		
	    LCD.drawString("Waiting...",0,0);
	    this.connection = Bluetooth.waitForConnection(); 
	    //this.connection=USB.waitForConnection();
	    LCD.clear();
	    LCD.drawString("Connected",0,0);
	
	    this.dis = this.connection.openDataInputStream();
	    this.dos = this.connection.openDataOutputStream();
	    
	    this.start();
	}
	
	public Message receiveMessage()
	{
		try{
		
	        long n = this.dis.readLong();
	        Message msg= new Message(n);
	        return msg;
	      
		}
		catch(IOException e)
		{
			System.out.println("Erreur de lecture");
			this.close();
			return new Message('a','b',OrdreRole.ROLE_SHUTDOWN);
		}
	}
	
	public void sendMessage(Message msg)
	{
		try{
			 long n= msg.ToLong();
	         dos.writeLong(n);
	         dos.flush(); 
	         msg.afficher();
			}
		catch (IOException ioe) {
       System.out.println("Write Exception");
       this.robot.onNewMessage(new Message('a','b',OrdreRole.ROLE_SHUTDOWN));
       this.close();
     }
		
	}
	
	public void close(){
		try {
			this.shutdown=true;
			this.setPriority(MIN_PRIORITY);
			this.dis.close();
		    this.dos.close();
		    this.connection.close();
		} catch (IOException ioe) {
		    System.out.println("IOException closing connection:");
		    System.out.println(ioe.getMessage());
		}
	}
	
	public void run()
	{
		while(!this.shutdown)
		{
			Message msg = this.receiveMessage();
			try{
				this.setPriority(MAX_PRIORITY);
				this.robot.onNewMessage(msg);
				this.setPriority(NORM_PRIORITY);
			}catch(NullPointerException e){
				System.out.println("Owner is null");
				this.close();
			}
		}
	}
}