import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.util.*;
import java.io.*;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;



/**
 * 
 * @author Aleksy Wo³owiec
 *
 */
		

public class Main {
	public static void main(String[] args){
		try{
			ArrayList<Holder> lista = new ArrayList<Holder>();
			ArrayList<HolderNodee> listaNodee = new ArrayList<HolderNodee>();
			File inputFile = new File(""); // Œcie¿ka do pliku XML zawieraj¹cego fragment mapy z serwisu OpenStreetMap
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			NodeList waysList = doc.getElementsByTagName("way");
			for(int i = 0; i < waysList.getLength(); i++){
				Node wayNode = waysList.item(i);
				Element wayElement = (Element) wayNode;
				NodeList ndsList = wayElement.getElementsByTagName("nd");
				ArrayList<Nodee> lista2 = new ArrayList<Nodee>();
				for(int j = 0; j< ndsList.getLength(); j++){
					Node ndNode = ndsList.item(j);
					Element ndElement = (Element) ndNode;
					lista2.add(new Nodee(ndElement.getAttribute("ref"),0,0));
				}
				String h = " ";
				String n = " ";
				NodeList tagsList = wayElement.getElementsByTagName("tag");
				for(int j = 0; j< tagsList.getLength(); j++){
					Node tagNode = tagsList.item(j);
					Element tagElement = (Element) tagNode;
					if(tagElement.getAttribute("k").equals("highway")){
						h = tagElement.getAttribute("v");
					}
					if(tagElement.getAttribute("k").equals("name")){
						n = tagElement.getAttribute("v");
					}
				}
				lista.add(new Holder(new Way(wayElement.getAttribute("id"),h,n),lista2));
		    }
			/**
			 * nieusuwac - zawiera typy dróg których nie usuwamy
			 * 
			 */
			String[] nieusuwac = {"motorway","trunk","secondary","secondary_link","primary","tertiary",
									"unclassified", "motorway_link", "trunk_link", "primary_link", "pedestrian",
									"secondary_link", "tertiary_link", "living_street", "residential"};
			List<String> nieusuwacc= Arrays.asList(nieusuwac); 
			lista.removeIf(v -> !nieusuwacc.contains(v.getWay().getType()));
			/**
			 *  usuwamy node które wystêpuj¹ jedynie raz
			 */
			for(int i = 0; i<lista.size();i++){
				for(int g = 1; g < lista.get(i).getLista().size()-1; g++){
					int czywystepuje = 0;
					for(int j = 0; j<lista.size();j++){
						if(i==j) continue;
						for(int f = 1; f < lista.get(j).getLista().size()-1; f++){
							if(lista.get(i).getLista().get(g).getId().equals(lista.get(j).getLista().get(f).getId())){
								czywystepuje++;
							}
						}
					}
					if(czywystepuje == 0){
						lista.get(i).getLista().remove(g);		
						g--;
					}
				}
			}
//			lista.forEach(d -> d.getLista().forEach(n -> n.numberOf++));
//			lista.forEach(d -> d.getLista().get(0).kraniec = true);
//			lista.forEach(d -> d.getLista().get(d.getLista().size() - 1).kraniec = true);
//			lista.forEach(d -> d.getLista().removeIf(n -> n.numberOf < 2 && n.kraniec == false));
			
			/**
			 * 
			 * tworzymy listê node-ów i s¹siadów poszczególnych node-ów
			 * zostawiamy tylko tych którzy maj¹ przynajmniej 2 s¹siadów
			 * 
			 */
			for(int i = 0; i<lista.size();i++){
				for(int g = 0; g < lista.get(i).getLista().size(); g++){
					HolderNodee pom = new HolderNodee(lista.get(i).getLista().get(g));
					for(int j = 0; j<lista.size();j++){
						for(int f = 0; f < lista.get(j).getLista().size(); f++){
							if(pom.getNode().getId().equals(lista.get(j).getLista().get(f).getId())){
								pom.getWayList().add(lista.get(j).getWay());
								if(f==0 ){
										pom.getNodeList().add(lista.get(j).getLista().get(f+1));
								}
								else if(f==lista.get(j).getLista().size()-1){
									pom.getNodeList().add(lista.get(j).getLista().get(f-1));
								}
							}
						}
					}
					if(pom.getNodeList().size()>1){
						listaNodee.add(pom);
					}
				}
			}
			for(int i = 0; i < listaNodee.size();i++){
//				if(listaNodee.get(i).getWayList().size() == 2 
//					&& listaNodee.get(i).getWayList().get(0).getName().equals(listaNodee.get(i).getWayList().get(1).getName())){
//					listaNodee.remove(i);
//					i--;
//				}
				for(int j = i+1; j<listaNodee.size(); j++)
				{
					if(listaNodee.get(i).getNode().getId().equals(listaNodee.get(j).getNode().getId()))
					{
						listaNodee.remove(j);
						j--;
					}
				}
			}
			for(int i = 0; i < listaNodee.size(); i++){
				NodeList nodesList = doc.getElementsByTagName("node");
				int start = 0;
				int koniec = nodesList.getLength();
				while(true)
				{
					int srodek = (start+koniec)/2;
					Node nodeNode = nodesList.item(srodek);
					Element nodeElement = (Element) nodeNode;
					if(nodeElement.getAttribute("id").equals(listaNodee.get(i).getNode().getId())){
						listaNodee.get(i).getNode().setLatitude(Double.parseDouble(nodeElement.getAttribute("lat")));
						listaNodee.get(i).getNode().setLongitude(Double.parseDouble(nodeElement.getAttribute("lon")));
						break;
					}
					else if(Long.valueOf(nodeElement.getAttribute("id"))>Long.valueOf(listaNodee.get(i).getNode().getId())){
						koniec = srodek;
					}
					else{
						start = srodek;
					}
				}
				for(int j = 0; j<listaNodee.get(i).getNodeList().size();j++){
					start = 0;
					koniec = nodesList.getLength();
					while(true)
					{
						int srodek = (start+koniec)/2;
						Node nodeNode = nodesList.item(srodek);
						Element nodeElement = (Element) nodeNode;
						if(nodeElement.getAttribute("id").equals(listaNodee.get(i).getNodeList().get(j).getId())){
							listaNodee.get(i).getNodeList().get(j).setLatitude(Double.parseDouble(nodeElement.getAttribute("lat")));
							listaNodee.get(i).getNodeList().get(j).setLongitude(Double.parseDouble(nodeElement.getAttribute("lon")));
							break;
						}
						else if(Long.valueOf(nodeElement.getAttribute("id"))>Long.valueOf(listaNodee.get(i).getNodeList().get(j).getId())){
							koniec = srodek;
						}
						else{
							start = srodek;
						}
					}
				}
			};
			try(  PrintWriter out = new PrintWriter( "out1.txt" );) {
				
			      for(HolderNodee c: listaNodee){
			    	  out.println(c.out());
			      }	
			      out.close();;
			}
			
			
			/**
			 *  Serializacja i deserializacja Standardowa;
			 */
				
				ObjectOutputStream out2 = new ObjectOutputStream(
											new BufferedOutputStream(
												new FileOutputStream("serializacja.ser")));
			
		      
		    	  out2.writeObject(listaNodee);
		    	  out2.close();
		
				FileInputStream ist = new FileInputStream("serializacja.ser");
				ObjectInputStream in = new ObjectInputStream(ist);
				
				ArrayList<HolderNodee> listanowa = new ArrayList<HolderNodee>();
				listanowa = (ArrayList<HolderNodee>) in.readObject();
				
			/**
			 * 
			 * Serializacja i Deserializacja XML
			 * 
			 */
//				XStream xstream = new XStream(new StaxDriver());
//				xstream.alias("HolderNodee", HolderNodee.class);
//				xstream.alias("Nodee", Nodee.class);
//				xstream.alias("Way", Way.class);
//				
//				PrintWriter outXML = new PrintWriter("outt.xml");
//				String xml = xstream.toXML(listaNodee);
//				outXML.println(xml);
//				outXML.close();
//				
//		
//				
//				ArrayList<HolderNodee> listaXML = new ArrayList<HolderNodee>();
//				listaXML = (ArrayList<HolderNodee>)xstream.fromXML(xml);
//				
//				for(HolderNodee c: listaXML){
//					c.print();
//				}
//				
			
				File xmlFile = new File("outXML.xml");

				XStream xstream = new XStream();
				Writer writer = new FileWriter(xmlFile);        
				writer.write(xstream.toXML(listaNodee));
				writer.close();

				ArrayList<HolderNodee> fromXML = (ArrayList<HolderNodee>) xstream.fromXML(new FileInputStream(xmlFile));
				
				for(HolderNodee c: fromXML){
					c.print();
				}
				
		} catch(Exception e){
		e.printStackTrace();
		}
	}

}


