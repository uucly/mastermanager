package de.master.manager.events;

import java.io.Serializable;

import org.apache.wicket.ajax.AjaxRequestTarget;

import de.master.manager.myproject.Course;

public class SelectedEvent implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private final AjaxRequestTarget target;
	private Course course;
	
	public SelectedEvent(AjaxRequestTarget target, Course course) {
		this.target = target;
		this.setCourse(course);
	}
	
	public AjaxRequestTarget getTarget() {
		return target;
	}
	public Course getCourse() {
		return course;
	}
	public void setCourse(Course course) {
		this.course = course;
	}

}
