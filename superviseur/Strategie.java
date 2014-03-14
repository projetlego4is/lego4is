package superviseur;
import java.util.*;


import commun.*;


public class Strategie {
	
	public static final int distMin=30;
	public static final int rayonMax=10000;
	private VirtualRobot[] predateurs;
	private VirtualRobot[] slaves;
	private boolean simMode;
	private int angLeader;

	public Strategie(VirtualRobot [] predateurs, boolean simMode){
		this.predateurs=predateurs;
		this.slaves=new VirtualRobot[0];
		this.simMode=simMode;
		this.angLeader=0;
	}
	
	public double distance(double x1, double y1, double x2, double y2){
		return Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
	}

	public void simMode(){
		this.simMode=true;
	}
	
	public int isLeader(){
		double min =Double.MAX_VALUE;
		int indLeader=0;
		for(int i=0; i<this.predateurs.length;i++){
			if (this.predateurs[i].getCoord().getDistance()<min){
				min=this.predateurs[i].getCoord().getDistance();
				indLeader=i;
			}
		}
	
		this.predateurs[indLeader].setLeader(true);
		
		for(int i=0; i<this.slaves.length;i++){
			this.slaves[i].setLeader(false);
		}
		
		if(this.simMode){
			this.predateurs[indLeader].sendTrajectoire();
		}
	
		int j=0;
		if(this.predateurs.length>1){
			VirtualRobot[] aux= new VirtualRobot[this.predateurs.length-1];
			for (int i=0; i<this.predateurs.length;i++){
				if(i!=indLeader){
					aux[j]=this.predateurs[i];
					if(this.simMode)
						this.predateurs[i].setLeader(false);
				}
				else {
					j--;
				}
				j++;
			}
			this.slaves=aux;
		}
		return (int) Trajectoire.angle(this.predateurs[indLeader].getCoord().getX(),this.predateurs[indLeader].getCoord().getY());
	}
	
	public void calculerStrategie(){
		double [][] distances;
		double [] points;
		boolean noConflict=true;
		
		this.angLeader=this.isLeader();
		
		if(this.slaves.length>0){
			points= new double[2*(this.slaves.length)];
			for (int i=0; i<this.slaves.length;i++){
				points[i*2]=Strategie.distMin*Math.cos(this.angLeader*Math.PI/180+2*(i+1)*Math.PI/this.predateurs.length);
				points[i*2+1]=Strategie.distMin*Math.sin(this.angLeader*Math.PI/180+2*(i+1)*Math.PI/this.predateurs.length);
			}
			distances= new double[this.slaves.length][this.slaves.length];
			for(int i=0;i<this.slaves.length;i++){
				for(int j=0; j<this.slaves.length;j++){
					distances[i][j]=this.distance(points[2*j], points[2*j+1], this.slaves[i].getCoord().getX(), this.slaves[i].getCoord().getY());
				}
			}
			//on considere deux esclaves au max pour faciliter le code
			if(this.slaves.length==1)
			{
				this.slaves[0].setObjectif((int)(points[0]),(int) (points[1]));
			}
			else if (this.slaves.length==2) {
				if((distances[0][1]+distances[1][0])>(distances[0][0]+distances[1][1])){
					this.slaves[0].setObjectif((int)(points[0]),(int) (points[1]));
					this.slaves[1].setObjectif((int)(points[2]),(int) (points[3]));
				}else{
					this.slaves[1].setObjectif((int)(points[0]),(int) (points[1]));
					this.slaves[0].setObjectif((int)(points[2]),(int) (points[3]));
				}
			}else{
				System.out.println("On a pas codé !!!!!!!!!");
			}
			/*
			for(int j=0;j<this.slaves.length;j++)
			{	
				this.slaves[j].calcTrajectoire();
			}
			
			for(int j=0; j<this.slaves.length-1;j++){
				for(int k=j+1; k<this.slaves.length;k++){
					if(this.slaves [j].getTrajectoire().conflitTrajectoire(this.slaves[j], this.slaves[k])){
						this.slaves[j].Stop();
						noConflict=false;
					}
					if(noConflict)
						this.slaves[j].sendTrajectoire();
				}
			}*/
		}
	}
				
		

}
