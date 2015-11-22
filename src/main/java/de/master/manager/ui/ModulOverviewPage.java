package de.master.manager.ui;

import org.apache.wicket.spring.injection.annot.SpringBean;

import de.master.manager.profStuff.IModulLoader;
import de.master.manager.profStuff.Prof;
import de.master.manager.ui.panel.NavsPanel;

public class ModulOverviewPage extends BasePage{

	private static final long serialVersionUID = 1L;
	
	@SpringBean(name="wahlPflicht")
	private IModulLoader courseLoader;
	
	public ModulOverviewPage(){
		
		Prof breunig = loadProf("BreunigPflicht.txt", "Breunig", courseLoader), 
				hinz = loadProf("HinzPflicht.txt", "Hinz", courseLoader),
				heck = loadProf("HeckPflicht.txt", "Heck", courseLoader), 
				hennes = loadProf("HennesPflicht.txt", "Hennes", courseLoader);
		NavsPanel panel = new NavsPanel("navPanel", courseLoader, breunig, hinz, heck, hennes);
		add(panel);
	}
	
	
	/* methods */
	
	private Prof loadProf(String pflichtFileName, String profName, IModulLoader courseLoader){
		return new Prof(profName, courseLoader.loadModul(pflichtFileName));
	}
	
}
