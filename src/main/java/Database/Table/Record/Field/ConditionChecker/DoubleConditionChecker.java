package Database.Table.Record.Field.ConditionChecker;

public class DoubleConditionChecker implements ConditionChecker {
    public boolean checkCondition(String value, String condition,  String comparedValue){
        switch(condition){
            case "=":
                return (Double.parseDouble(value) == Double.parseDouble(comparedValue));

            case ">":
                return (Double.parseDouble(value) > Double.parseDouble(comparedValue));

            case ">=":
                return (Double.parseDouble(value) >= Double.parseDouble(comparedValue));

            case "<":
                return (Double.parseDouble(value) < Double.parseDouble(comparedValue));

            case "<=":
                return (Double.parseDouble(value) <= Double.parseDouble(comparedValue));

            default:
                return false;
        }
    }
}
