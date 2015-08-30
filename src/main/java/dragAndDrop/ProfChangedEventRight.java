package dragAndDrop;

import java.io.Serializable;

import org.springframework.stereotype.Service;

@Service
public class ProfChangedEventRight extends AbstractEvent implements Serializable{
	
	public ProfChangedEventRight() {
		super();
	}
}
