package de.master.manager.profStuff;

import java.io.Serializable;
import java.util.List;
import java.util.OptionalDouble;

public interface IModul extends Serializable{

	void addCourse(ICourse course);
	
	void removeCourse(ICourse course);
	
	OptionalDouble calculateGrade();
	
	double calculatePoints();
	
	List<ICourse> getCourses();
	
	boolean contains(ICourse course);
}
