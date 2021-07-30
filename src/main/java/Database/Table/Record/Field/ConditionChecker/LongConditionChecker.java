package Database.Table.Record.Field.ConditionChecker;

public class LongConditionChecker implements ConditionChecker{
    public boolean checkCondition(String value, String condition,  String comparedValue) {
        switch(condition){
            case "=":
                return (Long.parseLong(value) == Long.parseLong(comparedValue));

            case ">":
                return (Long.parseLong(value) > Long.parseLong(comparedValue));

            case ">=":
                return (Long.parseLong(value) >= Long.parseLong(comparedValue));

            case "<":
                return (Long.parseLong(value) < Long.parseLong(comparedValue));

            case "<=":
                return (Long.parseLong(value) <= Long.parseLong(comparedValue));

            default:
                return false;
        }
    }
}
