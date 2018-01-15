package controller;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import model.Arete;
import model.Cercle;
import model.Figure;
import model.Rect;

public class SaveFile {
	
	public void saveGraphe(ArrayList<Figure> liste) {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			ArrayList<Arete> existList = new ArrayList<>();
			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("graphe");
			doc.appendChild(rootElement);
			System.out.println(liste);
			for(Figure f : liste){
				if(f instanceof Cercle || f instanceof Rect){
					Element figure = doc.createElement("figure");
					rootElement.appendChild(figure);
					if(f instanceof Cercle){
						// type de figure
						Attr attr = doc.createAttribute("type");
						attr.setValue("cercle");
						figure.setAttributeNode(attr);
						// nom de la figure
						Element nom = doc.createElement("nom");
						nom.appendChild(doc.createTextNode(f.getNom()));
						figure.appendChild(nom);
						// x
						Element x = doc.createElement("x");
						x.appendChild(doc.createTextNode(Double.toString(f.getCenterX())));
						figure.appendChild(x);
						// y
						Element y = doc.createElement("y");
						y.appendChild(doc.createTextNode(Double.toString(f.getCenterY())));
						figure.appendChild(y);
						// taille
						Element taille = doc.createElement("taille");
						taille.appendChild(doc.createTextNode(Double.toString(f.getTaille())));
						figure.appendChild(taille);
						// poids
						Element poids = doc.createElement("poids");
						poids.appendChild(doc.createTextNode(Double.toString(f.getPoids())));
						figure.appendChild(poids);
						// poids min
						Element poidsMin = doc.createElement("poidsMin");
						poidsMin.appendChild(doc.createTextNode(Double.toString(f.getPoidsMin())));
						figure.appendChild(poidsMin);
						boolean exist;
						for(Arete a : f.getListeAretes()){
							exist = false;
							for(Arete arete : existList) {
								if(a.getStartX() == arete.getStartX() && a.getStartY() == arete.getStartY()){
									exist = true;
								}
							}
							if(exist == true){
								System.out.println(exist);
							}else if (exist == false){
								//arete
								Element arete = doc.createElement("arete");
								figure.appendChild(arete);
								// startX
								Attr startX = doc.createAttribute("startX");
								startX.setValue(Double.toString(a.getStartX()));
								arete.setAttributeNode(startX);							
								// startY
								Attr startY = doc.createAttribute("startY");
								startY.setValue(Double.toString(a.getStartY()));
								arete.setAttributeNode(startY);	
								// endX
								Attr endX = doc.createAttribute("endX");
								endX.setValue(Double.toString(a.getEndX()));
								arete.setAttributeNode(endX);	
								// endY
								Attr endY = doc.createAttribute("endY");
								endY.setValue(Double.toString(a.getEndY()));
								arete.setAttributeNode(endY);
								// poids
								Attr pds = doc.createAttribute("poids");
								pds.setValue(Double.toString(a.getPoids()));
								arete.setAttributeNode(pds);
								existList.add(a);
							}
						}
					}else if(f instanceof Rect){
						// type de figure
						Attr attr = doc.createAttribute("type");
						attr.setValue("rectangle");
						figure.setAttributeNode(attr);
						// nom de la figure
						Element nom = doc.createElement("nom");
						nom.appendChild(doc.createTextNode(f.getNom()));
						figure.appendChild(nom);
						// x
						Element x = doc.createElement("x");
						x.appendChild(doc.createTextNode(Double.toString(f.getCenterX())));
						figure.appendChild(x);
						// y
						Element y = doc.createElement("y");
						y.appendChild(doc.createTextNode(Double.toString(f.getCenterY())));
						figure.appendChild(y);
						// taille
						Element taille = doc.createElement("taille");
						taille.appendChild(doc.createTextNode(Double.toString(f.getTaille())));
						figure.appendChild(taille);
						// poids
						Element poids = doc.createElement("poids");
						poids.appendChild(doc.createTextNode(Double.toString(f.getPoids())));
						figure.appendChild(poids);
						// poids min
						Element poidsMin = doc.createElement("poidsMin");
						poidsMin.appendChild(doc.createTextNode(Double.toString(f.getPoidsMin())));
						figure.appendChild(poidsMin);
						boolean exist2;
						for(Arete a : f.getListeAretes()){
							exist2 = false;
							System.out.println(a.getStartX());
							for(Arete arete : existList) {
								if(a.getStartX() == arete.getStartX() && a.getStartY() == arete.getStartY()){
									exist2 = true;
								}
							}
							if(exist2 == true){
								System.out.println(exist2);
							}else if (exist2 == false){
								//arete
								Element arete = doc.createElement("arete");
								figure.appendChild(arete);
								// startX
								Attr startX = doc.createAttribute("startX");
								startX.setValue(Double.toString(a.getStartX()));
								arete.setAttributeNode(startX);							
								// startY
								Attr startY = doc.createAttribute("startY");
								startY.setValue(Double.toString(a.getStartY()));
								arete.setAttributeNode(startY);	
								// endX
								Attr endX = doc.createAttribute("endX");
								endX.setValue(Double.toString(a.getEndX()));
								arete.setAttributeNode(endX);	
								// endY
								Attr endY = doc.createAttribute("endY");
								endY.setValue(Double.toString(a.getEndY()));
								arete.setAttributeNode(endY);
								// poids
								Attr pds = doc.createAttribute("poids");
								pds.setValue(Double.toString(a.getPoids()));
								arete.setAttributeNode(pds);
								existList.add(a);
							}
						}
					}
					
				}
			}
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("graphe.xml"));

			transformer.transform(source, result);

			System.out.println("File saved!");
			
			
		}catch (Exception e) {
	         e.printStackTrace();
	    }
	}
}
