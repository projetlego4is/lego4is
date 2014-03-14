import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout.Constraints;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.plaf.basic.BasicScrollPaneUI.HSBChangeListener;
public class Interface_graphique_2 extends JFrame implements ActionListener {
	
	public static final int DEPART = 1 ;
	public static final int ARRIVEE = 2 ;
	public static final int NB_OBJECTIFS = 3 ;
	public static final int OBJECTIFS = 4 ;
	public static final int VOIR_OBJ = 5 ;
	public static final int DIMENSION = 6 ;
	
	public static final Color vert = new Color(51,204,0) ;
	
	//private JFrame fenetre ;
	private JPanel panneau ; //Contiendra tous les composants présents sur la fenetre
	private JButton bouton_dep ;
	private JButton bouton_arr ;
	private JButton bouton_obj ;
	private JButton bouton_voir_obj;
	private JButton bouton_demarrer ;
	private JButton bouton_dimension ;
	private JLabel label_dep ;
	private JLabel label_arr ;
	private JLabel label_nb ;
	private JLabel label_dim ;
	
	public Carte map ;
	
	public Point dep ;
	public Point arr ;
	public Point[] tab_obj ;
	public double largeur, longueur, angle, pas ;
	
	public boolean dep_OK, arr_OK, carte_OK ; //Indique si les coordonnées du pt de départ (pt d'arr) ont été renseignées
		
	public int nbr_obj ;
	
	private static final Font fonte = new Font("Bold", Font.PLAIN, 18) ;
	private static final Font fonte2 = new Font("Bold", Font.PLAIN, 16) ;
	
	//
	public GridBagLayout repartiteur = new GridBagLayout() ;
	public GridBagConstraints contraintes ;
	public Container interieur = getContentPane() ;
		
	
	public Interface_graphique_2() {
		
		this.dep_OK = false ;
		this.arr_OK = false ;
		this.nbr_obj = 0 ;
		
		//this.interieur = new JFrame("Mon robot avance");
		
		this.interieur.setLayout(repartiteur);
		//this.interieur.setName("Mon robot avance");
		
		//Ajout de l'image
		this.ajouter_img("img_robot.jpg", 0, 0) ;
		int place = GridBagConstraints.RELATIVE ;
		System.out.println(place) ;
		this.bouton_dimension = this.ajouter_bouton("Définir la carte", 9, 0);
		this.label_dim = this.ajouter_label("Carte non définie", 9, 1) ;
		this.bouton_dep= this.ajouter_bouton("Définir le départ",9, 2);
		this.label_dep = this.ajouter_label("Point de départ non défini", 9,3) ;
		this.bouton_arr= this.ajouter_bouton("Définir l'arrivée", 9, 4);
		this.label_arr = this.ajouter_label("Point d'arrivée non défini", 9, 5) ;
		this.bouton_obj= this.ajouter_bouton("Définir les objectifs", 9, 6);
		this.label_nb = this.ajouter_label("0 objectif défini", 9, 7) ;
		this.bouton_voir_obj= this.ajouter_bouton("Voir les objectifs", 9, 8);
		this.bouton_demarrer= this.ajouter_bouton("Démarrer", 0, 9) ;
		
	
	}
	
	

	public static void main(String[] arg) {	
		
		Interface_graphique graph = new Interface_graphique() ;
		//panneau.setBackground(Color.RED); //Définit la couleur de la fenetre
		//graph.fenetre.setContentPane(graph.panneau); 
		/*graph.fenetre.setLocation(300, 100); //Emplacement de ma fenetre sur l'écran
		graph.fenetre.pack(); //Calcule la dimension de la fenetre pour qu'elle s'adapte au contenu
		graph.fenetre.setVisible(true); //Rend visible la fenetre
		graph.fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Termine l'appli quand on ferme la fenetre*/
		//graph.setPreferredSize(new Dimension(600, 500));
		graph.setLocation(300, 100); //Emplacement de ma fenetre sur l'écran
		graph.pack(); //Calcule la dimension de la fenetre pour qu'elle s'adapte au contenu
		graph.setVisible(true); //Rend visible la fenetre
		graph.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Termine l'appli quand on ferme la fenetre
	
	}
	
