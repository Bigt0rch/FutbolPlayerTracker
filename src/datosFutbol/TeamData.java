package datosFutbol;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class TeamData implements Serializable{
	public String nombre;
	public int goles;
	public int tiros_a_puerta;
	public int goles_recibidos;
	public int tiros_a_puerta_recibidos;
	public int pases;
	public int asistencias;
	public int duelos;
	public int duelos_ganados;
	public int faltas;
	public int rojas_directas;
	public int rojas_por_2a_amarilla;
	public int rojas;
	public int amarillas;
	

	public TeamData(List<Integer> indices, String[][] data, Map<String, Integer> columnas) {
		goles = 0;
		tiros_a_puerta = 0;
		goles_recibidos = 0;
		tiros_a_puerta_recibidos = 0;
		pases = 0;
		asistencias = 0;
		duelos = 0;
		duelos_ganados = 0;
		faltas = 0;
		rojas_directas = 0;
		rojas_por_2a_amarilla = 0;
		rojas = 0;
		amarillas = 0;
		
		for(int i : indices) {
			String[] jugador = data[i];
			goles += Integer.parseInt(jugador[columnas.get("goals")]);
			tiros_a_puerta += Integer.parseInt(jugador[columnas.get("total_shots")]);
			pases += Integer.parseInt(jugador[columnas.get("total_passes")]);
			asistencias += Integer.parseInt(jugador[columnas.get("goal_assists")]);
			duelos += Integer.parseInt(jugador[columnas.get("duels")]);
			duelos_ganados += Integer.parseInt(jugador[columnas.get("duels_won")]);
			faltas += Integer.parseInt(jugador[columnas.get("total_fouls_conceded")]);
			rojas_directas += Integer.parseInt(jugador[columnas.get("straight_red_cards")]);
			rojas_por_2a_amarilla += Integer.parseInt(jugador[columnas.get("red_cards_2nd_yellow")]);
			rojas += Integer.parseInt(jugador[columnas.get("total_red_cards")]);
			amarillas += Integer.parseInt(jugador[columnas.get("total_red_cards")]);
		}
	}
	
}
