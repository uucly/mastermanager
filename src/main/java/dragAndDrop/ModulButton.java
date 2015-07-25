package dragAndDrop;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.modul.SelectedEvent;
import com.modul.SelectedModulContainer;

public class ModulButton extends AjaxButton{

	private static final long serialVersionUID = 1L;
	private final SelectedModulContainer modulContainer;

	public ModulButton(String id, SelectedModulContainer modulContainer, IModel<String> model) {
		super(id, model);
		this.modulContainer = modulContainer;
	}

	@Override
	protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
		super.onSubmit(target, form);
		setEnabled(false);
		add(new AttributeModifier("class", Model.of("btn btn-xs btn-danger")));
		modulContainer.addSelectedModulName(getModel());
		target.add(this);
		send(getPage(), Broadcast.DEPTH, new SelectedEvent(target));
	}

}
