import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.Box;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;

public class Dialogue extends JDialog implements ActionListener {
	
	public static final int DEPART = 1 ;
	public static final int ARRIVEE = 2 ;
	public static final int NB_OBJECTIFS = 3 ;
	public static final int OBJECTIFS = 4 ;
	public static final int VOIR_OBJ = 5 ;
	public static final int DIMENSION = 6 ;
	
	private int choix ;
	public int nb_obj ;
	
	public Interface_graphique graph ;
	
	public double dep_x, dep_y, arr_x, arr_y, obj_x, obj_y;
	public Point dep, arr, obj ;
	public double largeur, longueur ;
	
	public boolean annule ;
	
	private JTextField saisieX, saisieY, saisie_l, saisie_L ;  
	private JButton valider = new JButton("Valider") ;
	private JButton annuler = new JButton("Annuler") ;
	private JButton ok = new JButton("OK") ;
	private JButton changerObj = new JButton("Changer les objectifs") ;
		
	public Dialogue(int c, int no_obj, Interface_graphique inter) {
		
		this.choix = c ;
		this.graph = inter ;
						
		this.annule = false ; 
		
		this.saisieX = new JTextField(5);
		this.saisieY = new JTextField(5);
		this.valider = new JButton("Valider") ;
		this.annuler = new JButton("Annuler") ;
		
		JPanel panneau ;
		Box boite = Box.createVerticalBox();
		setModal(true);
		
		this.valider.addActionListener(this);
		this.annuler.addActionListener(this);
		
		//********************************************************************//
		//***********Def des pts de départ, d'arrivée ou objectif*************//
		//********************************************************************//
		if (this.choix == DEPART || this.choix == ARRIVEE || this.choix == OBJECTIFS) {
			
			if (this.graph.carte_OK == true) {
				
				System.out.println("CARTE : "+this.graph.carte_OK) ;
				this.setPreferredSize(new Dimension(350, 150));
				String titre ;
				
				if (this.choix == DEPART) {
					titre = "Définir le point de départ" ;
				} 
				else if (this.choix == ARRIVEE) {
					titre = "Définir le point d'arrivée" ;
				}
				else {
					titre = "Définir l'objectif n° " + no_obj ;
				}
							
				this.setTitle(titre) ;
							
				panneau = new JPanel();
				JLabel label = new JLabel("");
				panneau.add(label);
				boite.add(panneau);
						
				//Coordonnées x
				panneau = new JPanel() ;
				JLabel label_X = new JLabel("Saisissez la coordonnée en X");
				panneau.add(label_X);
				panneau.add(saisieX);
				//panneau.setLocation(panneau.getLocation().x, 50);
				boite.add(panneau);
				
				//coordonnées y
				panneau = new JPanel() ;
				JLabel label_Y = new JLabel("Saisissez la coordonnée en Y");
				panneau.add(label_Y);
				panneau.add(saisieY);
				boite.add(panneau);
	
				//Boutons
				panneau = new JPanel() ;
				panneau.setLayout(new FlowLayout());
				valider.setSize(100,50);
				panneau.add(valider);
				panneau.add(annuler);
				boite.add(panneau);
				
				this.add(boite);
				
				this.setLocation(400,200) ;
			    this.pack() ;
			    this.setVisible(true) ;
			}
			
			//Si la carte n'a pas encore été définie
			else {
				JOptionPane.showMessageDialog(this, "Veuillez tout d'abord définir la carte", "Attention, carte non définie", JOptionPane.WARNING_MESSAGE) ;
			}
		}
		

		//***********************************************//
		//*********** Def des dim de la carte ***********//
		//***********************************************//
		else if (this.choix == DIMENSION) {
			
			this.setPreferredSize(new Dimension(350, 150));
			this.setTitle("Saisissez les dimensions de la carte") ;
						
			panneau = new JPanel();
			JLabel label = new JLabel("");
			panneau.add(label);
			boite.add(panneau);
					
			//Largeur
			panneau = new JPanel() ;
			JLabel label_l = new JLabel("Largeur");
			panneau.add(label_l);
			saisie_l = new JTextField(5);
			panneau.add(saisie_l);
			boite.add(panneau);
			
			//Longueur
			panneau = new JPanel() ;
			JLabel label_L = new JLabel("Longueur");
			panneau.add(label_L);
			saisie_L = new JTextField(5);
			panneau.add(saisie_L);
			boite.add(panneau);
						
			//Boutons
			panneau = new JPanel() ;
			panneau.setLayout(new FlowLayout());
			valider.setSize(100,50);
			panneau.add(valider);
			panneau.add(annuler);
			boite.add(panneau);
			
			this.add(boite);
			
			this.setLocation(400,200) ;
		    this.pack() ;
		    this.setVisible(true) ;
		}
		
		
		//***********************************************//
		//*********** Def du nb d'objectifs *************//
		//***********************************************//
		else if (this.choix == NB_OBJECTIFS) {
			if (this.graph.carte_OK == true) {
				Integer[] vecteur_nb = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20} ;
				Integer reponse = (Integer)JOptionPane.showInputDialog(this, "Combien voulez-vous d'objectifs ?", "Définir le nombre d'objectifs", JOptionPane.QUESTION_MESSAGE, null, vecteur_nb, vecteur_nb[0]) ;
				this.nb_obj = (int)reponse ;
			} 
			else {
				JOptionPane.showMessageDialog(this, "Veuillez tout d'abord définir la carte", "Attention, carte non définie", JOptionPane.WARNING_MESSAGE) ;
			}
		}
		
