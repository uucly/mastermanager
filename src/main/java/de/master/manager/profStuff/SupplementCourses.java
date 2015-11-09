package de.master.manager.profStuff;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SupplementCourses implements Serializable{

	private final List<ICourse> courses;
	
	public SupplementCourses(){
		courses = new ArrayList<>(10);
	}
	
	public void addCourse(ICourse course){
		courses.add(course);
	}
	
	public List<ICourse> getAllCourses(){
		return courses;
	}
	
	public double calculatePoints(){
		return courses.stream().mapToDouble(ICourse::getPoints).sum();
	}
	
}
