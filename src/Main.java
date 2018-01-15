import controller.Controller;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Model;
import view.View;

public class Main extends Application{

	private Model model;
	private View view;
	private Controller controller;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		model = new Model();
		view = new View();
		controller = new Controller(model, view);
		view.bindController(controller);
		primaryStage.setTitle("Editeur de graphes");
		Scene scene = new Scene(view);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
        launch(args);
    }
}
