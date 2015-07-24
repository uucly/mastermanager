package dragAndDrop;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class ModulButton extends AjaxButton{

	private static final long serialVersionUID = 1L;

	public ModulButton(String id, IModel<String> model) {
		super(id, model);
	}

	@Override
	protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
		super.onSubmit(target, form);
		setEnabled(false);
		add(new AttributeModifier("class", Model.of("btn btn-xs btn-danger")));
		target.add(this);
	}

}
