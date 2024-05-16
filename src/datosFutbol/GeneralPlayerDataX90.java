package datosFutbol;

import java.util.Map;

import java.io.Serializable;

public class GeneralPlayerDataX90 extends PlayerDataX90{
	
	
	public int goals;
	public int shots;
	public double pShotAccuracy;
	public int assists;
	public int minutes;
	public int passes;
	public int  succesfull_passes;
	public double pPassAccuracy;
	public int duels;
	public int duels_won;
	public double pDuelAccuracy;
	public int fouls;
	public int red_cards_2nd_yellow;
	public int yellow_cards;
	public int total_red_cards; 
	public int straight_red_cards;
	public int successful_dribbles;
	public int unsuccessful_dribbles;
	public double pDribbleAccuracy;

	public GeneralPlayerDataX90(String datos[], Map<String,Integer> campos) {
		position = datos[campos.get("posicion")];
		minutes = Integer.parseInt(datos[campos.get("minutes")]);
		name = datos[campos.get("nombre")];
		team = datos[campos.get("equipo")];
		nationality = datos[campos.get("pais")];
		height = Integer.parseInt(datos[campos.get("altura")]);
		weight = Integer.parseInt(datos[campos.get("peso")]);
		position = datos[campos.get("posicion")];
		
		pShotAccuracy = (Integer.parseInt(datos[campos.get("goals")]))/(Integer.parseInt(datos[campos.get("shots")]));
		pPassAccuracy = (Integer.parseInt(datos[campos.get("succesfull_passes")]))/(Integer.parseInt(datos[campos.get("passes")]));
		pDuelAccuracy = (Integer.parseInt(datos[campos.get("duels_won")]))/(Integer.parseInt(datos[campos.get("duels")]));
		pDribbleAccuracy = Integer.parseInt(datos[campos.get("successful_dribbles")])/(Integer.parseInt(datos[campos.get("successful_dribbles")])+Integer.parseInt(datos[campos.get("unsuccessful_dribbles")]));
		
		goals = (Integer.parseInt(datos[campos.get("goals")])/minutes)*90;
		shots = (Integer.parseInt(datos[campos.get("total_shots")])/minutes)*90;
		assists = (Integer.parseInt(datos[campos.get("goal_assists")])/minutes)*90;
		passes = (Integer.parseInt(datos[campos.get("total_passes")])/minutes)*90;
		succesfull_passes = (Integer.parseInt(datos[campos.get("succesfull_passes")])/minutes)*90;
		duels = (Integer.parseInt(datos[campos.get("duels")])/minutes)*90;
		duels_won = (Integer.parseInt(datos[campos.get("duels_won")])/minutes)*90;
		fouls = (Integer.parseInt(datos[campos.get("total_fouls_conceded")])/minutes)*90;
		red_cards_2nd_yellow = (Integer.parseInt(datos[campos.get("red_cards_2nd_yellow")])/minutes)*90;
		yellow_cards = (Integer.parseInt(datos[campos.get("yellow_cards")])/minutes)*90;
		total_red_cards = (Integer.parseInt(datos[campos.get("total_red_cards")])/minutes)*90;
		straight_red_cards = (Integer.parseInt(datos[campos.get("straight_red_cards")])/minutes)*90;
		successful_dribbles = (Integer.parseInt(datos[campos.get("successful_dribbles")])/minutes)*90;
		unsuccessful_dribbles = (Integer.parseInt(datos[campos.get("unsuccessful_dribbles")])/minutes)*90;

		
	}
}
