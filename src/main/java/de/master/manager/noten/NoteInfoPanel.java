package de.master.manager.noten;

import java.util.List;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import de.master.manager.model.TransformationModel;
import de.master.manager.profStuff.ModulCourse;
import de.master.manager.profStuff.Prof;

public class NoteInfoPanel extends Panel{

	public NoteInfoPanel(String id, IModel<List<ModulCourse>> loadSelectedPflichtCourses, TransformationModel<Prof,List<ModulCourse>> loadSelectedWahlCourses) {
		super(id);
	
	}

}
