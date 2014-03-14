package superviseur;

import commun.Constantes;
import commun.OrdreDeplacement;

public class Trajectoire
{
	/**
	 * Horrible classe qui contient toutes les fonctions mathématique
	 * pour calculer les trajectoires de robots et les stocker
	 */
	private char OrdreType;
	private double angleAParcourir;
	private double distance;
	private double rayon;
	private double[] centre;
	private char direction;
	private double [] ptConflit;
	
	public Trajectoire ()
	{
		this.centre= new double [2];
		this.centre[0]=0;
		this.centre[1]=0;
		this.rayon=Integer.MAX_VALUE;
		this.distance=0;
		this.angleAParcourir=0;
		this.direction=Constantes.DROITE;
	}
	
	/**
	 * 
	 * @param robot1
	 * @param robot2
	 * @return true:conflit
	 * 
	 * Verifie si les trajectoires d'arc ne sont pas conflictuelles
	 * ATTENTION: les trajectoires doivent avoir ete calculer avec la methode calcArc avant d'appeler
	 * cette methode
	 */
	
	//TODO A debogger
	public boolean conflitTrajectoire(VirtualRobot robot1, VirtualRobot robot2)
	{
		boolean result;
		//on calcul les points d'intersections si ils existent
		double [] intersections=this.pointIntersection(robot1.getTrajectoire().getCentre()[0],robot1.getTrajectoire().getCentre()[1], Math.abs(robot1.getTrajectoire().getRayon()),robot2.getTrajectoire().getCentre()[0], robot2.getTrajectoire().getCentre()[1],Math.abs( robot2.getTrajectoire().getRayon()));
		switch(intersections.length){
			case 0:
				//si pas de point d'intersection c cool!
				result=false;
				break;
			case 4: 
				//pb si au moins un des pts d'intersections peut etre attend par les deux robot (cad appartient a l'arc de cercle des trajectoire)
				for(int j=0; j<4; j+=2){
					int angint0=(int) this.angleAParcourir(robot1.getCoord().getX(),robot1.getCoord().getY(),(int)(intersections[j]),(int)(intersections[j+1]),robot1.getTrajectoire().getCentre()[0],robot1.getTrajectoire().getCentre()[1],robot1.getCoord().getCap());
					int angint1=(int) this.angleAParcourir(robot2.getCoord().getX(),robot2.getCoord().getY(),(int)(intersections[j]),(int)(intersections[j+1]),robot2.getTrajectoire().getCentre()[0],robot2.getTrajectoire().getCentre()[1],robot2.getCoord().getCap());
					
					if (angint0>robot1.getTrajectoire().getAngle()||angint1>robot2.getTrajectoire().getAngle()){
						this.ptConflit[0]=Double.MAX_VALUE;
						this.ptConflit[1]=Double.MAX_VALUE;
						if(j==4){
							result=false;
						}
					}
				}
				result=true;
				break;
			default: 
				result=true;
		}
		return result;
	}
	
	/**
	 * calcul les points d'intersection entre deux cercles et renvoi un tableau (x0,y0,x1,y1) si ceux ci
	 * existent sinon on renvoi un tableu de taille nulle (pour le contenu c que des math!!)
	 */
	public double[] pointIntersection(double x0,double y0,double R0, double x1,double y1, double R1)
	{
		double xint1,xint2,yint1,yint2;
		double [] result= new double[0];
	if((y0-y1)!=0){
		double N= (R1*R1-R0*R0-x1*x1+x0*x0-y1*y1+y0*y0)/(2*(y0-y1));
		double a=1+(x0-x1)*(x0-x1)/((y0-y1)*(y0-y1));
		double b=2*y0*(x0-x1)/(y0-y1)-2*N*(x0-x1)/(y0-y1)-2*x0;
		double c=x0*x0+y0*y0+N*N-R0*R0-2*y0*N;
		double delta= b*b-4*a*c;
		if(delta>=0){
			result= new double[4];
			xint1=((-b+Math.sqrt(delta))/(2*a));
			xint2=((-b-Math.sqrt(delta))/(2*a));
			yint1=N-xint1*(x0-x1)/(y0-y1);
			yint2=N-xint2*(x0-x1)/(y0-y1);
			
			result[0]=xint1;
			result[1]=yint1;
			result[2]=xint2;
			result[3]=yint2;
		}
	}
	else if(x0!=x1){
		xint1=xint2=(R1*R1-R0*R0-x1*x1+x0*x0)/(2*(x0-x1));
		double a=1;
		double b=-2*y1;
		double c=x1*x1+xint1*xint1-2*xint1*x1+y1*y1-R1*R1;
		double delta= b*b-4*a*c;
		if(delta>=0){
			result= new double[4];
			yint1=((-b+Math.sqrt(delta))/(2*a));
			yint2=((-b-Math.sqrt(delta))/(2*a));
			
			result[0]=xint1;
			result[1]=yint1;
			result[2]=xint2;
			result[3]=yint2;
		}
	}
	this.ptConflit= result;
	return result;
	
	}
	

	
	
