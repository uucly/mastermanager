package de.master.manager.mastermanager;

import org.apache.wicket.spring.injection.annot.SpringBean;
import de.master.manager.noten.NavsPanel;
import de.master.manager.profStuff.ICourseLoader;
import de.master.manager.profStuff.Prof;

public class DragAndDropPage extends BasePage{

	private static final long serialVersionUID = 1L;
	
	@SpringBean(name="wahlPflicht")
	private ICourseLoader courseLoader;
	
	public DragAndDropPage(){
		Prof breunig = loadProf("BreunigPflicht.txt", "Breunig", courseLoader), 
				hinz = loadProf("HinzPflicht.txt", "Hinz", courseLoader),
				heck = loadProf("HeckPflicht.txt", "Heck", courseLoader), 
				hennes = loadProf("HennesPflicht.txt", "Hennes", courseLoader);
		NavsPanel panel = new NavsPanel("navPanel", courseLoader, breunig, hinz, heck, hennes);
		add(panel);
	}
	
	
	/* methods */
	
	private Prof loadProf(String pflichtFileName, String profName, ICourseLoader courseLoader){
		return new Prof(profName, courseLoader.loadCourses(pflichtFileName));
	}
	
}
