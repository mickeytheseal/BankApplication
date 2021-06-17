package org.bankapplication.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.bankapplication.DBWorker;
import org.bankapplication.mappers.AccountMapper;
import org.bankapplication.mappers.OperationMapper;
import org.bankapplication.models.Account;
import org.bankapplication.models.Operation;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ClientController{
    private final String getAccQuery = "SELECT * FROM Accounts WHERE gov_id = ?";
    private final String getOpersQuery = "SELECT * FROM AccLog WHERE acc_id IN (:acc_ids) ";

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
        List<Account> accounts = DBWorker.getJdbcTemplate().query(getAccQuery, new AccountMapper(),getGovId());
        accountsData.addAll(accounts);
        title_col.setCellValueFactory(new PropertyValueFactory<Account,String>("acc_code"));
        amount_col.setCellValueFactory(new PropertyValueFactory<Account,Long>("amount"));
        title_col.setResizable(false);
        amount_col.setResizable(false);
        accounts_table.setItems(accountsData);
    }

    public void opersTableInit(){
        List<Account> accounts = DBWorker.getJdbcTemplate().query(getAccQuery, new AccountMapper(),getGovId());
        List<String> acc_codes = accounts.stream()
                .map(Account::getAcc_code)
                .collect(Collectors.toList());
        List<Integer> acc_ids = accounts.stream()
                .map(Account::getAcc_id)
                .collect(Collectors.toList());
        String query_arg = acc_ids.stream()
                .map(Object::toString)
                .collect(Collectors.joining(",","(",")"));
        System.out.println(query_arg);

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("acc_ids", acc_ids);

        opersData.addAll(DBWorker.getNpJdbcTemplate().query(getOpersQuery,parameters, new OperationMapper()));
        oper_col.setCellValueFactory(new PropertyValueFactory<Operation,String>("operation"));
        date_col.setCellValueFactory(new PropertyValueFactory<Operation,String>("entry_date"));
        oper_col.setResizable(false);
        date_col.setResizable(false);
        opers_table.setItems(opersData);
    }

    public void menuButtonInit(){
        List<Account> accounts = DBWorker.getJdbcTemplate().query(getAccQuery, new AccountMapper(),getGovId());
        List<String> acc_codes = accounts.stream()
                .map(Account::getAcc_code)
                .collect(Collectors.toList());
        List<MenuItem> items = acc_codes.stream()
                .map(Menu::new)
                .collect(Collectors.toList());
        pick_account.getItems().addAll(items);
    }

    public void openClick(ActionEvent event) {
        System.out.println(Arrays.toString(Account.getAccounts().toArray()));
    }

    public void settingsClick(ActionEvent event) {
    }


    public void closeClick(ActionEvent event) {
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
    }

    public void transferClick(ActionEvent event) {
    }

    public void update_click(ActionEvent event) {
        accounts_table.getItems().clear();
        accountsTableInit();
        opers_table.getItems().clear();
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
