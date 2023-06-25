package it.polito.tdp.yelp.model;

public class Event implements Comparable<Event>{
	
	public enum EventType{
		DA_INTERVISTARE,
		FERIE
	}
	
	private EventType type;
	private User intervistato;
	private Giornalista giornalista;
	private int giorno;
	
	
	public Event(EventType type, User intervistato, Giornalista giornalista, int giorno) {
		super();
		this.type = type;
		this.intervistato = intervistato;
		this.giornalista = giornalista;
		this.giorno = giorno;
	}


	public EventType getType() {
		return type;
	}


	public User getIntervistato() {
		return intervistato;
	}


	public Giornalista getGiornalista() {
		return giornalista;
	}


	public int getGiorno() {
		return giorno;
	}


	@Override
	public int compareTo(Event o) {
		return this.giorno-o.giorno;
	}


	@Override
	public String toString() {
		return "Event [type=" + type + ", intervistato=" + intervistato + ", giornalista=" + giornalista + ", giorno="
				+ giorno + "]";
	}
	
	
	
}
