import java.util.*;


public class Holder {
	private ArrayList<Nodee> lista = new ArrayList<Nodee>();
	private Way way;
	
	public Holder(Way w, ArrayList<Nodee> n){
		way = w;
		lista = n;
	}
	
	public void print(){
		System.out.println("Way Id:" + way.getId()+ " Typ: " + way.getType() + " Nazwa: " + way.getName() );
		for(Nodee x: lista){
			System.out.println("\tNodee Id:" + x.getId());
		}
	}
	
	public Way getWay(){
		return way;
	}
	
	/**
	 * 
	 * @return lista - zawieraj¹ca node na danej drodze
	 * 
	 */
	
	public ArrayList<Nodee> getLista(){
		return lista;
	}
}
