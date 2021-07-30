package Database.Table.Record.Field.ConditionChecker;

public class ConditionCheckerFactory {

    public static ConditionChecker getInstance(String type){
        switch(type){

            case "Long":
                return new LongConditionChecker();

            case "Float":
                return new FloatConditionChecker();

            case "Double":
                return new DoubleConditionChecker();


            case "Integer":
                return new IntegerConditionChecker();

            default:
                return new StringConditionChecker();
        }
    }


}
