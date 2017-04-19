import java.io.*;

public class Way implements Serializable {
	private String id;
	private String name;
	private String type;
	
	public Way(String i, String t, String n){
		id = i;
		name = n;
		type = t;
	}
	
	public Way(){
		id = "nosz";
		name = "kurwa!!!!";
	}
	
	public void setName(String n){
		name = n;
	}
	
	public String getName(){
		return name;
	}
	
	public String getId(){
		return id;
	}
	
	public String getType(){
		return type;
	}
	
	public String print(){
		return " " + id + " " + name;
	}
	
	public String toString(){
		return " " + id + " " + name + "";
	}
}
