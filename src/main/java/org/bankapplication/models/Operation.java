package org.bankapplication.models;

public class Operation {
    private int entry_id;
    private int acc_id;
    private String operation;
    private String entry_date;

    public Operation(int entry_id, int acc_id, String operation, String entry_date) {
        this.entry_id = entry_id;
        this.acc_id = acc_id;
        this.operation = operation;
        this.entry_date = entry_date;
    }

    public int getEntry_id() { return entry_id; }
    public void setEntry_id(int entry_id) { this.entry_id = entry_id; }

    public int getAcc_id() { return acc_id; }
    public void setAcc_id(int acc_id) { this.acc_id = acc_id; }

    public String getOperation() { return operation; }
    public void setOperation(String operation) { this.operation = operation; }

    public String getEntry_date() { return entry_date; }
    public void setEntry_date(String entry_date) { this.entry_date = entry_date; }
}
