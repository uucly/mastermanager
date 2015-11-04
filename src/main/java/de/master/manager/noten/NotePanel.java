package de.master.manager.noten;

import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import de.master.manager.events.GradeChangedEvent;
import de.master.manager.events.ProfChangedEvent;
import de.master.manager.profStuff.Prof;

public class NotePanel extends Panel{

	private static final long serialVersionUID = 1L;
	private final NoteInfoPanel noteInfoPanel;

	public NotePanel(String id, IModel<Prof> profOfPanel1, IModel<Prof> profOfPanel2) {
		super(id);
		NoteProfPanel noteProfPanel1 = new NoteProfPanel("noteProfPanel1", profOfPanel1);
		NoteProfPanel noteProfPanel2 = new NoteProfPanel("noteProfPanel2", profOfPanel2);
		noteInfoPanel = new NoteInfoPanel("noteInfoPanel", profOfPanel1, profOfPanel2);
		add(noteProfPanel1, noteProfPanel2);
		add(noteInfoPanel);
	}

	@Override
	public void onEvent(IEvent<?> event) {
		Object payload = event.getPayload();
		if(payload instanceof ProfChangedEvent){
			((ProfChangedEvent) payload).getTarget().add(this);
		}
		
		if(payload instanceof GradeChangedEvent){
			((GradeChangedEvent) payload).getTarget().add(noteInfoPanel);
		}
	}
}
