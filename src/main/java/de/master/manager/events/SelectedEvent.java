package de.master.manager.events;

import java.io.Serializable;

import org.apache.wicket.ajax.AjaxRequestTarget;

import de.master.manager.profStuff.AbstractCourse;

public class SelectedEvent implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private final AjaxRequestTarget target;
	private AbstractCourse course;
	
	public SelectedEvent(AjaxRequestTarget target, AbstractCourse course) {
		this.target = target;
		this.setCourse(course);
	}
	
	public AjaxRequestTarget getTarget() {
		return target;
	}
	public AbstractCourse getCourse() {
		return course;
	}
	public void setCourse(AbstractCourse course) {
		this.course = course;
	}

}
