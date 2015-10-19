package de.master.manager.myproject;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.wicketstuff.annotation.mount.MountPath;

import de.master.manager.menueBar.BasePage;
import de.master.manager.noten.NavsPanel;
import de.master.manager.profStuff.Prof;
import de.master.manager.profStuff.WahlPflichtModuleLoader;

@MountPath(value = "/", alt = "/home")
public class DragAndDropPage extends BasePage{

	private static final long serialVersionUID = 1L;
	
	public DragAndDropPage() throws IOException{
		InputStream resource = getClass().getResourceAsStream("WahlPflichtModule.txt");
		InputStream breunigPflichtResource = getClass().getResourceAsStream("BreunigPflicht.txt");
		InputStream hinzPflichtResource = getClass().getResourceAsStream("HinzPflicht.txt");
		InputStream heckPflichtResource = getClass().getResourceAsStream("HeckPflicht.txt");
		InputStream hennesPflichtResource = getClass().getResourceAsStream("HennesPflicht.txt");
		
		WahlPflichtModuleLoader courseLoader = new WahlPflichtModuleLoader(IOUtils.toString(resource, "UTF-8"));
		Prof breunig = new Prof("Breunig", courseLoader.loadCourseOfProf(IOUtils.toString(breunigPflichtResource, "UTF-8"))), 
				hinz = new Prof("Hinz", courseLoader.loadCourseOfProf(IOUtils.toString(hinzPflichtResource, "UTF-8"))), 
				heck = new Prof("Heck", courseLoader.loadCourseOfProf(IOUtils.toString(heckPflichtResource, "UTF-8"))), 
				hennes = new Prof("Hennes", courseLoader.loadCourseOfProf(IOUtils.toString(hennesPflichtResource, "UTF-8")));
		NavsPanel panel = new NavsPanel("navPanel", courseLoader, breunig, hinz, heck, hennes);
		add(panel);
	}
	
}
