package org.bankapplication.models;

import org.bankapplication.DBWorker;
import org.bankapplication.mappers.AccountMapper;
import org.bankapplication.mappers.ClientMapper;

import java.util.List;

public class Client {

    private String gov_id;
    private String full_name;
    private String initials;
    private String birth_date;
    private String phone_num;
    private String email;

    public Client(String gov_id, String full_name, String birth_date, String phone_num, String email) {
        this.gov_id = gov_id;
        this.full_name = full_name;
        this.birth_date = birth_date;
        this.phone_num = phone_num;
        this.email = email;
        String[] name = full_name.split(" ");
        initials = name[0] + " ";
        initials += name[1].charAt(0) + ". ";
        initials += name[2].charAt(0) + ".";
    }


    public static List<Client> getClients(){
        return DBWorker.getJdbcTemplate().query("SELECT * FROM Clients",new ClientMapper());
    }

    public List<Account> getClientAccounts(){
        return DBWorker.getJdbcTemplate().query("SELECT * FROM Accounts WHERE gov_id = ?",new AccountMapper(),gov_id);
    }

    public String getGov_id() {
        return gov_id;
    }
    public void setGov_id(String gov_id) {
        this.gov_id = gov_id;
    }

    public String getFull_name() {
        return full_name;
    }
    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getInitials() {
        return initials;
    }
    public void setInitials(String initials) { this.initials = initials; }

    public String getBirth_date() {
        return birth_date;
    }
    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public String getPhone_num() {
        return phone_num;
    }
    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Client{" +
                "gov_id='" + gov_id + '\'' +
                ", full_name='" + full_name + '\'' +
                ", initials='" + initials + '\'' +
                ", birth_date='" + birth_date + '\'' +
                ", phone_num='" + phone_num + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
