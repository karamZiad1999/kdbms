import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;



public class Client{

    public static void main(String[] args) {
        DB myDatabase = new DB();
        myDatabase.createTable("Students", "students.kdb", "Students-metaData.kdb");
        myDatabase.getRecord("Students", 1);
//        System.out.println(record.getField("primary key"));


 }
}