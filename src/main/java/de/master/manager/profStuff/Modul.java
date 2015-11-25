package de.master.manager.profStuff;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Optional;

public class Modul extends AbstractList<ICourse> implements IModul{

	private static final long serialVersionUID = 1L;

	public Modul(){
		super(new ArrayList<ICourse>());
	}
	
	public Modul(List<ICourse> courses) {
		super(courses);
	}
	
	public static IModul createInstance(IModul... moduls){
		List<ICourse> courses = new ArrayList<>(moduls.length);
		for(IModul modul : moduls){
			for(ICourse c : modul){
				courses.add(c);
			}
		}
		return new Modul(courses);
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
		return sumPoints;
	}

}
