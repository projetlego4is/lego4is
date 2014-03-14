package robots;

public class Coordonnees {

	private double x ;
	private double y ;
	
	public float angleAbsolu(float angleVu, float angleBoussole)
	{
		float angleAux ;
		float angleAbsolu = 0 ;
		
		//On met angle_boussole en angle négatif lorsqu'il est supérieur à 180°
		if(angleBoussole > 180 && angleBoussole < 360)
		angleBoussole = angleBoussole - 360 ;
		
		//Cas particuliers
		if (angleBoussole == 0 && angleVu == 0)
			angleAbsolu = 0;
		if(angleVu == 0)
			angleAbsolu = angleBoussole ;
		
		//Cas où les angles sont de signes opposés mais de même orientation (On ajoute leurs valeurs absolues)
		//Valable dans tous les cadrans
	    if(angleBoussole > 0 && angleVu < 0)
			angleAbsolu = angleBoussole + Math.abs(angleVu) ;
		else if(angleBoussole < 0 && angleVu > 0)
		{
		    angleAux = Math.abs(angleBoussole) + angleVu ;
		    angleAbsolu = 360 - Math.abs(angleAux) ;
		}
	    
		// Cas où les angles sont de même signe et de sens d'orientation opposés
	    // Variable selon les cadrans
	    if(angleBoussole < 0 && angleVu < 0 || angleBoussole > 0 && angleVu > 0)
	    	//Si l'angle de la boussole est plus grand sue l'angle vu
			if(Math.abs(angleBoussole) > Math.abs(angleVu))
			{
				angleAux = Math.abs(angleBoussole) - Math.abs(angleVu) ;
				// cadran Nord Est et Nord Ouest
				if(angleBoussole < 0 && angleVu < 0)
					angleAbsolu = 180 - angleAux ;
				// cadran Sud Ouest et Sud Est
				else if(angleBoussole > 0 && angleVu > 0)	
					angleAbsolu = 180 + angleAux ;
			}
	    	//Sinon
			else
			{
				angleAux = Math.abs(angleVu) - Math.abs(angleBoussole) ;
				// cadran Nord Est et Nord Ouest
				if (angleBoussole > 0 && angleVu > 0)
				angleAbsolu = 180 - angleAux ;
				// cadran Sud Ouest et Sude Est
				else if(angleBoussole < 0 && angleVu < 0)
					angleAbsolu = 180 + angleAux ;
			}
		
		return angleAbsolu ;
	}
	
	public void calculCoordonnees(float longueur, float angleVu, float angleBoussole)
	{	
		double angleAbsolu ;		
		angleAbsolu = angleAbsolu(angleVu, angleBoussole) ;
		
		// cadran Nord Est
		if (angleAbsolu>0 && angleAbsolu<90)
		{
			this.x = Math.cos(angleAbsolu)*longueur ;
			this.y = Math.sin(angleAbsolu)*longueur ;
		}
		// cadran Nord Ouest
		else if (angleAbsolu>90 && angleAbsolu<180)
		{
			this.x = -Math.sin(angleAbsolu-90)*longueur ;
			this.y = Math.cos(angleAbsolu-90)*longueur ;
		}
		// cadran Sud Ouest
		else if (angleAbsolu>180 && angleAbsolu<270)
		{
			this.x = -Math.cos(angleAbsolu-180)*longueur ;
			this.y = -Math.sin(angleAbsolu-180)*longueur ;
		}
		// cadran Sud Est
		else if (angleAbsolu>270 && angleAbsolu<360)
		{
			this.x = Math.sin(angleAbsolu-270)*longueur ;
			this.y = -Math.cos(angleAbsolu-270)*longueur ;
		}
	}
	
	public double getX()
	{
		return this.x ;
	}
	
	public double getY()
	{
		return this.y ;
	}

}
