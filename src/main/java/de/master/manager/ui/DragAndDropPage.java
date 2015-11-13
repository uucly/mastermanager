package de.master.manager.ui;

import java.awt.Image;
import java.util.ArrayList;

import org.apache.wicket.spring.injection.annot.SpringBean;

import de.master.manager.profStuff.BasicCourses;
import de.master.manager.profStuff.BasicModul;
import de.master.manager.profStuff.ICourseLoader;
import de.master.manager.profStuff.IModul;
import de.master.manager.profStuff.Prof;
import de.master.manager.profStuff.SupplementCourses;
import de.master.manager.profStuff.SupplementModul;
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
		
		SupplementModul supplementModul = new SupplementModul(new ArrayList<>());
		BasicModul basicModul = new BasicModul(new ArrayList<>());
		
		Prof breunig = loadProf("BreunigPflicht.txt", "Breunig", courseLoader, supplementModul, basicModul), 
				hinz = loadProf("HinzPflicht.txt", "Hinz", courseLoader, supplementModul, basicModul),
				heck = loadProf("HeckPflicht.txt", "Heck", courseLoader, supplementModul, basicModul), 
				hennes = loadProf("HennesPflicht.txt", "Hennes", courseLoader, supplementModul, basicModul);
		NavsPanel panel = new NavsPanel("navPanel", courseLoader, breunig, hinz, heck, hennes);
		add(panel);
	}
	
	
	/* methods */
	
	private Prof loadProf(String pflichtFileName, String profName, ICourseLoader courseLoader, SupplementModul supplementCourses, BasicModul basicCourses){
		return new Prof(profName, courseLoader.loadCourses(pflichtFileName), supplementCourses, basicCourses);
	}
	
}
