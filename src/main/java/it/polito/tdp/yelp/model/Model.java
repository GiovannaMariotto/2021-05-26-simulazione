package it.polito.tdp.yelp.model;

import java.sql.SQLException;
import java.time.Year;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	
	private YelpDao dao;
	private Graph<Business,DefaultWeightedEdge> grafo;
	private Map<String,Business> idMap;
	private List<Business> vertici;
	private List<Review> reviews ;
	
	
	public Model() {
		dao = new YelpDao();
		idMap = new HashMap<String,Business>();
		this.getMapa();//riempire il MapID
		reviews = new LinkedList(dao.getAllReviews());

	}
	
	public void getMapa() {
		for(Business b : dao.getAllBusiness()) {
			idMap.put(b.getBusinessId(), b);
		}
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
	
	
	
	
	public String  creaGrafo(String citta, Year anno) {
		//Grafo: Semplice, orientato e pesato
		this.grafo = new SimpleDirectedWeightedGraph<Business,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		//Nodi: locali commerciali ( tabella Business )
		this.vertici=dao.getBusinessByCityAndYear(citta, anno);
		Graphs.addAllVertices(this.grafo, this.vertici);
		
		//IPOTESI 1: calcolare la media recesioni mentro leggo i Business
//		for(Business b1 : this.vertici) {
//			for(Business b2 : this.vertici) {
//				if(b1.getMedia()< b2.getMedia()) {
//					Double peso = Math.abs(b2.getMedia()-b1.getMedia());
//					Graphs.addEdge(this.grafo, b1, b2, peso);
//				}
//			}
//		}
		// Map<Business, Double> medieRecesioni = new HashMap<>();
		//caria la mappa con il DAO --> Modo per non 'sporcare' l'oggetto Business
		
		//IPOTESI 3: faccio calcolare gli archi al DB 
		List<Arco> archi = dao.getArchi(citta, anno);
		for(Arco a : archi) {
			Graphs.addEdge(this.grafo, this.idMap.get(a.getB1().getBusinessId()), this.idMap.get(a.getB2().getBusinessId()), a.getPeso());
			
		}
		
		return String.format("Grafo creato con %d vertici e %d archi \n",this.grafo.vertexSet().size(), this.grafo.edgeSet().size());
		
	}
	
	public Set<Business> getVertexSet(){
		return this.grafo.vertexSet();
	}
	
	public Set<DefaultWeightedEdge> getAllArchi(){
		return this.grafo.edgeSet();
	}
	
	//Differenza tra i pesi 
	public Business getLocaleMigliore() {
		double max = 0.0;
		Business result =null;
		
		for(Business b : this.grafo.vertexSet()) {
			double val =0.0;
			for(DefaultWeightedEdge e : this.grafo.incomingEdgesOf(b)) {
				val+=this.grafo.getEdgeWeight(e);
			}
			for(DefaultWeightedEdge e : this.grafo.outgoingEdgesOf(b)) {
				val-= this.grafo.getEdgeWeight(e);
			
			}
			if(val>max) {
				max=val;
				result=b;
			}
		}
		
		return result;
		
	}
	
	/*
	// IPOTESI "Giuseppe": calcolare la media recensioni mentre leggo i Business
	for(Business b1: this.vertici) {
		for(Business b2: this.vertici) {
			if(b1.getMediaRecensioni() < b2.getMediaRecensioni()) {
				Graphs.addEdge(this.grafo, b1, b2, b2.getMediaRecensioni()-b1.getMediaRecensioni()) ;
			}
		}
	}
	*/
	
	/*
	// IPOTESI "Giuseppe + Mappa": non modifico oggetto Business, ma creo
	// una mappa per ricordarmi le medie delle recensioni
	Map<Business, Double> medieRecensioni = new HashMap<>() ;
	// carica la mappa con il DAO
	for(Business b1: this.vertici) {
		for(Business b2: this.vertici) {
			if(medieRecensioni.get(b1) < medieRecensioni.get(b2)) {
				Graphs.addEdge(this.grafo, b1, b2, medieRecensioni.get(b2)-medieRecensioni.get(b1)) ;
			}
		}
	}
	*/
	
}
