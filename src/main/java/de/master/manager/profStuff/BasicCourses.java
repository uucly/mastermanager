package de.master.manager.profStuff;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class BasicCourses implements Serializable{
	
	private final List<ICourse> courses;

	public BasicCourses() {
		this.courses = new ArrayList<>(10);
	}

	public List<ICourse> getCourses() {
		return courses;
	}
	
	public void addCourse(ICourse course){
		courses.add(course);
	}
	
	public double calculatePoints(){
		return courses.stream().mapToDouble(ICourse::getPoints).sum();
	}

}
