package applications;

import controlador.Controlador_Inicio_Login;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class Main extends Application {

	public static Stage stage = null;
	private double xOffset = 0;
    private double yOffset = 0;
    
    private static final double MIN_WIDTH = 800;
    private static final double MIN_HEIGHT = 600;

	@Override
	public void start(Stage stage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("../vista/Pantalla_Inicio_Login.fxml"));
			

			Scene scene = new Scene(root);
			stage.initStyle(StageStyle.UNDECORATED);
			stage.setScene(scene);
			// Permitir que la ventana sea redimensionable
            stage.setResizable(true);

            // Establecer tamaño mínimo
            stage.setMinWidth(MIN_WIDTH);
            stage.setMinHeight(MIN_HEIGHT);

            // Eventos del mouse para mover y redimensionar la ventana
            root.setOnMousePressed(event -> {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            });

            root.setOnMouseDragged(event -> {
                // Redimensionar la ventana
                if (event.getX() > stage.getWidth() - 10 || event.getY() > stage.getHeight() - 10) {
                    double newWidth = Math.max(MIN_WIDTH, event.getX());
                    double newHeight = Math.max(MIN_HEIGHT, event.getY());
                    stage.setWidth(newWidth);
                    stage.setHeight(newHeight);
                } else {
                    // Mover la ventana
                    stage.setX(event.getScreenX() - xOffset);
                    stage.setY(event.getScreenY() - yOffset);
                }
            });

            // Evento para cambiar el cursor cuando estás cerca de los bordes
            root.setOnMouseMoved(event -> {
                if (event.getX() > stage.getWidth() - 10 || event.getY() > stage.getHeight() - 10) {
                    scene.setCursor(javafx.scene.Cursor.SE_RESIZE);
                } else {
                    scene.setCursor(javafx.scene.Cursor.DEFAULT);
                }
            });
			this.stage = stage;
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
