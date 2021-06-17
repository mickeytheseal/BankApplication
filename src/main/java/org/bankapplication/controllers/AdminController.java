package org.bankapplication.controllers;

import javafx.beans.binding.Bindings;
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
import javafx.util.Callback;
import org.bankapplication.DBWorker;
import org.bankapplication.models.Account;
import org.bankapplication.models.Client;

import java.io.IOException;

public class AdminController {
    private ObservableList<Client> clientsData = FXCollections.observableArrayList();
    private ObservableList<Account> accountsData = FXCollections.observableArrayList();

    public TableView<Account> accounts_table;
    public TableColumn<Account,String> client_col;
    public TableColumn<Account,String> account_col;
    public TableColumn<Account,Long> amount_col;
    public TableColumn<Account,String> date_col;
    public TableColumn<Account,Boolean> blocked_col;
    public Button add_client_btn;
    public Button update_table_btn;
    public Text username;
    public TableView<Client> clients_table;
    public TableColumn<Client,String> id_col;
    public TableColumn<Client,String> fio_col;
    public TableColumn<Client,String> birth_col;
    public TableColumn<Client,String> phone_col;
    public TableColumn<Client,String> email_col;
    public Button settings_btn;
    public Button exit_btn;

    @FXML
    public void initialize() {
        clientTableInit();
        clientTableContextInit();
        accountTableInit();
        accountTableContextInit();
    }

    private void clientTableInit(){
        clientsData.addAll(Client.getClients());
        id_col.setCellValueFactory(new PropertyValueFactory<Client, String>("gov_id"));
        fio_col.setCellValueFactory(new PropertyValueFactory<Client, String>("initials"));
        birth_col.setCellValueFactory(new PropertyValueFactory<Client, String>("birth_date"));
        phone_col.setCellValueFactory(new PropertyValueFactory<Client, String>("phone_num"));
        email_col.setCellValueFactory(new PropertyValueFactory<Client, String>("email"));
        id_col.setResizable(false);
        fio_col.setResizable(false);
        birth_col.setResizable(false);
        phone_col.setResizable(false);
        email_col.setResizable(false);
        clients_table.setItems(clientsData);
    }

    //Source: https://gist.github.com/james-d/7758918
    private void clientTableContextInit(){
        clients_table.setRowFactory(new Callback<TableView<Client>, TableRow<Client>>() {
            public TableRow<Client> call(TableView<Client> tableView) {
                final TableRow<Client> row = new TableRow<>();
                final ContextMenu contextMenu = new ContextMenu();
                final MenuItem removeMenuItem = new MenuItem("Удалить");
                removeMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        clients_table.getItems().remove(row.getItem());
                        DBWorker.getJdbcTemplate().update("DELETE FROM Clients WHERE gov_id = ?",row.getItem().getGov_id());
                        updateClients();
                    }
                });
                contextMenu.getItems().add(removeMenuItem);

                row.contextMenuProperty().bind(
                        Bindings.when(row.emptyProperty())
                                .then((ContextMenu)null)
                                .otherwise(contextMenu)
                );
                return row ;
            }
        });
    }

    private void accountTableInit(){
        accountsData.addAll(Account.getAccounts());
        client_col.setCellValueFactory(new PropertyValueFactory<Account, String>("gov_id"));
        account_col.setCellValueFactory(new PropertyValueFactory<Account, String>("acc_code"));
        amount_col.setCellValueFactory(new PropertyValueFactory<Account, Long>("amount"));
        date_col.setCellValueFactory(new PropertyValueFactory<Account, String>("creation_date"));
        blocked_col.setCellValueFactory(new PropertyValueFactory<Account, Boolean>("isBlocked"));
        client_col.setResizable(false);
        account_col.setResizable(false);
        amount_col.setResizable(false);
        date_col.setResizable(false);
        blocked_col.setResizable(false);
        accounts_table.setItems(accountsData);
    }
    private void accountTableContextInit(){
        accounts_table.setRowFactory(new Callback<TableView<Account>, TableRow<Account>>() {
            public TableRow<Account> call(TableView<Account> tableView) {
                final TableRow<Account> row = new TableRow<>();
                final ContextMenu contextMenu = new ContextMenu();
                final MenuItem removeMenuItem = new MenuItem("Заблокировать/Разблокировать");
                removeMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        if(row.getItem().getIsBlocked()) {
                            DBWorker.getJdbcTemplate().update("UPDATE Accounts SET isBlocked = 0 WHERE acc_code = ?", row.getItem().getAcc_code());
                        } else {
                            DBWorker.getJdbcTemplate().update("UPDATE Accounts SET isBlocked = 1 WHERE acc_code = ?", row.getItem().getAcc_code());
                        }
                        updateAccounts();
                    }
                });
                contextMenu.getItems().add(removeMenuItem);

                row.contextMenuProperty().bind(
                        Bindings.when(row.emptyProperty())
                                .then((ContextMenu)null)
                                .otherwise(contextMenu)
                );
                return row ;
            }
        });
    }

    @FXML
    public void addClientClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("addclientscene.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Добавление клиента");
            stage.getIcons().add(new Image("/icon.png"));
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void updateClick(ActionEvent event) {
        updateClients();
        updateAccounts();
    }

    public void updateClients(){
        clients_table.getItems().clear();
        clientTableInit();
    }

    public void updateAccounts(){
        accounts_table.getItems().clear();
        accountTableInit();
    }

    public void settingsClick(ActionEvent event) {
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

    public void setLogin(String login){
        username.setText(login);
    }
}
