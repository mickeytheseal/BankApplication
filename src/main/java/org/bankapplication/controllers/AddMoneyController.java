package org.bankapplication.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.bankapplication.DBWorker;
import org.bankapplication.mappers.AccountMapper;
import org.bankapplication.models.Account;

import java.util.List;
import java.util.stream.Collectors;

public class AddMoneyController {
    public ClientController parent_controller;
    private final String getAccQuery = "SELECT * FROM Accounts WHERE gov_id = ?";
    private final String getAccId = "SELECT acc_id FROM Accounts WHERE acc_code = ?";
    private final String addMoney = "UPDATE Accounts SET amount += ? WHERE acc_id = ?";
    private final String isBlockedQuery = "SELECT isBlocked FROM Accounts WHERE acc_code = ?";

    public MenuButton pick_account;
    public Button submit_btn;
    public TextField money_textfield;

    @FXML
    public void initialize(){
        money_textfield.textProperty().addListener(
                (observable, oldValue, newValue) -> validate(newValue));
    }

    public void submitClick(ActionEvent event) {
        int acc_id = DBWorker.getJdbcTemplate().queryForObject(getAccId,Integer.class,pick_account.getText());
        long amount = Long.parseLong(money_textfield.getText());
        DBWorker.getJdbcTemplate().update(addMoney,amount,acc_id);
        parent_controller.menuButtonInit();
        parent_controller.opersTableInit();
        parent_controller.accountsTableInit();
        Stage stage = (Stage) submit_btn.getScene().getWindow();
        stage.close();
    }

    public void menuButtonInit(String gov_id){
        pick_account.getItems().clear();
        List<Account> accounts = DBWorker.getJdbcTemplate().query(getAccQuery, new AccountMapper(),gov_id);
        List<String> acc_codes = accounts.stream()
                .map(Account::getAcc_code)
                .collect(Collectors.toList());
        List<MenuItem> items = acc_codes.stream()
                .map(MenuItem::new)
                .collect(Collectors.toList());
        for (MenuItem item: items) {
            item.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    pick_account.setText(item.getText());
                }
            });
        }
        pick_account.getItems().addAll(items);
    }

    public void setParent(ClientController clientController){
        parent_controller = clientController;
    }

    private boolean isBlocked(){
        if (!pick_account.getText().equals("Выберите счет")){
            return DBWorker.getJdbcTemplate().queryForObject(isBlockedQuery,Boolean.class,pick_account.getText());
        } else {
            return true;
        }
    }

    private void validate(String value){
        submit_btn.setDisable(!value.matches("[0-9]+") || pick_account.getText().equals("Выберите счет")
        || isBlocked());
    }
}
