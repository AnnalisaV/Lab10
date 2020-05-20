package it.polito.tdp.bar.model;

public class TestSimulator {

	public static void main(String[] args) {
		
Simulator sim = new Simulator();
		
		//impostazione dei parametri 
	/*	sim.setTIMEIN(tIMEIN);
		sim.setTavDaQuattro(tavDaQuattro);
		sim.setTavDaSei(tavDaSei);
		sim.setTavDaOtto(tavDaOtto);
		sim.setTavDaDieci(tavDaDieci);   */


		
		
		sim.run() ; //avvio simulazione 
		
		//risultati
		int totClients = sim.getClienti() ;
		int satisfied= sim.getSoddisfatti(); 
		int dissatisfied = sim.getInsoddisfatti() ;
		
		System.out.format("Arrived %d clients, %d were dissatisfied, %d were satisfied\n", 
				totClients, dissatisfied, satisfied);

	}

}
