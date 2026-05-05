package ru.nsu.dizmestev;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Основной класс для запуска приложения JavaFX.
 */
public class SnakeApp extends Application {

    /**
     * Инициализирует и показывает главное окно приложения.
     *
     * @param stage Главная сцена (окно).
     * @throws IOException Проблема с получением ресурсов FXML.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SnakeApp.class.getResource("game-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 400);

        GameController controller = fxmlLoader.getController();
        scene.setOnKeyPressed(controller::handleKeyPressed);

        stage.setTitle("OOP Snake Game");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
