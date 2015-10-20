package de.master.manager.profStuff;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;

public class Prof implements Serializable{

	private final String name, wahlModulPath;
	private final List<Course> selectedModuls;
	private final List<Course> selectedPflichtModuls;
	private final List<Course> pflichtCourses;
	
	public Prof(String name, List<Course> pflichtCourses) {
		this.name = name;
		this.wahlModulPath = name + "_Wahl.txt";
		selectedModuls = Lists.newArrayList();
		selectedPflichtModuls = Lists.newArrayList();
		this.pflichtCourses = pflichtCourses;
	}


	public String getName() {
		return name;
	}

	public String getPath() {
		return wahlModulPath;
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

	public void clearAll(){
		selectedModuls.clear();
		selectedPflichtModuls.clear();
	}
	
	@Override
	public String toString(){
		return name;
	}


	public List<Course> getPflichtCourse() {
		return pflichtCourses;
	}
}
