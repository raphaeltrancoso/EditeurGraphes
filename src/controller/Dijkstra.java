package controller;

import java.util.ArrayList;

import javafx.animation.FillTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.util.Duration;
import model.Arete;
import model.Cercle;
import model.Figure;
import model.Rect;

public class Dijkstra {
	
	ArrayList<Figure> liste;
	ArrayList<Figure> listeSucc;
	ArrayList<Figure> chemin;
	Figure firstNode, lastNode;
	int poidsTotalMin;
	int nbSommets = 1;
	Figure figureSuivante;
	boolean allVisited;
	boolean finished;
	Color couleurCheminPC = Color.DEEPSKYBLUE;
	Color couleurAreteVisitee = Color.CORAL;

	//ArrayList<Figure> alg = new ArrayList<>();
	
	public Dijkstra(ArrayList<Figure> listeFigure){
		this.liste = listeFigure;

		for(Figure f : liste){
			if(f.getFirstNode() == true){
				firstNode = f;
			} 
			if(f.getLastNode() == true){
				lastNode = f;
			}
		}
		listeSucc = new ArrayList<Figure>();
		chemin = new ArrayList<Figure>();
	}
	
	public ArrayList<Figure> getListe(){
		return liste;
	}
	
	public Figure getFirstNode(){
		return firstNode;
	}
	
	public void addListeSucc(Figure f){
		listeSucc.add(f);
	}
	
	public ArrayList<Figure> getListeSucc(){
		return listeSucc;
	}
	
	public Figure figureSuivante(){
		Figure f = null;
		for(Figure figure : listeSucc){
			if(f != null && figure.getVisited() == false){
				if(figure.getPoidsMin() < f.getPoidsMin()){
					f = figure;
				}
			}else if(f == null && figure.getVisited() == false){
				f = figure;
			}
		}
		return f;
	}
	
	public Figure figure(Figure f){
		for(Figure figure : liste){
			if(figure == f){
				return figure;
			}
		}
		return null;
	}
	
	int duration = 0;
	
