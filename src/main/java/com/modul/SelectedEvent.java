package com.modul;

import org.apache.wicket.ajax.AjaxRequestTarget;

public class SelectedEvent {

	private final AjaxRequestTarget target;
	
	public SelectedEvent(AjaxRequestTarget target) {
		this.target = target;
	}

	public AjaxRequestTarget getTarget() {
		return target;
	}

}
