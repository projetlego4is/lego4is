
public class Dijkstra {
	
	//Carte : environnement du robot
	public Carte carte ;
	//Point d'où part le robot
	public Point pt_depart ;
	//Point où doit arriver le robot
	public Point pt_arrivee ;
	//Nombre de sommets dans la carte
	private int nb_sommets ;
	
	//Point précédent à l'actuel : chemin[i] = i précédant
	public Point[] tab_chemin ;
	//Cout pour aller du pt de départ au pt i
	public double[] tab_cout ;
	//Point traité ou non ? traité => 1
	public boolean[] tab_traite ;
	//Nombre de sommets traités
	public int nb_traite ;
	
	
	public Dijkstra (Carte map, Point dep, Point arr) {
		
		this.carte = map ;
		this.pt_depart = dep ;
		this.pt_arrivee = arr ;
		this.nb_sommets = this.carte.get_nb_pt() ;
		
		this.tab_chemin = new Point[this.nb_sommets] ; 
		this.tab_cout = new double[this.nb_sommets] ; 
		this.tab_traite = new boolean[this.nb_sommets] ; 
		this.nb_traite = 0 ;
		
		int i ;
        for (i = 0; i < this.nb_sommets; i++) {
            this.tab_traite[i] = false;//init du tableau de booleens
            this.tab_cout[i] = Double.MAX_VALUE;//init du tableau des couts des chemins les plus courts
            this.tab_chemin[i] = null;//init du tableau des noeuds precedents
        }
        this.tab_cout[pt_depart.name] = 0; // le noeud init est a 0cm de lui-meme
	}
	
	
	//Cette fonction Choisit parmi les sommets non marqués celui dont le cout est minimum
	public Point Trouver_min() {
		double cout_min = Double.MAX_VALUE;
		Point pt_retenu = null ;
					
		for (int i = 0; i < this.nb_sommets; i++) {
			if ((this.tab_cout[i] < cout_min) && this.tab_traite[i] == false) {
				cout_min = this.tab_cout[i];
	            pt_retenu = this.carte.tab_point[i];
	        }
		}
		
		/*if (pt_retenu != null) {
            tab_traite[pt_retenu.name] = true;
            this.nb_traite++ ;
        }*/
		
		if (pt_retenu == null) {
        	for (int i = 0; i < this.nb_sommets; i++) {
    			if (this.tab_traite[i] == false) {
    				pt_retenu = this.carte.tab_point[i] ;
    			}
        	} 
        }
		tab_traite[pt_retenu.name] = true;
        this.nb_traite++ ;
        
		return pt_retenu ;
	}
	
	//Mettre à jour les longueur des plus courts chemins entre le noeud retenus et ses suivants
	public void MAJ_distance(Point pt_retenu) {
	
		Liste l_aux = pt_retenu.liste ;
			
        while (l_aux != null) {
        	if (tab_cout[l_aux.pt.name]> (l_aux.cout + tab_cout[pt_retenu.name])) {
        		tab_cout[l_aux.pt.name]=  l_aux.cout + tab_cout[pt_retenu.name] ;
        		tab_chemin[l_aux.pt.name] = pt_retenu ;  
        	}
        	l_aux = l_aux.suivant ;
        }
		
	}
	
	//Algo de Dijkstra complet
	public void Run_Dijkstra() {
		Point pt ;
		while (this.nb_traite!=this.nb_sommets) {
			pt = this.Trouver_min() ;
			this.MAJ_distance(pt) ;
		}
	}
	
	//Retourne le chemin à emprunter après appel à Dijkstra
	public Point[] Get_Chemin() {
		int nb_points = 1;
        Point aux = this.pt_arrivee;

        //on cherche le nombre de noeud que l'on va passer en revue pour arriver Ã  notre poins de dÃ©part
        while (aux != this.pt_depart) {
            aux = this.tab_chemin[aux.name];
            nb_points++;
        }

        Point[] tab_resul = new Point[nb_points];
        aux = this.pt_arrivee;

        //on remplit notre tableau final en remontant
        while (nb_points > 0) {
            tab_resul[nb_points-1] = aux;
            aux = tab_chemin[aux.name];
            nb_points--;
        }
        return tab_resul;
	}
	
	
}
