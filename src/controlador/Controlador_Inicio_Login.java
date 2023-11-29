package controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
public class Controlador_Inicio_Login {

    @FXML
    private Pane contenidoArea;

    @FXML
    private JFXTextField email_login;

    @FXML
    private JFXPasswordField password_login;

    @FXML
    private void initialize() {
        // Puedes realizar inicializaciones aquí si es necesario
    }

    @FXML
    private void cerrarApp(MouseEvent event) {
        // Cerrar la aplicación correctamente utilizando un Stage
        Stage stage = (Stage) contenidoArea.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void IrRegistrate(MouseEvent event) throws IOException {
        // Cambiar a la vista de registro
        Parent fxml = FXMLLoader.load(getClass().getResource("/vista/Pagina_Registro.fxml"));
        contenidoArea.getChildren().setAll(fxml);
    }

//    @FXML
//    private void iniciarSesion(MouseEvent event) {
//        String email = email_login.getText();
//        String password = password_login.getText();
//
//        // Aquí debes realizar la lógica de autenticación, por ejemplo, verificar en una base de datos
//        boolean autenticado = autenticarUsuario(email, password);
//
//        if (autenticado) {
//            // Si el usuario es autenticado con éxito, puedes navegar a la siguiente vista o realizar otras acciones
//            // En este ejemplo, simplemente mostramos un mensaje de éxito
//            mostrarMensaje("Inicio de sesión exitoso", "¡Bienvenido, " + email + "!");
//        } else {
//            // Si la autenticación falla, muestra un mensaje de error
//            mostrarMensaje("Error de inicio de sesión", "Credenciales incorrectas. Por favor, inténtalo de nuevo.", Alert.AlertType.ERROR);
//        }
//    }
//
//    private boolean autenticarUsuario(String email, String password) {
//        // Aquí debes implementar la lógica de autenticación, por ejemplo, verificar en una base de datos
//        // Devuelve true si la autenticación es exitosa, false en caso contrario.
//        // Este es un ejemplo básico, en una aplicación real debes implementar una lógica de autenticación segura.
//        return email.equals("usuario@example.com") && password.equals("contraseña123");
//    }
//
//    private void mostrarMensaje(String titulo, String contenido) {
//        mostrarMensaje(titulo, contenido, Alert.AlertType.INFORMATION);
//    }
//
//    private void mostrarMensaje(String titulo, String contenido, Alert.AlertType tipo) {
//        Alert alert = new Alert(tipo);
//        alert.setTitle(titulo);
//        alert.setHeaderText(null);
//        alert.setContentText(contenido);
//        alert.showAndWait();
//    }
}