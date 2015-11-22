package de.master.manager.profStuff;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

public class Prof implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private final String name, wahlModulPath;
	private final IModul wahlModulSelected;
	private final IModul pflichtModulSelected;
	private final IModul pflichtModul;
	
	public Prof(String name, List<ICourse> pflichtCourses) {
		this.name = name;
		this.wahlModulPath = name + "_Wahl.txt";
		this.wahlModulSelected = new Modul(new ArrayList<>(10));
		this.pflichtModulSelected = new Modul(new ArrayList<>(10));
		this.pflichtModul = new Modul(pflichtCourses);
	}


	public String getName() {
		return name;
	}

	public String getPath() {
		return wahlModulPath;
	}

	public IModul getWahlModulSelected(){
		return wahlModulSelected;
	}
	
	public IModul getPflichtModulSelected(){
		return pflichtModulSelected;
	}
	
	public void addSelectedModul(ICourse course){
		wahlModulSelected.add(course);
	}
	
	public void addSelectedPflichtModul(ICourse course){
		pflichtModulSelected.add(course);
	}
	
	public double calculatePflichtPointsToReach(){
		return pflichtModul.calculatePoints();
	}
	
	public double calculateWahlPoints(){
		return wahlModulSelected.calculatePoints();
	}
	
	public double calculatePflichtPoints(){
		return pflichtModulSelected.calculatePoints();
	}
	
	public OptionalDouble calculateFinalGrade(){
		IModul jointModuls = new Modul(new ArrayList<>(wahlModulSelected.size() + pflichtModulSelected.size()));
		jointModuls.addAll(pflichtModulSelected);
		jointModuls.addAll(wahlModulSelected);
		return jointModuls.calculateGrade();
	}
	
	public OptionalDouble calculateFinalWahlGrade(){
		return wahlModulSelected.calculateGrade();
	}
	
	public OptionalDouble calculateFinalPflichtGrade(){
		return pflichtModulSelected.calculateGrade();
	}
		
	@Override
	public String toString(){
		return name;
	}

	public IModul getPflichtModul() {
		return pflichtModul;
	}
	
}
