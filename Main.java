import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application { // головний клас проекту для запуску програми з інтерфейсом
    @Override
    public void start(Stage stage) throws IOException { // метод запуску інтерфейсу
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("system_interface2.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Система обліку запасних частин на складі");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) { // метод запуску програми, що запускає інтерфейс
        launch();
    }
}