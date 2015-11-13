package de.master.manager.ui.panel;

import de.master.manager.profStuff.BasicModul;

public class BasicGradePanel extends SingleGradePanel {

	public BasicGradePanel(String id, String name, BasicModul modul) {
		super(id, name, modul.getCourses());
	}

}
