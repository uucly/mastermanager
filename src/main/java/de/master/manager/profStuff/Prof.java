package de.master.manager.profStuff;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

public class Prof implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private final String name, wahlModulPath;
	private final List<ICourse> selectedWahlCourses;
	private final List<ICourse> selectedPflichtCourses;
	private final List<ICourse> pflichtCourses;
	
	
	public Prof(String name, List<ICourse> pflichtCourses) {
		this.name = name;
		this.wahlModulPath = name + "_Wahl.txt";
		this.selectedWahlCourses = new ArrayList<>(10);
		this.selectedPflichtCourses = new ArrayList<>(10);
		this.pflichtCourses = pflichtCourses;
	}


	public String getName() {
		return name;
	}

	public String getPath() {
		return wahlModulPath;
	}

	public List<ICourse> getSelectedModuls() {
		return selectedWahlCourses;
	}
	
	public List<ICourse> getSelectedPflichtModuls() {
		return selectedPflichtCourses;
	}
	
	public void addSelectedModul(ICourse course){
		selectedWahlCourses.add(course);
	}
	
	public void addSelectedPflichtModul(ICourse course){
		selectedPflichtCourses.add(course);
	}
	
	
	public double calculateWahlPoints(){
		return calculatePoints(selectedWahlCourses);
	}
	
	public double calculatePflichtPoints(){
		return calculatePoints(selectedPflichtCourses);
	}
	
	public OptionalDouble calculateFinalGrade(){
		List<ICourse> jointCourseLists = new ArrayList<>(selectedWahlCourses.size() + selectedPflichtCourses.size());
		jointCourseLists.addAll(selectedPflichtCourses);
		jointCourseLists.addAll(selectedWahlCourses);
		return jointCourseLists.stream().filter(m -> m.getGrade().isPresent()).mapToDouble(m -> m.getGrade().get()).average();
	}
	
	public OptionalDouble calculateFinalWahlGrade(){
		return selectedWahlCourses.stream().filter(course -> course.getGrade().isPresent()).mapToDouble(course -> course.getGrade().get()).average();
	}
	
	public OptionalDouble calculateFinalPflichtGrade(){
		return selectedPflichtCourses.stream().filter(course -> course.getGrade().isPresent()).mapToDouble(course -> course.getGrade().get()).average();
	}
		
	@Override
	public String toString(){
		return name;
	}


	public List<ICourse> getPflichtCourse() {
		return pflichtCourses;
	}
	
	private final static double calculatePoints(List<ICourse> courses){
		return courses.stream().mapToDouble(ICourse::getPoints).sum();
	}
}