	public void init(){
		Timeline timeline = new Timeline();
		for(Figure v : liste){
			if(v.getFirstNode() == false) v.setPoidsMin(1000000000);
		}
		/*pour  chaque figure de la liste*/
		for(Figure voisine : liste){		
			if(voisine.getListeAretes() != null){
				/*pour chaque arete reliée à la figure*/
				for(Arete arete : figure(firstNode).getListeAretes()){
					/*si une figure de la liste est reliée a l'arete*/
					if(voisine.getCenterX() == arete.getEndX() && voisine.getCenterY() == arete.getEndY()){
						/*alors il s'agit d'un voisin et on met a jour son poids*/
						voisine.setPoidsMin(arete.getPoids());
						String t = String.valueOf(voisine.getPoidsMin());
						timeline.getKeyFrames().add(new KeyFrame(Duration.millis(2000),
					    		new KeyValue ((voisine.getPds()).textProperty(), t),
					    		new KeyValue (arete.strokeProperty(), couleurAreteVisitee)
					    ));
						listeSucc.add(voisine);
					}
				}
			}
			
		}
		
		figure(firstNode).setVisited(true);
		FillTransition ft = new FillTransition(Duration.seconds(1), (Shape) firstNode, firstNode.getCouleurNoeud(), couleurAreteVisitee);
		ft.setCycleCount(1);
		ft.setDelay(Duration.seconds(duration));
		ft.play();
		ft.setOnFinished(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				timeline.play();		
			}
			
		});
		duration += 3;
		figureSuivante = figureSuivante();
	}
	
	public void algoNoeud(Figure figSuiv){
		Timeline timeline = new Timeline();
		if(figSuiv == lastNode){
			figSuiv.setVisited(true);
			for(Figure voisine : liste){
				if(voisine.getVisited() == true && voisine.getListeAretes() != null){
					if(figSuiv.getListeAretes() != null){
						/*pour chaque arete reliée à la figure ou l'on est positionné*/
						for(Arete arete : figSuiv.getListeAretes()){
							/*si une figure de la liste est reliée a l'arete*/
							if(voisine.getCenterX() == arete.getStartX() && voisine.getCenterY() == arete.getStartY()){
								/*alors il s'agit d'un voisin et on met a jour son poids*/
								if(figSuiv.getPoidsMin() == voisine.getPoidsMin() + arete.getPoids()){
									timeline.getKeyFrames().add(new KeyFrame(Duration.millis(2000), new KeyValue(arete.strokeProperty(), couleurCheminPC)));
								}
							}
						}
					}
				}
			}
			chemin.add(figSuiv);
		}
		if(figSuiv != null){
			/*pour  chaque figure de la liste*/
			for(Figure voisine : liste){
				if(voisine.getVisited() == false && voisine.getListeAretes() != null){
					if(figSuiv.getListeAretes() != null){
						/*pour chaque arete reliée à la figure ou l'on est positionné*/
						for(Arete arete : figSuiv.getListeAretes()){
							/*si une figure de la liste est reliée a l'arete*/
							if(voisine.getCenterX() == arete.getEndX() && voisine.getCenterY() == arete.getEndY()){
								/*alors il s'agit d'un voisin et on met a jour son poids*/
								if(figSuiv.getPoidsMin() + arete.getPoids() < voisine.getPoidsMin()){
									chemin.add(figSuiv);
									chemin.remove(voisine);
									voisine.setPoidsMin(figSuiv.getPoidsMin() + arete.getPoids());
									String t = String.valueOf(voisine.getPoidsMin());
									timeline.getKeyFrames().add(new KeyFrame(Duration.millis(2000),
								    		new KeyValue ((voisine.getPds()).textProperty(), t),
								    		new KeyValue (arete.strokeProperty(), couleurAreteVisitee)
								    ));

									listeSucc.add(voisine);
									
								}
							}
						}
					}
				}
				if(voisine.getVisited() == true && voisine.getListeAretes() != null){
					if(figSuiv.getListeAretes() != null){
						/*pour chaque arete reliée à la figure ou l'on est positionné*/
						for(Arete arete : figSuiv.getListeAretes()){
							/*si une figure de la liste est reliée a l'arete*/
							if(voisine.getCenterX() == arete.getStartX() && voisine.getCenterY() == arete.getStartY()){
								/*alors il s'agit d'un voisin et on met a jour son poids*/
								if(figSuiv.getPoidsMin() == voisine.getPoidsMin() + arete.getPoids()){
									timeline.getKeyFrames().add(new KeyFrame(Duration.millis(2000), new KeyValue(arete.strokeProperty(), couleurCheminPC)));
								}else{
									for(Arete ar : voisine.getListeAretes()){
										if(ar != arete){
											timeline.getKeyFrames().add(new KeyFrame(Duration.millis(2000), new KeyValue(ar.strokeProperty(), couleurAreteVisitee)));
										}
									}
								}
							}
						}
					}
				}
			}
		}
		listeSucc.remove(figSuiv);
		if(figSuiv != null){
			figSuiv.setVisited(true);
			FillTransition ft = new FillTransition(Duration.seconds(1), (Shape) figSuiv, figSuiv.getCouleurNoeud(), couleurAreteVisitee);
			ft.setCycleCount(1);
			ft.setDelay(Duration.seconds(duration));
			ft.play();
			
			ft.setOnFinished(new EventHandler<ActionEvent>(){

				@Override
				public void handle(ActionEvent event) {
					timeline.play();		
				}
				
			});
		}
		figureSuivante = figureSuivante();
	}
	
	public void algo(boolean play, boolean init){
		if(play == true){
			poidsTotalMin = 0;
			init();
			while(allVisited() == false){
				algoNoeud(figure(figureSuivante));
				duration += 3;
			}
			for(Figure f : liste){
				if(f.getLastNode() == true){
					poidsTotalMin = f.getPoidsMin();
				}
			}
			System.out.println("Le plus court chemin : " + poidsTotalMin + "\n\n");
		}else if(play == false){
			System.out.println(init);
			poidsTotalMin = 0;
			if(init == false){
				init();
			}else if(init == true){
				if(allVisited() == false){
					algoNoeud(figure(figureSuivante));
					duration += 3;
				}else{
					for(Figure f : liste){
						if(f.getLastNode() == true){
							poidsTotalMin = f.getPoidsMin();
						}
					}
				}
				System.out.println("Le plus court chemin : " + poidsTotalMin + "\n\n");
			}
			
			
		}
	}
	
	public boolean allVisited(){
		for(Figure f : liste){
			if(f.getVisited() == false && (f instanceof Cercle || f instanceof Rect)){
				return false;
			}
		}
		return true;
	}
	
	public void afficherFigure(Figure f){
		if(f instanceof Cercle || f instanceof Rect){
			System.out.println("Sommet " + f.getNom() + " : [" + f.getCenterX() + "," + f.getCenterY() + "] poidsMin : " + f.getPoidsMin() + ", visité : " + f.getVisited() + "\n");
		}
	}
	
	

}
