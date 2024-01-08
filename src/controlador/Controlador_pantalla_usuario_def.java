package controlador;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Controlador_pantalla_usuario_def {
    @FXML
    private Rectangle cuadrado_pasillo;

    @FXML
    private Button on_pasillo;

    @FXML
    private Button off_pasillo;

    private boolean estadoPasillo = false; // Inicialmente apagado

    @FXML
    void initialize() {
    	System.out.println("Controlador inicializado");
        actualizarInterfaz();
    }

    private void actualizarInterfaz() {
        Color color = estadoPasillo ? Color.GREEN : Color.RED;
        //cuadrado_pasillo.setFill(color);
    }

    @FXML
    void onBotonOnPasilloClicked() {
        estadoPasillo = true;
        actualizarInterfaz();
        System.out.println("hola");
    }

    @FXML
    private void onBotonOffPasilloClicked() {
        System.out.println("off");

        estadoPasillo = false;
        actualizarInterfaz();
    }
}
