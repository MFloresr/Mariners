package mario;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Created by Vipi on 05/04/2016.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        GridPane root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
        Scene sc = new Scene(root);
        primaryStage.setScene(sc);
        primaryStage.setTitle("Carrega Mariners");
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
