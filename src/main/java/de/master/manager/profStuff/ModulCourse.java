package de.master.manager.profStuff;

import java.util.Optional;

public class ModulCourse extends AbstractCourse{

	private static final long serialVersionUID = 1L;
	private Optional<Double> note;
	
	public ModulCourse(String name, double points) {
		super(name, points);
		note = Optional.empty();
	}

	public Optional<Double> getNote() {
		return note;
	}

	public void setNote(double note) {
		this.note = Optional.of(note);
	}
	
	

}
