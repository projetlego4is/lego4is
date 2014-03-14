package superviseur;

public class Coordonnees {
	/**
	 * Met a jour et traite les coordonnees des robots distants
	 */
	private int x;
	private int y;
	private int boussole;
	private int anglebss;
	private int distance;

	public Coordonnees() {
		this.x = Integer.MAX_VALUE;
		this.y = Integer.MAX_VALUE;
		this.boussole = 0;
		this.anglebss=0;
		this.distance=Integer.MAX_VALUE;
	}

	public void setX(int x) {
		this.x = x;
		this.anglebss=(int) (Math.atan2(this.y,this.x)*180/3.14);
		if(this.anglebss<0) this.anglebss+=360;
		this.distance=(int) Math.sqrt(this.x*this.x+this.y*this.y);
	}

	public void setY(int y) {
		this.y = y;
		this.anglebss=(int) (Math.atan2(this.y,this.x)*180/3.14);
		if(this.anglebss<0) this.anglebss+=360;
		this.distance=(int) Math.sqrt(this.x*this.x+this.y*this.y);
	}

	public void setCap(int boussole) {
		if(boussole<0) this.boussole=boussole+=360;
		else this.boussole=boussole%360;
		
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public int getCap() {
		return this.boussole;
	}
	
	public int getAnglebss(){
		return this.anglebss;
	}

	public int getDistance(){
		return this.distance;
	}
	
	public void setAnglebss(int anglebss){
		this.anglebss=anglebss;
	}

	public void setDistance(int distance){
		 this.distance=distance;
	}

}