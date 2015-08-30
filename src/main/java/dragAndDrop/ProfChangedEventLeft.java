package dragAndDrop;

import java.io.Serializable;

import org.springframework.stereotype.Service;

@Service
public class ProfChangedEventLeft extends AbstractEvent implements Serializable {

	public ProfChangedEventLeft() {
		super();
	}
		
}
