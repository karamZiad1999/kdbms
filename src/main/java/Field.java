public class Field<T> {

    private T t;
    private String type;

    public Field(){}
    public Field(String type){
        this.type = type;
    }

    public Field(String type, T t){
        this.t = t;
        this.type = type;
    }

    public void set(T t)
    {
        this.t = t;
    }

    public T getElement(){return t;}

    public String getType(){return type;}



}
