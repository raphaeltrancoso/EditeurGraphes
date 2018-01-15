package model;

import java.util.ArrayList;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;

public class Cercle extends Circle implements Figure{
	
	public double x, y;
	public double rayon;
	public String nom;
	public int poids;
	private Color couleur;
	private Color couleurNoeud;
	Label pds, n;
	ArrayList<Arete> listeAretes;
	
	boolean firstNode;
	boolean lastNode;
	private int poidsMin;
	boolean visited;
	
	public Cercle(double x, double y, double rayon){
		this.x = x;
		this.y = y;
		this.rayon = rayon;
		nom = "name";
		poids = 0;
		couleur = Color.BLACK;
		couleurNoeud = Color.LIGHTGREY;
		setStroke(couleur);
		setStrokeWidth(2);
		setFill(couleurNoeud);
		
		pds = new Label();
		pds.setFont(Font.font(20));
		n = new Label();
		listeAretes = new ArrayList<>();
		firstNode = false;
		lastNode = false;
	}
	
	public boolean getFirstNode(){
		return firstNode;
	}
	
	public boolean getLastNode(){
		return lastNode;
	}
	
	public void setFirstNode(boolean b){
		this.firstNode = b;
	}
	
	public void setLastNode(boolean b){
		this.lastNode = b;
	}
	
	public void addArete(Arete a){
		listeAretes.add(a);
	}
	
	public ArrayList<Arete> getListeAretes(){
		return listeAretes;
	}
	
	public Label getPds(){
		return pds;
	}
	
	public Label getN(){
		return n;
	}
	
	public void setPds(Label pds){
		this.pds = pds;
	}
	
	public void setN(Label n){
		this.n = n;
	}
	
	public double getX(){
		return x;
	}
	
	public double getY(){
		return y;
	}
	
	public double getTaille(){
		return rayon;
	}
	
	public double getRayon(){
		return rayon;
	}
	
	public String getNom(){
		return nom;
	}
	
	public int getPoids(){
		return poids;
	}
	
	public void setNom(String nom){
		this.nom = nom;
	}
	
	public void setPoids(int pds){
		this.poids = pds;
	}
	
	public Color getCouleur(){
		return couleur;
	}
	
	public void setCouleur(Color c){
		this.couleur = c;
		setStroke(couleur);
	}
	
	public int getPoidsMin(){
		return poidsMin;
	}
	
	public void setPoidsMin(int n){
		this.poidsMin = n;
		this.poids = n;
		String zero = String.valueOf(0);
		String t = String.valueOf(getPoidsMin());
		if(n == 1000000000){
			pds.setText(zero);
		}
		pds.setText(t);
	}
	
	public boolean getVisited(){
		return visited;
	}
	
	public void setVisited(boolean b){
		this.visited = b;
	}

	public Color getCouleurNoeud() {
		return couleurNoeud;
	}

	@Override
	public void setCouleurNoeud(Color c) {
		this.couleurNoeud = c;
		setFill(couleurNoeud);
		
	}

}
