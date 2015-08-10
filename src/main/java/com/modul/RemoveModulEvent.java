package com.modul;

import org.apache.wicket.ajax.AjaxRequestTarget;

public class RemoveModulEvent {

	private final AjaxRequestTarget target;
	
	public RemoveModulEvent(AjaxRequestTarget target) {
		this.target = target;
	}

	public AjaxRequestTarget getTarget() {
		return target;
	}
	
}
