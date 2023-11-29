package applications;
	
import controlador.Controlador_Inicio_Login;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	
	public static Stage stage;
	@Override
	public void start(Stage stage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../vista/Pantalla_Inicio_Login.fxml"));
			
			Controlador_Inicio_Login control = new Controlador_Inicio_Login();
			
			loader.setController(control);

			Parent root = loader.load();
			
			Scene scene = new Scene(root);
			scene.getStylesheets().add("/applications/application.css");  // Modifica la ruta según la ubicación real de tu archivo CSS

			stage.setScene(scene);
			stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
