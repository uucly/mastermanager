package dragAndDrop;

import java.util.Arrays;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.modul.Modul;
import com.modul.SelectedEvent;
import com.modul.SelectedModulContainer;
import com.professoren.Prof;

public class ModulButton extends AjaxButton {

	private static final long serialVersionUID = 1L;
	private final SelectedModulContainer modulContainer;
	private IModel<Prof> prof;
	private Modul modul;

	public ModulButton(String id, SelectedModulContainer modulContainer, Modul modul, IModel<Prof> prof) {
		super(id, Model.of(modul.getName()));
		setOutputMarkupId(true);
		this.modulContainer = modulContainer;
		this.modul = modul;
		this.prof = prof;
	}

	@Override
	protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
		super.onSubmit(target, form);
		setSelected();
		prof.getObject().addSelectedModul(modul);
		//modulContainer.addSelectedModulName(getModel());
		target.add(this);
		send(getPage(), Broadcast.DEPTH, new SelectedEvent(target));
	}

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		/*if (modulContainer.getSelectedModulNames().contains(getModel())) {
			setEnabled(false);
		//	add(new AttributeAppender("class", Model.of(" btn-danger")));
		}*/
		if(prof.getObject().getSelectedModuls().contains(modul)){
			setSelected();
		} else if(containsProf(prof.getObject(), modul)) {
			setEnabled(false);
		}
	}

	@Override
	public void onEvent(IEvent<?> event) {
		super.onEvent(event);
		/*if (event.getPayload() instanceof AbstractEvent) {
			if (modulContainer.getSelectedModulNames().contains(getModel())) {
				setEnabled(false);
				((AbstractEvent) event.getPayload()).getTarget().add(this);
			}
		} else */if(event.getPayload() instanceof SelectedEvent){
			/*if (modulContainer.getSelectedModulNames().contains(getModel())) {
				setEnabled(false);
				((SelectedEvent) event.getPayload()).getTarget().add(this);
			}*/
			if(containsProf(prof.getObject(), modul)){
				setEnabled(false);
				((SelectedEvent) event.getPayload()).getTarget().add(this);
			}
			
		}
		
	}
	
	private static boolean containsProf(Prof prof, Modul modul){
		return Arrays.asList(Prof.values()).stream().filter(p -> p!=prof).anyMatch(p-> p.getSelectedModuls().contains(modul));
	}
	
	private void setSelected(){
		setEnabled(false);
		add(new AttributeModifier("class", Model.of("btn btn-xs btn-danger")));
	}

}
