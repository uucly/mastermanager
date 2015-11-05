package de.master.manager.ui.events;

import java.io.Serializable;

import org.apache.wicket.ajax.AjaxRequestTarget;

public class PanelChangedEvent implements Serializable{

	private final AjaxRequestTarget target;
	
	public PanelChangedEvent(AjaxRequestTarget target){
		this.target = target;
	}

	public AjaxRequestTarget getTarget() {
		return target;
	}
}
