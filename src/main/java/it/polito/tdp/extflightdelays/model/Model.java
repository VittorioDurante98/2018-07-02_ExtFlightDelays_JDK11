package it.polito.tdp.extflightdelays.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model {

	private ExtFlightDelaysDAO dao;
	private Graph<Airport, DefaultWeightedEdge> grafo;
	private Map<Integer, Airport> mapAirports;
	private List<Arco> archi;
	
	public Model() {
		this.dao = new ExtFlightDelaysDAO();
	}
	
	public void creaGrafo(int distanza) {
		this.grafo = new SimpleWeightedGraph<Airport, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(grafo, dao.loadAllAirports());
		this.mapAirports = new HashMap<Integer, Airport>();
		for (Airport a : dao.loadAllAirports()) {
			mapAirports.put(a.getId(), a);
		}
		this.archi = new ArrayList<Arco>(dao.getArchi(distanza, mapAirports));
		for (Arco e : archi) {
			Graphs.addEdgeWithVertices(grafo, e.getA1(), e.getA2(), e.getPeso());
		}
		System.out.println(grafo.vertexSet().size()+"<--Vertici--Archi--> "+grafo.edgeSet().size());
		
	}
	
	public Collection<Airport> listaVertici(){
		return mapAirports.values();
	}
	
	public List<String> getVicini(Airport a){
		List<Airport> lista= new ArrayList<Airport>(Graphs.neighborListOf(grafo, a));
		List<String> result= new ArrayList<>();
		for (Airport a1 : lista) {
			if(grafo.containsEdge(a1, a)) {
				result.add(a1.getId()+" "+grafo.getEdgeWeight(grafo.getEdge(a1, a)));
			}
			else if(grafo.containsEdge(a, a1)) {
				result.add(a1.getId()+" "+grafo.getEdgeWeight(grafo.getEdge(a, a1)));
			}
		}
		/*for (Airport r : lista) {
			for (Arco e : archi) {
				if((r.equals(e.getA1()) && e.getA2().equals(a)) || (r.equals(e.getA2()) && e.getA1().equals(a))) {
					result.add(r.getId()+" "+e.getPeso());
				}
			}
		}*/
		return result;
	}
}