		//***********************************************//
		//************** Voir les objectifs *************//
		//***********************************************//
		else if (this.choix == VOIR_OBJ) {
			//this.setPreferredSize(new Dimension(350, 150));
			if (graph.nbr_obj == 0) {
				JOptionPane.showMessageDialog(this, "Il n'y a aucun objectif défini", "Nombre d'objectifs", JOptionPane.WARNING_MESSAGE) ;
			} else {
				this.setTitle("Nombre d'objectifs") ;
				
				for (int i=0 ; i<graph.nbr_obj ; i++) {
					panneau = new JPanel() ;
					
					//arrondi du nombre à un chiffre après la virgule 
					String masque = new String("#0.#"); 
					DecimalFormat form = new DecimalFormat(masque); 

					JLabel label = new JLabel("Objectif n°"+(i+1)+" : (x;y) = ("+ form.format(graph.tab_obj[i].x) + " ; "+ form.format(graph.tab_obj[i].y)+")") ;
					panneau.add(label, BorderLayout.CENTER);
					boite.add(panneau);
				}
				
				
				//Boutons
				panneau = new JPanel() ;
				panneau.setLayout(new FlowLayout());
				
				ok.addActionListener(this);
				panneau.add(ok);
				changerObj.addActionListener(this);
				panneau.add(changerObj);
				boite.add(panneau) ;
				
				this.add(boite);
				
				this.setLocation(400,200) ;
			    this.pack() ;
			    this.setVisible(true) ;	
			}
		}
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		//**************************************//
		//***********Bouton valider*************//
		//**************************************//
		if (e.getSource() == valider) {
			
			if (this.choix == DEPART || this.choix == ARRIVEE || this.choix == OBJECTIFS) {
				try {
					double x_saisie = new Double(this.saisieX.getText()) ;
					double y_saisie = new Double(this.saisieY.getText());
					
					//On teste si les coordonnées saisies sont OK
					if (x_saisie<0 || x_saisie>this.graph.largeur || y_saisie<0 || y_saisie>this.graph.longueur) {
						String message = new String("Les coordonnées que vous demandez sont invalides, veuillez choisir x entre 0 et "+ (int)this.graph.largeur + " et y entre 0 et "+ (int)this.graph.longueur) ;
						JOptionPane.showMessageDialog(this,message ,"Avertissement", JOptionPane.WARNING_MESSAGE);
					} else {
						
						if (this.choix == DEPART) {
							this.dep_x = x_saisie ;
							this.dep_y = y_saisie ;
							this.dep = this.graph.map.Trouver_noeud_proche(this.dep_x, this.dep_y) ; 
						} 
						else if (this.choix == ARRIVEE) {
							this.arr_x = x_saisie ;
							this.arr_y = y_saisie ;
							this.arr = this.graph.map.Trouver_noeud_proche(this.arr_x, this.arr_y) ; 
						} 
						else {
							this.obj_x = x_saisie ;
							this.obj_y = y_saisie ;
							this.obj = this.graph.map.Trouver_noeud_proche(this.obj_x, this.obj_y) ; 
						}
						this.dispose() ;
					}
				
				//Si l'utilisateur n'a pas renseigné les champs
				} catch (NumberFormatException i) { 
					JOptionPane.showMessageDialog(this, "Vous n'avez pas renseigné tous les champs","Avertissement", JOptionPane.WARNING_MESSAGE);
				}	
			} 
			
			else if (this.choix == DIMENSION) {
				try {
					
					double l_saisie = new Double(this.saisie_l.getText()) ;
					double L_saisie = new Double(this.saisie_L.getText()) ;
					
					if (l_saisie<0 || L_saisie<0) {
						String message = new String("Les dimensions que vous demandez sont invalides, veuillez les choisir positives") ;
						JOptionPane.showMessageDialog(this,message ,"Avertissement", JOptionPane.WARNING_MESSAGE);
					} 
					else {
						this.largeur = l_saisie ;
						this.longueur = L_saisie ;
						this.dispose() ;
					}
					
				}
				catch (NumberFormatException i){
					JOptionPane.showMessageDialog(this, "Vous n'avez pas renseigné tous les champs","Avertissement", JOptionPane.WARNING_MESSAGE);
				}
				
			}
			
		}
		
		//**************************************//
		//***********Bouton annuler*************//
		//**************************************//
		else if (e.getSource() == annuler) {
			this.annule = true ;
			if (this.choix == OBJECTIFS) {
				this.graph.nbr_obj = 0 ;
				this.graph.tab_obj = null ;
			}
			this.dispose() ;
		}
		
		//**************************************//
		//************* Bouton ok **************//
		//**************************************//
		else if (e.getSource() == ok) {
			this.dispose() ;
		}
		
		//**************************************//
		//****Bouton changement d'objectifs ****//
		//**************************************//
		else if (e.getSource() == changerObj) {
			this.dispose();
			this.graph.actionObj() ;
		}
		
	}
	


}
