package de.master.manager.noten;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import de.master.manager.profStuff.Prof;

public class NotePanel extends Panel{

	private static final long serialVersionUID = 1L;

	public NotePanel(String id, IModel<Prof> profOfPanel1, IModel<Prof> profOfPanel2) {
		super(id);
		NoteProfPanel noteProfPanel1 = new NoteProfPanel("noteProfPanel1", profOfPanel1);
		
		add(noteProfPanel1);
	}

}
