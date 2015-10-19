package de.master.manager.events;

import java.io.Serializable;

import org.apache.wicket.ajax.AjaxRequestTarget;

public abstract class AbstractEvent implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private AjaxRequestTarget target;
	
	public AbstractEvent(AjaxRequestTarget target){
		this.target = target;
	}

	public AjaxRequestTarget getTarget() {
		return target;
	}

	public void setTarget(AjaxRequestTarget target) {
		this.target = target;
	}
	
}
