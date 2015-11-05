package de.master.manager.ui.events;

import java.io.Serializable;

import org.apache.wicket.ajax.AjaxRequestTarget;

import de.master.manager.profStuff.ICourse;

public class SelectedEvent implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private final AjaxRequestTarget target;
	private ICourse course;
	
	public SelectedEvent(AjaxRequestTarget target, ICourse course) {
		this.target = target;
		this.setCourse(course);
	}
	
	public AjaxRequestTarget getTarget() {
		return target;
	}
	public ICourse getCourse() {
		return course;
	}
	public void setCourse(ICourse course) {
		this.course = course;
	}

}
