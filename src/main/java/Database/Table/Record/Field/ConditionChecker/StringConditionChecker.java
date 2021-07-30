package Database.Table.Record.Field.ConditionChecker;

public class StringConditionChecker implements ConditionChecker{

    public boolean checkCondition(String value, String condition, String comparedValue) {
        if(condition.equals("=")) return (value == comparedValue);
        else return false;
    }
}
