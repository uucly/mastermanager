package de.master.manager.ui.button;

import java.util.List;

import org.apache.wicket.event.IEvent;
import org.apache.wicket.model.IModel;
import de.master.manager.profStuff.ICourse;
import de.master.manager.profStuff.Prof;
import de.master.manager.ui.events.SelectedEvent;

public class CoursePflichtButton extends AbstractCourseButton {

	private static final long serialVersionUID = 1L;
	private List<Prof> allProfs;

	public CoursePflichtButton(String id, ICourse course, IModel<Prof> prof, List<Prof> allProfs) {
		super(id, course, prof.getObject());
		this.allProfs = allProfs;
	}

	@Override
	protected void addModulFunction(Prof prof, ICourse course) {
		prof.addSelectedPflichtModul(course);
		
	}

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		if(isAlreadySelected(Prof::getSelectedPflichtModuls)){
			setSelected();
		} else if(isAlreadySelectedInOtherProf(Prof::getSelectedPflichtModuls, allProfs)) {
			setEnabled(false);
		}
	}

	@Override
	public void onEvent(IEvent<?> event) {
		Object payload = event.getPayload();
		if(payload instanceof SelectedEvent){
			if(isAlreadySelectedInOtherProf(Prof::getSelectedPflichtModuls, allProfs)){
				setEnabled(false);
				((SelectedEvent) payload).getTarget().add(this);
			}
		}
	}
	
}