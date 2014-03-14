package superviseur;
import java.awt.Color; 

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import commun.Constantes;

  
public class GUI extends JFrame {
	/**
	 * Ici est codée la partie traitement des message de la GUI
	 * Elle ne fait que recevoir les message venant du Supervisuer
	 * Et mettre en place le premier contenueur "fond"
	 */
	private static final long serialVersionUID = 2669697481571631795L;
	private Superviseur chef;
	private Fond fond;
	
	
	
	
	public GUI(Superviseur master){     
	  
    this.setTitle("Mastodonte");
    this.setSize(900, 630);
    this.setLocationRelativeTo(null);               
    this.chef=master;
    //On prévient notre JFrame que notre JPanel sera son content pane
    this.fond= new Fond(this.chef);
    this.setContentPane(this.fond);               
    this.setVisible(true); 
  } 
	
	public Carte getCarte()
	{
		return this.fond.getCarte();
	}
	
	public void cibleCaptured()
	{
		this.fond.getCarte().setIsCaptured(true);
	}
	public boolean getLancer(){
		return this.fond.getLancer();
	}

}

class Fond extends JPanel { 
	/**
	 * Ce conteneur est le contenuer principal 
	 * il dessine les boutons, le bg, la carte
	 * ATTENTION pour que ceci marche il faut inclure 
	 * les image de imagetut (pas de lien virtuel)
	 */
	private static final long serialVersionUID = -8520576563003529754L;
	private Superviseur chef;
	private Bouton droite;
	private Bouton gauche;
	private Bouton bas;
	private Bouton haut;
	private Bouton allumer;
	private Bouton Bob;
	private Bouton NXT;
	private Bouton Robot9;
	private Bouton Robot10;
	private JLabel selectProie;
	private JLabel selectPred;
	private Carte carte;
	private boolean lancer;

	
	
	public Fond(Superviseur master){
		super();
		this.chef=master;
		this.lancer=false;
		
		Bouton droite= new Bouton("droite",this.chef,this);
	    droite.setPreferredSize(new Dimension(120,121));
	    this.droite=droite;
	    
	    Bouton gauche= new Bouton("gauche",this.chef,this);
	    gauche.setPreferredSize(new Dimension(120,121));
	    this.gauche=gauche;
	    
	    Bouton haut= new Bouton("haut",this.chef,this);
	    haut.setPreferredSize(new Dimension(120,60));
	    this.haut=haut;
	    
	    Bouton bas= new Bouton("bas",this.chef,this);
	    bas.setPreferredSize(new Dimension(120,60));
	    this.bas=bas;
	    
	    Bouton allumer= new Bouton("allumer",this.chef,this);
	    allumer.setPreferredSize(new Dimension(47,45));
	    this.allumer=allumer;
	    
	    Bouton Bob= new Bouton(Constantes.Bob,this.chef,this);
	    Bob.setPreferredSize(new Dimension(47,45));
	    this.Bob=Bob;
	    
	    Bouton NXT= new Bouton(Constantes.NXT,this.chef,this);
	    NXT.setPreferredSize(new Dimension(47,45));
	    this.NXT=NXT;
	    
	    Bouton Robot9= new Bouton(Constantes.Robot9,this.chef,this);
	    Robot9.setPreferredSize(new Dimension(47,45));
	    this.Robot9=Robot9;
	    
	    Bouton Robot10= new Bouton(Constantes.Robot10,this.chef,this);
	    Robot10.setPreferredSize(new Dimension(47,45));
	    this.Robot10=Robot10;
	    
	    Font font= new Font(Font.SERIF,Font.BOLD,36);
	    this.selectProie= new JLabel("Choissisez la proie");
	    this.selectProie.setFont(font);
	    this.selectProie.setForeground(Color.decode("10807276"));
	   
	  
	    this.selectPred= new JLabel("Choissisez les predateurs");
	    this.selectPred.setFont(font);
	    this.selectPred.setForeground(Color.decode("10807276"));
	    

	    Carte carte= new Carte(this.chef.getRobots());
	    carte.setPreferredSize(new Dimension (445,368));
	    this.carte=carte;
	    
	    
	    
	    this.setFocusable(true);
	    this.addKeyListener(new ClavierListener(this));	  
	}
	
	

