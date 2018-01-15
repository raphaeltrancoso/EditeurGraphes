package model;

import java.util.ArrayList;

import javafx.beans.property.DoubleProperty;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.QuadCurve;
import javafx.scene.text.Font;

public class Arete extends QuadCurve implements Figure{
	
	private double startX, startY, endX, endY;
	public int poids;
	Color couleur = Color.BLACK;
	Label pds;
	
	public Arete(double startX, double endX, double startY, double endY){
		this.startX = startX;
		this.endX = endX;
		this.startY = startY;
		this.endY = endY;
		this.poids = 0;
		setStroke(couleur);
		setStrokeWidth(2);
		setFill(null);
		pds = new Label();
	}

	@Override
	public Label getPds() {
		return pds;
	}
	
	public void setPds(Label pds){
		this.pds = pds;
	}

	@Override
	public int getPoids() {
		return poids;
	}

	@Override
	public Color getCouleur() {
		return couleur;
	}

	@Override
	public void setCouleur(Color c) {
		this.couleur = c;
		
	}

	@Override
	public void setPoids(int pds) {
		this.poids = pds;
	}
	
	/*********** NONE **********/
	
	@Override
	public double getX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getY() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getCenterX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getCenterY() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getTaille() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public Label getN() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNom() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void setNom(String nom) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public DoubleProperty centerXProperty() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DoubleProperty centerYProperty() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void addArete(Arete a) {
		// TODO Auto-generated method stub
		return;
	}

	@Override
	public ArrayList<Arete> getListeAretes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getFirstNode() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getLastNode() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setFirstNode(boolean b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLastNode(boolean b) {
		// TODO Auto-generated method stub
		
	}

	public int getPoidsMin(){
		return 0;
	}
	
	public void setPoidsMin(int n){
		
	}

	@Override
	public boolean getVisited() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setVisited(boolean b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Color getCouleurNoeud() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCouleurNoeud(Color c) {
		// TODO Auto-generated method stub
		
	}
	
}
