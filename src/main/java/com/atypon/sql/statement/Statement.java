package com.atypon.sql.statement;

import java.io.PrintWriter;

public interface Statement {
    public String getTableName();
    public void setOutputStream(PrintWriter out);
    public PrintWriter getOutputStream();
    public String getSchemaName();
}
