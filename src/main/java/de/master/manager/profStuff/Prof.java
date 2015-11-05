package de.master.manager.profStuff;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

import com.google.common.collect.Lists;

public class Prof implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private final String name, wahlModulPath;
	private final List<ICourse> selectedModuls;
	private final List<ICourse> selectedPflichtModuls;
	private final List<ICourse> pflichtCourses;
	
	public Prof(String name, List<ICourse> pflichtCourses) {
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

	public List<ICourse> getSelectedModuls() {
		return selectedModuls;
	}
	
	public List<ICourse> getSelectedPflichtModuls() {
		return selectedPflichtModuls;
	}
	
	public void addSelectedModul(ICourse modul){
		selectedModuls.add(modul);
	}
	
	public void addSelectedPflichtModul(ICourse modul){
		selectedPflichtModuls.add(modul);
	}
	
	public double calculateWahlPoints(){
		return selectedModuls.stream().mapToDouble(ICourse::getPoints).sum();
	}
	
	public double calculatePflichtPoints(){
		return selectedPflichtModuls.stream().mapToDouble(ICourse::getPoints).sum();
	}
	
	public OptionalDouble calculateFinalGrade(){
		List<ICourse> jointCourseLists = new ArrayList<>(selectedModuls.size() + selectedPflichtModuls.size());
		jointCourseLists.addAll(selectedPflichtModuls);
		jointCourseLists.addAll(selectedModuls);
		return jointCourseLists.stream().filter(m -> m.getGrade().isPresent()).mapToDouble(m -> m.getGrade().get()).average();
	}
	
	public OptionalDouble calculateFinalWahlGrade(){
		return selectedModuls.stream().filter(course -> course.getGrade().isPresent()).mapToDouble(course -> course.getGrade().get()).average();
	}
	
	public OptionalDouble calculateFinalPflichtGrade(){
		return selectedPflichtModuls.stream().filter(course -> course.getGrade().isPresent()).mapToDouble(course -> course.getGrade().get()).average();
	}
		
	@Override
	public String toString(){
		return name;
	}


	public List<ICourse> getPflichtCourse() {
		return pflichtCourses;
	}
}
