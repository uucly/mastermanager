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

import com.modul.Course;
import com.modul.SelectedEvent;
import com.professoren.Prof;

public class CoursePflichtButton extends AjaxButton {

	private static final long serialVersionUID = 1L;
	private IModel<Prof> prof;
	private Course modul;

	public CoursePflichtButton(String id, Course modul, IModel<Prof> prof) {
		super(id, Model.of(modul.getName()));
		setOutputMarkupId(true);
		this.modul = modul;
		this.prof = prof;
	}

	@Override
	protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
		super.onSubmit(target, form);
		setSelected();
		prof.getObject().addSelectedPflichtModul(modul);
		target.add(this);
		send(getPage(), Broadcast.DEPTH, new SelectedEvent(target));
	}

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		if(prof.getObject().getSelectedPflichtModuls().contains(modul)){
			setSelected();
		} else if(containsProf(prof.getObject(), modul)) {
			setEnabled(false);
		}
	}

	@Override
	public void onEvent(IEvent<?> event) {
		super.onEvent(event);
		if(event.getPayload() instanceof SelectedEvent){
			if(containsProf(prof.getObject(), modul)){
				setEnabled(false);
				((SelectedEvent) event.getPayload()).getTarget().add(this);
			}
			
		}
		
	}
	
	private static boolean containsProf(Prof prof, Course modul){
		return Arrays.asList(Prof.values()).stream().filter(p -> p!=prof).anyMatch(p-> p.getSelectedPflichtModuls().contains(modul));
	}
	
	private void setSelected(){
		setEnabled(false);
		add(new AttributeModifier("class", Model.of("btn btn-xs btn-success btn-block")));
	}

}