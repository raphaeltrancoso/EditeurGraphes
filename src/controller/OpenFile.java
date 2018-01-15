package controller;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import model.Cercle;
import model.Figure;
import model.Rect;
import view.View;

public class OpenFile {
	
	Controller controller;
	View view;
	boolean selected;
	Figure f1;
	double x1, y1;
	
	public void openGraphe(Controller c, String chemin) {

	    try {

		File fXmlFile = new File(chemin);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
				
		//optional, but recommended
		//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
		doc.getDocumentElement().normalize();

		System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
				
		NodeList nList = doc.getElementsByTagName("figure");
				
		System.out.println("----------------------------");

		for (int temp = 0; temp < nList.getLength(); temp++) {

			Node nNode = nList.item(temp);
					
			System.out.println("\nCurrent Element :" + nNode.getNodeName());
					
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {

				Element eElement = (Element) nNode;

				System.out.println("type : " + eElement.getAttribute("type"));
				System.out.println("nom : " + eElement.getElementsByTagName("nom").item(0).getTextContent());
				System.out.println("x : " + eElement.getElementsByTagName("x").item(0).getTextContent());
				System.out.println("y : " + eElement.getElementsByTagName("y").item(0).getTextContent());
				System.out.println("taille : " + eElement.getElementsByTagName("taille").item(0).getTextContent());
				System.out.println("poids : " + eElement.getElementsByTagName("poids").item(0).getTextContent());
				System.out.println("poidsMin : " + eElement.getElementsByTagName("poidsMin").item(0).getTextContent());

				if(eElement.getAttribute("type").equals("cercle")){
					double x = Double.parseDouble(eElement.getElementsByTagName("x").item(0).getTextContent());
					double y = Double.parseDouble(eElement.getElementsByTagName("y").item(0).getTextContent());
					double rayon = Double.parseDouble(eElement.getElementsByTagName("taille").item(0).getTextContent());
					c.createNode(x, y, rayon, 0);
				}else if(eElement.getAttribute("type").equals("rectangle")){
					double x = Double.parseDouble(eElement.getElementsByTagName("x").item(0).getTextContent());
					double y = Double.parseDouble(eElement.getElementsByTagName("y").item(0).getTextContent());
					double cote = Double.parseDouble(eElement.getElementsByTagName("taille").item(0).getTextContent());
					c.createNode(x, y, cote, 1);
				}
				
			}
		}
		
		NodeList nListAretes = doc.getElementsByTagName("arete");
		
		for (int temp = 0; temp < nListAretes.getLength(); temp++) {

			Node nNode = nListAretes.item(temp);
					
			System.out.println("\nCurrent Element :" + nNode.getNodeName());
					
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {

				Element eElement = (Element) nNode;
				System.out.println("startX : " + eElement.getAttribute("startX"));
				System.out.println("startY : " + eElement.getAttribute("startY"));
				System.out.println("endX : " + eElement.getAttribute("endX"));
				System.out.println("endY : " + eElement.getAttribute("endY"));
				System.out.println("poids : " + eElement.getAttribute("poids"));
				
				for(Figure f : c.getListeOpen()){
					if(f instanceof Cercle || f instanceof Rect){
						if((!selected) && (Double.parseDouble(eElement.getAttribute("startX")) == (f.getCenterX())) 
								&& (Double.parseDouble(eElement.getAttribute("startY")) == (f.getCenterY()))){
							f1 = f;
							x1 = Double.parseDouble(eElement.getAttribute("startX"));
							y1 = Double.parseDouble(eElement.getAttribute("startY"));
							selected = true;
						}else if((selected) && (Double.parseDouble(eElement.getAttribute("endX")) == (f.getCenterX())) 
								&& (Double.parseDouble(eElement.getAttribute("endY")) == (f.getCenterY()))){
							double x2 = Double.parseDouble(eElement.getAttribute("endX"));
							double y2 = Double.parseDouble(eElement.getAttribute("endY"));
							double i = Double.parseDouble(eElement.getAttribute("poids"));
							int ii = (int) i;
							c.createAreteOpen(f1, x1, y1, f, x2, y2, ii);
							selected = false;
						}
					}
				}
				
			}
		}

	    } catch (Exception e) {
		e.printStackTrace();
	    }
	  }

}
