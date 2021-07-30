package Database.Table.Record.Field.ConditionChecker;

public class IntegerConditionChecker implements ConditionChecker{
    public boolean checkCondition(String value, String condition,  String comparedValue) {
        switch(condition){
            case "=":
                return (Integer.parseInt(value) == Integer.parseInt(comparedValue));

            case ">":
                return (Integer.parseInt(value) > Integer.parseInt(comparedValue));

            case ">=":
                return (Integer.parseInt(value) >= Integer.parseInt(comparedValue));

            case "<":
                return (Integer.parseInt(value) < Integer.parseInt(comparedValue));

            case "<=":
                return (Integer.parseInt(value) <= Integer.parseInt(comparedValue));

            default:
                return false;
        }
    }
}
