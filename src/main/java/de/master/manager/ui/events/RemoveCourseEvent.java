package de.master.manager.ui.events;

import org.apache.wicket.ajax.AjaxRequestTarget;

public class RemoveCourseEvent {

	private final AjaxRequestTarget target;
	
	public RemoveCourseEvent(AjaxRequestTarget target) {
		this.target = target;
	}

	public AjaxRequestTarget getTarget() {
		return target;
	}
	
}