	/**
	 * Petite methode qui retourne le chemin courant
	 */
	public static String getpath()
	{
		String path=System.getProperty("user.dir");
	  	char [] tab= new char[path.length()];
	  	tab= path.toCharArray();
	  	for(int i =0; i<path.length(); i++)
	  	{
	  		if (tab[i]=='\\') tab[i]='/';	
	  	}
	  	path=String.valueOf(tab);
		return path;
	}
	
	/**
	 * Peint le fond de la GUI (arriere plan+ bouton) et instancie la carte
	 */
	 public void paintComponent(Graphics g){
	   

	  	//on ajoute les boutons sur le fond
	    this.add(this.droite);  
	    this.add(this.gauche);
	    this.add(this.haut);
	    this.add(this.bas);
	    this.add(this.allumer);
	    this.add(this.carte);
	    this.add(this.Bob);
	    this.add(this.NXT);
	    this.add(this.Robot9);
	    this.add(this.Robot10);
	    this.add(this.selectPred);
	    this.add(this.selectProie);

	    // on place les boutons
	    this.droite.setLocation(707, 252);
	    this.gauche.setLocation(75, 252);
	    this.haut.setLocation(390, 45);
	    this.bas.setLocation(390, 515);
	    this.allumer.setLocation(145, 125); 
	    this.carte.setLocation(228, 126);
	    this.Bob.setLocation(110, 475);
	    this.NXT.setLocation(322, 475);
	    this.Robot9.setLocation(533, 475);
	    this.Robot10.setLocation(744, 475);
	    this.selectPred.setLocation(370, 100);
	    this.selectProie.setLocation( 370, 100);
	    
	    //si on a pas lancer le jeu on affiche l'acceuil
	    if (!this.lancer)
	    {
	  	  try {
	  		  	Image img;
	  			img= ImageIO.read(new File(Fond.getpath()+"/imagetut/acceuil.jpg"));
	  			g.drawImage(img,0,0,this);
	  			g.setColor(Color.decode("10807276"));
	  			Font font= new Font(Font.SERIF,Font.BOLD,24);
	  			g.setFont(font);
	  			g.drawString(Constantes.Bob, 110, 546);
	  			g.drawString(Constantes.NXT, 322, 546);
	  			g.drawString(Constantes.Robot9, 533, 546);
	  			g.drawString(Constantes.Robot10, 744, 546);
	  			
	  			//si on est en train de selectionner les predateur
	  			if(Bouton.isPred) {
	  				this.selectProie.setVisible(false);
	  				this.selectPred.setVisible(true);
	  			}
	  			//si on est en train de selectionner la proie
	  			else {
	  				this.selectPred.setVisible(false);
	  				this.selectProie.setVisible(true);
	  			}
	  			
	  	    } catch (IOException e) {
	  			e.printStackTrace();
	  		}
	  	  
	  	  	//on cache les boutons de controle de la carte
		    this.droite.setVisible(false);
		    this.gauche.setVisible(false);
		    this.haut.setVisible(false);
		    this.bas.setVisible(false);
		    this.carte.setVisible(false);
	    }
	    
	    //si le jeu est lancée (carte active)
	    else
	    {
	    	
	  	  try {
	  		  	Image img;
	  			img= ImageIO.read(new File(Fond.getpath()+"/imagetut/fond.jpg"));
	  			g.drawImage(img,0,0,this);		
	  		
	  	    } catch (IOException e) {
	  			e.printStackTrace();
	  		}
	  	  
	  	  	// on cache la selection des robots
		  	this.Bob.setVisible(false);
		    this.NXT.setVisible(false);
		    this.Robot9.setVisible(false);
		    this.Robot10.setVisible(false);
		    this.selectPred.setVisible(false);
			this.selectProie.setVisible(false);

			//on affiche les boutons de controle de la carte et... La carte!
	    	this.droite.setVisible(true);
		    this.gauche.setVisible(true);
		    this.haut.setVisible(true);
		    this.bas.setVisible(true);
		    this.carte.setVisible(true);
	    }
	    
	  }
	 public Bouton getDroite()
	 {
		 return this.droite;
	 }
	 
