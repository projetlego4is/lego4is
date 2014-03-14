//Carte fictive représentant l'environnement du robot

public class Carte {
	
	//Constante définissant la direction d'arrivée du robot
	private static final int VERS_BAS = 2 ;
	private static final int VERS_HAUT = 1 ;
	private static final int VERS_DROITE = 4 ;
	private static final int VERS_GAUCHE = 3 ;
	
	//Largeur du plateau représentant l'environnement du robot => l
	public double largeur ;
	//Longueur du plateau représentant l'environnement du robot => L
	public double longueur ;
	//Pas définissant l'espace entre les points de la carte
	private double pas ;
	//Angle permettant de balayer l'environnement pour ajouter les points
	private double angle ;
	//Nombre de points de la carte 
	private int no_pt ;
	//Point initial de coordonnees (0,0) ;
	public Point P0 ;
	//Tableau de tous les points de la carte : tab_point(i) = point de nom i
	public Point[] tab_point ;
	
	public double cout_horiz ;
	public double cout_vert ;
	
    public Carte(double l, double L, double p, double alpha) {
   
    	this.largeur = l ;
    	this.longueur = L ;
    	this.pas = p ;
    	this.angle = alpha*Math.PI/180 ; //conversion de l'angle en radian
    	this.cout_horiz = this.pas*Math.cos(this.angle) ;
    	this.cout_vert = this.pas ;
    	
    	
    	//Initialisation du tableau des points de la carte : null pour chaque case
    	this.tab_point = new Point[(int)(this.largeur/this.pas+1)*(int)(this.longueur/this.pas+1)]; //nbre de points max de la carte
    	for (int i = 0; i<this.tab_point.length ; i++) {
    		this.tab_point[i]=null ;
    	}
    	
    	//Création des noeuds
        double x = 0 ;
        double y1 = 0 ;
        double y2 = 0 ;
        this.no_pt = 0 ;
        Point precedent, actuel, bas, P0_bis ;
        
        P0 = new Point(this.no_pt,x,y2) ; //Création du premier point de la carte : coordonnées (0,0)
        this.tab_point[this.no_pt] = P0 ;
        //System.out.println(P0.name + " : " + P0.x);
        
        P0_bis = P0 ; //P0_bis = premier point de la ligne en cours
        precedent = P0_bis ;
                       
        while (y1 <= this.longueur) {
        	        	
        	//Création de la premiere ligne
        	if (y1 == 0) {
        		x += this.pas ;
        		y2 = x*Math.tan(this.angle) ; 
        		while (x <= this.largeur && y2 <= this.longueur) {
        			
        			//Ajout du nouveau point
        			this.no_pt++ ;
        			    			
        			actuel = new Point(this.no_pt,x,y2,this.cout_horiz,precedent) ; //création du nouveau pt
        			this.tab_point[this.no_pt] = actuel ;
        			//System.out.println(actuel.name + " : " + actuel.x + " ; " + y2);   
        			
        			//Ajout à la liste du précédent le nouveau point
        			if (precedent.liste == null) {
        				precedent.liste = new Liste(this.cout_horiz,actuel) ;
        				precedent.droite = actuel ;
        			} else {
        				precedent.liste.ajout(this.cout_horiz,actuel) ;
        				precedent.droite = actuel ;   
        			}
            		precedent = actuel ;
                    x += this.pas ;
                    y2 = x*Math.tan(this.angle);  
                    
            	}
        		
        	}
        	
        	//Création des autres lignes
        	else {
        		//System.out.println("debut de ligne") ;
        		y2 = y1 ;
        		        		
        		this.no_pt++ ;
        		
        		//Création du premier point de la ligne et ajout du suivant du dessous
        		actuel = new Point(this.no_pt,x,y2,this.cout_vert,P0_bis) ;
        		this.tab_point[this.no_pt] = actuel ;
        		//System.out.println(actuel.name + " : " + actuel.x + " ; " + y2); 
        		        		
        		//Ajout dans la liste du point du bas de l'actuel
        		P0_bis.liste.ajout(this.cout_vert, actuel) ;
        		
        		bas = P0_bis.droite ; 
        		        		
        		P0_bis = actuel ; //Point bas du 1er noeud de la prochaine ligne
        		precedent = actuel ;
        		x += pas ;
        		y2 += x*Math.tan(this.angle);
        		        		
        		while (x <= this.largeur && y2 <= this.longueur){
        			
        			this.no_pt++;
        			
        			//création du pt et ajout à la liste le pt à sa gauche
        			actuel = new Point (this.no_pt,x,y2, this.cout_horiz,precedent) ;
        			this.tab_point[this.no_pt] = actuel ;
        			//System.out.println(actuel.name + " : " + actuel.x + " ; " + y2); 
        			
        			
        			precedent.droite = actuel ;
        			
        			//Ajout à sa liste le point en dessous
        			actuel.liste.ajout(this.cout_vert,bas) ;
        			
        			//Ajout à la liste du point de gauche le point actuel
        			if (precedent.liste == null) {
        				precedent.liste = new Liste(this.cout_horiz,actuel) ;
        			} else {
        				precedent.liste.ajout(this.cout_horiz,actuel) ;  
        			}
        			
        			//Ajout à la liste du point en dessous le point actuel
        			if (bas != null) {
	        			if (bas.liste == null) {
	        				bas.liste = new Liste(this.cout_vert,actuel) ;
	        			} else {
	        				bas.liste.ajout(this.cout_vert,actuel) ;  
	        			}
        			}
        			
        			precedent = actuel ;
        			if (bas.droite != null) {
        				bas = bas.droite ;	
        			}
        			
        			x += pas ;
            		y2 += x*Math.tan(this.angle);
        			
        		}
        	}
        	
        	x = 0 ;
        	y1 += this.pas ;

        }
    }
    
