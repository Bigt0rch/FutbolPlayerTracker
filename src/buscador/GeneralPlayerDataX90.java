package buscador;

import java.util.Map;

import java.io.Serializable;

public class GeneralPlayerDataX90 implements Serializable{



	String name;
	String team;
	String nationality;
	String position;
	int height;
	int weight;
	double goals;
	double shots;
	double pShotAccuracy;
	double assists;
	double minutes;
	double passes;
	double succesfull_passes;
	double pPassAccuracy;
	double duels;
	double duels_won;
	double pDuelAccuracy;
	double red_cards_2nd_yellow;
	double yellow_cards;
	double total_red_cards; 
	double straight_red_cards;
	double successful_dribbles;
	double unsuccessful_dribbles;
	double pDribbleAccuracy;

	public GeneralPlayerDataX90(String datos[], Map<String,Integer> campos) {
		minutes = Integer.parseInt(datos[campos.get("minutes")]);
		name = datos[campos.get("nombre")];
		team = datos[campos.get("equipo")];
		nationality = datos[campos.get("pais")];
		height = Integer.parseInt(datos[campos.get("altura")]);
		weight = Integer.parseInt(datos[campos.get("peso")]);
		position = datos[campos.get("posicion")];
		goals = (Integer.parseInt(datos[campos.get("goals")])/minutes)*90;
		shots = (Integer.parseInt(datos[campos.get("shots")])/minutes)*90;
		assists = (Integer.parseInt(datos[campos.get("assists")])/minutes)*90;
		passes = (Integer.parseInt(datos[campos.get("passes")])/minutes)*90;
		succesfull_passes = (Integer.parseInt(datos[campos.get("succesfull_passes")])/minutes)*90;
		duels = (Integer.parseInt(datos[campos.get("duels")])/minutes)*90;
		duels_won = (Integer.parseInt(datos[campos.get("duels_won")])/minutes)*90;
		red_cards_2nd_yellow = (Integer.parseInt(datos[campos.get("red_cards_2nd_yellow")])/minutes)*90;
		yellow_cards = (Integer.parseInt(datos[campos.get("yellow_cards")])/minutes)*90;
		total_red_cards = (Integer.parseInt(datos[campos.get("total_red_cards")])/minutes)*90;
		straight_red_cards = (Integer.parseInt(datos[campos.get("straight_red_cards")])/minutes)*90;
		successful_dribbles = (Integer.parseInt(datos[campos.get("successful_dribbles")])/minutes)*90;
		unsuccessful_dribbles = (Integer.parseInt(datos[campos.get("unsuccessful_dribbles")])/minutes)*90;

		pShotAccuracy = goals/shots;
		pPassAccuracy = succesfull_passes/passes;
		pDuelAccuracy = duels_won/duels;
		pDribbleAccuracy = successful_dribbles/(successful_dribbles+unsuccessful_dribbles);
	}
}
