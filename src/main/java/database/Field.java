package database;

public class Field {

    private String value;
    private String type;


    public Field(){}

    public Field(String type){
        this.type = type;
    }

    public Field(String type, String value){
        this.value = value;
        this.type = type;
    }

    public void set(String value)
    {
        this.value = value;
    }

    public String getValue(){return value;}

    public String getType(){return type;}

    public boolean checkCondition(String condition, String value){
        switch(condition){

            case "=":
                return checkEquality(value);
            case "<":
                return checkIfLess(value);
            case ">":
                return checkIfGreater(value);
            case "<=":
                return checkIfLessOrEqual(value);
            case ">=":
                return checkIfGreaterOrEqual(value);
            default:
                return false;
        }
    }


    private boolean checkEquality(String comparedValue){
        return value.equalsIgnoreCase(comparedValue);
    }

    private boolean checkIfGreater(String comparedValue){

        switch(type){

            case "Long":
                return (Long.parseLong(value) > Long.parseLong(comparedValue));


            case "Float":
                return (Float.parseFloat(value) > Float.parseFloat(comparedValue));


            case "Double":
                return (Double.parseDouble(value) > Double.parseDouble(comparedValue));


            case "Integer":
                return (Integer.parseInt(value) > Integer.parseInt(comparedValue));

            default:
                return false;
        }
    }

    private boolean checkIfLess(String comparedValue){

        switch(type){

            case "Long":
                return (Long.parseLong(value) < Long.parseLong(comparedValue));


            case "Float":
                return (Float.parseFloat(value) < Float.parseFloat(comparedValue));


            case "Double":
                return (Double.parseDouble(value) < Double.parseDouble(comparedValue));


            case "Integer":
                return (Integer.parseInt(value) < Integer.parseInt(comparedValue));

            default:
                return false;
        }
    }

    private boolean checkIfLessOrEqual(String comparedValue){

        switch(type){

            case "Long":
                return (Long.parseLong(value) <= Long.parseLong(comparedValue));


            case "Float":
                return (Float.parseFloat(value) <= Float.parseFloat(comparedValue));


            case "Double":
                return (Double.parseDouble(value) <= Double.parseDouble(comparedValue));


            case "Integer":
                return (Integer.parseInt(value) <= Integer.parseInt(comparedValue));

            default:
                return false;
        }
    }


    private boolean checkIfGreaterOrEqual(String comparedValue){

        switch(type){
            case "Long":
                return (Long.parseLong(value) >= Long.parseLong(comparedValue));


            case "Float":
                return (Float.parseFloat(value) >= Float.parseFloat(comparedValue));


            case "Double":
                return (Double.parseDouble(value) >= Double.parseDouble(comparedValue));


            case "Integer":
                return (Integer.parseInt(value) >= Integer.parseInt(comparedValue));

            default:
                return false;
        }
    }
}
