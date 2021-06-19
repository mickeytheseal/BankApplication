package org.bankapplication.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.bankapplication.DBWorker;
import org.bankapplication.mappers.AccountMapper;
import org.bankapplication.mappers.OperationMapper;
import org.bankapplication.models.Account;
import org.bankapplication.models.Operation;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ClientController{
    private final String getAccQuery = "SELECT * FROM Accounts WHERE gov_id = ?";
    private final String getAllOpersQuery = "SELECT * FROM AccLog WHERE acc_id IN (:acc_ids) ";
    private final String getOpersQuery = "SELECT * FROM AccLog al, Accounts acc WHERE al.acc_id = acc.acc_id and acc_code = ?";

    private ObservableList<Account> accountsData = FXCollections.observableArrayList();
    private ObservableList<Operation> opersData = FXCollections.observableArrayList();

    public Button open_btn;
    public Button settings_btn;
    public Button exit_btn;
    public Button update_btn;
    public Button close_btn;
    public TableView<Operation> opers_table;
    public TableColumn<Operation,String> oper_col;
    public TableColumn<Operation,String> date_col;
    public MenuButton pick_account;
    public Button addmoney_btn;
    public Button transfer_btn;
    public TableView<Account> accounts_table;
    public TableColumn<Account,String> title_col;
    public TableColumn<Account,Long> amount_col;
    public Text username;



    public void accountsTableInit(){
        accounts_table.getItems().clear();
        List<Account> accounts = DBWorker.getJdbcTemplate().query(getAccQuery, new AccountMapper(),getGovId());
        accountsData.addAll(accounts);
        title_col.setCellValueFactory(new PropertyValueFactory<Account,String>("acc_code"));
        amount_col.setCellValueFactory(new PropertyValueFactory<Account,Long>("amount"));
        title_col.setResizable(false);
        amount_col.setResizable(false);
        accounts_table.setItems(accountsData);
    }

    public void opersTableInit(){
        opers_table.getItems().clear();
        String acc_code;
        if(pick_account.getText().equals("Выберите счет")){ acc_code = null; }
        else{ acc_code = pick_account.getText(); }
        opersData.addAll(DBWorker.getJdbcTemplate().query(getOpersQuery,new OperationMapper(),acc_code));
        oper_col.setCellValueFactory(new PropertyValueFactory<Operation,String>("operation"));
        date_col.setCellValueFactory(new PropertyValueFactory<Operation,String>("entry_date"));
        oper_col.setResizable(false);
        date_col.setResizable(false);
        opers_table.setItems(opersData);
    }

    public void menuButtonInit(){
        pick_account.setText("Выберите счет");
        pick_account.getItems().clear();
        List<Account> accounts = DBWorker.getJdbcTemplate().query(getAccQuery, new AccountMapper(),getGovId());
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
                    opersTableInit();
                }
            });
        }
        pick_account.getItems().addAll(items);
    }

    public void openClick(ActionEvent event) {
        DBWorker.getJdbcTemplate().update("INSERT INTO Accounts VALUES(?,?,GETDATE(),0,0)",getGovId(),generateCode());
        accountsTableInit();
        menuButtonInit();
    }

    private int generateCode(){
        return (getGovId() + new Date()).hashCode() & 0xfffffff;
    }

    public void closeClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("closeaccscene.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            CloseAccContoller closeAccContoller = fxmlLoader.getController();
            closeAccContoller.menuButtonInit(getGovId());
            closeAccContoller.setParent(this);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Закрыть счет");
            stage.getIcons().add(new Image("/icon.png"));
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void settingsClick(ActionEvent event) {
    }




    public void pickaccClick(ActionEvent event) {
    }

    public void exitClick(ActionEvent event) {
        Node source = (Node)  event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("loginscene.fxml"));
        Parent root = null;
        try {
            root = (Parent) fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert root != null;
        stage.getIcons().add(new Image("/icon.png"));
        stage.setTitle("Bank Application");
        stage.setResizable(false);
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void addmoney_click(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("addmoneyscene.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            AddMoneyController addMoneyController = fxmlLoader.getController();
            addMoneyController.menuButtonInit(getGovId());
            addMoneyController.setParent(this);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Пополнить счет");
            stage.getIcons().add(new Image("/icon.png"));
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void transferClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("transferscene.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            TransferController transferController = fxmlLoader.getController();
            transferController.menuButtonInit(getGovId());
            transferController.setParent(this);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Перевести средства");
            stage.getIcons().add(new Image("/icon.png"));
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update_click(ActionEvent event) {
        accountsTableInit();
        menuButtonInit();
        opersTableInit();
    }

    public void setLogin(String login){
        username.setText(login);
    }

    public String getGovId(){
        return DBWorker.getJdbcTemplate().queryForObject("SELECT client_id FROM Auth WHERE username = ?",
                        String.class,username.getText());
    }

}
