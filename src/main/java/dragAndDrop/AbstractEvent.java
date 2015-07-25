package dragAndDrop;

import org.apache.wicket.ajax.AjaxRequestTarget;

public abstract class AbstractEvent {

	private AjaxRequestTarget target;

	public AbstractEvent(AjaxRequestTarget target){
		this.target = target;
	}

	public AjaxRequestTarget getTarget() {
		return target;
	}

	public void setTarget(AjaxRequestTarget target) {
		this.target = target;
	}
}
