//Classe représentant un point de la carte fictive


public class Point {
	
	//nom du point représenté par un nombre
	public int name ; 
	//coordonnees en x
    public double x; 
    //coordonnees en y
    public double y; 
    //Liste des suivants
    public Liste liste ;
    //Point à droite de this
    public Point droite ;

    public Point(int name, double x, double y) {
        this.x = x;
        this.y = y;
        this.name = name;
       /* this.gauche = g ;
        this.bas = b ;
        this.haut = h ;
        this.bas = b ;*/
        this.liste = null ;
        this.droite = null ;
        
    }
    
    public Point(int name, double x, double y, double cost, Point p) {
    	 this.x = x;
         this.y = y;
         this.name = name;
         //this.liste.ajout(cost,p) ;
         this.liste = new Liste(cost,p) ;
        // System.out.println(this.liste.cout) ;
         this.droite = null ;
    }
    
    public int getName() {
        return this.name;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }
  
    public String toString(){
        return String.valueOf(this.getName());
    }


}

