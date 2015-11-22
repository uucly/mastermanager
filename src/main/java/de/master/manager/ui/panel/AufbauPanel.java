package de.master.manager.ui.panel;

import java.util.List;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import de.master.manager.profStuff.IModul;
import de.master.manager.profStuff.IModulLoader;
import de.master.manager.profStuff.Prof;

public class AufbauPanel extends Panel{

	private static final long serialVersionUID = 1L;

	public AufbauPanel(String id, IModulLoader courseLoader, IModel<Prof> profOfPanel1, IModel<Prof> profOfPanel2, IModul basicModul, IModul supplementModul, List<Prof> allProfs) {
		super(id);
		InfoPanel infoPanel = new InfoPanel("infoPanel", profOfPanel1, profOfPanel2, basicModul, supplementModul, allProfs);
		add(infoPanel);
		
	add(new AufbauButtonPanel("aufbauButtonPanel", courseLoader.loadModul("Aufbau.txt"), basicModul));
		
	}
	
}
