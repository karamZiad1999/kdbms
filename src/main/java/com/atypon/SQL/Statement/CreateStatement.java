package com.atypon.SQL.Statement;

public interface CreateStatement extends Statement {
    public String getMetaDataInString();
    public String getPrimaryKey();
}
