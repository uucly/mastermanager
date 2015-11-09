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
		super(id, course, prof);
		this.allProfs = allProfs;
	}

	@Override
	protected void addModulFunction(Prof prof, ICourse course) {
		prof.addSelectedPflichtModul(course);
		
	}

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		if(isAlreadySelected(prof.getObject(), course)){
			setSelected();
		} else if(isAlreadySelectedInOtherProf(prof.getObject(), course, allProfs)) {
			setEnabled(false);
		}
	}

	@Override
	public void onEvent(IEvent<?> event) {
		if(event.getPayload() instanceof SelectedEvent){
			if(isAlreadySelectedInOtherProf(prof.getObject(), course, allProfs)){
				setEnabled(false);
				((SelectedEvent) event.getPayload()).getTarget().add(this);
			}
		}
	}
	
	private static boolean isAlreadySelected(Prof prof, ICourse course){
		return prof.getSelectedPflichtModuls().contains(course);
	}
	
	private static boolean isAlreadySelectedInOtherProf(Prof prof, ICourse modul, List<Prof> allProfs){
		return allProfs.stream().filter(p -> p!=prof).anyMatch(p-> p.getSelectedPflichtModuls().contains(modul));
	}
	
	
}