package dragAndDrop;

import java.io.Serializable;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.springframework.stereotype.Service;

@Service
public class ProfChangedEvent extends AbstractEvent implements Serializable {

	private static final long serialVersionUID = 1L;

	public ProfChangedEvent(AjaxRequestTarget target) {
		super(target);
	}
		
}
