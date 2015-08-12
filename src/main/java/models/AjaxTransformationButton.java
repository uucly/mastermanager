package models;

import java.util.Objects;
import java.util.function.Function;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;

public class AjaxTransformationButton extends AjaxButton{

	private static final long serialVersionUID = 1L;
	private Function<String, Void> transformation;

	public AjaxTransformationButton(String id, IModel<String> model, Function<String, Void> transformation) {
		super(id, model);
		this.transformation = transformation;
	}
	
	public AjaxTransformationButton(String id, Function<String, Void> transformation) {
		super(id);
		this.transformation = transformation;
	}
	
	@Override
	public void onSubmit() {
		// TODO Auto-generated method stub
		super.onSubmit();
	}
	
	@Override
	protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
		super.onSubmit(target, form);
	}
	
	protected void onSubmit(IModel<String> model){
		Objects.requireNonNull(transformation);
		transformation.apply(model.getObject());
	}

}
