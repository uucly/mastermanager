package de.master.manager.ui.panel;

import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import de.master.manager.profStuff.IModul;
import de.master.manager.profStuff.Prof;
import de.master.manager.ui.events.GradeChangedEvent;
import de.master.manager.ui.events.ProfChangedEvent;

public class GradePanel extends Panel{

	private static final long serialVersionUID = 1L;
	private final GradeInfoPanel noteInfoPanel;

	public GradePanel(String id, IModel<Prof> profOfPanel1, IModel<Prof> profOfPanel2, IModul basicModul, IModul supplementModul) {
		super(id);
		GradeProfPanel noteProfPanel1 = new GradeProfPanel("noteProfPanel1", profOfPanel1);
		GradeProfPanel noteProfPanel2 = new GradeProfPanel("noteProfPanel2", profOfPanel2);
		SingleGradePanel basicGradePanel = new BasicGradePanel("gradeBasicPanel", "Aufbau", basicModul);
		SingleGradePanel supplementGradePanel = new SupplementGradePanel("supplementGrade", "Ergänzung", supplementModul);
		
		noteInfoPanel = new GradeInfoPanel("noteInfoPanel", profOfPanel1, profOfPanel2, basicModul, supplementModul);
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
