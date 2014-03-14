
public class Liste {
	
	double INF = Double.MAX_VALUE;
	
	//Coût pour aller au point suivant
	public double cout ;
	//Point actuel
	public Point pt ;
	//Point suivant ;
	public Liste suivant ;
	
	public Liste(double c, Point p) {
		this.cout = c ;
		this.pt = p ;
		this.suivant = null ;
	}
	
	public Liste trouver_dernier() {
		Liste l = this ;
	   	while (l.suivant != null) {
    	l = l.suivant ;
    	}
    	return l ;
	}
	
	public void ajout(double c , Point p) {
		if (this != null) {
			Liste l = this.trouver_dernier() ;
			l.suivant = new Liste(c,p) ;
		} else {
			System.out.println("Liste vide") ;
		}
	}
	
	

}
