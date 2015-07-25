package dragAndDrop;

import org.apache.wicket.ajax.AjaxRequestTarget;

public abstract class AbstractEvent{

	private AjaxRequestTarget target;
	private double id = Math.random()*100;
	
	public AbstractEvent(){}
	
	public AbstractEvent(AjaxRequestTarget target){
		this.target = target;
	}

	public AjaxRequestTarget getTarget() {
		return target;
	}

	public void setTarget(AjaxRequestTarget target) {
		this.target = target;
	}
	
	public double getId(){
		return id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(id);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractEvent other = (AbstractEvent) obj;
		if (Double.doubleToLongBits(id) != Double.doubleToLongBits(other.id))
			return false;
		return true;
	}
	
	
	
}
