package com.professoren;

import java.util.List;

import com.google.common.collect.Lists;
import com.modul.Modul;

public enum Prof{

	BREUNIG("Breunig", getWahlModulPath("Breunig"), getPflichtModulPath("breunig")), 
	HINZ("Hinz", getWahlModulPath("Hinz"), getPflichtModulPath("hinz")), 
	HENNES("Hennes,", getWahlModulPath("Hennes"), getPflichtModulPath("hennes")), 
	HECK( "Heck", getWahlModulPath("Heck"), getPflichtModulPath("heck"));

	private final String name, wahlModulPath;
	private final List<Modul> selectedModuls;
	private final String pflichtModulPath;
	
	Prof(String name, String wahlModulPath, String pflichtModulPath) {
		this.name = name;
		this.wahlModulPath = wahlModulPath;
		this.pflichtModulPath = pflichtModulPath;
		selectedModuls = Lists.newArrayList();
	}

	private static String getPflichtModulPath(String prof) {
		return "src/main/resources/"+prof+"Pflicht.txt";
	}

	public String getName() {
		return name;
	}

	public String getPath() {
		return wahlModulPath;
	}

	private static String getWahlModulPath(String prof) {
		return "src/main/resources/" + prof + "_Wahl.txt";
	}

	public List<Modul> getSelectedModuls() {
		return selectedModuls;
	}
	
	public void addSelectedModul(Modul modul){
		selectedModuls.add(modul);
	}
	
	public double calculatePoints(){
		return selectedModuls.stream().mapToDouble(Modul::getPoints).sum();
	}

	public String getPflichtModulPath() {
		return pflichtModulPath;
	}
}
