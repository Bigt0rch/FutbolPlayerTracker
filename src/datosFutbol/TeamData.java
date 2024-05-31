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
			goles += (int) Double.parseDouble(jugador[columnas.get("goals")]);
			tiros_a_puerta += (int) Double.parseDouble(jugador[columnas.get("total_shots")]);
			if(jugador[columnas.get("posicion")].equals("Portero")) {
				goles_recibidos += (int) Double.parseDouble(jugador[columnas.get("goals_conceded")]);
				tiros_a_puerta_recibidos += (int) Double.parseDouble(jugador[columnas.get("goals_conceded")]) + (int) Double.parseDouble(jugador[columnas.get("saves_made")]);				
			}
			pases += (int) Double.parseDouble(jugador[columnas.get("total_passes")]);
			asistencias += (int) Double.parseDouble(jugador[columnas.get("goal_assists")]);
			duelos += (int) Double.parseDouble(jugador[columnas.get("duels")]);
			duelos_ganados += (int) Double.parseDouble(jugador[columnas.get("duels_won")]);
			faltas += (int) Double.parseDouble(jugador[columnas.get("total_fouls_conceded")]);
			rojas_directas += (int) Double.parseDouble(jugador[columnas.get("straight_red_cards")]);
			rojas_por_2a_amarilla += (int) Double.parseDouble(jugador[columnas.get("red_cards_2nd_yellow")]);
			rojas += (int) Double.parseDouble(jugador[columnas.get("total_red_cards")]);
			amarillas += (int) Double.parseDouble(jugador[columnas.get("yellow_cards")]);
		}
	}
	
}
