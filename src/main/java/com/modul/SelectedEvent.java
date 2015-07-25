package com.modul;

import org.apache.wicket.ajax.AjaxRequestTarget;

import dragAndDrop.AbstractEvent;

public class SelectedEvent extends AbstractEvent {

	public SelectedEvent(AjaxRequestTarget target) {
		super(target);
	}

}
