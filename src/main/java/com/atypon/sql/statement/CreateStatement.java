package com.atypon.sql.statement;

import java.util.ArrayList;

public interface CreateStatement extends Statement {
    public String getMetaDataInString();
    public boolean isCreateSchema();
    public String getPrimaryKey();
}
