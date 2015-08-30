package com.professoren;

import java.util.List;

import com.google.common.collect.Lists;
import com.modul.Course;

public enum Prof{

	BREUNIG("Breunig", getWahlModulPath("Breunig"), getPflichtModulPath("breunig")), 
	HINZ("Hinz", getWahlModulPath("Hinz"), getPflichtModulPath("hinz")), 
	HENNES("Hennes,", getWahlModulPath("Hennes"), getPflichtModulPath("hennes")), 
	HECK("Heck", getWahlModulPath("Heck"), getPflichtModulPath("heck"));

	private final String name, wahlModulPath;
	private final List<Course> selectedModuls;
	private final String pflichtModulPath;
	private final List<Course> selectedPflichtModuls;
	
	Prof(String name, String wahlModulPath, String pflichtModulPath) {
		this.name = name;
		this.wahlModulPath = wahlModulPath;
		this.pflichtModulPath = pflichtModulPath;
		selectedModuls = Lists.newArrayList();
		selectedPflichtModuls = Lists.newArrayList();
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

	public List<Course> getSelectedModuls() {
		return selectedModuls;
	}
	
	public List<Course> getSelectedPflichtModuls() {
		return selectedPflichtModuls;
	}
	
	public void addSelectedModul(Course modul){
		selectedModuls.add(modul);
	}
	
	public void addSelectedPflichtModul(Course modul){
		selectedPflichtModuls.add(modul);
	}
	
	public double calculateWahlPoints(){
		return selectedModuls.stream().mapToDouble(Course::getPoints).sum();
	}
	
	public double calculatePflichtPoints(){
		return selectedPflichtModuls.stream().mapToDouble(Course::getPoints).sum();
	}

	public String getPflichtModulPath() {
		return pflichtModulPath;
	}
	
	public void clearAll(){
		selectedModuls.clear();
		selectedPflichtModuls.clear();
	}
}
