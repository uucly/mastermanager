package com.professoren;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;
import com.modul.Course;

public class Prof implements Serializable{

	private final String name, wahlModulPath, pflichtModulPath;
	private final List<Course> selectedModuls;
	private final List<Course> selectedPflichtModuls;
	
	public Prof(String name) {
		this.name = name;
		this.wahlModulPath = getCoursePath(name + "_Wahl.txt");
		this.pflichtModulPath = getCoursePath(name+"Pflicht.txt");
		selectedModuls = Lists.newArrayList();
		selectedPflichtModuls = Lists.newArrayList();
	}


	public String getName() {
		return name;
	}

	public String getPath() {
		return wahlModulPath;
	}

	private static String getCoursePath(String fileName) {
		return "src/main/resources/" + fileName;
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
	
	@Override
	public String toString(){
		return name;
	}
}
