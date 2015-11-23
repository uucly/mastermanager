package de.master.manager.ui.button;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;

import com.google.common.base.Function;

import de.master.manager.profStuff.ICourse;
import de.master.manager.profStuff.IModul;
import de.master.manager.profStuff.Prof;
import de.master.manager.ui.events.SelectedEvent;

public abstract class AbstractCourseButton extends AjaxButton{

	private static final long serialVersionUID = 1L;
	protected ICourse course;
	protected IModul modul;
	
	public AbstractCourseButton(String id, ICourse course, IModul modul) {
		super(id, Model.of(course.getName()));
		setOutputMarkupId(true);
		this.course = course;
		this.modul = modul;
	}
	
	@Override
	protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
		super.onSubmit(target, form);
		setSelected();
		modul.add(course);
		target.add(this);
		send(getPage(), Broadcast.DEPTH, new SelectedEvent(target, course));
	}
	
	protected boolean isAlreadySelected(){
		return modul.contains(course);
	}
	
	protected boolean isAlreadySelectedInOtherProf(final Prof prof, List<Prof> allProfs, Function<Prof, IModul> loadModul){
		for(Prof p : allProfs){
			if(p != prof){
				return loadModul.apply(p).contains(course);
			}
		}
		return false;
	//	return allProfs.stream().filter(p -> p!=prof).anyMatch(p-> loadModul.apply(p).contains(course));
	}
	
	protected void setSelected(){
		setEnabled(false);
		add(new AttributeModifier("class", Model.of("btn btn-xs btn-success")));
	}
		
}
