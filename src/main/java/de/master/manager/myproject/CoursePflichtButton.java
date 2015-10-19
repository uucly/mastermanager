package de.master.manager.myproject;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import events.SelectedEvent;

public class CoursePflichtButton extends AjaxButton {

	private static final long serialVersionUID = 1L;
	private IModel<Prof> prof;
	private Course modul;
	private List<Prof> allProfs;

	public CoursePflichtButton(String id, Course modul, IModel<Prof> prof, List<Prof> allProfs) {
		super(id, Model.of(modul.getName()));
		setOutputMarkupId(true);
		this.modul = modul;
		this.prof = prof;
		this.allProfs = allProfs;
	}

	@Override
	protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
		super.onSubmit(target, form);
		setSelected();
		prof.getObject().addSelectedPflichtModul(modul);
		target.add(this);
		send(getPage(), Broadcast.DEPTH, new SelectedEvent(target, modul));
	}

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		if(prof.getObject().getSelectedPflichtModuls().contains(modul)){
			setSelected();
		} else if(containsProf(prof.getObject(), modul, allProfs)) {
			setEnabled(false);
		}
	}

	@Override
	public void onEvent(IEvent<?> event) {
		super.onEvent(event);
		if(event.getPayload() instanceof SelectedEvent){
			if(containsProf(prof.getObject(), modul, allProfs)){
				setEnabled(false);
				((SelectedEvent) event.getPayload()).getTarget().add(this);
			}
			
		}
		
	}
	
	private static boolean containsProf(Prof prof, Course modul, List<Prof> allProfs){
		return allProfs.stream().filter(p -> p!=prof).anyMatch(p-> p.getSelectedPflichtModuls().contains(modul));
	}
	
	private void setSelected(){
		setEnabled(false);
		add(new AttributeModifier("class", Model.of("btn btn-xs btn-success btn-block")));
	}

}