	//Actions appelées au clique sur l'un des boutons
	public void actionPerformed(ActionEvent e) {
		 		 
		//***********************************************//
		//***********Def du point de départ *************//
		//***********************************************//
	     if (e.getSource() == bouton_dep) { 
	    	 Dialogue2 dialog = new Dialogue2(DEPART, 0, this) ;
	    	 if (dialog.dep != null && dialog.annule == false) {
	    		 this.dep = dialog.dep ;
	    		 this.dep_OK = true ;
	    		 System.out.println("depart :" +this.dep_OK) ;
	    		 this.label_dep.setText("Point de départ (x ; y) = (" + dialog.dep_x + " ; " + dialog.dep_y +")") ;
	    		 this.label_dep.setForeground(this.vert) ;
	    		 
	    	 } 
	    	
	     }
	     
	     //***********************************************//
	     //***********Def du point d'arrivée *************//
	     //***********************************************//
	     else if (e.getSource() == bouton_arr) {
	    	 Dialogue2 dialog = new Dialogue2(ARRIVEE, 0, this) ;
	    	 if (dialog.arr != null && dialog.annule == false) {
	    		 this.arr = dialog.arr ;
	    		 this.arr_OK = true ;
	    		 System.out.println("arrivee :" +this.dep_OK) ;
	    		 this.label_arr.setText("Point d'arrivée (x ; y) = (" + dialog.arr_x + " ; " + dialog.arr_y +")") ;
	    		 this.label_arr.setForeground(this.vert) ;
	    		 
	    	 } 
	    }
	     
	     //***********************************************//
	     //************** Def des objectifs **************//
	     //***********************************************//
	     else if (e.getSource() == bouton_obj) {
	    	 actionObj() ;
	     
	    	// fenetre_bouton.setTitle("Définir les objectifs") ;
	     }
	     
	     //***********************************************//
	     //************** Voir les objectifs *************//
	     //***********************************************//
	     else if (e.getSource() == bouton_voir_obj) {
	    	 Dialogue2 dialog = new Dialogue2(VOIR_OBJ, 0, this) ;
	     }
	     //***********************************************//
	     //****************** Démarrage ******************//
	     //***********************************************//
	     else if (e.getSource() == bouton_demarrer) {
	    	 if (this.dep_OK && this.arr_OK) {
	    		 JOptionPane.showMessageDialog(this, "Fermeture de la fenêtre et lancement du robot", "C'est parti !!!",JOptionPane.INFORMATION_MESSAGE);
	    		 this.dispose() ;
	    	 } else {
	    		 JOptionPane.showMessageDialog(this, "Vous n'avez pas défini le point de départ et/ou d'arrivée","Erreur", JOptionPane.ERROR_MESSAGE);
	    	 }
	     }
	     //***********************************************//
	     //*************** Def de la carte ***************//
	     //***********************************************//
	     else if (e.getSource() == bouton_dimension) {
	    	 
	    	 Dialogue2 dialog = new Dialogue2(DIMENSION, 0, this) ;
	    	 try {
	    		 if (dialog.annule == false) {
	    			 
		    		 this.longueur = dialog.longueur ;
		    		 this.largeur = dialog.largeur ;
		    		 this.angle = dialog.angle ;
		    		 this.pas = dialog.pas ;
		    		 this.map = new Carte(this.largeur, this.longueur, this.pas, this.angle) ; 
		    		 
		    		 //Première définition de la carte
			    	 if (this.carte_OK == false) {
			    		 this.carte_OK = true ;
			    		 this.label_dim.setText("Dimension de la carte : "+this.largeur+"X"+this.longueur) ;
			    		 this.label_dim.setForeground(this.vert) ;
			    	 }
			    	 //Redéfinition
			    	 else {
			    		 this.label_dim.setText("Dimension de la carte : "+this.largeur+"X"+this.longueur) ;
			    		 this.label_dim.setForeground(this.vert) ;
			    		 this.dep = null ;
			    		 this.label_dep.setText("Point de départ non défini");
			    		 this.label_dep.setForeground(Color.red) ;
			    		 this.arr = null ;
			    		 this.label_arr.setText("Point d'arrivée non défini");
			    		 this.label_arr.setForeground(Color.red) ;
			    		 this.nbr_obj = 0 ;
			    		 this.label_nb.setText("0 objectif défini");
			    		 this.label_nb.setForeground(Color.black) ;
			    		 this.tab_obj = null ;
			    	 }
	    		 }
		    		 
	    	 } catch (ArrayIndexOutOfBoundsException i) {
	    	 } 
	    	
	    	 
	     }
	    
	     
	}

	
	private JButton ajouter_bouton(String titre, int x, int y) {
		
		//Définition des caractéristiques du label (emplacement, taille, marge)
		GridBagConstraints contraintes = new GridBagConstraints() ;
		contraintes.ipadx = 15 ;
		contraintes.ipady = 20 ;
		contraintes.insets = new Insets(10, 10, 10, 10) ;
		contraintes.gridx = x ;
		contraintes.gridy = y ;
		contraintes.weightx = 1 ;
		contraintes.weighty = 1 ;
				
		JButton bouton = new JButton(titre);
		bouton.setFont(fonte) ;
		this.repartiteur.setConstraints(bouton, contraintes) ;
		this.interieur.add(bouton);
		bouton.addActionListener(this);
		return(bouton);
	}
	
	
	private JLabel ajouter_label(String titre, int x, int y) {
		//Définition des caractéristiques du label (emplacement, taille, marge)
		GridBagConstraints contraintes = new GridBagConstraints() ;
		contraintes.ipadx = 50 ;
		contraintes.ipady = 20 ;
		//contraintes.insets = new Insets(10,10,10,10) ;
		contraintes.gridx = x ;
		contraintes.gridy = y ;
		
		JLabel label = new JLabel(titre,SwingConstants.CENTER ) ; //définit le label et le centre 
		label.setForeground(Color.red) ; //Label en rouge
		label.setFont(fonte2) ;
		this.repartiteur.setConstraints(label, contraintes);
		this.interieur.add(label);
		return(label);
	}
	private JLabel ajouter_img(String nom_img, int x, int y) {
		//Définition des caractéristiques du label (emplacement, taille, marge)
		GridBagConstraints contraintes = new GridBagConstraints() ;
		contraintes.ipadx = 15 ;
		contraintes.ipady = 20 ;
		contraintes.insets = new Insets(10, 10, 10, 10) ;
		contraintes.gridx = x ;
		contraintes.gridy = y ;
		contraintes.gridheight = 9 ;
		contraintes.gridwidth = 9 ;
		
		JLabel image = new JLabel( new ImageIcon( nom_img));
		image.setForeground(Color.red) ; //Label en rouge
		image.setFont(fonte2) ;
		this.repartiteur.setConstraints(image, contraintes);
		this.interieur.add(image);
		return(image);
	}
	
	public void actionObj () {
		System.out.println(this.carte_OK) ;
	   	 Dialogue2 dialog = new Dialogue2(NB_OBJECTIFS, 0, this);
		 this.nbr_obj = dialog.nb_obj ;
		 //Si on veut des objectifs
		 if (this.nbr_obj != 0 && dialog.annule == false) {
			 this.tab_obj = new Point[this.nbr_obj] ;
			 int no_obj ;
			 for (no_obj = 1 ; no_obj <= this.nbr_obj ; no_obj++) {
				 Dialogue2 dialog2 = new Dialogue2(OBJECTIFS, no_obj, this) ;
	    		 if (this.nbr_obj != 0 && dialog.annule == false) {
	    			 this.tab_obj[no_obj-1] = dialog2.obj ;	
				 }
			 }
		 }
		 
		//Une fois les objectifs définis
		 if (this.nbr_obj!=0) {
			 this.label_nb.setText(this.nbr_obj + " objectifs définis") ;
			 this.label_nb.setForeground(this.vert) ;
		 } else {
			 this.label_nb.setText("0 objectif défini") ;
			 this.label_nb.setForeground(Color.black) ;
		 }
	 
	}
	  
}

