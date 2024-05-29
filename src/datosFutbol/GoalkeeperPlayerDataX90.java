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
		height = Double.parseDouble(datos[campos.get("altura")]);
		weight = Double.parseDouble(datos[campos.get("peso")]);
		position = datos[campos.get("posicion")];
		
		games_played = (int) Double.parseDouble(datos[campos.get("appearances")]);
		clean_sheets = (int) Double.parseDouble(datos[campos.get("clean_sheets")]);
		goals_conceded = (int) Double.parseDouble(datos[campos.get("goals_conceded")]);
		catches = (int) Double.parseDouble(datos[campos.get("saves_made")]);
		pCatchAccuracy = catches/(goals_conceded+catches);
		pClean_sheets_percentage = clean_sheets/Double.parseDouble(datos[campos.get("appearances")]);
		pSaves_made_from_outside_box_percentage = Double.parseDouble(datos[campos.get("saves_made_from_outside_box")])/(Double.parseDouble(datos[campos.get("saves_made_from_inside_box")]) + Double.parseDouble(datos[campos.get("saves_made_from_outside_box")]));
		pSaves_made_from_inside_box_percentage = Double.parseDouble(datos[campos.get("saves_made_from_inside_box")])/(Double.parseDouble(datos[campos.get("saves_made_from_inside_box")]) + Double.parseDouble(datos[campos.get("saves_made_from_outside_box")]));
		penalties_faced = (int) Double.parseDouble(datos[campos.get("penalties_faced")]);
		penalties_saved = (int) Double.parseDouble(datos[campos.get("penalties_saved")]);
		if(penalties_faced != 0)
			pPenaltyAccuracy = penalties_saved/penalties_faced;
		else
			pPenaltyAccuracy=0;
		
		
		
	}

}
