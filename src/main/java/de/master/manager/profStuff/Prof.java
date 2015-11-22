package de.master.manager.profStuff;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

public class Prof implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private final String name, wahlModulPath;
	private final IModul wahlModul;
	private final IModul pflichtModul;
	private final List<ICourse> pflichtCourses;
	private final String pflichtModulPath;	
	
	public Prof(String name, List<ICourse> pflichtCourses) {
		this.name = name;
		this.wahlModulPath = name + "_Wahl.txt";
		this.pflichtModulPath = name + "Pflicht.txt";
		this.wahlModul = new WahlModul(new ArrayList<>(10));
		this.pflichtModul = new PflichtModul(new ArrayList<>(10));
		this.pflichtCourses = pflichtCourses;
	}


	public String getName() {
		return name;
	}

	public String getPath() {
		return wahlModulPath;
	}

	public IModul getWahlModul(){
		return wahlModul;
	}
	
	public IModul getPflichtModul(){
		return pflichtModul;
	}
	
	public List<ICourse> getSelectedCourses() {
		return wahlModul;
	}
	
	public List<ICourse> getSelectedPflichtCourses() {
		return pflichtModul;
	}
	
	public void addSelectedModul(ICourse course){
		wahlModul.add(course);
	}
	
	public void addSelectedPflichtModul(ICourse course){
		pflichtModul.add(course);
	}
	
	public double calculatePflichtPointsToReach(){
		return pflichtCourses.stream().mapToDouble(ICourse::getPoints).sum();
	}
	
	public double calculateWahlPoints(){
		return wahlModul.calculatePoints();
	}
	
	public double calculatePflichtPoints(){
		return pflichtModul.calculatePoints();
	}
	
	public OptionalDouble calculateFinalGrade(){
		List<ICourse> jointCourseLists = new ArrayList<>(getSelectedCourses().size() + getSelectedPflichtCourses().size());
		jointCourseLists.addAll(getSelectedPflichtCourses());
		jointCourseLists.addAll(getSelectedCourses());

		return jointCourseLists.stream().filter(m -> m.getGrade().isPresent()).mapToDouble(m -> m.getGrade().get()).average();
	}
	
	public OptionalDouble calculateFinalWahlGrade(){
		return wahlModul.calculateGrade();
	}
	
	public OptionalDouble calculateFinalPflichtGrade(){
		return pflichtModul.calculateGrade();
	}
		
	@Override
	public String toString(){
		return name;
	}


	public String getPflichtModulPath() {
		return pflichtModulPath;
	}
	
}
