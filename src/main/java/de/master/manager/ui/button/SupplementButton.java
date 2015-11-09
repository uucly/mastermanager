package de.master.manager.ui.button;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;

import de.master.manager.profStuff.ICourse;

public class SupplementButton extends AjaxButton{

	private static final long serialVersionUID = 1L;

	public SupplementButton(String id, ICourse course) {
		super(id, Model.of(course.getName()));
		setOutputMarkupId(true);
		
	}
	
	@Override
	protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
		
	}

}
