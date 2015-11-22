package de.master.manager.profStuff;

import java.util.Comparator;
import java.util.List;
import java.util.OptionalDouble;

public abstract class AModul extends AbstractList<ICourse> implements IModul, Comparator<String>{

	private static final long serialVersionUID = 1L;
		
	public AModul(List<ICourse> courses) {
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

	@Override
	public int compare(String o1, String o2) {
		return o1.compareTo(o2);
	}

}
