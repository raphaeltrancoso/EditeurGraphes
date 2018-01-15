package view;

import java.util.ArrayList;
import java.util.Optional;

import controller.Controller;
import controller.OpenFile;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import model.Arete;
import model.Cercle;
import model.Figure;
import model.Rect;

public class View extends BorderPane{ 
	Pane pane;
	MenuBar menuBar;
	HBox paneBottom, hBox, bBox;
	VBox leftPane, centerPane, rightPane;
	TextField textField;
	Menu fichier, edition;
	ToolBar tb;
	ColorPicker cp;
	ColorPicker cp2;
	ColorPicker cp3;
	Button annTB = new Button();
	Button revTB = new Button();
	
	boolean selected;
	Figure f1;
	double x1, y1;
	
	ArrayList<Figure> liste = new ArrayList<>();
	
	ToggleGroup toggle = new ToggleGroup();
	ToggleButton bCercle = new ToggleButton();
	ToggleButton bRect = new ToggleButton();
	ToggleButton bGomme = new ToggleButton();
	{
		bCercle.setSelected(true);
		bCercle.setToggleGroup(toggle);
		bRect.setToggleGroup(toggle);
		bGomme.setToggleGroup(toggle);
	}
	
	Button fNode = new Button("Premier Noeud");
	Button lNode = new Button("Dernier Noeud");
	
	Controller controller;
	double rayon = 20, cote = 40;
	private boolean dragged;
	private boolean selection;
	private boolean selectionArete;
	private boolean menuOpen;
	private boolean hide;
	private boolean hideAll;
	private boolean firstChosen;
	private boolean lastChosen;
	int choixCercle = 0, choixRectangle = 1;
	
	Figure figArete;
	Arete ar;
	Label p;
	double xn1, yn1, xn2, yn2;
	
	OpenFile of;
	
	ColorPicker cNoeud;
	Color couleurPrincipale = Color.BLACK;
	Color couleurNoeud = Color.LIGHTGREY;
	Color couleurSelection = Color.PALEGREEN;
	Color couleurArete = Color.PALEGREEN;
	Color couleurFirstNode = Color.LIGHTGREEN;
	Color couleurLastNode = Color.CRIMSON;
	
	public View(){
		createMenuBar();
		createToolBar();
		createPane();
		createPaneBottom();
		setTop(menuBar);
		setLeft(tb);
		setCenter(pane);
		setBottom(paneBottom);
		dragged = false;
		selection = false;
		selectionArete = false;
		menuOpen = false;
		hide = true;
		hideAll = true;
		firstChosen = false;
		lastChosen = false;
	}
	
	public void bindController(Controller c){
		  this.controller = c;
	}
	
	public void createPane(){
		pane = new Pane();
		pane.setPrefSize(800, 400);
		
		pane.setOnMouseClicked(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {
				if(event.getButton().equals(MouseButton.PRIMARY)){
					if(menuOpen){
						menuOpen = false;
						return;
					}
					if(selection || selectionArete){
						return;
					}
					if(!dragged && !selection && !selectionArete) {
						if(toggle.getSelectedToggle().equals(bCercle)){
							if(event.getX() < 0 + rayon || event.getX() > pane.getWidth() - rayon) return;
							if(event.getY() < 0 + menuBar.getHeight() || event.getY() > pane.getHeight() - rayon) return;
							controller.createNode(event.getX(), event.getY(), rayon, choixCercle);
						}else if(toggle.getSelectedToggle().equals(bRect)){
							if(event.getX() < 0 + cote || event.getX() > pane.getWidth() - cote) return;
							if(event.getY() < 0 + menuBar.getHeight() || event.getY() > pane.getHeight() - cote) return;
							controller.createNode(event.getX(), event.getY(), cote, choixRectangle);
						}
					}
					else {
						dragged = false;
					}
				}
				
			}
			
		});
		
