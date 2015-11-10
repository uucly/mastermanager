package de.master.manager.ui;

import org.apache.wicket.spring.injection.annot.SpringBean;

import de.master.manager.profStuff.BasicCourses;
import de.master.manager.profStuff.ICourseLoader;
import de.master.manager.profStuff.Prof;
import de.master.manager.profStuff.SupplementCourses;
import de.master.manager.ui.panel.NavsPanel;

public class DragAndDropPage extends BasePage{

	private static final long serialVersionUID = 1L;
	
	@SpringBean(name="wahlPflicht")
	private ICourseLoader courseLoader;
	
	@SpringBean
	private SupplementCourses supplementCourses;
	
	@SpringBean
	private BasicCourses basicCourses;
	
	public DragAndDropPage(){
		Prof breunig = loadProf("BreunigPflicht.txt", "Breunig", courseLoader, supplementCourses, basicCourses), 
				hinz = loadProf("HinzPflicht.txt", "Hinz", courseLoader, supplementCourses, basicCourses),
				heck = loadProf("HeckPflicht.txt", "Heck", courseLoader, supplementCourses, basicCourses), 
				hennes = loadProf("HennesPflicht.txt", "Hennes", courseLoader, supplementCourses, basicCourses);
		NavsPanel panel = new NavsPanel("navPanel", courseLoader, breunig, hinz, heck, hennes);
		add(panel);
	}
	
	
	/* methods */
	
	private Prof loadProf(String pflichtFileName, String profName, ICourseLoader courseLoader, SupplementCourses supplementCourses, BasicCourses basicCourses){
		return new Prof(profName, courseLoader.loadCourses(pflichtFileName), supplementCourses, basicCourses);
	}
	
}