	 public Bouton getGauche()
	 {
		 return this.gauche;
	 }
	 
	 public Bouton getHaut()
	 {
		 return this.haut;
	 }
	 
	 public Bouton getBas()
	 {
		 return this.bas;
	 }
	 
	 public void reFocus()
	 {
		 this.requestFocus();
	 }
	 
	 
	 public Carte getCarte()
	 {
		 return this.carte;
	 }
	 public Superviseur getChef()
	 {
		 return this.chef;
	 }
	 
	 public void setLancer(boolean launch){
			this.lancer=launch;
		}
		
		public boolean getLancer(){
			return this.lancer;
		}
	}


 class Bouton extends JButton implements MouseListener{
	  /**
	 * Bouton un peu customiser pour envoyer les message 
	 * lors du clic sur le bouton, et changer d'apparence quand 
	 * on navigue dessus
	 */
	private static final long serialVersionUID = -3688840306468459941L;
	private String name;
	  private Image img;
	  private Superviseur chef;
	  private Fond father;
	  private boolean stayActive;
	  public static boolean isPred=false;
	  
	  public Bouton(String str,Superviseur master,Fond father){
	    super();
	    this.chef=master;
	    this.name=str;
	    this.addMouseListener(this); //ajout d'une fonctionnalite qui ecoute les evenement envoye depuis la sourie
	    this.father=father;
	    this.stayActive=false;
	    Border noBorder = new LineBorder(Color.WHITE,0); //permet d'eviter d'avoir des bordure sur les boutons
		this.setBorder(noBorder);
		
		this.setInactive();
	  }
	  
	  /**
	   * on peint l'image du bouton adequate
	   */
	  public void paintComponent(Graphics g){
		  
		  	g.drawImage(this.img,0,0,this);
		  	this.father.reFocus();
		  	
	  }
	  
	 
	  public void mouseClicked(MouseEvent event) {
	    //Inutile mias obligatoire (implement)
	  }
	 /**
	  * Lorsque la souris rentre sur le bouton on change d'apparence
	  */
	  public void mouseEntered(MouseEvent event) {    
		  this.setActive();
	  }
	  /**
	   * Ici on remet le bouton de base lorque la souris sort du bouton
	   */
	 
	  public void mouseExited(MouseEvent event) {
		  this.setInactive();
	  }
	   /**
	     * Ici on envoi l'ordre lorsque le souris est préssée
	     */
	  public void mousePressed(MouseEvent event) {
		  
		if(!this.stayActive)this.executeOrder(); //si bouton non bloque on va executer l'ordre du bouton
	    this.father.reFocus();						//on rend le focus au fond (focus= celui qui interpretera les evenements des IO comme lui etant destine)
	  }
	 
	  public void mouseReleased(MouseEvent event) {
	       
	  }   
	  /**
	   * Regroupe les ordre a executer sur lors du clic sur les boutons, different bien sur suivant les id
	   */
	  public void executeOrder(){
		 
			  
		   if(!this.chef.getSimMode()){
			  //Boutons de commande de la carte
			  
			  //Pour les fleches on tourne pour se realigner sur l'axe demandé
			  if (this.name=="droite"){
				  this.chef.getProie().TournerInfini(true);
			  }
			    else if (this.name=="gauche"){
					this.chef.getProie().TournerInfini(false);
			    }
				else if (this.name=="bas"){
					this.chef.getProie().Tourner(180);
					this.chef.getProie().Avancer(true);
					
				}
				else if (this.name=="haut"){
					this.chef.getProie().Avancer(true);
					
				}
		   }else{
			   if (this.name=="droite"){
					for(int i=0; i<this.chef.getPredateurs().length;i++){
						this.chef.getPredateurs()[i].getCoord().setX(this.chef.getPredateurs()[i].getCoord().getX()-10);
					}
				  }
			    else if (this.name=="gauche"){
			    	for(int i=0; i<this.chef.getPredateurs().length;i++){
						this.chef.getPredateurs()[i].getCoord().setX(this.chef.getPredateurs()[i].getCoord().getX()+10);
					}
			    }
				else if (this.name=="bas"){
					for(int i=0; i<this.chef.getPredateurs().length;i++){
						this.chef.getPredateurs()[i].getCoord().setY(this.chef.getPredateurs()[i].getCoord().getY()+10);
					}
					
				}
				else if(this.name=="haut"){
					for(int i=0; i<this.chef.getPredateurs().length;i++){
						this.chef.getPredateurs()[i].getCoord().setY(this.chef.getPredateurs()[i].getCoord().getY()-10);
					}
				}
		   }
		  //Bouton de lancement de la carte
			 if (this.name=="allumer") {
				this.father.getCarte().setRobots(this.chef.getPredateurs());
				this.father.getCarte().MAJ();
				this.father.setLancer(!this.father.getLancer());
				if (this.father.getLancer()==false) {
					this.chef.endOfGame();
				}
				this.father.repaint();
				
			}
			  
		  //Boutons de la selection des robots
		  
		  //si on selectionnne la proie (isPred=false) on l'enregistre comme tel
		  //et vice et versa
			else if (this.name==Constantes.Bob) {
				this.chef.addVirtualRobot(this.name, Constantes.ADR_BOB,Bouton.isPred);
				this.setActive();
				this.stayActive=true;
				Bouton.isPred=true;
				this.father.repaint();
				
			}
			else if (this.name==Constantes.NXT){
				this.chef.addVirtualRobot(this.name, Constantes.ADR_NXT,Bouton.isPred);
				this.setActive();
				this.stayActive=true;
				Bouton.isPred=true;
				this.father.repaint();
			}
			else if (this.name==Constantes.Robot9) {
				this.chef.addVirtualRobot(this.name, Constantes.ADR_ROBOT9,Bouton.isPred);
				this.setActive();
				this.stayActive=true;
				Bouton.isPred=true;
				this.father.repaint();
			}
			else if (this.name==Constantes.Robot10){
				this.chef.addVirtualRobot(this.name, Constantes.ADR_ROBOT10,Bouton.isPred);
				this.setActive();
				this.stayActive=true;
				Bouton.isPred=true;
				this.father.repaint();
			}
		  
	  }
	  /**
	   * Change l'apparence du bouton lorsqu'on est dessus
	   */
	  public void setActive()
	  {
		  try{
			  if (this.name=="droite")
					this.img= ImageIO.read(new File(Fond.getpath()+"/imagetut/droite_vert.jpg"));
				  	else if (this.name=="gauche") this.img= ImageIO.read(new File(Fond.getpath()+"/imagetut/gauche_vert.jpg"));
				  	else if (this.name=="haut")	this.img= ImageIO.read(new File(Fond.getpath()+"/imagetut/haut_vert.jpg"));
				  	else if (this.name=="bas")this.img= ImageIO.read(new File(Fond.getpath()+"/imagetut/bas_vert.jpg"));
				  	else if (this.name=="allumer")this.img= ImageIO.read(new File(Fond.getpath()+"/imagetut/allumer.jpg"));
				  	else if (this.name==Constantes.Bob)this.img= ImageIO.read(new File(Fond.getpath()+"/imagetut/allumer3.jpg"));
				  	else if (this.name==Constantes.NXT)this.img= ImageIO.read(new File(Fond.getpath()+"/imagetut/allumer2.jpg"));
				  	else if (this.name==Constantes.Robot9)this.img= ImageIO.read(new File(Fond.getpath()+"/imagetut/allumer2.jpg"));
				  	else if (this.name==Constantes.Robot10)this.img= ImageIO.read(new File(Fond.getpath()+"/imagetut/allumer3.jpg"));
		  	}
			  catch(IOException e){
			  e.printStackTrace();
			  }
		  this.father.reFocus();
		  this.repaint();
	  }
	  
	  /**
	   * Change l'apparence du bouton quand on est pas dessus
	   */
	  public void setInactive()
	  {
		  if (!this.stayActive){
				try{
				  if (this.name=="droite")
						this.img= ImageIO.read(new File(Fond.getpath()+"/imagetut/droite.jpg"));
					  	else if (this.name=="gauche") this.img= ImageIO.read(new File(Fond.getpath()+"/imagetut/gauche.jpg"));
					  	else if (this.name=="haut")	this.img= ImageIO.read(new File(Fond.getpath()+"/imagetut/haut.jpg"));
					  	else if (this.name=="bas")this.img= ImageIO.read(new File(Fond.getpath()+"/imagetut/bas.jpg"));
					  	else if (this.name=="allumer")this.img= ImageIO.read(new File(Fond.getpath()+"/imagetut/allumer.jpg"));
					  	else if (this.name==Constantes.Bob)this.img= ImageIO.read(new File(Fond.getpath()+"/imagetut/eteint2.jpg"));
					  	else if (this.name==Constantes.NXT)this.img= ImageIO.read(new File(Fond.getpath()+"/imagetut/eteint.jpg"));
					  	else if (this.name==Constantes.Robot9)this.img= ImageIO.read(new File(Fond.getpath()+"/imagetut/eteint.jpg"));
					  	else if (this.name==Constantes.Robot10)this.img= ImageIO.read(new File(Fond.getpath()+"/imagetut/eteint2.jpg"));
				  
				  }
			  catch(IOException e){
			  e.printStackTrace();
			  }  
		  }
		  
		  this.father.reFocus();
		  this.repaint();
	  }
	}
 
 /**
  * Permet de mettre en place le controle du deplacement par clavier(fleches)
  */
 class ClavierListener implements KeyListener{
	
	 private Fond father;
	 private boolean enfonce;
	 public ClavierListener(Fond father)
	 {
		 this.enfonce=false;
		 this.father=father;
	 }
	 
	/**
	 * Quand la touche est enfoncée on envoi un ordre et on change le bouton d'etat
	 */
	 public void keyPressed(KeyEvent event) {
	 if(!this.enfonce){
	     switch(event.getKeyCode())
	     {
	     case KeyEvent.VK_UP:
	    	 this.father.getHaut().setActive();
	    	 this.father.getHaut().executeOrder();
	     break;
	     case KeyEvent.VK_DOWN:
	    	 this.father.getBas().setActive();
	    	 this.father.getBas().executeOrder();
	     break;
	     case KeyEvent.VK_RIGHT:
	    	 this.father.getDroite().setActive();
	    	 this.father.getDroite().executeOrder();
	     break;
	     case KeyEvent.VK_LEFT:
	    	 this.father.getGauche().setActive();
	    	 this.father.getGauche().executeOrder();
	     break;
	     default:System.out.println("touche non reconnue");
	     break;
	     }
	     this.enfonce=true;
	   }
	 }
	 
	 /**
	  * Quand le touche est libérer on repasse en mode normal pour les boutons
	  */
	
	   public void keyReleased(KeyEvent event) {
		   switch(event.getKeyCode())
		     {
		     case KeyEvent.VK_UP:this.father.getHaut().setInactive();
		     break;
		     case KeyEvent.VK_DOWN:this.father.getBas().setInactive();
		     break;
		     case KeyEvent.VK_RIGHT:this.father.getDroite().setInactive();
		     break;
		     case KeyEvent.VK_LEFT:this.father.getGauche().setInactive();
		     break;
		     default:System.out.println("autre");
		     break;
		     }
		   this.enfonce=false;
		   this.father.getChef().getProie().Stop();
	   }
	
	   public void keyTyped(KeyEvent event) {
	     
	   }   
 }
 

 
