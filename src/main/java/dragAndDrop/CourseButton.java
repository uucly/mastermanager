package dragAndDrop;

import java.util.List;
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

public class CourseButton extends AjaxButton {

	private static final long serialVersionUID = 1L;
	private final IModel<Prof> prof;
	private final Course modul;
	private final List<Prof> allProfs;
	private IModel<Prof> profRight;

	public CourseButton(String id, Course modul, IModel<Prof> prof, IModel<Prof> profRight, List<Prof> allProfs) {
		super(id, Model.of(modul.getName()));
		setOutputMarkupId(true);
		this.modul = modul;
		this.prof = prof;
		this.profRight = profRight;
		this.allProfs = allProfs;
	}

	@Override
	protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
		super.onSubmit(target, form);
		setSelected();
		prof.getObject().addSelectedModul(modul);
		target.add(this);
		send(getPage(), Broadcast.DEPTH, new SelectedEvent(target));
	}

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		if(isSelected(prof.getObject(), modul)){
			setSelected();
		} else if(profRight.getObject().getPflichtCourse().contains(modul)){
			setSelected();
		} else if(containsProf(prof.getObject(), modul, allProfs)) {
			setEnabled(false);
		}
	}
	
	private static boolean isSelected(Prof prof, Course course){
		return prof.getSelectedModuls().contains(course);
	}

	@Override
	public void onEvent(IEvent<?> event) {
		super.onEvent(event);
		if(event.getPayload() instanceof SelectedEvent){
			if(containsProf(prof.getObject(), modul, allProfs)){
				setEnabled(false);
				((SelectedEvent) event.getPayload()).getTarget().add(this);
			}
			
		}
		
	}
	
	private static boolean containsProf(Prof prof, Course modul, List<Prof> allProfs){
		return allProfs.stream().filter(p -> p!=prof).anyMatch(p-> p.getSelectedModuls().contains(modul));
	}
	
	private void setSelected(){
		setEnabled(false);
		add(new AttributeModifier("class", Model.of("btn btn-xs btn-success btn-block")));
	}

}
