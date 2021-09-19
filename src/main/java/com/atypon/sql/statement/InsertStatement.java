package com.atypon.sql.statement;

import com.atypon.database.table.Record.RecordFactory;

import java.util.ArrayList;

public interface InsertStatement extends Statement {
    public ArrayList<String> getValueList();
}
