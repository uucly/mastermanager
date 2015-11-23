package de.master.manager.profStuff;

import java.util.List;

import com.google.common.base.Optional;

public class Modul extends AbstractList<ICourse> implements IModul{

	private static final long serialVersionUID = 1L;

	public Modul(List<ICourse> courses) {
		super(courses);
	}
	
	@Override
	public Optional<Double> calculateGrade() {
		double sumWeightedGrades = 0;
		double sumPoints = 0;
		for(ICourse c : l){
			if(c.getGrade().isPresent()){
				sumWeightedGrades += c.getGrade().get()*c.getPoints();
				sumPoints += c.getPoints();
			}
		}
		double grade = sumWeightedGrades/sumPoints;
		return grade == 0 || Double.isNaN(grade)? Optional.<Double>absent() : Optional.of(grade);
	}

	@Override
	public double calculatePoints() {
		double sumPoints = 0;
		for(ICourse c : l){
			sumPoints += c.getPoints();
		}
		//return l.stream().mapToDouble(ICourse::getPoints).sum();
		return sumPoints;
	}

}
