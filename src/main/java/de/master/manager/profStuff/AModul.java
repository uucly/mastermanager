package de.master.manager.profStuff;

import java.util.List;
import java.util.OptionalDouble;

public abstract class AModul implements IModul{

	private static final long serialVersionUID = 1L;
	
	private final List<ICourse> courses;
	
	public AModul(List<ICourse> courses) {
		this.courses = courses;
	}

	@Override
	public OptionalDouble calculateGrade() {
		double sumWeightedGrades = courses.stream().filter(course -> course.getGrade().isPresent()).mapToDouble(course -> course.getGrade().get()*course.getPoints()).sum();
		double sumPoints = courses.stream().filter(c -> c.getGrade().isPresent()).mapToDouble(c -> c.getPoints()).sum();
		double grade = sumWeightedGrades/sumPoints;
		return grade == 0 || Double.isNaN(grade)? OptionalDouble.empty() : OptionalDouble.of(grade);
	}

	@Override
	public double calculatePoints() {
		return courses.stream().mapToDouble(ICourse::getPoints).sum();
	}

	@Override
	public List<ICourse> getCourses() {
		return courses;
	}

	@Override
	public void addCourse(ICourse course) {
		courses.add(course);
	}

	@Override
	public void removeCourse(ICourse course) {
		courses.remove(course);
	}
	
	@Override
	public boolean contains(ICourse course) {
		return courses.contains(course);
	}
}
