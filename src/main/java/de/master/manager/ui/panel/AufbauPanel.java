package de.master.manager.ui.panel;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import de.master.manager.profStuff.BasicModul;
import de.master.manager.profStuff.ICourseLoader;
import de.master.manager.profStuff.Prof;
import de.master.manager.profStuff.SupplementModul;
import de.master.manager.ui.model.TransformationModel2;

public class AufbauPanel extends Panel{

	private static final long serialVersionUID = 1L;

	public AufbauPanel(String id, ICourseLoader courseLoader, IModel<Prof> profOfPanel1, IModel<Prof> profOfPanel2, BasicModul basicModul, SupplementModul supplementModul, List<Prof> allProfs) {
		super(id);
		InfoPanel infoPanel = new InfoPanel("infoPanel", profOfPanel1, profOfPanel2, basicModul, supplementModul, allProfs);
		add(infoPanel);
		
	add(new AufbauButtonPanel("aufbauButtonPanel", courseLoader.loadCourses("Aufbau.txt"), basicModul));
		
	}
	
}
