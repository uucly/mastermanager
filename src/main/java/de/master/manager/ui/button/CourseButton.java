package de.master.manager.ui.button;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import de.master.manager.events.SelectedEvent;
import de.master.manager.profStuff.ICourse;
import de.master.manager.profStuff.Prof;

public class CourseButton extends AjaxButton {

	private static final long serialVersionUID = 1L;
	private final IModel<Prof> prof;
	private final ICourse modul;
	private final List<Prof> allProfs;
	private IModel<Prof> profRight;

	public CourseButton(String id, ICourse modul, IModel<Prof> prof, IModel<Prof> profRight, List<Prof> allProfs) {
		super(id, Model.of(modul.getName()));
		setOutputMarkupId(true);
		this.modul = modul;
		this.prof = prof;
		this.profRight = profRight;
		this.allProfs = allProfs;
	}

	@Override
	protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
		super.onSubmit(target, form);
		setSelected();
		prof.getObject().addSelectedModul(modul);
		target.add(this);
		send(getPage(), Broadcast.DEPTH, new SelectedEvent(target, modul));
	}

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		if(profRight.getObject().getPflichtCourse().contains(modul)){
			prof.getObject().getSelectedModuls().remove(modul);
			setEnabled(false);
		} else if(isAlreadySelected(prof.getObject(), modul)){
			setSelected();
		} else if(isAlreadySelectedInOtherProf(prof.getObject(), modul, allProfs)) {
			setEnabled(false);
		}
	}
	
	private static boolean isAlreadySelected(Prof prof, ICourse course){
		return prof.getSelectedModuls().contains(course);
	}

	@Override
	public void onEvent(IEvent<?> event) {
		super.onEvent(event);
		if(event.getPayload() instanceof SelectedEvent){
			SelectedEvent selectedEvent = ((SelectedEvent) event.getPayload());
			if(selectedEvent.getCourse().equals(modul)){
				setEnabled(false);
				selectedEvent.getTarget().add(this);
			}
		}
	}
	
	private static boolean isAlreadySelectedInOtherProf(Prof prof, ICourse modul, List<Prof> allProfs){
		return allProfs.stream().filter(p -> p!=prof).anyMatch(p-> p.getSelectedModuls().contains(modul));
	}
	
	private void setSelected(){
		setEnabled(false);
		add(new AttributeModifier("class", Model.of("btn btn-xs btn-success")));
	}

}