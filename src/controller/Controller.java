package controller;

import java.util.ArrayList;

import model.Arete;
import model.Cercle;
import model.Figure;
import model.Model;
import model.Rect;
import view.View;

public class Controller{

	Model model;
	View view;
	private ArrayList<Figure> listeFigure;
	private ArrayList<Figure> listeFigureEffacees;
	private ArrayList<Figure> listeOpen;
	
	Dijkstra dijkstra;
	
	SaveFile sf;
	OpenFile of;
	
	public Controller(Model model, View view) {
		this.model = model;
		this.view = view;
		listeFigure = new ArrayList<>();
		listeFigureEffacees = new ArrayList<>();
		listeOpen = new ArrayList<>();
	}
	
	boolean init;
	
	public void dijkstra(boolean fChosen, boolean lChosen, boolean play){
		dijkstra = new Dijkstra(listeFigure);
		if(fChosen && lChosen){
			if(play == true){
				dijkstra.algo(true, true);
			}else if(play == false){
				if(init == false){
					dijkstra.algo(false, false);
					init = true;
				}else if(init == true){
					dijkstra.algo(false, true);
				}
			}
		}
	}	
	
	public void addFigure(Figure f){
		listeFigure.add(f);
	}
	
	public void addOpen(Figure f){
		listeOpen.add(f);
	}
	
	public ArrayList<Figure> getListeFigure(){
		return listeFigure;
	}
	
	public ArrayList<Figure> getListeOpen(){
		return listeOpen;
	}	
	
	public boolean ifArete(Figure f, Arete a){
		if(f instanceof Cercle){
			if(f.getListeAretes().contains(a)) return true;
		}else if(f instanceof Rect){
			if(f.getListeAretes().contains(a)) return true;
		}
		return false;
		
	}
	
	public void removeFigure(Figure f){
		listeFigure.remove(f);
		view.removeFigure(f);
	}
	
	public void removeAllFigures(){
		for(Figure f : listeFigure){
			view.removeFigure(f);
		}
		listeFigure.clear();
	}
	
	public void createNode(double x, double y, double taille, int choix){
		if(choix == 0){
			if(!collision(x, y, taille))
				view.createCercle(x, y, taille);	
		}else{
			if(!collision(x, y, taille))
				view.createRectangle(x, y, taille);
		}
	}
	
	public void createAreteOpen(Figure f1, double startX, double startY, Figure f2, double endX, double endY, int pds){
		if(!estMemeNoeud(f1, f2)){
			view.createAreteOpen(f1, startX, startY, f2, endX, endY, pds);
		}
	}
	
	public void createArete(Figure f1, double startX, double startY, Figure f2, double endX, double endY){
		if(!estMemeNoeud(f1, f2)){
			view.createArete(f1, startX, startY, f2, endX, endY);
		}
	}
	
	public boolean collision(double x, double y, double taille){
		if(listeFigure.isEmpty()) return false;
		Cercle newC = new Cercle(x, y, taille);
		Cercle oldC;
		for(Figure f : listeFigure){
			double oldX = ((Figure)f).getCenterX();
			double oldY = ((Figure)f).getCenterY();
			double oldTaille = ((Figure)f).getTaille();
			oldC = new Cercle(oldX, oldY, oldTaille);

			double diffX = Math.pow((newC.getX() - oldC.getX()), 2);
			double diffY = Math.pow((newC.getY() - oldC.getY()), 2);
			double d = Math.sqrt(diffX+diffY);
			double totalRayon = newC.getTaille() + oldC.getTaille();
			
			if(d < totalRayon){
				return true;
			}
        }
        return false;
	}
	
	public boolean estMemeNoeud(Figure f1, Figure f2){
		if(f1.getCenterX() == f2.getCenterX() && f1.getCenterY() == f2.getCenterY())
			return true;
		return false;
	}
	
	public void annuler(){
		if(listeFigure.isEmpty()){
			return;
		}else{
			int tailleListe = listeFigure.size();
			int index = tailleListe - 1;
			Figure fig = listeFigure.get(index);
			listeFigureEffacees.add(fig);
			listeFigure.remove(index);
			view.removeFigure(fig);
		}
	}
	
	public void retablir(){
		if(listeFigureEffacees.isEmpty()){
			return;
		}else{
			int tailleListe = listeFigureEffacees.size();
			int index = tailleListe - 1;
			Figure fig = listeFigureEffacees.get(index);
			listeFigure.add(fig);
			listeFigureEffacees.remove(index);
			view.restoreFigure(fig);
		}
	}
	
	public boolean hide(boolean b){
		if(listeFigure.isEmpty()){
			return true;
		}else{
			if(b){
				for(Figure f : listeFigure){
					f.getN().setVisible(true);
				}
				return false;
			}else if(!b){
				for(Figure f : listeFigure){
					f.getN().setVisible(false);
				}
				return true;
			}
		}
		return true;
	}

	public void saveFile(){
		sf = new SaveFile();
		sf.saveGraphe(listeFigure);
	}
	
}