	/**
	 * 
	 * @param xrobot
	 * @param yrobot
	 * @param xfinal
	 * @param yfinal
	 * @param boussole
	 * @return coordonnes du centre
	 * 
	 * Pemet de calculer le centre du cercle de l'arc de cercle tangeant à la fleche boussole et passant par
	 * le point cible
	 * 
	 * ATTENTION: Pb avec le simulateur lorsqu on se trouve dans le cas ou le rayon de l'arc est "infini"
	 */
	public double[] calculCentre(int xrobot,int yrobot,int xfinal,int yfinal,double boussole)
	{
		double [] result= new double[2];
		double a1,a2,b1,b2,xk,yk,xg,yg;
		if ((Math.sin(boussole*3.14/180))!=0 && (yfinal-yrobot)!=0)
		{	
			//calcul des droite d1=>CK (droite perpendiculaire à droite robot/pt final passant par le poitn final) et d2=>AK (droite perpendiculaire a la fleche boussole passant par le robot)
			a1=-(double)(xfinal-xrobot)/(double)(yfinal-yrobot);
			a2=-(Math.cos(boussole*Math.PI/180))/(Math.sin(boussole*Math.PI/180));
			b1=yfinal-a1*xfinal;
			b2=yrobot-a2*xrobot;
			//calcul de K: l'intersection de d1 et d2 le segment position robot/K et le diametre du cercle
			xk=(b2-b1)/(a1-a2);
			yk=a1*xk+b1;
			//calcul du milieu de AK=> centre du cercle trajectoire
			xg=(xk+xrobot)/2;
			yg=(yk+yrobot)/2;
		} else if(Math.sin(boussole*Math.PI/180)!=0){
			a2=-(Math.cos(boussole*Math.PI/180))/(Math.sin(boussole*Math.PI/180));
			b2=yrobot-a2*xrobot;
			//si robot et final sur le meme y =>CK // a 0x donc xk=xc
			xk=xfinal;
			yk=a2*xk+b2;
			//calcul du milieu de AK=> centre du cercle trajectoire
			xg=(xk+xrobot)/2;
			yg=(yk+yrobot)/2;
		}else if((yfinal-yrobot)!=0){
			a1=-(double)(xfinal-xrobot)/(double)(yfinal-yrobot);
			b1=yfinal-a1*xfinal;
			//si boussole a 0 alors AK=>//0y donc xrobot=xk
			xk=xrobot;
			yk=a1*xk+b1;
			//calcul du milieu de AK=> centre du cercle trajectoire
			xg=(xk+xrobot)/2;
			yg=(yk+yrobot)/2;
			
		}else{
			//si le point final se trouve sur la fleche boussole alors la trajectoire est rectiligne
			xg=xrobot;
			yg=Double.MAX_VALUE;
		}
		
		result[0]=xg;
		result[1]=yg;
		return result;
		
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @return l'argument du vecteur
	 */
	public static double angle (int x, int y)
	{
		double result=0.0;
		result= Math.atan2(y, x)*180/3.14;
		if (result<0) result+=360;
		return result;
	}
	
	/**
	 * 
	 * @param xrobot
	 * @param yrobot
	 * @param xfinal
	 * @param yfinal
	 * @param xg
	 * @param yg
	 * @param boussole
	 * @return l'angle de l'arc de cercle a parcourir sur le cercle pour atteindre le point cible
	 */
	public double angleAParcourir (int xrobot,int yrobot,int xfinal,int yfinal,int xg,int yg,int boussole)
	{
		double result=0.0;
		char direction;
		
		//on determine si le centre se trouve a gauche ou a droite du robot
		direction=this.directionArc(xrobot, yrobot, xfinal, yfinal, boussole);
		//on calcul l'angle Final/G/Robot
		result=angle(xrobot-xg,yrobot-yg)-angle(xfinal-xg,yfinal-yg);
		
		if (direction==Constantes.GAUCHE) result=360-result;
		
		if (result<0)result+=360;
		else if (result>360)result-=360;
		return result;
	}
	
	/**
	 * 
	 * @param xrobot
	 * @param yrobot
	 * @param xfinal
	 * @param yfinal
	 * @param boussole
	 * @return la direction du centre du cercle par rapport au robot
	 */
	public char directionArc (int xrobot,int yrobot,int xfinal,int yfinal,int boussole)
	{
		char result;
		double aux;
		
		aux=angle(xfinal-xrobot,yfinal-yrobot)-angle((int)(Math.cos(boussole*3.14/180)*1000),(int)(Math.sin(boussole*3.14/180)*1000));
		
		if (aux<0) aux+=360;
		else if (aux>360)aux-=360;
		if (aux>=180)
		result=Constantes.DROITE;
		else result=Constantes.GAUCHE;
		return result;
	}
	
	/**
	 * 
	 * @param xrobot
	 * @param yrobot
	 * @param xfinal
	 * @param yfinal
	 * @param boussole
	 * @return rayon du cercle de la trajectoire
	 * 
	 * Fonction qui calcul la trajectoire d'arc de cercle pour atteindre le point final 
	 * et enregistre tout ca dans la classe
	 */
	public double calculArc(int xrobot,int yrobot,int xfinal,int yfinal,int boussole)
	{
		int result=Integer.MAX_VALUE;
		double xg,yg;
		double [] centre;
		
		
		centre=this.calculCentre(xrobot, yrobot, xfinal, yfinal, boussole);
		xg=centre[0];
		yg=centre[1];
		//calcul du rayon et met en positif si le centre est a gauche du robot en negatif sinon (pour le robot)
		this.direction=this.directionArc(xrobot, yrobot, xfinal,yfinal, boussole);
		if(this.direction==Constantes.DROITE)result=(int)Math.sqrt((xrobot-xg)*(xrobot-xg)+(yrobot-yg)*(yrobot-yg));
		else result=(int)-Math.sqrt((xrobot-xg)*(xrobot-xg)+(yrobot-yg)*(yrobot-yg));
		
		this.OrdreType=OrdreDeplacement.DEP_ARC_BACKWARD.getValue();
		this.centre[0]=xg;
		this.centre[1]=yg;
		this.rayon=result;
		this.angleAParcourir=this.angleAParcourir(xrobot, yrobot, xfinal, yfinal,(int) xg,(int) yg,boussole);
		return result;	
	
	}
	
	public double calculArc(int xrobot,int yrobot,double lFinal,double angFinal,int boussole)
	{
		return this.calculArc(xrobot, yrobot, lFinal*Math.cos(angFinal*3.14/180), lFinal*Math.sin(angFinal*3.14/180), boussole);
	}
	
	public double calculAngleDirect(int xrobot,int yrobot,double lFinal,double angFinal,int boussole)
	{
		return this.calculAngleDirect(xrobot, yrobot, lFinal*Math.cos(angFinal*3.14/180), lFinal*Math.sin(angFinal*3.14/180), boussole);
	}
	
	public double calculArc(Coordonnees coord,int xfinal,int yfinal){
		return this.calculArc(coord.getX(), coord.getY(), xfinal, yfinal, coord.getCap());
	}
	
	public int calculAngleDirect (int xrobot,int yrobot,int xfinal,int yfinal,int boussole)
	{
		int result;
		
		result=(int)(-boussole+angle(xfinal-xrobot,yfinal-yrobot));
		
		if (result<0)result+=360;
		else if (result>360)result-=360;
		
		this.OrdreType=OrdreDeplacement.DEP_TURN_ANGLE.getValue();
		this.angleAParcourir=result;
		this.distance=Math.sqrt((xrobot-xfinal)*(xrobot-xfinal)+(yrobot-yfinal)*(yrobot-yfinal));
		return result;
	}
	

	
	public char getOrdreType()
	{
		return this.OrdreType;
	}
	
	public int getDistance()
	{
		return (int) this.distance;
	}
	
	public int getAngle()
	{
		return (int) this.angleAParcourir;
	}
	
	public int getRayon()
	{
		return (int) this.rayon;
	}
	
	public int[] getCentre()
	{ 
		 int []result= new int [2];
		result[0]=(int)this.centre[0];
		result[1]=(int)this.centre[1];
		return result;
	}
	
	public int getAngleParcourir(){
		return (int) this.angleAParcourir;
	}
	
	public char getDirection(){
		return this.direction;
	}
	
	public double[] getPtConflict(){
		return this.ptConflit;
	}
	

		
}