package SQL.Statement;

import java.io.PrintWriter;

public interface Statement {
    public String getTableName();
    public void setOutputStream(PrintWriter out);
    public PrintWriter getOutputStream();
}
