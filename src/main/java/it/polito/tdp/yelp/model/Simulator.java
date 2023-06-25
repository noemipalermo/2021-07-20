package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.yelp.model.Event.EventType;

public class Simulator {

	// input
	private int nGiornalisti;
	private int nIntervistati;
	
	// stato del sistema
	private Graph<User, DefaultWeightedEdge> grafo;
	private Set<User> intervistati;
	
	// output
	private List<Giornalista> giornalisti;
	private int totGiorni;
	
	// coda degli eventi
	private PriorityQueue<Event> queue;

	public Simulator(Graph<User, DefaultWeightedEdge> grafo) {
		super();
		this.grafo = grafo;
	}
	
	public void inizialize(int nGiornalisti, int nIntervistati) {
		this.nGiornalisti = nGiornalisti;
		this.nIntervistati = nIntervistati;
		
		this.intervistati = new HashSet<>();
		
		this.totGiorni = 0;
		
		this.giornalisti = new ArrayList<>();
		
		for(int id=0; id<this.nGiornalisti; id++) {
			giornalisti.add(new Giornalista(id));
		}
		
		this.queue = new PriorityQueue<>();
		
		// primo evento della coda
		
		for(Giornalista g: giornalisti) {
			User intervistato = selezionaIntervistato(this.grafo.vertexSet());
			
			this.intervistati.add(intervistato);
			g.setNumIntervistati(g.getNumIntervistati()+1);
			
			this.queue.add(new Event(EventType.DA_INTERVISTARE, intervistato, g, 1));
		}
	}
	


	public void run() {
		while(!this.queue.isEmpty() && this.intervistati.size()<nIntervistati) {
			Event e = this.queue.poll();
			
			this.totGiorni = e.getGiorno();
		
			switch(e.getType()) {
			
			case DA_INTERVISTARE:
				double caso = Math.random();
				
				
				if(caso<0.6) {
					// caso 1 60%
					User vicino = selezionaAdiacente (e.getIntervistato());
					
					if(vicino ==  null)
						vicino = selezionaIntervistato(this.grafo.vertexSet());
				
				
					this.queue.add(new Event(EventType.DA_INTERVISTARE, vicino, e.getGiornalista(), e.getGiorno()+1));
					
					this.intervistati.add(vicino);
					e.getGiornalista().setNumIntervistati(e.getGiornalista().getNumIntervistati()+1);
				
				}else if(caso <0.8) {
					// caso 2 20%
					
					this.queue.add(new Event(EventType.FERIE, e.getIntervistato(), e.getGiornalista(), e.getGiorno()+1));
				
				} else {
					// caso 3 
					
					this.queue.add(new Event(EventType.DA_INTERVISTARE, e.getIntervistato(), e.getGiornalista(), e.getGiorno()+1));
				}
				
				break;
			
			case FERIE:
				
				User vicino = selezionaAdiacente(e.getIntervistato());
				
				if(vicino == null) {
					vicino = selezionaIntervistato(this.grafo.vertexSet());
				}
				
				this.queue.add(new Event(EventType.DA_INTERVISTARE, vicino, e.getGiornalista(), e.getGiorno()+1));
				
				this.intervistati.add(vicino);
				e.getGiornalista().setNumIntervistati(e.getGiornalista().getNumIntervistati()+1);
				
				break;
			}
			
		}
	}
	
	
	


	public int getnGiornalisti() {
		return nGiornalisti;
	}

	public void setnGiornalisti(int nGiornalisti) {
		this.nGiornalisti = nGiornalisti;
	}

	public int getnIntervistati() {
		return nIntervistati;
	}

	public void setnIntervistati(int nIntervistati) {
		this.nIntervistati = nIntervistati;
	}
	
	

	public int getTotGiorni() {
		return totGiorni;
	}

	
	public List<Giornalista> getGiornalisti() {
		return giornalisti;
	}

	private User selezionaIntervistato(Set<User> vertexSet) {
		// seleziono un intervistato dalla lisra, avendo prima rimosso chi è stato già intervistato
		
		Set<User> candidati = new HashSet<>(vertexSet);
		candidati.removeAll(intervistati);
		
		if(candidati.size()==0)
			return null;
		
		int scelto = (int)(Math.random()*candidati.size());
		
		return (new ArrayList<>(candidati).get(scelto));
	}
	
	
	private User selezionaAdiacente(User intervistato) {

		List<User> vicini = Graphs.neighborListOf(this.grafo, intervistato);
		// rimuovo chi è stato già intervistato
		vicini.removeAll(intervistati);
		
		if(vicini.size()==0) {
			return null;
		}
		
		double max = 0.0;
		
		for(User v: vicini) {
			double peso = this.grafo.getEdgeWeight(this.grafo.getEdge(intervistato,	 v));
			if(peso > max)
				max = peso;
		}
		
		List<User> migliori = new ArrayList<>();
		
		for(User v: vicini) {
			double peso = this.grafo.getEdgeWeight(this.grafo.getEdge(intervistato,	 v));
			if(peso == max)
				migliori.add(v);
		}
		
		int scelto = (int)(Math.random()*migliori.size());
		return migliori.get(scelto);
	}
}
