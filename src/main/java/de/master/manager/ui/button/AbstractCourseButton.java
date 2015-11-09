package de.master.manager.ui.button;

import java.util.List;
import java.util.function.Function;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import de.master.manager.profStuff.ICourse;
import de.master.manager.profStuff.Prof;
import de.master.manager.ui.events.SelectedEvent;

public abstract class AbstractCourseButton extends AjaxButton{

	private static final long serialVersionUID = 1L;
	protected Prof prof;
	protected ICourse course;
	
	public AbstractCourseButton(String id, ICourse course, Prof prof) {
		super(id, Model.of(course.getName()));
		setOutputMarkupId(true);
		this.course = course;
		this.prof = prof;
	}
	
	@Override
	protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
		super.onSubmit(target, form);
		setSelected();
		addModulFunction(prof, course);
		target.add(this);
		send(getPage(), Broadcast.DEPTH, new SelectedEvent(target, course));
	}
	
	protected abstract void addModulFunction(Prof prof, ICourse course);
	
	protected boolean isAlreadySelected(Function<Prof, List<ICourse>> loadSelectedModuls){
		return loadSelectedModuls.apply(prof).contains(course);
	}
	
	protected boolean isAlreadySelectedInOtherProf(Function<Prof, List<ICourse>> loadSelectedModuls, List<Prof> allProfs){
		return allProfs.stream().filter(p -> p!=prof).anyMatch(p-> loadSelectedModuls.apply(prof).contains(course));
	}
	
	protected void setSelected(){
		setEnabled(false);
		add(new AttributeModifier("class", Model.of("btn btn-xs btn-success")));
	}
		
}
