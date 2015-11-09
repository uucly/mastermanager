package de.master.manager.ui.button;

import java.util.List;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.model.IModel;
import de.master.manager.profStuff.ICourse;
import de.master.manager.profStuff.Prof;
import de.master.manager.ui.events.SelectedEvent;

public class CourseButton extends AbstractCourseButton {

	private static final long serialVersionUID = 1L;
	private final List<Prof> allProfs;
	private IModel<Prof> profRight;

	public CourseButton(String id, ICourse course, IModel<Prof> prof, IModel<Prof> profRight, List<Prof> allProfs) {
		super(id, course, prof.getObject());
		this.profRight = profRight;
		this.allProfs = allProfs;
	}

	@Override
	protected void addModulFunction(Prof prof, ICourse course) {
		prof.addSelectedModul(course);
	}

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		if(profRight.getObject().getPflichtCourse().contains(course)){
			prof.getSelectedModuls().remove(course);
			setEnabled(false);
		} else if(isAlreadySelected(Prof::getSelectedModuls)){
			setSelected();
		} else if(isAlreadySelectedInOtherProf(Prof::getSelectedModuls, allProfs)) {
			setEnabled(false);
		}
	}
	
	@Override
	public void onEvent(IEvent<?> event) {
		Object payload = event.getPayload();
		if(payload instanceof SelectedEvent){
			SelectedEvent selectedEvent = ((SelectedEvent) payload);
			if(selectedEvent.getCourse().equals(course)){
				setEnabled(false);
				selectedEvent.getTarget().add(this);
			}
		}
	}
	
}
