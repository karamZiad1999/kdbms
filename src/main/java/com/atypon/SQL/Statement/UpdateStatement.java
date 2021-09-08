package com.atypon.SQL.Statement;

import java.util.HashMap;

public interface UpdateStatement extends Statement{
    public HashMap<String, String> getUpdates();
    public String getField();
    public String getCondition();
    public String getValue();
}
