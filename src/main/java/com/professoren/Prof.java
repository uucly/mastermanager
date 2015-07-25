package com.professoren;

import java.util.List;

import com.google.common.collect.Lists;
import com.modul.Modul;

public enum Prof {

	BREUNIG("Breunig", getModulPath("Breunig")), 
	HINZ("Hinz", getModulPath("Hinz")), 
	HENNES("Hennes,", getModulPath("Hennes")), 
	HECK( "Heck", getModulPath("Heck"));

	private final String name, path;
	private final List<Modul> selectedModuls;
	private List<Modul> allModuls;
	
	Prof(String name, String path) {
		this.name = name;
		this.path = path;
		selectedModuls = Lists.newArrayList();
		allModuls = Lists.newArrayList();
	}

	public String getName() {
		return name;
	}

	public String getPath() {
		return path;
	}

	private static String getModulPath(String prof) {
		return "src/main/resources/" + prof + "_Wahl.txt";
	}

	public List<Modul> getSelectedModuls() {
		return selectedModuls;
	}
	
	public void addSelectedModul(Modul modul){
		selectedModuls.add(modul);
	}
	
	public void setAllModuls(List<Modul> allModuls){
		this.allModuls = allModuls;
	}
	
	public List<Modul> getAllModuls(){
		return allModuls;
	}
	
	public double calculatePoints(){
		return selectedModuls.stream().mapToDouble(Modul::getPoints).sum();
	}
}
