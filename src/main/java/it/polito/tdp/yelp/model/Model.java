package it.polito.tdp.yelp.model;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	
	private YelpDao dao;
	private Graph<Business,DefaultWeightedEdge> grafo;
	private Map<String,Business> idMap;
	private List<Review> reviews ;
	
	
	public Model() {
		dao = new YelpDao();
		idMap = new HashMap<String,Business>();
		this.getMapa();//riempire il MapID
		reviews = dao.getAllReviews();
		
		
	}
	
	public void getMapa() {
		for(Business b : dao.getAllBusiness()) {
			idMap.put(b.getBusinessId(), b);
		}
	}
	
	
	public List<Integer> getAllAnni(){
		return dao.getAllAnni();
	}
	
	public List<String> getCitta(){
		return dao.getAllCitta();
	}
	
	
	public void getMedia(Map idMap,String citta) {
		 try {
			dao.getMedia(idMap, citta);
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		 return;
	}
	
	
	public void  creaGrafo() {
		//Grafo: Semplice, orientato e pesato
		grafo = new SimpleDirectedWeightedGraph<Business,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		//Nodi: locali commerciali ( tabella Business )
		
		
		
	}
	
	
	
}
