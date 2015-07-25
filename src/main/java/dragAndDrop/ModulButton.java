package dragAndDrop;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.modul.SelectedEvent;
import com.modul.SelectedModulContainer;

public class ModulButton extends AjaxButton {

	private static final long serialVersionUID = 1L;
	private final SelectedModulContainer modulContainer;

	public ModulButton(String id, SelectedModulContainer modulContainer,
			IModel<String> model) {
		super(id, model);
		setOutputMarkupId(true);
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

	/*@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		if (modulContainer.getSelectedModulNames().contains(getModel())) {
			setEnabled(false);
			add(new AttributeAppender("class", Model.of(" btn-danger")));
		}
	}*/

	@Override
	public void onEvent(IEvent<?> event) {
		super.onEvent(event);
		if (event.getPayload() instanceof AbstractEvent) {
			if (modulContainer.getSelectedModulNames().contains(getModel())) {
				setEnabled(false);
				((AbstractEvent) event.getPayload()).getTarget().add(this);
			}
		} else if(event.getPayload() instanceof SelectedEvent){
			if (modulContainer.getSelectedModulNames().contains(getModel())) {
				setEnabled(false);
				((SelectedEvent) event.getPayload()).getTarget().add(this);
			}
		}
		
	}

}
