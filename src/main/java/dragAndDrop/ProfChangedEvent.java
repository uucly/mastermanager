package dragAndDrop;

import org.apache.wicket.ajax.AjaxRequestTarget;

public class ProfChangedEvent {

	private final AjaxRequestTarget target;

	public ProfChangedEvent(AjaxRequestTarget target){
		this.target = target;
	}

	public AjaxRequestTarget getTarget() {
		return target;
	}
}
