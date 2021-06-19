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

public class TransferController {
    public ClientController parent_controller;
    private final String getAccQuery = "SELECT * FROM Accounts WHERE gov_id = ?";
    private final String getAmount = "SELECT amount FROM Accounts WHERE acc_code = ?";
    private final String isExistQuery = " IF EXISTS(SELECT * FROM Accounts WHERE acc_code = ?) SELECT 1 ELSE SELECT 0";
    private final String withdrawMoney = "UPDATE Accounts SET amount -= ? WHERE acc_code = ?";
    private final String addMoney = "UPDATE Accounts SET amount += ? WHERE acc_code = ?";
    private final String isBlockedQuery = "SELECT isBlocked FROM Accounts WHERE acc_code = ?";
    public MenuButton pick_account;
    public Button submit_btn;
    public TextField money_textfield;
    public TextField recipient_textfield;

    @FXML
    public void initialize(){
        money_textfield.textProperty().addListener(
                (observable, oldValue, newValue) -> validate(newValue));
        recipient_textfield.textProperty().addListener(
                (observable, oldValue, newValue) -> validate(newValue));
    }

    public void submitClick(ActionEvent event) {
        String sender = pick_account.getText();
        String recipient = recipient_textfield.getText();
        long to_transfer = Long.parseLong(money_textfield.getText());
        long amount = DBWorker.getJdbcTemplate().queryForObject(getAmount,Long.class,sender);
        boolean isExist = (DBWorker.getJdbcTemplate().queryForObject(isExistQuery,Boolean.class,recipient));

        if(isExist && to_transfer <= amount) {
            DBWorker.getJdbcTemplate().update(withdrawMoney, to_transfer, sender);
            DBWorker.getJdbcTemplate().update(addMoney, to_transfer, recipient);
            parent_controller.menuButtonInit();
            parent_controller.opersTableInit();
            parent_controller.accountsTableInit();
            Stage stage = (Stage) submit_btn.getScene().getWindow();
            stage.close();
        }
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
        || recipient_textfield.getText().equals("") || money_textfield.getText().equals("")
        || isBlocked());
    }

}
