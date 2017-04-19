import java.io.*;

public class Nodee implements Serializable {
	private String id;
	private double latitude;
	private double longitude;
//	int numberOf;
//	boolean kraniec;
	
	public Nodee(String i, double lat, double lon){
		id = i;
		latitude = lat;
		longitude = lon;
//		numberOf = 0;
//		kraniec = false;
	}
	
	public Nodee(){
		id = "";
		latitude = 0;
		longitude = 0;
//		numberOf = 0;
//		kraniec = false;
	}
	
	public String print(){
		return id+" "+latitude+" "+longitude; 
	}
	
	public String getId(){
		return id;
	}
	
	public void setLatitude(double lat){
		latitude = lat;
	}
	
	public void setLongitude(double lon){
		longitude = lon;
	}
	
	public double getLatitude(){
		return latitude;
	}
	
	public double getLongitude(){
		return longitude;
	}
}
