package Database.Table.Record.Field;

import Database.Table.Record.Field.ConditionChecker.ConditionChecker;
import Database.Table.Record.Field.ConditionChecker.ConditionCheckerFactory;

public class Field {

    private String value;
    private String type;
    private ConditionChecker conditionChecker;


    public Field(String type){
        this.type = type;
        conditionChecker = new ConditionCheckerFactory().getInstance(type);
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public String getValue(){return value;}

    public String getType(){return type;}

    public boolean checkCondition(String condition, String comparedValue){

        return conditionChecker.checkCondition(value, condition, comparedValue);
    }


}
