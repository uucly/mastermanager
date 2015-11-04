package de.master.manager.myproject;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.master.manager.noten.NavsPanel;
import de.master.manager.profStuff.IWahlPflichtModuleLoader;
import de.master.manager.profStuff.Prof;
import de.master.manager.profStuff.WahlPflichtModuleLoader;

public class DragAndDropPage extends BasePage{

	private static final long serialVersionUID = 1L;
	
	//@SpringBean
	private WahlPflichtModuleLoader courseLoader;
	
	public DragAndDropPage(){
		WahlPflichtModuleLoader courseLoader = new WahlPflichtModuleLoader(loadFilePath("WahlPflichtModule.txt"));
		
		Prof breunig = loadProf("BreunigPflicht.txt", "Breunig", courseLoader), 
				hinz = loadProf("HinzPflicht.txt", "Hinz", courseLoader),
				heck = loadProf("HeckPflicht.txt", "Heck", courseLoader), 
				hennes = loadProf("HennesPflicht.txt", "Hennes", courseLoader);
		NavsPanel panel = new NavsPanel("navPanel", courseLoader, breunig, hinz, heck, hennes);
		add(panel);
	}
	
	
	/* methods */
	
	private Prof loadProf(String pflichtFileName, String profName, WahlPflichtModuleLoader courseLoader){
		String filePath = loadFilePath(pflichtFileName);
		return new Prof(profName, courseLoader.loadCourseOfProf(filePath));
	}
	
	/**
	 * 
	 * @param fileName with file ending (e.g. fileName.txt)
	 * @return path to file
	 */
	private String loadFilePath(String fileName){
		InputStream pflichtResource = getClass().getResourceAsStream(fileName);
		try {
			return IOUtils.toString(pflichtResource, "UTF-8");
		} catch (IOException e) {
			throw new RuntimeException("Cannot load file" + fileName, e);
		}
	}
}
