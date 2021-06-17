package org.bankapplication.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.bankapplication.DBWorker;

import java.io.IOException;

public class LoginController{
    @FXML
    public Button loginbtn;
    public TextField username;
    public PasswordField password;

    @FXML
    public void loginclick(ActionEvent event) {
        String isAdminQuery = "SELECT isAdmin FROM Auth WHERE username = ?";
        System.out.println(username.getText());
        System.out.println(password.getText());
        try {
            DBWorker.setDataSource(username.getText(), password.getText());
            Boolean isAdmin = DBWorker.getJdbcTemplate().queryForObject(isAdminQuery, Boolean.class,username.getText());
            if (isAdmin){ setAdminScene(event); }
            else { setClientScene(event); }
        } catch (Exception io) {
            io.printStackTrace();
        }
    }

    private void setAdminScene(ActionEvent event) throws IOException {
        Node source = (Node)  event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("adminscene.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        AdminController adminController = fxmlLoader.getController();
        adminController.setLogin(username.getText());
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void setClientScene(ActionEvent event) throws IOException {
        Node source = (Node)  event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("clientscene.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        ClientController clientController = fxmlLoader.getController();
        clientController.setLogin(username.getText());
        clientController.accountsTableInit();
        clientController.opersTableInit();
        clientController.menuButtonInit();
        stage.setScene(new Scene(root));
        stage.show();

    }

}
