package org.bankapplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class TestController {
    @FXML
    public Button loginbtn;
    public TextField username;
    public TextField password;

    @FXML
    public void loginclick(ActionEvent event) {
        System.out.println("You've clicked!");
        System.out.println(username.getText());
        System.out.println(password.getText());
    }
}
