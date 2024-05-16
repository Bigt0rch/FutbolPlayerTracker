package datosFutbol;

import java.util.Map;

public class GoalkeeperPlayerDataX90 extends PlayerDataX90{
	public int games_played;
	public int clean_sheets;
	public double pClean_sheets_percentage;
	public int goals_conceded;
	public int catches;
	public double pSaves_made_from_outside_box_percentage;
	public double pSaves_made_from_inside_box_percentage;
	public double pCatchAccuracy;
	public int penalties_faced;
	public int penalties_saved;
	public double pPenaltyAccuracy;
	
	public GoalkeeperPlayerDataX90(String datos[], Map<String,Integer> campos) {
		position = datos[campos.get("posicion")];
		name = datos[campos.get("nombre")];
		team = datos[campos.get("equipo")];
		nationality = datos[campos.get("pais")];
		height = Integer.parseInt(datos[campos.get("altura")]);
		weight = Integer.parseInt(datos[campos.get("peso")]);
		position = datos[campos.get("posicion")];
		
		games_played = Integer.parseInt(datos[campos.get("appearances")]);
		clean_sheets = Integer.parseInt(datos[campos.get("clean_sheets")]);
		goals_conceded = Integer.parseInt(datos[campos.get("goals_conceded")]);
		catches = Integer.parseInt(datos[campos.get("saves_made")]);
		pCatchAccuracy = catches/(goals_conceded+catches);
		pClean_sheets_percentage = clean_sheets/Integer.parseInt(datos[campos.get("appearances")]);
		pSaves_made_from_outside_box_percentage = Integer.parseInt(datos[campos.get("saves_made_from_outside_box")])/(Integer.parseInt(datos[campos.get("saves_made_from_inside_box")]) + Integer.parseInt(datos[campos.get("saves_made_from_outside_box")]));
		pSaves_made_from_inside_box_percentage = Integer.parseInt(datos[campos.get("saves_made_from_inside_box")])/(Integer.parseInt(datos[campos.get("saves_made_from_inside_box")]) + Integer.parseInt(datos[campos.get("saves_made_from_outside_box")]));
		penalties_faced = Integer.parseInt(datos[campos.get("penalties_faced")]);
		penalties_saved = Integer.parseInt(datos[campos.get("penalties_saved")]);
		pPenaltyAccuracy = penalties_saved/penalties_faced;
		
		
		
	}

}
