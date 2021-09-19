package com.atypon.sql.statement;

public interface DeleteStatement extends Statement {
    public String getField();
    public String getCondition();
    public String getValue();
}