		pane.toBack();
	}
	
	/**************** PARTIE MENU ****************/
	
	public ContextMenu createContextMenuArete(Arete a, Label pds){
		ContextMenu cm = new ContextMenu();
		MenuItem modifierPoids = new MenuItem("Changer poids");
		
		modifierPoids.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				String t = String.valueOf(a.getPoids());
				TextInputDialog tid = new TextInputDialog(t);
				tid.setHeight(40);
				tid.setTitle("Modifier poids");
				tid.setHeaderText("Modifier le poids de l'arete");
				tid.setContentText("Entrez la valeur du poids de l'arete : ");
				Optional<String> result = tid.showAndWait();
				if (result.isPresent()){
					int p = Integer.parseInt(result.get());
				    a.setPoids(p);
				    pds.setText(result.get());
				}
			}
			
		});

		cm.getItems().add(modifierPoids);
		return cm;
	}
	
	public ContextMenu createContextMenu(Figure f, Label pds, Label n){
		ContextMenu cm = new ContextMenu();
		MenuItem renommer = new MenuItem("Renommer");
		MenuItem modifierPoids = new MenuItem("Changer poids");
		MenuItem supprimer = new MenuItem("Supprimer");
		MenuItem cacher = new MenuItem("Afficher/Cacher nom");
		MenuItem firstNode = new MenuItem("Selectionner/Deselectionner premier noeud");
		MenuItem lastNode = new MenuItem("Selectionner/Deselectionner dernier noeud");
		SeparatorMenuItem separator = new SeparatorMenuItem();
		
		renommer.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				TextInputDialog tid = new TextInputDialog(f.getNom());
				tid.setHeight(40);
				tid.setTitle("Renommer");
				tid.setHeaderText("Vous voulez renommer votre sommet.");
				tid.setContentText("Entrez le nouveau nom : ");
				Optional<String> result = tid.showAndWait();
				if (result.isPresent()){
				    result.ifPresent(f::setNom);
				    n.setText(result.get());
				}
			}
			
		});
		
		modifierPoids.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				String t = String.valueOf(f.getPoids());
				TextInputDialog tid = new TextInputDialog(t);
				tid.setHeight(40);
				tid.setTitle("Modifier poids");
				tid.setHeaderText("Modifier le poids du sommet " + f.getNom());
				tid.setContentText("Entrez la valeur du poids du sommet : ");
				Optional<String> result = tid.showAndWait();
				if (result.isPresent()){
					int p = Integer.parseInt(result.get());
				    f.setPoids(p);
				    pds.setText(result.get());
				}
			}
			
		});
		
		supprimer.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				controller.removeFigure(f);
				if(selection) selection = false;
			}
			
		});
		
		cacher.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				if(hide){
					n.setVisible(true);
					hide = false;
				}else if(!hide){
					n.setVisible(false);
					hide = true;
				}
				
			}
			
		});
		
		firstNode.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				if(!firstChosen){
					firstChosen = true;
					f.setFirstNode(true);
					f.setCouleur(couleurFirstNode);
				}else if(firstChosen && f.getFirstNode() == true){
					firstChosen = false;
					f.setFirstNode(false);
					f.setCouleur(couleurPrincipale);
				}else if(firstChosen && f.getFirstNode() == false){
					for(Figure f : controller.getListeFigure()){
						f.setFirstNode(false);
						f.setCouleur(couleurPrincipale);
					}
					firstChosen = true;
					f.setFirstNode(true);
					f.setCouleur(couleurFirstNode);
				}
				
			}
			
		});
		
		lastNode.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				if(!lastChosen){
					lastChosen = true;
					f.setLastNode(true);
					f.setCouleur(couleurLastNode);
				}else if(lastChosen && f.getLastNode() == true){
					lastChosen = false;
					f.setLastNode(false);
					f.setCouleur(couleurPrincipale);
				}else if(lastChosen && f.getLastNode() == false){
					for(Figure f : controller.getListeFigure()){
						f.setLastNode(false);
						f.setCouleur(couleurPrincipale);
					}
					lastChosen = true;
					f.setLastNode(true);
					f.setCouleur(couleurLastNode);
				}
				
			}
			
		});
		
		cm.getItems().addAll(renommer, modifierPoids, supprimer, separator, cacher, firstNode, lastNode);
		return cm;
		
	}
	
	public void createMenuBar(){
		menuBar = new MenuBar();
		menuBar.setStyle("-fx-background-color: DARKGREY;");
		createMenus();
	}
	
	public void createToolBar(){
		tb = new ToolBar();
		tb.setOrientation(Orientation.VERTICAL);
		Label lcn = new Label("Couleur Noeuds : ");
		Label lcn2 = new Label("Couleur Exterieure : ");
		Label lcn3 = new Label("Couleur Aretes : ");
		cp = new ColorPicker();
		cp.setMaxWidth(100);
		cp2 = new ColorPicker();
		cp2.setMaxWidth(100);
		cp3 = new ColorPicker();
		cp3.setMaxWidth(100);
		cp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Color c = cp.getValue();
                if (c != null) {
                    couleurNoeud = c;
                }
            }
        });
		
		cp2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Color c = cp2.getValue();
                if (c != null) {
                    couleurPrincipale = c;
                }
            }
        });
		cp3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Color c = cp3.getValue();
                if (c != null) {
                    couleurArete = c;
                }
            }
        });

		tb.getItems().addAll(lcn, cp, lcn2, cp2, lcn3, cp3);
	}
	
	public void createPaneBottom(){
		paneBottom = new HBox();	
		paneBottom.setPadding(new Insets(20, 20, 20, 20));
		paneBottom.setSpacing(20);
		paneBottom.setStyle("-fx-background-color: #336699;");
		
		hBox = new HBox();
		hBox.setPadding(new Insets(0, 50, 0, 50));
		hBox.setSpacing(20);
		
		bBox = new HBox();
		bBox.setPadding(new Insets(0, 50, 0, 50));
		bBox.setSpacing(20);
		
		leftPane = new VBox();
		leftPane.setPadding(new Insets(0, 20, 0, 20));
		leftPane.setSpacing(20);
		
		centerPane = new VBox();
		centerPane.setPadding(new Insets(0, 50, 0, 50));
		centerPane.setSpacing(20);
		
		rightPane = new VBox();
		rightPane.setPadding(new Insets(0, 20, 0, 20));
		rightPane.setSpacing(20);
		
		Separator separatorLeft = new Separator();
		separatorLeft.setOrientation(Orientation.VERTICAL);
		Separator separatorRight = new Separator();
		separatorRight.setOrientation(Orientation.VERTICAL);
			
		Circle circle = new Circle();
		circle.setStroke(Color.BLACK);
		circle.setFill(Color.TRANSPARENT);
		circle.setStrokeWidth(2);
		circle.setRadius(20);
		bCercle.setGraphic(circle);

		Rectangle rectangle = new Rectangle();
		rectangle.setStroke(Color.BLACK);
		rectangle.setFill(Color.TRANSPARENT);
		rectangle.setStrokeWidth(2);
		rectangle.setHeight(40);
		rectangle.setWidth(40);
		bRect.setGraphic(rectangle);
		
		Image image = new Image("File:gomme.png");
		ImageView iv = new ImageView(image);
		iv.setFitHeight(40);
		iv.setFitWidth(40);
		bGomme.setGraphic(iv);
		
		fNode.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
            	if(selection){
            		if(!firstChosen){
    					firstChosen = true;
    					figArete.setFirstNode(true);
    					figArete.setCouleur(couleurFirstNode);
    					for(Arete a : figArete.getListeAretes()){
    						a.setCouleur(couleurPrincipale);
    					}
    				}else if(firstChosen && figArete.getFirstNode() == true){
    					firstChosen = false;
    					figArete.setFirstNode(false);
    					figArete.setCouleur(couleurPrincipale);
    				}else if(firstChosen && figArete.getFirstNode() == false){
    					for(Figure fig : controller.getListeFigure()){
    						fig.setFirstNode(false);
    						fig.setCouleur(couleurPrincipale);
    					}
    					firstChosen = true;
    					figArete.setFirstNode(true);
    					figArete.setCouleur(couleurFirstNode);
    				}
    				selection = false;
    				fNode.setDisable(true);
    				lNode.setDisable(true);
            	}
            }
        });
		
		lNode.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
            	if(selection){
            		if(!lastChosen){
    					lastChosen = true;
    					figArete.setLastNode(true);
    					figArete.setCouleur(couleurLastNode);
    				}else if(lastChosen && figArete.getLastNode() == true){
    					lastChosen = false;
    					figArete.setLastNode(false);
    					figArete.setCouleur(couleurPrincipale);
    				}else if(lastChosen && figArete.getLastNode() == false){
    					for(Figure f : controller.getListeFigure()){
    						f.setLastNode(false);
    						f.setCouleur(couleurPrincipale);
    					}
    					lastChosen = true;
    					figArete.setLastNode(true);
    					figArete.setCouleur(couleurLastNode);
    				}
    				selection = false;
    				fNode.setDisable(true);
    				lNode.setDisable(true);
            	}
            }
        });
		
		fNode.setDisable(true);
		lNode.setDisable(true);
		
		Button play = new Button("Lancer animation Dijkstra");
		Button pause = new Button("Stopper animation");
		Button next = new Button("Etape par étape");
		
		play.setPrefSize(200, 30);
		pause.setPrefSize(200, 30);
		next.setPrefSize(200, 30);
		
		play.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
            	for(Figure figure : controller.getListeFigure()) {
            		liste.add(figure);
				}
                controller.dijkstra(firstChosen, lastChosen, true);
                next.setDisable(true);
            }
        });
		
		pause.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
            	controller.removeAllFigures();
            	
            	for (Figure figure : liste) {
            		controller.getListeFigure().add(figure);
            		if(figure instanceof Cercle){
            			controller.createNode(figure.getCenterX(), figure.getCenterY(), figure.getTaille(), 0);
            			if((!selected)){
							f1 = figure;
							x1 = figure.getCenterX();
							y1 = figure.getCenterY();
							selected = true;
						}else if((selected)){
							for(Arete a : figure.getListeAretes()){
								if(a.getEndX() == f1.getCenterX() && a.getEndY() == f1.getCenterY()){
									controller.createArete(f1, x1, y1, figure, figure.getCenterX(), figure.getCenterY());
									selected = false;
								}
							}
						}
            		}else if(figure instanceof Rect){
            			controller.createNode(figure.getCenterX(), figure.getCenterY(), figure.getTaille(), 1);
            		}
            		
				}
            	
                play.setDisable(false);
                next.setDisable(false);
            }
        });
		
		next.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                controller.dijkstra(firstChosen, lastChosen, false);
                play.setDisable(true);
            }
        });
		
		Label label = new Label("Poids : ");
		label.setTextFill(Color.WHITE);
	     
	    textField = new TextField();
	    textField.setPromptText("Entrer le nouveau poids ici...");
	    textField.setPrefColumnCount(15);
	    
	    textField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
            	Integer i = Integer.valueOf(textField.getText());
                ar.setPoids(i);
                p.setText(textField.getText());
                ar.setStroke(couleurPrincipale);
				ar.getPds().setTextFill(couleurPrincipale);
				for(Figure f : controller.getListeFigure()){
					if(controller.ifArete(f, ar)){
						f.setCouleur(couleurPrincipale);
					}
				}
				selectionArete = false;
				e.consume();
            }
        });
	    
		Button valider = new Button("Valider");
		valider.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
            	Integer i = Integer.valueOf(textField.getText());
                ar.setPoids(i);
                p.setText(textField.getText());
                ar.setStroke(couleurPrincipale);
				ar.getPds().setTextFill(couleurPrincipale);
				for(Figure f : controller.getListeFigure()){
					if(controller.ifArete(f, ar)){
						f.setCouleur(couleurPrincipale);
					}
				}
				selectionArete = false;
				e.consume();
            }
        });
		
		hBox.getChildren().addAll(bCercle, bRect, bGomme);
		bBox.getChildren().addAll(fNode, lNode);
		leftPane.getChildren().addAll(hBox, bBox);
		centerPane.getChildren().addAll(play, pause, next);
		rightPane.getChildren().addAll(label, textField, valider);
		paneBottom.getChildren().addAll(leftPane, separatorLeft, centerPane, separatorRight, rightPane);
	}
	
	public void createMenus(){
		fichier = new Menu("Fichier");
		edition = new Menu("Edition");
		createMenuItems();
		menuBar.getMenus().addAll(fichier, edition);
	}
	
	public void createMenuItems(){
		MenuItem algoDijkstra = new MenuItem("Lancer algo");
		MenuItem open = new MenuItem("Ouvrir...");
		MenuItem save = new MenuItem("Sauvegarder");
		MenuItem quitter = new MenuItem("Quitter");
		MenuItem annuler = new MenuItem("Annuler");
		MenuItem retablir = new MenuItem("Rétablir");
		MenuItem supprimer = new MenuItem("Tout supprimer");
		MenuItem hide = new MenuItem("Afficher/Cacher tous les noms");
		SeparatorMenuItem sep = new SeparatorMenuItem();
		
		algoDijkstra.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                controller.dijkstra(firstChosen, lastChosen, true);
            }
        });
		
		open.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
            	of = new OpenFile();
        		of.openGraphe(controller, "graphe.xml");
            }
        });
		
		save.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                controller.saveFile();
            }
        });
		
		quitter.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                System.exit(0);
            }
        });
		
		annuler.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                controller.annuler();
            }
        });
		
		
		
		retablir.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                controller.retablir();
            }
        });
		
		supprimer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                controller.removeAllFigures();
            }
        });
		
		hide.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                hideAll = controller.hide(hideAll);
            }
        });
		
		//annuler.setAccelerator(new KeyCodeCombination(KeyCode.L, KeyCombination.CONTROL_DOWN, KeyCombination.SHORTCUT_DOWN));
		
		fichier.getItems().addAll(algoDijkstra, open, save, quitter);
		edition.getItems().addAll(annuler, retablir, supprimer, sep, hide);
	}
	
	public void removeFigure(Figure f){
		pane.getChildren().remove(f);
		pane.getChildren().remove(f.getPds());
		pane.getChildren().remove(f.getN());
	}
	
	public void restoreFigure(Figure f){
		if(f instanceof Cercle){
			pane.getChildren().addAll((Cercle) f, f.getPds(), f.getN());
		}else if(f instanceof Rect){
			pane.getChildren().addAll((Rect) f, f.getPds(), f.getN());
		}else{
			pane.getChildren().add((Arete) f);
		}
		
	}
	
	/**************** PARTIE DESSIN ****************/
	
	public void createCercle(double x, double y, double rayon){
		Cercle cercle = new Cercle(x, y, rayon);
		cercle.setFill(couleurNoeud);
		cercle.setStroke(couleurPrincipale);
		cercle.setCenterX(x);
		cercle.setCenterY(y);
		cercle.setRadius(rayon);
		
		String t = String.valueOf(cercle.getPoids());
		Label pds = new Label(t);
		pds.setLayoutX(x - rayon/4);
		pds.setLayoutY(y - rayon/3);
		
		cercle.setPds(pds);
		
		Label n = new Label(cercle.getNom());
		n.setLayoutX(x + rayon/4);
		n.setLayoutY(y + rayon);
		n.setVisible(false);
		
		cercle.setN(n);
		
		cercle.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
	        @Override 
	        public void handle(MouseEvent event) {
	        	ContextMenu cm = createContextMenu(cercle, pds, n);
	            if (event.getButton() == MouseButton.SECONDARY){ 
	                cm.show(cercle, event.getScreenX(), event.getScreenY());
	                menuOpen = true;
	            }else{
	            	cm.hide();
	            }
	        }
		});
		
		pds.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
	        @Override 
	        public void handle(MouseEvent event) {
	        	ContextMenu cm = createContextMenu(cercle, pds, n);
	            if (event.getButton() == MouseButton.SECONDARY){ 
	                cm.show(cercle, event.getScreenX(), event.getScreenY());
	                menuOpen = true;
	            }else{
	            	cm.hide();
	            }
	        }
		});
		
		cercle.setOnMouseClicked(new EventHandler<MouseEvent>(){
			
			@Override
			public void handle(MouseEvent event) {
				if(toggle.getSelectedToggle().equals(bGomme)){
					controller.removeFigure(cercle);
					if(selection) selection = false;
				}else{
					if(!selection && !selectionArete){
						if (event.getButton() == MouseButton.PRIMARY){
							selection = true;
							fNode.setDisable(false);
							lNode.setDisable(false);
							xn1 = cercle.getCenterX();
							yn1 = cercle.getCenterY();
							figArete = cercle;
							figArete.setCouleur(couleurSelection);
							if(!(cercle.getListeAretes().isEmpty())){
								for(Arete a : cercle.getListeAretes()){
									a.setStroke(couleurArete);
									a.getPds().setTextFill(couleurArete);
								}
							}
						}
					}else if(selection){
						if(figArete.getCouleur() == couleurSelection){
							if(figArete.getFirstNode() == true){
								figArete.setCouleur(couleurFirstNode);
							}
							if(figArete.getLastNode() == true){
								figArete.setCouleur(couleurLastNode);
							}
							if(figArete.getFirstNode() == false && figArete.getLastNode() == false){
								figArete.setCouleur(couleurPrincipale);
							}
							for(Arete a : figArete.getListeAretes()){
								a.setStroke(couleurPrincipale);
								a.getPds().setTextFill(couleurPrincipale);
							}
						}
						xn2 = cercle.getCenterX();
						yn2 = cercle.getCenterY(); 
						controller.createArete(figArete, xn1, yn1, cercle, xn2, yn2);
						selection = false;
						fNode.setDisable(true);
						lNode.setDisable(true);
					}
				}
			}
			
		});
		
		pds.setOnMouseClicked(new EventHandler<MouseEvent>(){
			
			@Override
			public void handle(MouseEvent event) {
				if(toggle.getSelectedToggle().equals(bGomme)){
					controller.removeFigure(cercle);
					if(selection) selection = false;
				}else{
					if(!selection && !selectionArete){
						if (event.getButton() == MouseButton.PRIMARY){
							selection = true;
							fNode.setDisable(false);
							lNode.setDisable(false);
							xn1 = cercle.getCenterX();
							yn1 = cercle.getCenterY();
							figArete = cercle;
							figArete.setCouleur(couleurSelection);
							if(!(cercle.getListeAretes().isEmpty())){
								for(Arete a : cercle.getListeAretes()){
									a.setStroke(couleurArete);
									a.getPds().setTextFill(couleurArete);
								}
							}
						}
					}else if(selection){
						if(figArete.getCouleur() == couleurSelection){
							if(figArete.getFirstNode() == true){
								figArete.setCouleur(couleurFirstNode);
							}
							if(figArete.getLastNode() == true){
								figArete.setCouleur(couleurLastNode);
							}
							if(figArete.getFirstNode() == false && figArete.getLastNode() == false){
								figArete.setCouleur(couleurPrincipale);
							}
							for(Arete a : figArete.getListeAretes()){
								a.setStroke(couleurPrincipale);
								a.getPds().setTextFill(couleurPrincipale);
							}
						}
						xn2 = cercle.getCenterX();
						yn2 = cercle.getCenterY(); 
						controller.createArete(figArete, xn1, yn1, cercle, xn2, yn2);
						selection = false;
						fNode.setDisable(false);
						lNode.setDisable(false);
					}
				}
			}
			
		});
		
		cercle.setOnMouseDragged(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {
				if (event.getButton() == MouseButton.PRIMARY){
					if(event.getX() < 0 + cercle.getRayon() || event.getX() > pane.getWidth() - rayon) return;
					if(event.getY() < 0 + menuBar.getHeight() || event.getY() > pane.getHeight() - rayon) return;
					if(!dragged) dragged = true;
					cercle.setCenterX(event.getX());
					cercle.setCenterY(event.getY());
					pds.setLayoutX(event.getX() - rayon/4);
					pds.setLayoutY(event.getY() - rayon/3);
					n.setLayoutX(event.getX() + rayon/4);
					n.setLayoutY(event.getY() + rayon);
					for(Arete a : cercle.getListeAretes()){
						double newStartX = a.getStartX() + (cercle.getCenterX() - event.getX());
						double newStartY = a.getStartY() + (cercle.getCenterY() - event.getY());
						double newEndX = a.getEndX() + (cercle.getCenterX() - event.getX());
						double newEndY = a.getEndY() + (cercle.getCenterY() - event.getY());
						a.getPds().setLayoutX((newStartX + newEndX)/2);
						a.getPds().setLayoutY((newStartY + newEndY)/2);
					}
				}

			}
			
		});
		
		pds.setOnMouseDragged(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {
				if (event.getButton() == MouseButton.PRIMARY){
					if(!dragged) dragged = true;
					double diffXFig = event.getX() + pds.getLayoutX() - pane.getLayoutX() - rayon/4;
					double diffXPds = event.getX() + pds.getLayoutX() - pane.getLayoutX() - rayon/4;
					double diffXNom = event.getX() + pds.getLayoutX() - pane.getLayoutX() - rayon/4;
					double diffYFig = event.getY() + pds.getLayoutY() - pane.getLayoutY() - rayon/3;
					double diffYPds = event.getY() + pds.getLayoutY() - pane.getLayoutY() - rayon/3;
					double diffYNom = event.getY() + pds.getLayoutY() - pane.getLayoutY() - rayon/3;
					cercle.setCenterX(diffXFig + pds.getWidth()/2 );
					cercle.setCenterY(diffYFig + menuBar.getHeight() + pds.getHeight()/2 );
					pds.setLayoutX(diffXPds);
					pds.setLayoutY(diffYPds + menuBar.getHeight());
					n.setLayoutX(diffXNom);
					n.setLayoutY(diffYNom + menuBar.getHeight() + menuBar.getHeight());
					for(Arete a : cercle.getListeAretes()){
						double newStartX = a.getStartX() + (cercle.getCenterX() - event.getX());
						double newStartY = a.getStartY() + (cercle.getCenterY() - event.getY());
						double newEndX = a.getEndX() + (cercle.getCenterX() - event.getX());
						double newEndY = a.getEndY() + (cercle.getCenterY() - event.getY());
						a.getPds().setLayoutX((newStartX + newEndX)/2);
						a.getPds().setLayoutY((newStartY + newEndY)/2);
					}
				}

			}
			
		});
		
		cercle.setOnMouseEntered(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {
				cercle.setStrokeWidth(4);
			}
			
		});
		
		cercle.setOnMouseExited(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {
				cercle.setStrokeWidth(2);
			}
			
		});
		
		pds.setOnMouseEntered(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {
				cercle.setStrokeWidth(4);
			}
			
		});
		
		pds.setOnMouseExited(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {
				cercle.setStrokeWidth(2);
			}
			
		});

		pane.getChildren().addAll(cercle, pds, n);
		controller.addFigure(cercle);
		controller.addOpen(cercle);
		
	}
	
	public void createRectangle(double x, double y, double cote){
		Rect rectangle = new Rect(x, y, cote);
		double c = cote/2;
		rectangle.setFill(couleurNoeud);
		rectangle.setStroke(couleurPrincipale);
		rectangle.setX(x-c);
		rectangle.setY(y-c);
		rectangle.setHeight(cote);
		rectangle.setWidth(cote);
		
		String t = String.valueOf(rectangle.getPoids());
		Label pds = new Label(t);
		pds.setLayoutX(x - c/4);
		pds.setLayoutY(y - c/3);
		
		rectangle.setPds(pds);
		
		Label n = new Label(rectangle.getNom());
		n.setLayoutX(x + c/4);
		n.setLayoutY(y + c);
		n.setVisible(false);
		
		rectangle.setN(n);
		
		rectangle.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
	        @Override 
	        public void handle(MouseEvent event) {
	        	ContextMenu cm = createContextMenu(rectangle, pds, n);
	            if (event.getButton() == MouseButton.SECONDARY){ 
	                cm.show(rectangle, event.getScreenX(), event.getScreenY());
	                menuOpen = true;
	            }else{
	            	cm.hide();
	            }
	        }
		});
		
		pds.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
	        @Override 
	        public void handle(MouseEvent event) {
	        	ContextMenu cm = createContextMenu(rectangle, pds, n);
	            if (event.getButton() == MouseButton.SECONDARY){ 
	                cm.show(rectangle, event.getScreenX(), event.getScreenY());
	                menuOpen = true;
	            }else{
	            	cm.hide();
	            }
	        }
		});
		
		rectangle.setOnMouseClicked(new EventHandler<MouseEvent>(){
			
			@Override
			public void handle(MouseEvent event) {
				if(toggle.getSelectedToggle().equals(bGomme)){
					controller.removeFigure(rectangle);
					if(selection) selection = false;
				}else{
					if(!selection && !selectionArete){
						if (event.getButton() == MouseButton.PRIMARY){
							selection = true;
							fNode.setDisable(false);
							lNode.setDisable(false);
							xn1 = rectangle.getCenterX();
							yn1 = rectangle.getCenterY();
							figArete = rectangle;
							figArete.setCouleur(couleurSelection);
							if(!(rectangle.getListeAretes().isEmpty())){
								for(Arete a : rectangle.getListeAretes()){
									a.setStroke(couleurArete);
									a.getPds().setTextFill(couleurArete);
								}
							}
						}
					}else if(selection){
						if(figArete.getCouleur() == couleurSelection){
							if(figArete.getFirstNode() == true){
								figArete.setCouleur(couleurFirstNode);
							}
							if(figArete.getLastNode() == true){
								figArete.setCouleur(couleurLastNode);
							}
							if(figArete.getFirstNode() == false && figArete.getLastNode() == false){
								figArete.setCouleur(couleurPrincipale);
							}
							for(Arete a : figArete.getListeAretes()){
								a.setStroke(couleurPrincipale);
								a.getPds().setTextFill(couleurPrincipale);
							}
						}
						xn2 = rectangle.getCenterX();
						yn2 = rectangle.getCenterY(); 
						controller.createArete(figArete, xn1, yn1, rectangle, xn2, yn2);
						selection = false;
						fNode.setDisable(false);
						lNode.setDisable(false);
					}
				}
			}
			
		});
		
		pds.setOnMouseClicked(new EventHandler<MouseEvent>(){
			
			@Override
			public void handle(MouseEvent event) {
				if(toggle.getSelectedToggle().equals(bGomme)){
					controller.removeFigure(rectangle);
					if(selection) selection = false;
				}else{
					if(!selection && !selectionArete){
						if (event.getButton() == MouseButton.PRIMARY){
							selection = true;
							xn1 = rectangle.getCenterX();
							yn1 = rectangle.getCenterY();
							figArete = rectangle;
							figArete.setCouleur(couleurSelection);
							if(!(rectangle.getListeAretes().isEmpty())){
								for(Arete a : rectangle.getListeAretes()){
									a.setStroke(couleurArete);
									a.getPds().setTextFill(couleurArete);
								}
							}
						}
					}else if(selection){
						if(figArete.getCouleur() == couleurSelection){
							if(figArete.getFirstNode() == true){
								figArete.setCouleur(couleurFirstNode);
							}
							if(figArete.getLastNode() == true){
								figArete.setCouleur(couleurLastNode);
							}
							if(figArete.getFirstNode() == false && figArete.getLastNode() == false){
								figArete.setCouleur(couleurPrincipale);
							}
							for(Arete a : figArete.getListeAretes()){
								a.setStroke(couleurPrincipale);
								a.getPds().setTextFill(couleurPrincipale);
							}
						}					
						xn2 = rectangle.getCenterX();
						yn2 = rectangle.getCenterY(); 
						controller.createArete(figArete, xn1, yn1, rectangle, xn2, yn2);
						selection = false;
					}
				}
			}
			
		});
		
		rectangle.setOnMouseDragged(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {
				if (event.getButton() == MouseButton.PRIMARY){
					if(!dragged) dragged = true;
					if(event.getX() < 0 + rectangle.getCote() || event.getX() > pane.getWidth() - cote) return;
					if(event.getY() < 0 + menuBar.getHeight() || event.getY() > pane.getHeight() - cote) return;
					rectangle.setCenterX(event.getX() - c);
					rectangle.setCenterY(event.getY() - c);
					pds.setLayoutX(event.getX() - c/4);
					pds.setLayoutY(event.getY() - c/3);
					n.setLayoutX(event.getX() + c/4);
					n.setLayoutY(event.getY() + c);
					for(Arete a : rectangle.getListeAretes()){
						double newStartX = a.getStartX() + (rectangle.getCenterX() - event.getX());
						double newStartY = a.getStartY() + (rectangle.getCenterY() - event.getY());
						double newEndX = a.getEndX() + (rectangle.getCenterX() - event.getX());
						double newEndY = a.getEndY() + (rectangle.getCenterY() - event.getY());
						a.getPds().setLayoutX((newStartX + newEndX)/2);
						a.getPds().setLayoutY((newStartY + newEndY)/2);
					}
				}

			}
			
		});
		
		pds.setOnMouseDragged(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {
				if (event.getButton() == MouseButton.PRIMARY){
					if(!dragged) dragged = true;
					double diffXFig = event.getX() + pds.getLayoutX() - pane.getLayoutX() - c;
					double diffXPds = event.getX() + pds.getLayoutX() - pane.getLayoutX() - c/4;
					double diffXNom = event.getX() + pds.getLayoutX() - pane.getLayoutX() - c/4;
					double diffYFig = event.getY() + pds.getLayoutY() - pane.getLayoutY() - c;
					double diffYPds = event.getY() + pds.getLayoutY() - pane.getLayoutY() - c/3;
					double diffYNom = event.getY() + pds.getLayoutY() - pane.getLayoutY() - c/3;
					rectangle.setCenterX(diffXFig + pds.getWidth()/2 );
					rectangle.setCenterY(diffYFig + menuBar.getHeight() + pds.getHeight()/2 );
					pds.setLayoutX(diffXPds);
					pds.setLayoutY(diffYPds + menuBar.getHeight());
					n.setLayoutX(diffXNom);
					n.setLayoutY(diffYNom + menuBar.getHeight() + menuBar.getHeight());
					for(Arete a : rectangle.getListeAretes()){
						double newStartX = a.getStartX() + (rectangle.getCenterX() - event.getX());
						double newStartY = a.getStartY() + (rectangle.getCenterY() - event.getY());
						double newEndX = a.getEndX() + (rectangle.getCenterX() - event.getX());
						double newEndY = a.getEndY() + (rectangle.getCenterY() - event.getY());
						a.getPds().setLayoutX((newStartX + newEndX)/2);
						a.getPds().setLayoutY((newStartY + newEndY)/2);
					}
				}

			}
		});
		
		rectangle.setOnMouseEntered(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {
				rectangle.setStrokeWidth(4);
			}
			
		});
		
		rectangle.setOnMouseExited(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {
				rectangle.setStrokeWidth(2);
			}
			
		});
		
		pds.setOnMouseEntered(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {
				rectangle.setStrokeWidth(4);
			}
			
		});
		
		pds.setOnMouseExited(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {
				rectangle.setStrokeWidth(2);
			}
			
		});
		
		pane.getChildren().addAll(rectangle, pds, n);
		controller.addFigure(rectangle);
		controller.addOpen(rectangle);
	}
	
	public void createAreteOpen(Figure f1, double startX, double startY, Figure f2, double endX, double endY, int poids){
		Arete arete = new Arete(startX, startY, endX, endY);
		arete.setStartX(startX);
		arete.setStartY(startY);
		arete.setEndX(endX);
		arete.setEndY(endY);
		arete.setControlX((startX + endX)/2);
		arete.setControlY((startY + endY)/2);
		arete.setPoids(poids);
		
		arete.startXProperty().bind(f1.centerXProperty());
		arete.startYProperty().bind(f1.centerYProperty());
		arete.endXProperty().bind(f2.centerXProperty());
		arete.endYProperty().bind(f2.centerYProperty());
		
		String t = String.valueOf(arete.getPoids());
		Label pds = new Label(t);
		pds.setLayoutX((startX + endX)/2);
		pds.setLayoutY((startY + endY)/2);
		arete.setPds(pds);
		
		arete.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
	        @Override 
	        public void handle(MouseEvent event) {
	        	ContextMenu cm = createContextMenuArete(arete, pds);
	            if (event.getButton() == MouseButton.SECONDARY){ 
	                cm.show(arete, event.getScreenX(), event.getScreenY());
	                menuOpen = true;
	            }else{
	            	cm.hide();
	            }
	        }
		});
		
		arete.setOnMouseClicked(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {
				if(!selectionArete){
					if (event.getButton() == MouseButton.PRIMARY){
						selectionArete = true;
						arete.setStroke(couleurSelection);
						arete.getPds().setTextFill(couleurSelection);
						for(Figure f : controller.getListeFigure()){
							if(controller.ifArete(f, arete)){
								f.setCouleur(couleurArete);
							}
						}
						textField.requestFocus();
						String t = String.valueOf(arete.getPoids());
						textField.setPromptText(t);
						ar = arete;
						p = pds;
					}
				}else if(selectionArete){
					if (event.getButton() == MouseButton.PRIMARY){
						arete.setStroke(couleurPrincipale);
						arete.getPds().setTextFill(couleurPrincipale);
						for(Figure f : controller.getListeFigure()){
							if(controller.ifArete(f, arete)){
								f.setCouleur(couleurPrincipale);
							}
						}
					}
					selectionArete = false;
					event.consume();
				}
				
			}
			
		});
		
		arete.setOnMouseDragged(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {
				if (event.getButton() == MouseButton.PRIMARY){
					if(!dragged) dragged = true;
					arete.setControlX(event.getX());
					arete.setControlY(event.getY());
					pds.setLayoutX(event.getX());
					pds.setLayoutY(event.getY());
				}

			}
			
		});
		
		arete.setOnMouseEntered(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {
				arete.setStrokeWidth(4);
			}
			
		});
		
		arete.setOnMouseExited(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {
				arete.setStrokeWidth(2);
			}
			
		});
		
		pane.getChildren().addAll(arete, pds);
		controller.addFigure(arete);
		f1.addArete(arete);
		f2.addArete(arete);
		arete.toBack();
		
	}
	
	public void createArete(Figure f1, double startX, double startY, Figure f2, double endX, double endY){
		Arete arete = new Arete(startX, startY, endX, endY);
		arete.setStartX(startX);
		arete.setStartY(startY);
		arete.setEndX(endX);
		arete.setEndY(endY);
		arete.setControlX((startX + endX)/2);
		arete.setControlY((startY + endY)/2);
		
		arete.startXProperty().bind(f1.centerXProperty());
		arete.startYProperty().bind(f1.centerYProperty());
		arete.endXProperty().bind(f2.centerXProperty());
		arete.endYProperty().bind(f2.centerYProperty());
		
		String t = String.valueOf(arete.getPoids());
		Label pds = new Label(t);
		pds.setLayoutX((startX + endX)/2);
		pds.setLayoutY((startY + endY)/2);
		arete.setPds(pds);
		
		arete.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
	        @Override 
	        public void handle(MouseEvent event) {
	        	ContextMenu cm = createContextMenuArete(arete, pds);
	            if (event.getButton() == MouseButton.SECONDARY){ 
	                cm.show(arete, event.getScreenX(), event.getScreenY());
	                menuOpen = true;
	            }else{
	            	cm.hide();
	            }
	        }
		});
		
		arete.setOnMouseClicked(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {
				if(!selectionArete){
					if (event.getButton() == MouseButton.PRIMARY){
						selectionArete = true;
						arete.setStroke(couleurSelection);
						arete.getPds().setTextFill(couleurSelection);
						for(Figure f : controller.getListeFigure()){
							if(controller.ifArete(f, arete)){
								f.setCouleur(couleurArete);
							}
						}
						textField.requestFocus();
						String t = String.valueOf(arete.getPoids());
						textField.setPromptText(t);
						ar = arete;
						p = pds;
					}
				}else if(selectionArete){
					if (event.getButton() == MouseButton.PRIMARY){
						arete.setStroke(couleurPrincipale);
						arete.getPds().setTextFill(couleurPrincipale);
						for(Figure f : controller.getListeFigure()){
							if(controller.ifArete(f, arete)){
								f.setCouleur(couleurPrincipale);
							}
						}
					}
					selectionArete = false;
					event.consume();
				}
				
			}
			
		});
		
		arete.setOnMouseDragged(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {
				if (event.getButton() == MouseButton.PRIMARY){
					if(!dragged) dragged = true;
					arete.setControlX(event.getX());
					arete.setControlY(event.getY());
					pds.setLayoutX(event.getX());
					pds.setLayoutY(event.getY());
				}

			}
			
		});
		
		arete.setOnMouseEntered(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {
				arete.setStrokeWidth(4);
			}
			
		});
		
		arete.setOnMouseExited(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {
				arete.setStrokeWidth(2);
			}
			
		});
		
		pane.getChildren().addAll(arete, pds);
		controller.addFigure(arete);
		f1.addArete(arete);
		f2.addArete(arete);
		arete.toBack();
		
	}
	
}
