package datosFutbol;

import java.util.Arrays;
import java.util.Map;

import java.io.Serializable;

public class GeneralPlayerDataX90 extends PlayerDataX90{
	
	
	public double goals;
	public double shots;
	public double pShotAccuracy;
	public double assists;
	public int minutes;
	public double passes;
	public double succesfull_passes;
	public double pPassAccuracy;
	public double duels;
	public double duels_won;
	public double pDuelAccuracy;
	public double fouls;
	public double red_cards_2nd_yellow;
	public double yellow_cards;
	public double total_red_cards; 
	public double straight_red_cards;
	public double successful_dribbles;
	public double unsuccessful_dribbles;
	public double pDribbleAccuracy;

	public GeneralPlayerDataX90(String datos[], Map<String,Integer> campos) {
		position = datos[campos.get("posicion")];
		minutes = (int) Double.parseDouble(datos[campos.get("time_played")]);
		name = datos[campos.get("nombre")];
		team = datos[campos.get("equipo")];
		nationality = datos[campos.get("pais")];
		height = (double) Double.parseDouble(datos[campos.get("altura")]);
		weight = (double) Double.parseDouble(datos[campos.get("peso")]);
		position = datos[campos.get("posicion")];
		
		pShotAccuracy = ((double) Double.parseDouble(datos[campos.get("goals")]))/((double) Double.parseDouble(datos[campos.get("total_shots")]));
		pPassAccuracy = ((double) Double.parseDouble(datos[campos.get("total_successful_passes_excl_crosses_corners")]))/((double) Double.parseDouble(datos[campos.get("total_passes")]));
		pDuelAccuracy = ((double) Double.parseDouble(datos[campos.get("duels_won")]))/((double) Double.parseDouble(datos[campos.get("duels")]));
		pDribbleAccuracy = (double) Double.parseDouble(datos[campos.get("successful_dribbles")])/((double) Double.parseDouble(datos[campos.get("successful_dribbles")])+(double) Double.parseDouble(datos[campos.get("unsuccessful_dribbles")]));
		
		goals = ((double) Double.parseDouble(datos[campos.get("goals")])/minutes)*90;
		shots = ((double) Double.parseDouble(datos[campos.get("total_shots")])/minutes)*90;
		assists = ((double) Double.parseDouble(datos[campos.get("goal_assists")])/minutes)*90;
		passes = ((double) Double.parseDouble(datos[campos.get("total_passes")])/minutes)*90;
		succesfull_passes = ((double) Double.parseDouble(datos[campos.get("total_successful_passes_excl_crosses_corners")])/minutes)*90;
		duels = ((double) Double.parseDouble(datos[campos.get("duels")])/minutes)*90;
		duels_won = ((double) Double.parseDouble(datos[campos.get("duels_won")])/minutes)*90;
		fouls = ((double) Double.parseDouble(datos[campos.get("total_fouls_conceded")])/minutes)*90;
		red_cards_2nd_yellow = ((double) Double.parseDouble(datos[campos.get("red_cards_2nd_yellow")])/minutes)*90;
		yellow_cards = ((double) Double.parseDouble(datos[campos.get("yellow_cards")])/minutes)*90;
		total_red_cards = ((double) Double.parseDouble(datos[campos.get("total_red_cards")])/minutes)*90;
		straight_red_cards = ((double) Double.parseDouble(datos[campos.get("straight_red_cards")])/minutes)*90;
		successful_dribbles = ((double) Double.parseDouble(datos[campos.get("successful_dribbles")])/minutes)*90;
		unsuccessful_dribbles = ((double) Double.parseDouble(datos[campos.get("unsuccessful_dribbles")])/minutes)*90;

		
	}
}
