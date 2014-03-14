package robots;
public class Coordonnes {
	
	/**
	 * Gere les coordonnees pour les robots reels
	 */
	private double x ;
	private double y ;
	private double distance;

	public Coordonnes()
	{
		this.x=Double.MAX_VALUE;
		this.y=Double.MAX_VALUE;
		this.distance=Double.MAX_VALUE;
	}

	public double getX()
	{
		return this.x ;
	}

	public double getY()
	{
		return this.y ;
	}
	
	public double getDistance(){
		return this.distance;
	}

	public double angleAbsolu(double angleVu, double angleBoussole)
	{
		double angleAbsolu=0;
		double angleCible=0;
		
		//on basule en 0-360°
		if (angleVu<0) angleCible=360+angleVu;
		else angleCible=angleVu;
		
		//calcul de l'angle absolu
		angleAbsolu=angleVu+angleBoussole+180; //ou angleVu-angleBoussole-180 depend si sens trigo ou antitrigo
		
		//recadrage entre 0-360°
		if (angleAbsolu<0)angleAbsolu+=360;
		angleAbsolu-=(int)(angleAbsolu/360)*360;
		

		return angleAbsolu ;
	}

	public void calculCoordonnees(double signal, double angleVu, double angleBoussole)
	{
		double angleAbsolu=0;
		double longueur=0;
		
		//coefficient de conversion signal longueur a1,b1=>150/100 a2/b2=>100/60 a3,b3=>60/10
		double a1=-0.2501;
		double a2=-0.7948;
		double a3=-0.8807;
		
		
		double b1=45.252;
		double b2=119.58;
		double b3=113.04;
	
  
		angleAbsolu=angleAbsolu(angleVu,angleBoussole);
		if (signal>140) longueur=a1*signal+b1; //Longueur  0 à 10
		else if (signal>120) longueur=a2*signal+b2; //Longueur 10 à 20
		else if (signal>30) longueur=a3*signal+b3; //Longueur 20 à 80
		else if (signal>10) longueur= 110+signal;
		
		
		if (angleAbsolu>360||angleAbsolu<0||signal<10) 
		{
			this.x=10000;
			this.y=10000;
		}
		else
		{
		this.x=Math.cos(angleAbsolu*3.14/180)*longueur;
		this.y=-Math.sin(angleAbsolu*3.14/180)*longueur;
		}
		this.distance=longueur;
	}

}
