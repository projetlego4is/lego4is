import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.JFrame;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;


public class Mission {
	
	
	//*DEFINITION DES VARIABLES GLOBALES*//
	private static final double INF = Double.MAX_VALUE;
	
	
	//variables que l'on peut changer pour determiner de combien de fois le pas le robot avance
	private static final int NBR_POINT_AUTORISE_A_AVANCER = 2;
	
	
	//ATTENTION unique à chaque robot!!!!!!!!!
	private static final String BLUETOOTH_ADDRESS="00:16:53:06:da:cf";
	
	
	//verifier que les variables qui suivent soient les mêmes dans la classe du pc "Carte"
	private static final int VERS_HAUT = 1;
	private static final int VERS_BAS = 2;
	private static final int VERS_GAUCHE = 3;
	private static final int VERS_DROITE = 4;
	
	//verifier que les variables qui suivent soient les mêmes dans la classe du robot "WallE"
	
		//constante pour savoir s'il y a eu un obstacle
		private static final int PRESENCE_OBSTACLE = 1;		
		private static final int PAS_OBSTACLE = 2;
		
		//constantes pour le choix des parties du code 
		private static final int DEBUT = 1;
		private static final int NEXT_POINT =2 ;
		private static final int NEXT_POINT_IF =21 ;
		private static final int NEXT_POINT_ELSE =22 ;
		private static final int OBSTACLE =3 ;		
		private static final int OBJ_ATTEINT = 4;
		private static final int OBJ_NON_ATTEIGNABLE = 5;
		private static final int FIN = 6;
	
		
	
	//************************************************************************************************//
		//*****************fonction qui calcule la distance à parcourir entre deux points***************//
		//************************************************************************************************//
	
	public static float distanceAParcourir (Point actuel, Point objecif){
		return (float)Math.sqrt(Math.pow((actuel.y-objecif.y),2)+Math.pow((actuel.x-objecif.x),2));
	}
	
	
	//************************************************************************************************//
	//*****************fonction qui calcule l'angel de rotation du robot******************************//
	//************************************************************************************************//

	//fonction qui renvoie l'angle de rotation du robot necessaire pour pointer vers l'objectif
	// parametres : 1) Point : point de la carte où le robot se trouve
	//				2) Point : point de la carte de l'objectif à atteindre
	//				3) float : (0 à 360)angle cartesien en degree du robot (le zéro étant le pan horizontal inférieur de l'environnement)
	//resultat : angle en degree avec le signe adequat pour tourner vers l'objectif
	public static float  angleATourner(Point actuel, Point objecif, float directionRobot){
		double angleARetourner=0;	
		double angle =0;
		
		//cas où l'objectif est au-dessus du robot
		if (actuel.y-objecif.y <= 0){
			
			//calcul de l'angle que fait l'alignement des deux points par rapport au zéro cartesien
			if (actuel.x-objecif.x < 0){//l'objectif est sur la droite du robot
			angle = Math.atan((Math.abs(actuel.y-objecif.y))/(Math.abs(actuel.x-objecif.x)))*180/Math.PI;	
			}
			else if (actuel.x-objecif.x > 0){//l'objectif est sur la gauche du robot
				angle =180-(Math.atan((Math.abs(actuel.y-objecif.y))/(Math.abs(actuel.x-objecif.x)))*180/Math.PI);
			}
			else if ((actuel.x==objecif.x )&& (actuel.y!=objecif.y)){
				angle=90;
			}	
		
			angleARetourner = angle-directionRobot;
			if (Math.abs(angleARetourner) > 180){
				//on tourne dans l'autre direction pour aller plus vite
				angleARetourner = 360 - Math.abs(angleARetourner);
			}
		}
		
		//cas où l'objectif est au-dessous du robot
		if (actuel.y-objecif.y > 0){
			
			//calcul de l'angle que fait l'alignement des deux points par rapport au zéro cartesien
			if (actuel.x-objecif.x < 0){//l'objectif est sur la droite du robot
				angle = 360-Math.atan((Math.abs(actuel.y-objecif.y))/(Math.abs(actuel.x-objecif.x)))*180/Math.PI;	
				}
				else if (actuel.x-objecif.x > 0){//l'objectif est sur la gauche du robot
					angle =180+(Math.atan((Math.abs(actuel.y-objecif.y))/(Math.abs(actuel.x-objecif.x)))*180/Math.PI);
				}
				else if (actuel.x-objecif.x == 0){
					angle=270;
				}	
			
			
			
			angleARetourner = angle-directionRobot; 					
			if (Math.abs(angleARetourner) > 180){
				//on tourne dans l'autre direction pour aller plus vite
				angleARetourner = Math.abs(angleARetourner)-360;
			}
		}	
		
		
	//retourne l'angle de rotation que le robot doit effectuer
	return (float)angleARetourner;
	}
	
	


