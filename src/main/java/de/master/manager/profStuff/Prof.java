package de.master.manager.profStuff;

import java.io.Serializable;
import java.util.List;
import java.util.OptionalDouble;

import com.google.common.collect.Lists;

public class Prof implements Serializable{

	private final String name, wahlModulPath;
	private final List<ModulCourse> selectedModuls;
	private final List<ModulCourse> selectedPflichtModuls;
	private final List<ModulCourse> pflichtCourses;
	
	public Prof(String name, List<ModulCourse> pflichtCourses) {
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

	public List<ModulCourse> getSelectedModuls() {
		return selectedModuls;
	}
	
	public List<ModulCourse> getSelectedPflichtModuls() {
		return selectedPflichtModuls;
	}
	
	public void addSelectedModul(ModulCourse modul){
		selectedModuls.add(modul);
	}
	
	public void addSelectedPflichtModul(ModulCourse modul){
		selectedPflichtModuls.add(modul);
	}
	
	public double calculateWahlPoints(){
		return selectedModuls.stream().mapToDouble(AbstractCourse::getPoints).sum();
	}
	
	public double calculatePflichtPoints(){
		return selectedPflichtModuls.stream().mapToDouble(AbstractCourse::getPoints).sum();
	}
	
	public OptionalDouble calculateFinalWahlGrade(){
		return selectedModuls.stream().filter(course -> course.getNote().isPresent()).mapToDouble(course -> course.getNote().get()).average();
	}
	
	public OptionalDouble calculateFinalPflichtGrade(){
		return selectedPflichtModuls.stream().filter(course -> course.getNote().isPresent()).mapToDouble(course -> course.getNote().get()).average();
	}
	

	public void clearAll(){
		selectedModuls.clear();
		selectedPflichtModuls.clear();
	}
	
	@Override
	public String toString(){
		return name;
	}


	public List<ModulCourse> getPflichtCourse() {
		return pflichtCourses;
	}
}
