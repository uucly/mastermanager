package dragAndDrop;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.springframework.stereotype.Service;

@Service
public class ProfChangedEventLeft extends AbstractEvent {

	public ProfChangedEventLeft() {
		super();
	}
	
	public ProfChangedEventLeft(AjaxRequestTarget target) {
		super(target);
	}
		
}
