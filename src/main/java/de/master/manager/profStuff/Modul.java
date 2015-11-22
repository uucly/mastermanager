package de.master.manager.profStuff;

import java.util.List;
import java.util.OptionalDouble;

public class Modul extends AbstractList<ICourse> implements IModul{

	private static final long serialVersionUID = 1L;

	public Modul(List<ICourse> courses) {
		super(courses);
	}
	
	@Override
	public OptionalDouble calculateGrade() {
		double sumWeightedGrades = l.stream().filter(course -> course.getGrade().isPresent()).mapToDouble(course -> course.getGrade().get()*course.getPoints()).sum();
		double sumPoints = l.stream().filter(c -> c.getGrade().isPresent()).mapToDouble(c -> c.getPoints()).sum();
		double grade = sumWeightedGrades/sumPoints;
		return grade == 0 || Double.isNaN(grade)? OptionalDouble.empty() : OptionalDouble.of(grade);
	}

	@Override
	public double calculatePoints() {
		return l.stream().mapToDouble(ICourse::getPoints).sum();
	}

}
