package de.master.manager.profStuff;

import java.io.Serializable;
import java.util.List;

import com.google.common.base.Optional;

public interface IModul extends List<ICourse>, Serializable{

	Optional<Double> calculateGrade();
	
	double calculatePoints();
	
}
