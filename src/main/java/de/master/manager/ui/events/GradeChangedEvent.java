package de.master.manager.ui.events;

import java.io.Serializable;

import org.apache.wicket.ajax.AjaxRequestTarget;

public class GradeChangedEvent implements Serializable{

	private static final long serialVersionUID = 1L;
	private AjaxRequestTarget target;
	
	public GradeChangedEvent(AjaxRequestTarget target) {
		this.target = target;
	}

	public AjaxRequestTarget getTarget() {
		return target;
	}

}
