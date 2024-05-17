package datosFutbol;

import java.util.Arrays;
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
		System.out.println(Arrays.toString(datos));
		minutes = (int) Double.parseDouble(datos[campos.get("time_played")]);
		name = datos[campos.get("nombre")];
		team = datos[campos.get("equipo")];
		nationality = datos[campos.get("pais")];
		height = (int) Double.parseDouble(datos[campos.get("altura")]);
		weight = (int) Double.parseDouble(datos[campos.get("peso")]);
		position = datos[campos.get("posicion")];
		
		pShotAccuracy = ((int) Double.parseDouble(datos[campos.get("goals")]))/((int) Double.parseDouble(datos[campos.get("total_shots")]));
		pPassAccuracy = ((int) Double.parseDouble(datos[campos.get("total_successful_passes_excl_crosses_corners")]))/((int) Double.parseDouble(datos[campos.get("total_passes")]));
		pDuelAccuracy = ((int) Double.parseDouble(datos[campos.get("duels_won")]))/((int) Double.parseDouble(datos[campos.get("duels")]));
		pDribbleAccuracy = (int) Double.parseDouble(datos[campos.get("successful_dribbles")])/((int) Double.parseDouble(datos[campos.get("successful_dribbles")])+(int) Double.parseDouble(datos[campos.get("unsuccessful_dribbles")]));
		
		goals = ((int) Double.parseDouble(datos[campos.get("goals")])/minutes)*90;
		shots = ((int) Double.parseDouble(datos[campos.get("total_shots")])/minutes)*90;
		assists = ((int) Double.parseDouble(datos[campos.get("goal_assists")])/minutes)*90;
		passes = ((int) Double.parseDouble(datos[campos.get("total_passes")])/minutes)*90;
		succesfull_passes = ((int) Double.parseDouble(datos[campos.get("total_successful_passes_excl_crosses_corners")])/minutes)*90;
		duels = ((int) Double.parseDouble(datos[campos.get("duels")])/minutes)*90;
		duels_won = ((int) Double.parseDouble(datos[campos.get("duels_won")])/minutes)*90;
		fouls = ((int) Double.parseDouble(datos[campos.get("total_fouls_conceded")])/minutes)*90;
		red_cards_2nd_yellow = ((int) Double.parseDouble(datos[campos.get("red_cards_2nd_yellow")])/minutes)*90;
		yellow_cards = ((int) Double.parseDouble(datos[campos.get("yellow_cards")])/minutes)*90;
		total_red_cards = ((int) Double.parseDouble(datos[campos.get("total_red_cards")])/minutes)*90;
		straight_red_cards = ((int) Double.parseDouble(datos[campos.get("straight_red_cards")])/minutes)*90;
		successful_dribbles = ((int) Double.parseDouble(datos[campos.get("successful_dribbles")])/minutes)*90;
		unsuccessful_dribbles = ((int) Double.parseDouble(datos[campos.get("unsuccessful_dribbles")])/minutes)*90;

		
	}
}
