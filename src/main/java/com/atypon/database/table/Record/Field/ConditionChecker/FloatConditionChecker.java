package com.atypon.database.table.Record.Field.ConditionChecker;

public class FloatConditionChecker implements ConditionChecker{
    public boolean checkCondition(String value, String condition,  String comparedValue){
        switch(condition){
            case "=":
                return (Float.parseFloat(value) == Float.parseFloat(comparedValue));

            case ">":
                return (Float.parseFloat(value) > Float.parseFloat(comparedValue));

            case ">=":
                return (Float.parseFloat(value) >= Float.parseFloat(comparedValue));

            case "<":
                return (Float.parseFloat(value) < Float.parseFloat(comparedValue));

            case "<=":
                return (Float.parseFloat(value) <= Float.parseFloat(comparedValue));

            default:
                return false;
        }
    }
}
