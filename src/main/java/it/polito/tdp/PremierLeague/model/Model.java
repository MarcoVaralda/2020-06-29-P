package it.polito.tdp.PremierLeague.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	PremierLeagueDAO dao;
	Graph<Match,DefaultWeightedEdge> grafo;
	Map<Integer,Match> idMap;
	
	List<Adiacenza> adiacenze ;
	
	List<Match> soluzioneMigliore;
	int pesoMigliore;
	
	public Model() {
		this.dao = new PremierLeagueDAO();
		this.idMap = new HashMap<>();
	}
	
	public void creaGrafo(int mese, int min) {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.adiacenze = new LinkedList<>();
	
		// Aggiungo i vertici		
		List<Match> vertici = this.dao.listMatchesGrafo(mese, this.idMap);
		Graphs.addAllVertices(this.grafo,vertici);
		
		// Aggiungo gli archi
		this.adiacenze = this.dao.getAllAdiacenze(idMap, min);
		for(Adiacenza a : this.adiacenze)
			if(!this.grafo.containsEdge(a.getM1(),a.getM2()))
				Graphs.addEdge(this.grafo,a.getM1(),a.getM2(),a.getPeso());
		
	}
	
	public String getNumeroVertici() {
		return "Numero di vertici: "+this.grafo.vertexSet().size() +"\n";
	}
	
	public String getNumeroArchi() {
		return "Numero di archi: "+this.grafo.edgeSet().size();
	}
	
	public List<Match> getVertexSet() {
		List<Match> result = new LinkedList<>();
		for(Match m : this.grafo.vertexSet())
			result.add(m);
		return result;
	}
	
	
	// Connessione MAX
	public List<Adiacenza> cercaConnessioneMax() {
		int max = 0;
		List<Adiacenza> connMax = new LinkedList<>();
		
		for(Adiacenza a : this.adiacenze) {
			if(a.getPeso()==max) {
				connMax.add(a);
			}
			if(a.getPeso()>max) {
				max = a.getPeso();
				connMax = new LinkedList<>();
				connMax.add(a);
			}
		}
		
		return connMax;
	}
	
	
	// Ricorsione
	public String cercaCammino(Match partenza, Match arrivo) {
		List<Match> parziale = new LinkedList<>();
		parziale.add(partenza);
		
		this.pesoMigliore=0;
		
		ricorsione(parziale,arrivo,0);
		
		if(this.soluzioneMigliore == null) {
			return null;
		}
		
		String risultato = "";
		
		for(Match m : this.soluzioneMigliore)
			risultato += m +"\n";
		
		risultato += "Con punteggio: " +this.pesoMigliore;
		
		return risultato;
		
	}
	
	public void ricorsione(List<Match> parziale, Match arrivo, int pesoParziale) {
		
		Match attuale = parziale.get(parziale.size()-1);
		// Caso terminale
		if(attuale.equals(arrivo)) {
			// Controllo se è la soluzione migliore finora
			if(pesoParziale>pesoMigliore) {
				pesoMigliore = pesoParziale;
				this.soluzioneMigliore = new LinkedList<>(parziale);
			}
			return;
		}
			
		// ...altrimenti
		for(Match vicino : Graphs.neighborListOf(this.grafo,attuale)) {
			// 1. Controllo che non sia già in parziale --> evito i cicli
			// 2. Controllo che non ci siano già coppie team1 vs team2 oppure team 2 vs team1
			if(!parziale.contains(vicino) && controllaParziale(parziale,vicino)==true) {
				// Provo ad aggiungere questo vicino e aggiorno pesoParziale
				parziale.add(vicino);
				DefaultWeightedEdge arco = this.grafo.getEdge(attuale, vicino);
				int nuovoPesoParziale = pesoParziale + (int)this.grafo.getEdgeWeight(arco);
				ricorsione(parziale,arrivo,nuovoPesoParziale);
				parziale.remove(vicino);
			}
		}
	}
	
	public boolean controllaParziale(List<Match> parziale, Match vicino) {
		for(Match m : parziale) {
			if( (m.teamHomeID==vicino.teamHomeID && m.teamAwayID==vicino.teamAwayID) || 
					(m.teamHomeID==vicino.teamAwayID && m.teamAwayID==vicino.teamHomeID) )
				return false;
		}
		return true;
	}
	
}
