import java.util.*;
import java.text.DecimalFormat;
import java.io.*;

public class HolderNodee implements Serializable {
	private Nodee wezel;
	private ArrayList<Nodee> sasiedzi = new ArrayList<Nodee>();
	private ArrayList<Way> polaczenia = new ArrayList<Way>();
	
	public HolderNodee(){
		wezel =null;
	}
	
	public HolderNodee(Nodee w){
		wezel = w;
	}
	
	public Nodee getNode(){
		return wezel;
	}
	
	public ArrayList<Nodee> getNodeList(){
		return sasiedzi;
	}
	
	public ArrayList<Way> getWayList(){
		return polaczenia;
	}
	
	private double distance(Nodee a, Nodee b){
		final int R = 6371; // Radius of the earth

	    Double latDistance = Math.toRadians(a.getLatitude() - b.getLatitude());
	    Double lonDistance = Math.toRadians(a.getLongitude() - b.getLongitude());
	    Double c = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
	            + Math.cos(Math.toRadians(a.getLatitude())) * Math.cos(Math.toRadians(b.getLatitude()))
	            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
	    Double d = 2 * Math.atan2(Math.sqrt(c), Math.sqrt(1 - c));
	    double distance = R * d * 1000; // convert to meters

	    double height = a.getLongitude() - b.getLongitude();

	    distance = Math.pow(distance, 2) + Math.pow(height, 2);
	    return Math.sqrt(distance);
	}
	
	public void print(){
		System.out.println("Node Id: " + wezel.getId() + " lat: "+ wezel.getLatitude() + " lon: "+ wezel.getLongitude() );
		for(int i = 0; i<sasiedzi.size();i++){
			System.out.println("\tNodee Id: " + sasiedzi.get(i).getId() + " lat: " + sasiedzi.get(i).getLatitude() 
					+ " lon: " + sasiedzi.get(i).getLongitude() + " dystans: " 
					+ new DecimalFormat("#0.00").format((distance(wezel,sasiedzi.get(i)))) + " metrow" 
					+ " polaczone drogo: " + polaczenia.get(i).getName());
		}
	}
	
	public String out(){
		String out = "Node Id: " + wezel.getId() + " lat: "+ wezel.getLatitude() + " lon: "+ wezel.getLongitude() + "\r\n";
		for(int i = 0; i<sasiedzi.size();i++){
			out += "\tNodee Id: " + sasiedzi.get(i).getId() + " lat: " + sasiedzi.get(i).getLatitude() 
					+ " lon: " + sasiedzi.get(i).getLongitude() + " dystans: " 
					+ new DecimalFormat("#0.00").format((distance(wezel,sasiedzi.get(i)))) + " metrow" 
					+ " polaczone drogo: " + polaczenia.get(i).getName() + "\r\n";
		}
		return out;
	}
	
	public String outTo(){
		String out = wezel.getLatitude() + " "+ wezel.getLongitude() + ";";
		for(int i = 0; i<sasiedzi.size();i++){
			out += sasiedzi.get(i).getLatitude() +" "+ sasiedzi.get(i).getLongitude()+";";
		}
		return out;
	}
}
