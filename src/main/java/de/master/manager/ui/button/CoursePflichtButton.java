package de.master.manager.ui.button;

import java.util.List;

import org.apache.wicket.event.IEvent;
import org.apache.wicket.model.IModel;

import de.master.manager.profStuff.ICourse;
import de.master.manager.profStuff.IModul;
import de.master.manager.profStuff.Prof;
import de.master.manager.ui.events.SelectedEvent;

public class CoursePflichtButton extends AbstractCourseButton {

	private static final long serialVersionUID = 1L;
	private final List<Prof> allProfs;
	private final IModel<Prof> prof;

	public CoursePflichtButton(String id, ICourse course, IModul modul, IModel<Prof> prof, List<Prof> allProfs) {
		super(id, course, modul);
		this.allProfs = allProfs;
		this.prof = prof;
	}

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		if(isAlreadySelected()){
			setSelected();
		} else if(isAlreadySelectedInOtherProf(prof.getObject(), allProfs)) {
			setEnabled(false);
		}
	}

	@Override
	public void onEvent(IEvent<?> event) {
		Object payload = event.getPayload();
		if(payload instanceof SelectedEvent){
			if(isAlreadySelectedInOtherProf(prof.getObject(), allProfs)){
				setEnabled(false);
				((SelectedEvent) payload).getTarget().add(this);
			}
		}
	}
	
}