package de.master.manager.mastermanager;

import de.master.manager.noten.NavsPanel;
import de.master.manager.profStuff.Prof;
import de.master.manager.profStuff.WahlPflichtCourseLoader;

public class DragAndDropPage extends BasePage{

	private static final long serialVersionUID = 1L;
	
	//@SpringBean
	//private WahlPflichtModuleLoader courseLoader;
	
	public DragAndDropPage(){
		WahlPflichtCourseLoader courseLoader = new WahlPflichtCourseLoader("WahlPflichtModule.txt");
		
		Prof breunig = loadProf("BreunigPflicht.txt", "Breunig", courseLoader), 
				hinz = loadProf("HinzPflicht.txt", "Hinz", courseLoader),
				heck = loadProf("HeckPflicht.txt", "Heck", courseLoader), 
				hennes = loadProf("HennesPflicht.txt", "Hennes", courseLoader);
		NavsPanel panel = new NavsPanel("navPanel", courseLoader, breunig, hinz, heck, hennes);
		add(panel);
	}
	
	
	/* methods */
	
	private Prof loadProf(String pflichtFileName, String profName, WahlPflichtCourseLoader courseLoader){
		return new Prof(profName, courseLoader.loadCourses(pflichtFileName));
	}
	
}