	//************************************************************************************************//
	//*****************fonction qui calcule l'orientaion du robot******************************//
	//************************************************************************************************//
	
	public static int orientationRobot(float direction){
		int orientation=0;
		if (direction>=45 && direction<135){
			orientation= VERS_HAUT;
		}
		if (direction>=135 && direction<225){
			orientation= VERS_GAUCHE;
		}
		if (direction>=225 && direction<315){
			orientation= VERS_BAS;
		}
		if (direction>=315 || direction<45){
			orientation= VERS_DROITE;
		}		
		return orientation;
	}
	

	//************************************************************************************************//
	//Fonction qui trouve le prochain objectif (celui qui minimise la distance par rappport au point de depart)//
	//************************************************************************************************//
	
	public static Point prochainObjectif(Interface_graphique graph, Point positionDepart){
		Point prochainPoint=null;
		double coutduPoint= INF;
		Point[] nouveau_tab  ;
		
		if (graph.tab_obj==null){
			prochainPoint=graph.arr;			
		}
		else {
			int i;
			
			for (i=0; i<graph.tab_obj.length; i++) {
				
				Dijkstra dij= new Dijkstra(graph.map,positionDepart,graph.tab_obj[i]);
				dij.Run_Dijkstra() ;
				
				if (dij.tab_cout[graph.tab_obj[i].name] < coutduPoint) {
					coutduPoint = dij.tab_cout[graph.tab_obj[i].name] ;
					prochainPoint = graph.tab_obj[i] ;
				}
			}
			
			
			//Enleve le point d'objectif choisi du tableau des objectifs
			if (graph.tab_obj.length == 1) {
				nouveau_tab = null ;
			} else {
				nouveau_tab = new Point[graph.tab_obj.length -1] ;
				int j = 0 ;
				for (i=0; i<graph.tab_obj.length; i++)  {
					
					if (graph.tab_obj[i] != prochainPoint) {
						
						nouveau_tab[j] = graph.tab_obj[i] ;
						j++ ;
					}
				}
			}
			graph.tab_obj = nouveau_tab ;
		}
		return prochainPoint ;
	}
	
	//************************************************************************************************//
		//*fonction qui calcule le nouvel itérateur pour ne pas que le robot s'arrête tous les point****//
		//************************************************************************************************//
	
	
	//calcul du prochain point pour avec le même x ou même y pour pas
	//que le robot fasse des acouts mais plutôt avance en ligne droite
	//mais au max il peut faire pas plus de nbrPointAutorise
	public static int IduProchainPoint(Point positionDepart, Dijkstra dij, int iterateur){
	
	boolean continu= true;							
	int nbrIteration=0;	
	if (positionDepart.x==dij.Get_Chemin()[iterateur].x) {
		while( (iterateur<dij.Get_Chemin().length) && (continu == true) && (nbrIteration<NBR_POINT_AUTORISE_A_AVANCER) ){
			if (positionDepart.x==dij.Get_Chemin()[iterateur].x){
				iterateur++;
				nbrIteration++;
			}
			else{
				continu = false;
			}
		}
	}											
	
	else if (positionDepart.y==dij.Get_Chemin()[iterateur].y) {							
		while( (iterateur<dij.Get_Chemin().length) && (continu == true) && (nbrIteration<NBR_POINT_AUTORISE_A_AVANCER) ){
			if (positionDepart.y==dij.Get_Chemin()[iterateur].y){
				iterateur++;
				nbrIteration++;
			}
			else{
				continu = false;										
			}
		}			
	}
	//pour revenir au point qui faut 
	 iterateur--;
	return iterateur;														
	//code avant à mettre dans une fonction à part
	
}
	
	
	   //******************************************************************************************//
	 //*******************************|\  /|   /\   | |\  |*****************************************//
	//********************************| \/ |  /--\  | | \ |*****************************************//
	 //*******************************|    | /    \ | |  \|***************************************//
	  //**************************************************************************************//
	

