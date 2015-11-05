package de.master.manager.ui.events;

import java.io.Serializable;

import org.apache.wicket.ajax.AjaxRequestTarget;

public class ProfChangedEvent extends AbstractEvent implements Serializable {

	private static final long serialVersionUID = 1L;

	public ProfChangedEvent(AjaxRequestTarget target) {
		super(target);
	}
		
}
