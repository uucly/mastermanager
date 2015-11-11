package de.master.manager.ui.panel;

import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import de.master.manager.profStuff.Prof;
import de.master.manager.ui.events.GradeChangedEvent;
import de.master.manager.ui.events.ProfChangedEvent;

public class GradePanel extends Panel{

	private static final long serialVersionUID = 1L;
	private final NoteInfoPanel noteInfoPanel;

	public GradePanel(String id, IModel<Prof> profOfPanel1, IModel<Prof> profOfPanel2) {
		super(id);
		GradeProfPanel noteProfPanel1 = new GradeProfPanel("noteProfPanel1", profOfPanel1);
		GradeProfPanel noteProfPanel2 = new GradeProfPanel("noteProfPanel2", profOfPanel2);
		SingleGradePanel basicGradePanel = new SingleGradePanel("gradeBasicPanel", profOfPanel1.getObject().getBasicCourses().getCourses());
		SingleGradePanel supplementGradePanel = new SingleGradePanel("supplementGrade", profOfPanel1.getObject().getSupplementCourses().getAllCourses());
		
		noteInfoPanel = new NoteInfoPanel("noteInfoPanel", profOfPanel1, profOfPanel2);
		add(noteProfPanel1, noteProfPanel2);
		add(basicGradePanel);
		add(supplementGradePanel);
		add(noteInfoPanel);
	}

	@Override
	public void onEvent(IEvent<?> event) {
		Object payload = event.getPayload();
		if(payload instanceof ProfChangedEvent){
			((ProfChangedEvent) payload).getTarget().add(this);
		}
		
		if(payload instanceof GradeChangedEvent){
			((GradeChangedEvent) payload).getTarget().add(this);
		}
	}
}
