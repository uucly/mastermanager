package de.master.manager.ui.button;

import java.util.List;
import java.util.function.Consumer;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import de.master.manager.profStuff.ICourse;
import de.master.manager.profStuff.Prof;
import de.master.manager.ui.events.SelectedEvent;

public class CourseButton extends AbstractCourseButton {

	private static final long serialVersionUID = 1L;
	private final List<Prof> allProfs;
	private IModel<Prof> profRight;

	public CourseButton(String id, ICourse course, IModel<Prof> prof, IModel<Prof> profRight, List<Prof> allProfs) {
		super(id, course, prof);
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
			prof.getObject().getSelectedModuls().remove(course);
			setEnabled(false);
		} else if(isAlreadySelected(Prof::getSelectedModuls)){
			setSelected();
		} else if(isAlreadySelectedInOtherProf(prof.getObject(), course, allProfs)) {
			setEnabled(false);
		}
	}
	
	@Override
	public void onEvent(IEvent<?> event) {
		if(event.getPayload() instanceof SelectedEvent){
			SelectedEvent selectedEvent = ((SelectedEvent) event.getPayload());
			if(selectedEvent.getCourse().equals(course)){
				setEnabled(false);
				selectedEvent.getTarget().add(this);
			}
		}
	}
	
	private static boolean isAlreadySelectedInOtherProf(Prof prof, ICourse modul, List<Prof> allProfs){
		return allProfs.stream().filter(p -> p!=prof).anyMatch(p-> p.getSelectedModuls().contains(modul));
	}
	
}
