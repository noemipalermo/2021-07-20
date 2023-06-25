package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	
	private List<User> allUsers;
	private List<Arco> archi;
	private Graph<User, DefaultWeightedEdge> grafo;
	private Map<String, User> userIdMap;
	private YelpDao dao;
	
	
	private List<Giornalista> giornalisti ;
	private int totGiorni;
	public Model() {
		dao = new YelpDao();
		this.allUsers = new ArrayList<>();
		userIdMap = new HashMap<>();
		this.archi = new ArrayList<>();
		
		this.giornalisti = new ArrayList<>();
		this.totGiorni = 0;
	}
	
	public void creaGrafo(int anno, int nRecensioni) {
		grafo = new SimpleWeightedGraph<User, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		allUsers = dao.getUsersFiltered(nRecensioni, userIdMap);
		
		Graphs.addAllVertices(this.grafo, allUsers);
		
		archi = dao.getArchi(anno, userIdMap);
		
		for(Arco a: archi) {
			Graphs.addEdge(this.grafo, a.getU1(), a.getU2(), (double)a.getPeso());
		}
		
		System.out.println("Vertici: "+this.grafo.vertexSet().size()+", Archi: "+this.grafo.edgeSet().size());
	}
	
	public List<User> utenteSimile(User u) {
		List<User> simili = new ArrayList<>();
		double pesomax = 0.0;
		
		for(DefaultWeightedEdge edge : this.grafo.edgesOf(u)) {
			if(this.grafo.getEdgeWeight(edge)>pesomax)
				pesomax =this.grafo.getEdgeWeight(edge);
		}
		
		
		for(DefaultWeightedEdge edge : this.grafo.edgesOf(u)) {
			if(this.grafo.getEdgeWeight(edge)== pesomax)
				simili.add(Graphs.getOppositeVertex(this.grafo,edge,u));
		}
		
		System.out.print(pesomax+"");
		return simili;
	}

	public List<User> getVertices() {
		
		return this.allUsers;
	}

	public void simula(int giornalisti, int intervistati) {
		
		Simulator sim = new Simulator(grafo);
		sim.inizialize(giornalisti, intervistati);
		sim.run();
		
		this.giornalisti = sim.getGiornalisti();
		this.totGiorni = sim.getTotGiorni();
		
	}

	public List<Giornalista> getGiornalisti() {
		return giornalisti;
	}

	public int getTotGiorni() {
		return totGiorni;
	}
	
	

}
