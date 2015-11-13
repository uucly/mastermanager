package de.master.manager.ui.panel;

import de.master.manager.profStuff.SupplementModul;

public class SupplementGradePanel extends SingleGradePanel{

	public SupplementGradePanel(String id, String name, SupplementModul modul) {
		super(id, name, modul.getCourses());
	}

}
