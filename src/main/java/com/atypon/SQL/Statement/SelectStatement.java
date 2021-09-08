package com.atypon.SQL.Statement;

public interface SelectStatement extends Statement{

    public boolean isCondition();
    public String getField();
    public String getCondition();
    public String getValue();
}
