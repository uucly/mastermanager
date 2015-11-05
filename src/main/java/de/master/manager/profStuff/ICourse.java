package de.master.manager.profStuff;

import java.io.Serializable;

import com.google.common.base.Optional;

public interface ICourse extends Serializable {

	Optional<Double> getGrade();

	void setGrade(Double note);

	double getPoints();

	String getName();

}