    public int get_nb_pt() {
    	return (this.no_pt+1) ;
    }

    //Methode permettant à partir de coordonnées de trouver le noeud le plus proche
    public Point Trouver_noeud_proche (double xx, double yy) {
    	Point proche = this.P0 ;
    	Point aux, proche_preced ;
    	double distance_proche ;
    	double distance_aux ;
    	Liste l_aux ;
    	
    	do {
    		proche_preced = proche ;
    		l_aux = proche.liste ;
    		while (l_aux != null) {
    			aux = l_aux.pt ;
    			distance_proche = Math.sqrt((xx-proche.x)*(xx-proche.x)+(yy-proche.y)*(yy-proche.y)) ;
	    		distance_aux = Math.sqrt((xx-aux.x)*(xx-aux.x)+(yy-aux.y)*(yy-aux.y)) ;
	    		if (distance_aux < distance_proche) {
	    			proche = aux ;
	    		}
	    		l_aux = l_aux.suivant ;
	    	}
    	} while (proche_preced != proche) ;
    	
    	return proche ;
    }
    
    //Méthode qui rajoute un arc entre deux points
	public void Ajouter_arc(Point pt_init, Point pt_final, double cout) {
		pt_init.liste.ajout(cout,pt_final) ;
		pt_final.liste.ajout(cout, pt_init) ;
	}
	
	  
    //Méthode qui enlève un arc (met le cout de l'arc à l'infini)
    public void enlever_arc (Point pt_dep, Point pt_arr) {
    	
    	//Enlever l'arc de pt_dep à pt_arr
    	Liste aux = pt_dep.liste ;
    	while (aux.pt!=pt_arr && aux!=null) {
    		aux = aux.suivant ;
    	}
    	if (aux==null) {
    		System.out.println("Pas d'arc entre les points " + pt_dep.name + " et " + pt_arr.name) ;
    	} else {
    		aux.cout = Double.MAX_VALUE ;
    		
    		//Enlever arc de pt_arr à pt_dep
        	aux = pt_arr.liste ;
        	while (aux.pt!=pt_dep && aux!=null) {
        		aux = aux.suivant ;
        	}
        	aux.cout = Double.MAX_VALUE ; 
    	}      
    }
    
    //Méthode qui enlève toute une zone de la carte en cas de rencontre d'un obstacle
    //taille_x et y : taille de l'obstacle ; x et y : destination souhaitée du robot
    public void enlever_zone (double taille_x, double taille_y, double x, double y, int direction) { 
    	
    	double x_pt, y_pt ;
    	Liste liste ;
    	Boolean condition = false ;
    	//int j = 0 ;
    	
    	if (direction == VERS_HAUT) {
			y=y+10 ;
		}
		else if (direction == VERS_BAS) {
			y = y-10 ;
		}
		else if (direction == VERS_DROITE) {
			x=x+10 ;
		}
		else if (direction == VERS_GAUCHE) {
			x=x-10 ;
		}
    	
    	for (int i = 0 ; i < this.tab_point.length ; i++) { 
    		
    		x_pt = this.tab_point[i].x ;
    		y_pt = this.tab_point[i].y ;
    		
    		//On définit pour chaque direction possible d'arrivée du robot la zone à enlever
    		if (direction == VERS_HAUT) {
    			condition = (x_pt >= x-taille_x/2 && x_pt <= x+taille_x/2 && y_pt >= y && y_pt <= y+taille_y) ;
    		}
    		else if (direction == VERS_BAS) {
    			condition = (x_pt >= x-taille_x/2 && x_pt <= x+taille_x/2 && y_pt <= y && y_pt >= y-taille_y) ;
    		}
    		else if (direction == VERS_DROITE) {
    			condition = (x_pt >= x && x_pt <= x+taille_x && y_pt <= y+taille_y/2 && y_pt >= y-taille_y/2) ;
    		}
    		else if (direction == VERS_GAUCHE) {
    			condition = (x_pt >= x-taille_x && x_pt <= x && y_pt <= y+taille_y/2 && y_pt >= y-taille_y/2) ;
    		}
    		
    		//On teste alors pour la condition adéquat si les points appartiennent à cette zone 
    		//Si oui, on enleve les arcs avec leurs suivants
    		if (condition) {
    			liste = this.tab_point[i].liste ;
    			while (liste != null) {
    				System.out.println("j'enleve l'arc entre "+ this.tab_point[i] + " et "+ liste.pt);
    				this.enlever_arc(this.tab_point[i], liste.pt) ;
    				liste = liste.suivant ;
    			}
    		}
    		
    	}
    }
    
  

}
