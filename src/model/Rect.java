package model;

import java.util.ArrayList;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Rect extends Rectangle implements Figure{
		
	public double x, y;
	DoubleProperty dpx;
    DoubleProperty dpy;
	public double cote;
	public String nom;
	public int poids;
	public Color couleur;
	public Color couleurNoeud;
	Label pds, n;
	ArrayList<Arete> listeAretes;
	boolean firstNode, lastNode;
	int poidsMin;
	boolean visited;
	
	public Rect(double x, double y, double cote){
		this.x = x;
		this.y = y;
		dpx = new SimpleDoubleProperty(x) ;
        dpy = new SimpleDoubleProperty(y) ;
		this.cote = cote;
		nom = "name";
		poids = 0;
		couleur = Color.BLACK;
		couleurNoeud = Color.LIGHTGREY;
		setStroke(couleur);
		setStrokeWidth(2);
		setFill(couleurNoeud);
		
		pds = new Label();
		n = new Label();
		listeAretes = new ArrayList<>();
		firstNode = false;
		lastNode = false;
		poidsMin = 0;
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
	
	public double getCenterX() {
		return x;
	}

	public double getCenterY() {
		return y;
	}
	
	public void setCenterX(double x) {
		this.x = x;
		setX(x);
		dpx.set(x);
	}

	public void setCenterY(double y) {
		this.y = y;
		setY(y);
		dpy.set(y);
	}
		
	public double getCote(){
		return cote;
	}
	
	public double getTaille(){
		return cote;
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

	@Override
	public DoubleProperty centerXProperty() {
		// TODO Auto-generated method stub
		return dpx;
	}

	@Override
	public DoubleProperty centerYProperty() {
		// TODO Auto-generated method stub
		return dpy;
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
		String t = String.valueOf(getPoidsMin());
		pds.setText(t);
	}

	public boolean getVisited(){
		return visited;
	}
	
	public void setVisited(boolean b){
		this.visited = b;
	}

	@Override
	public Color getCouleurNoeud() {
		return couleurNoeud;
	}

	@Override
	public void setCouleurNoeud(Color c) {
		this.couleurNoeud = c;
		setFill(couleurNoeud);
		
	}
}
