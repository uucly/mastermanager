package de.master.manager.profStuff;

import java.io.Serializable;
import java.util.List;
import java.util.OptionalDouble;

public interface IModul extends List<ICourse>, Serializable{

	OptionalDouble calculateGrade();
	
	double calculatePoints();
	
}
