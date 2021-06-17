package org.bankapplication.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.bankapplication.DBWorker;

import java.io.IOException;
import java.time.ZoneId;
import java.util.Date;

public class AddClientController {
    private final String addQuery = "INSERT INTO CLIENTS VALUES(?,?,?,?,?)";
    @FXML
    public TextField id_tf;
    @FXML
    public TextField name_tf;
    @FXML
    public TextField phone_tf;
    @FXML
    public TextField email_tf;
    @FXML
    public DatePicker birth_tf;
    @FXML
    public Button add_btn;

    public void addClick(ActionEvent event) {
        Date birth = Date.from(birth_tf.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        DBWorker.getJdbcTemplate().update(addQuery,id_tf.getText(),name_tf.getText(),birth,phone_tf.getText(),email_tf.getText());
        Stage stage = (Stage) add_btn.getScene().getWindow();
        stage.close();
    }
}
