package org.bankapplication;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.net.URL;


public class App extends Application {
    protected Stage stage;

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader loginloader = new FXMLLoader();
        URL loginUrl = getClass().getClassLoader().getResource("loginscene.fxml");
        loginloader.setLocation(loginUrl);
        Parent root = loginloader.load();
        stage.getIcons().add(new Image("/icon.png"));
        stage.setTitle("Bank Application");
        stage.setResizable(false);
        stage.setScene(new Scene(root));
        stage.show();
        this.stage = stage;
    }

    public static void main(String[] args) {
        launch();
    }

}