package org.bankapplication.models;

import org.bankapplication.DBWorker;
import org.bankapplication.mappers.AccountMapper;
import org.bankapplication.mappers.ClientMapper;

import java.util.List;

public class Account {
    private int acc_id;
    private String gov_id;
    private String acc_code;
    private String creation_date;
    private long amount;
    private boolean isBlocked;

    public Account(int acc_id, String gov_id, String acc_code, String creation_date, long amount, boolean isBlocked) {
        this.acc_id = acc_id;
        this.gov_id = gov_id;
        this.acc_code = acc_code;
        this.creation_date = creation_date;
        this.amount = amount;
        this.isBlocked = isBlocked;
    }

    public static List<Account> getAccounts(){
        return DBWorker.getJdbcTemplate().query("SELECT * FROM Accounts",new AccountMapper());
    }

    public int getAcc_id() { return acc_id; }
    public void setAcc_id(int acc_id) { this.acc_id = acc_id; }

    public String getGov_id() { return gov_id; }
    public void setGov_id(String gov_id) { this.gov_id = gov_id; }

    public String getAcc_code() { return acc_code; }
    public void setAcc_code(String acc_code) { this.acc_code = acc_code; }

    public String getCreation_date() { return creation_date; }
    public void setCreation_date(String creation_date) { this.creation_date = creation_date; }

    public long getAmount() { return amount; }
    public void setAmount(long amount) { this.amount = amount; }

    public boolean getIsBlocked() { return isBlocked; }
    public void setIsBlocked(boolean blocked) { isBlocked = blocked; }


    @Override
    public String toString() {
        return "Account{" +
                "acc_id=" + acc_id +
                ", gov_id='" + gov_id + '\'' +
                ", acc_code='" + acc_code + '\'' +
                ", creation_date='" + creation_date + '\'' +
                ", amount=" + amount +
                ", isBlocked=" + isBlocked +
                '}';
    }
}
