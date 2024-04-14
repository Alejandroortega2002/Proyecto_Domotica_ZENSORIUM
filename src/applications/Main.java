package applications;

import controlador.Controlador_Inicio_Login;
import controlador.Controlador_InterfazDispositivos;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class Main extends Application {

	@Override
	public void start(Stage stage) {
		try {
		
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Pantalla_Inicio_Login.fxml"));

			Controlador_Inicio_Login control = new Controlador_Inicio_Login();

			loader.setController(control);

			Parent root = loader.load();
			Stage primaryStage = new Stage();
			primaryStage.setScene(new Scene(root));
			primaryStage.show();
			
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Principal principal = new Principal();
		principal.MySQLConnect();
		launch(args);
	}
}
