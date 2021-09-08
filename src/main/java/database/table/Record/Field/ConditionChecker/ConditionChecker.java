package database.table.Record.Field.ConditionChecker;

public interface ConditionChecker{
    public boolean checkCondition(String value, String condition, String comparedValue);
}
