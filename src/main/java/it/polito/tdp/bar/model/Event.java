package it.polito.tdp.bar.model;

import java.time.LocalTime;

public class Event implements Comparable<Event> {

	
	public enum EventType{
		ARRIVO_GRUPPO_CLIENTI, TAVOLO4_RESTITUITO,TAVOLO6_RESTITUITO,TAVOLO8_RESTITUITO,TAVOLO10_RESTITUITO, 
	}
	
	
	private LocalTime time; 
	private EventType type;
	
	
	/**
	 * @param time
	 * @param type
	 */
	public Event(LocalTime time, EventType type) {
		super();
		this.time = time;
		this.type = type;
	}


	public LocalTime getTime() {
		return time;
	}


	public void setTime(LocalTime time) {
		this.time = time;
	}


	public EventType getType() {
		return type;
	}


	public void setType(EventType type) {
		this.type = type;
	}

	


	@Override
	public String toString() {
		return type+" "+time;
	}


	@Override
	public int compareTo(Event other) {
		
		return this.time.compareTo(other.time);
	} 
	
	
	
}
