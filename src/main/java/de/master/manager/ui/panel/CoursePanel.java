package de.master.manager.ui.panel;

import java.util.List;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import de.master.manager.profStuff.IModul;
import de.master.manager.profStuff.IModulLoader;
import de.master.manager.profStuff.Prof;

public class CoursePanel extends Panel{

	private static final long serialVersionUID = 1L;

	public CoursePanel(String id, IModulLoader courseLoader, IModel<Prof> profOfPanel1, IModel<Prof> profOfPanel2, IModul basicModul, IModul supplementModul, List<Prof> allProfs){
		super(id);
		
		/* load the ui */
		ModulButtonPanel panel1 = new ModulButtonPanel("buttonPanel1", profOfPanel1, profOfPanel2,  allProfs, supplementModul, courseLoader);
		ModulButtonPanel panel2 = new ModulButtonPanel("buttonPanel2", profOfPanel2, profOfPanel1, allProfs, supplementModul, courseLoader);
		
		InfoPanel infoPanel = new InfoPanel("infoPanel", profOfPanel1, profOfPanel2, basicModul, supplementModul, allProfs);
		add(panel1, panel2, infoPanel);
	}

	
}
