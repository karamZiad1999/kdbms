package com.atypon.sql.statement;

public interface SelectStatement extends Statement{

    public boolean isCondition();
    public String getField();
    public String getCondition();
    public String getValue();
    public boolean isMeta();
}
