package de.master.manager.noten;

import java.io.Serializable;

import org.apache.wicket.ajax.AjaxRequestTarget;

public class GradeChangedEvent implements Serializable{

	private final AjaxRequestTarget target;
	
	public GradeChangedEvent(AjaxRequestTarget target) {
		this.target = target;
	}

	public AjaxRequestTarget getTarget() {
		return target;
	}

}
