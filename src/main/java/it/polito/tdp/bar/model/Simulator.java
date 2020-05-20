package it.polito.tdp.bar.model;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.PriorityQueue;

import it.polito.tdp.bar.model.Event.EventType;


public class Simulator {

	//coda eventi
	private PriorityQueue<Event> queue= new PriorityQueue<>(); 
	
	//parametri di simulazione
	                                        
	private int tavDaDieci=2; /*numero tavoli con 10 posti a sedere*/
	private int tavDaOtto=4;
	private int tavDaSei=4;
	private int tavDaQuattro=5;
	private double tolleranza=0.7; 
	//li faccio impostare dall'esterno 
	
	public void setTavDaDieci(int tavDaDieci) {
		this.tavDaDieci = tavDaDieci;
	}
	public void setTavDaOtto(int tavDaOtto) {
		this.tavDaOtto = tavDaOtto;
	}
	public void setTavDaSei(int tavDaSei) {
		this.tavDaSei = tavDaSei;
	}
	public void setTavDaQuattro(int tavDaQuattro) {
		this.tavDaQuattro = tavDaQuattro;
	}
	public void setTolleranza(double tolleranza) {
	this.tolleranza= tolleranza; 
	}
	
	//scelgo i "tempi" di simulazione
	private final LocalTime oraApertura= LocalTime.of(6, 00); 
	private final LocalTime oraChiusura= LocalTime.of(22, 00); 
	
	//modello del mondo
	private int numTavDieci; /*disponibilita' di tavoli con 10 posti a sedere*/
	private int numTavOtto; 
	private int numTavSei; 
	private int numTavQuattro; 
	
	
	//valori da calcolare
	private int clienti; //numeor totale di clienti arrivati
	private int soddisfatti; 
	private int insoddisfatti; 
	//li faccio visulaizzare all'esterno 
	public int getClienti() {
		return clienti;
	}
	public int getSoddisfatti() {
		return soddisfatti;
	}
	public int getInsoddisfatti() {
		return insoddisfatti;
	}
	
	//SIMULAZIONE 

	
	public void generaEventi() {
		
		this.queue.clear(); 
		//ipotesi -> il primo gruppo arriva subito appena apro il mio bar
	      LocalTime oraArrivoGruppo=this.oraApertura; 
		//genero i 2000 eventi
		int i=0; 
		do {
			Event e= new Event(oraArrivoGruppo,EventType.ARRIVO_GRUPPO_CLIENTI); 
			queue.add(e); 
			
			 /*frequenza arrivo gruppi di clienti*/           //tra 1 e 10 min
			Duration TIMEIN=Duration.of((long)(Math.random()*10)+1, ChronoUnit.MINUTES);
			oraArrivoGruppo= oraArrivoGruppo.plus(TIMEIN); //aggiorno con la frequenza impostata sopra
			i++; 
		}while(i<=2000 && oraArrivoGruppo.isBefore(oraChiusura)); 
		
	}
	
	public void run() {
		this.generaEventi();
		
		//impostazione iniziale 
		this.numTavDieci= this.tavDaDieci; 
		this.numTavOtto= this.tavDaOtto; 
		this.numTavSei= this.tavDaSei; 
		this.numTavQuattro= this.tavDaQuattro; 
		this.clienti=this.insoddisfatti=this.soddisfatti=0; 
		
		//esecuzione del ciclo di simulazione
				while(!this.queue.isEmpty()) {
					//l'estrazione e' ordinata per il time (com ee' stato implementato in compareTo di Event)
					Event e= this.queue.poll(); //lo estraggo finche' c'e'
					System.out.println(e); // a scopo di debug
					// se il gruppo arriva dopo l'orario di chiusura, non lo analizzo
					if(e.getTime().compareTo(oraChiusura)>0) {
				   return; 
					}
					else {
						//lo elaboro
						 this.processEvent(e); //eleborazione dell'evento
					}
				
			}
		
		
		
	}
	