	public static void main(String[] args) {
		
		//Ouverture de l'interface graphique
		Interface_graphique graph = new Interface_graphique() ;	
		graph.setLocation(300, 100); //Emplacement de ma fenetre sur l'écran
		graph.pack(); //Calcule la dimension de la fenetre pour qu'elle s'adapte au contenu
		graph.setVisible(true); //Rend visible la fenetre
		graph.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Termine l'appli quand on ferme la fenetre
		
		//Attente de la fermeture de l'interface graphique
		while (graph.isVisible());
		
		//On enleve des arcs de la carte pour modéliser un obstacle
		/*graph.map.enlever_zone(15, 30, 4*0, 10, 4) ;
		
		graph.map.enlever_zone(60, 10, 25, 50, 1) ;
		graph.map.enlever_zone(60, 20, 60, 90, 1) ;*/
		
		try {
			
			//Connexion bluetooth
			NXTComm nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
			NXTInfo nxtInfo = new NXTInfo(NXTCommFactory.BLUETOOTH, "NXT", BLUETOOTH_ADDRESS);//dernier paramêtre change si on change de robot
			nxtComm.open(nxtInfo);
			OutputStream os=nxtComm.getOutputStream();
			InputStream is=nxtComm.getInputStream();
			DataOutputStream dos = new DataOutputStream(os);	
			DataInputStream dis = new DataInputStream(is);
			
			Point positionDepart =graph.dep;
			Point prochainObj=null;
			float angle = 0;
			float distance = 0;
			float direction = 0 ;
			int obstacle=PAS_OBSTACLE;
			int orientationR=VERS_DROITE;			
			boolean obj_non_atteignable=false;
			boolean dernier_voyage = false ;
			boolean on_continue = true;
			int i=1;
			int j=0;
			Dijkstra dij=null;
			
			int toDo=DEBUT;
			
			try {
				
				
				while(on_continue==true){
					
					dos.writeInt(toDo) ; //Pour indiquer au robot quelle partie du code il doit executer
					dos.flush();
					System.out.println("On envoie ce que le robot doit faire = " + toDo) ;	
					
					switch (toDo) {					
					
					//****************DEBUT*************************//
					case DEBUT:				
						
						//On cherche le prochain objectifs
						prochainObj=prochainObjectif(graph, positionDepart);
						System.out.println("POSITION DEPART :" + positionDepart) ;
						System.out.println("PROCHAIN OBJ :" + prochainObj) ;
						
						if (prochainObj == graph.arr) {
							dernier_voyage = true ;
						}
						//on calcule le chemin à faire
						dij= new Dijkstra(graph.map,positionDepart,prochainObj);
						dij.Run_Dijkstra();
						
						//on s'assure qu'il y a effectivement un chemin
						try {
							if (dij.Get_Chemin()[1] != null) {
								System.out.println("Il y a un chemin je peux y aller") ;
								toDo=NEXT_POINT;
							}
						} catch (NullPointerException e ){
							System.out.println("L'objectif n'est pas atteignable "+e) ;
							toDo=OBJ_NON_ATTEIGNABLE;	
						}
						
						
						break;
					
						
					//****************NEXT_OBJ*************************//
					case NEXT_POINT :
						if (i<dij.Get_Chemin().length){
							//on envoie au robot dans quelle partie du if on est
							dos.writeInt(NEXT_POINT_IF) ;
							dos.flush() ;
							
							//calcul du prochain itérateur pour ne pas s'arrêter à tous les points
							i=IduProchainPoint(positionDepart,dij, i);
							
							//on reçoit la direction du robot
							direction= dis.readFloat() ;
							System.out.println("direction du robot: "+direction); 
							
							//on calcule l'angle a tourner et la distance à parcourir pour aller au prochain point
							angle = angleATourner(positionDepart, dij.Get_Chemin()[i],direction);								
							distance = distanceAParcourir(positionDepart, dij.Get_Chemin()[i]) ;
							
							//on les envoie au robot
							dos.writeFloat(distance) ;
							dos.flush() ;
							System.out.println("Envoi distance = "+distance); 
							dos.writeFloat(angle) ;
							dos.flush() ;
							System.out.println("Envoi angle = "+angle); 
							
							//obstacle ou pas?								
							obstacle=dis.readInt();//on recoit s'il y a obstacle							
							System.out.println("obstacle? =" +obstacle); 
							
							if (obstacle==PRESENCE_OBSTACLE){
								toDo=OBSTACLE;
								}
							else {
								//on réinitialise la position de départ 
								//tout en gardant la trace du numéro de l'itérateur du nouveau point de départ 
								//et on incrémente l'itérateur 								
								positionDepart = dij.Get_Chemin()[i] ;
								j=i;
								i++;								
								}
								
							}
						else{
							//on envoie au robot dans quelle partie du if on est
							dos.writeInt(NEXT_POINT_ELSE) ;
							dos.flush() ;
							//on est à la fin d'un objectif
							if(dernier_voyage==true){
								toDo=FIN;
								}
							else {
								toDo=OBJ_ATTEINT;	
								}
							
							}
						break;
						
					//****************OBSTACLE*************************//	
					case OBSTACLE :
						System.out.println("Il y a un obstacle");
						
						//on récupére la direction du robot
						direction= dis.readFloat() ;
						System.out.println("direction du robot : "+direction); 
						
						//on calcule son orientation pour enlever la zone qu'il faut
						orientationR=orientationRobot(direction);
						System.out.println("orientation du robot " +orientationR);
						
						//on récupére la distance que le robot a parcourue
						float distanceRoule=dis.readFloat();
						//on calulce le nbre de pas qu'il a effectué
						int nbrePas=(int)(distanceRoule/graph.pas);//distanceRoule reçu est négatif du fait de la position inveerse des moteurs
						System.out.println("Nbre de pas parcouru = "+nbrePas);
						//on calcule la distance qu'il doit reculer
						float dist =(float) (distanceRoule-nbrePas*graph.pas);
						System.out.println("Distance a reculer = "+dist);
						//on l'envoit au robot
						dos.writeFloat(dist) ;
						dos.flush() ;
						System.out.println("Envoi distanceARouler car obstacle =" +dist);
						//on remet l'iterateur de point et la positiondeDepart là où le robot s'est arrêté
						i=j+Math.abs(nbrePas);
						positionDepart = dij.Get_Chemin()[i] ;
						//on enlève la zone qu'il faut
						graph.map.enlever_zone (20, 20,dij.Get_Chemin()[i].x , dij.Get_Chemin()[i].y, orientationR);
						graph.map.enlever_arc(dij.Get_Chemin()[i], dij.Get_Chemin()[i+1]) ;	
						//on recalcule le chemin le plus court pour y aller
						dij= new Dijkstra(graph.map,positionDepart,prochainObj);
						dij.Run_Dijkstra();
						//on s'assure qu'il y a effectivement un chemin
						try {
							if (dij.Get_Chemin()[1] != null) {
								System.out.println("Malgre l'obstacle, il y a un chemin je peux y aller") ;
							}
						} catch (NullPointerException e ){
							System.out.println("A cause de l'obstacle, l'objectif n'est pas atteignable") ;
							obj_non_atteignable=true;							
						}
						//on remet les itérateurs à zéro
						i=1;
						j=0;
						
						if (obj_non_atteignable==false){
							toDo=NEXT_POINT;
						}
						else {
							toDo=OBJ_NON_ATTEIGNABLE;
						}
						obj_non_atteignable=false;
						break;
						
						
					//****************OBJ_ATTEINT*************************//		
					case OBJ_ATTEINT:
						System.out.println("WAHOU!!!!!!OBJECTIF ATTEINT!");
						toDo=DEBUT;
						i=1;
						j=0;						
						break; 
					
					//****************OBJ_NON_ATTEIGNABLE*************************//
					case OBJ_NON_ATTEIGNABLE:
						System.out.println("ECHEC!!!!!!OBJECTIF NON ATTEIGNABLE!");
						if (dernier_voyage==true){
							toDo=FIN;
							}
						else {
							toDo=DEBUT;
						}
						break; 
					
					
					//****************FIN*************************//
					case FIN :
						System.out.println("ON EST ARRIVE OU ON NE PEUT PAS ATTEINDRE L'ARRIVEE");
						//on ferme la connexion
						dos.close() ;
						dis.close() ;
						os.close() ;
						is.close() ;
						nxtComm.close() ;
						
						//on a fini
						on_continue=false;
						break; 
									
				default:
					break;
				}
				}
						
			}
		    catch (IOException e) {
				System.out.println(" erreur a l'ecriture "+e); 
				e.printStackTrace();
			}
			
	 }				
	catch (NXTCommException e) {
		System.out.println(" erreur a l'etablissement de la connection du côte PC "+e); 
		e.printStackTrace();
		}
	
//fin main		
 }
	
	
	
	

//fin classe
}
		
		
