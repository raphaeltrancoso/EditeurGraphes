package model;

import java.util.ArrayList;

import javafx.beans.property.DoubleProperty;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public interface Figure {

	double getX();
    double getY();
    double getCenterX();
    double getCenterY();
    double getTaille();
    Label getPds();
    Label getN();
    void addArete(Arete a);
    ArrayList<Arete> getListeAretes();
    String getNom();
    int getPoids();
    Color getCouleur();
    void setCouleur(Color c);
    void setNom(String nom);
    void setPoids(int pds);
    DoubleProperty centerXProperty();
    DoubleProperty centerYProperty();
    boolean getFirstNode();
    boolean getLastNode();
    void setFirstNode(boolean b);
    void setLastNode(boolean b);
    int getPoidsMin();
    void setPoidsMin(int n);
    boolean getVisited();
    void setVisited(boolean b);
    Color getCouleurNoeud();
    void setCouleurNoeud(Color c);
}