	private void processEvent(Event e) {
		//che evento ho 
		switch(e.getType()) {
		case ARRIVO_GRUPPO_CLIENTI : 
			//genero un numero random di quante persone fanno parte del gruppo
			int numPersone= (int)Math.random()*10+1; 
			
			double numero=Math.random(); //genero a caso la permanenza la tavolo 
			Duration permanenza; 
			if (numero<1.0/2.0) {
				//un'ora
				permanenza= Duration.of(60, ChronoUnit.MINUTES); 
			}
			else {
				//due ore
				permanenza= Duration.of(120, ChronoUnit.MINUTES); 
			}
			
			if (numPersone<=4) {
				// e' disponibil il tavolo da quattro?
				if(this.numTavQuattro>0) {
					this.clienti++; 
					soddisfatti++; 
					this.numTavQuattro--; 
					Event nuovo= new Event(e.getTime().plus(permanenza), EventType.TAVOLO4_RESTITUITO); 
					this.queue.add(nuovo); 
				}
				else if(this.numTavSei>0) {
					this.clienti++;
					soddisfatti++; 
					this.numTavSei--;
					Event nuovo= new Event(e.getTime().plus(permanenza), EventType.TAVOLO6_RESTITUITO); 
					this.queue.add(nuovo); 
					}
				else if(this.numTavOtto>0) {
					this.clienti++; 
					soddisfatti++; 
					this.numTavOtto--;
					Event nuovo= new Event(e.getTime().plus(permanenza), EventType.TAVOLO8_RESTITUITO); 
					this.queue.add(nuovo); 
				}
				else if (this.numTavDieci>0) {
					this.clienti++; 
					soddisfatti++;
					this.numTavDieci--;
					Event nuovo= new Event(e.getTime().plus(permanenza), EventType.TAVOLO10_RESTITUITO); 
					this.queue.add(nuovo); 
					
				}
				else {
					// non ci sono tavoli 
					double bancone= Math.random(); 
					if(bancone<=tolleranza) {
						//va bene vanno al bancone
						clienti++; 
						soddisfatti++; 
					}
					else {
						//non vanno nemmeno al bancone 
						clienti++; 
						insoddisfatti++; 
					}
				}
				break; 
			}
			
			if(numPersone<=6) {
				if(this.numTavSei>0) {
					this.clienti++;
					soddisfatti++; 
					this.numTavSei--;
					Event nuovo= new Event(e.getTime().plus(permanenza), EventType.TAVOLO6_RESTITUITO); 
					this.queue.add(nuovo); 
					}
				else if(this.numTavOtto>0) {
					this.clienti++; 
					soddisfatti++; 
					this.numTavOtto--;
					Event nuovo= new Event(e.getTime().plus(permanenza), EventType.TAVOLO8_RESTITUITO); 
					this.queue.add(nuovo); 
				}
				else if (this.numTavDieci>0) {
					this.clienti++; 
					soddisfatti++;
					this.numTavDieci--;
					Event nuovo= new Event(e.getTime().plus(permanenza), EventType.TAVOLO10_RESTITUITO); 
					this.queue.add(nuovo); 
					
				}
				else {
					// non ci sono tavoli 
					double bancone= Math.random(); 
					if(bancone<=tolleranza) {
						//va bene vanno al bancone
						clienti++; 
						soddisfatti++; 
					}
					else {
						//non vanno nemmeno al bancone 
						clienti++; 
						insoddisfatti++; 
					}
				}
				break; 
			}
			
			if(numPersone<=8) {
				if(this.numTavOtto>0) {
					this.clienti++; 
					soddisfatti++; 
					this.numTavOtto--;
					Event nuovo= new Event(e.getTime().plus(permanenza), EventType.TAVOLO8_RESTITUITO); 
					this.queue.add(nuovo); 
				}
				else if (this.numTavDieci>0) {
					this.clienti++; 
					soddisfatti++;
					this.numTavDieci--;
					Event nuovo= new Event(e.getTime().plus(permanenza), EventType.TAVOLO10_RESTITUITO); 
					this.queue.add(nuovo); 
					
				}
				else {
					// non ci sono tavoli 
					double bancone= Math.random(); 
					if(bancone<=tolleranza) {
						//va bene vanno al bancone
						clienti++; 
						soddisfatti++; 
					}
					else {
						//non vanno nemmeno al bancone 
						clienti++; 
						insoddisfatti++; 
					}
				}
				
				break; 
				
			}
			
			if(numPersone<=10) {
				if (this.numTavDieci>0) {
					this.clienti++; 
					soddisfatti++;
					this.numTavDieci--;
					Event nuovo= new Event(e.getTime().plus(permanenza), EventType.TAVOLO10_RESTITUITO); 
					this.queue.add(nuovo); 
					
				}
				else {
					// non ci sono tavoli 
					double bancone= Math.random(); 
					if(bancone<=tolleranza) {
						//va bene vanno al bancone
						clienti++; 
						soddisfatti++; 
					}
					else {
						//non vanno nemmeno al bancone 
						clienti++; 
						insoddisfatti++; 
					}
				}
				
			}
			
			break; 
			
		case TAVOLO4_RESTITUITO : 
			this.numTavQuattro++; 
			break; 
		case TAVOLO6_RESTITUITO: 
			this.numTavSei++; 
			break; 
		case TAVOLO8_RESTITUITO: 
			this.numTavOtto++; 
			break; 
		case TAVOLO10_RESTITUITO: 
			this.numTavDieci++; 
			break; 
			
			
	} //fine switch 
		
		
	}
}
