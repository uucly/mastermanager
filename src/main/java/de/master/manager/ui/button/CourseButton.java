package de.master.manager.ui.button;

import java.util.List;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.master.manager.profStuff.ICourse;
import de.master.manager.profStuff.ICourseLoader;
import de.master.manager.profStuff.IModul;
import de.master.manager.profStuff.Prof;
import de.master.manager.ui.events.SelectedEvent;

public class CourseButton extends AbstractCourseButton {

	private static final long serialVersionUID = 1L;
	
	@SpringBean
	private ICourseLoader courseLoader;
	
	private final List<Prof> allProfs;
	private final IModel<Prof> profRight;
	private final IModel<Prof> prof;

	public CourseButton(String id, ICourse course, IModul modul, IModel<Prof> prof, IModel<Prof> profRight, List<Prof> allProfs) {
		super(id, course, modul);
		this.profRight = profRight;
		this.prof = prof;
		this.allProfs = allProfs;
	}

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		List<ICourse> pflichtCourses = courseLoader.loadCourses(profRight.getObject().getPflichtModulPath());
		if(pflichtCourses.contains(course)){
			prof.getObject().getSelectedCourses().remove(course);
			setEnabled(false);
		} else if(isAlreadySelected()){
			setSelected();
		} else if(isAlreadySelectedInOtherProf(prof.getObject(), allProfs, Prof::getWahlModul)) {
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
