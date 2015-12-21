package de.master.manager.ui.panel;

import java.util.List;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import de.master.manager.profStuff.IModul;
import de.master.manager.profStuff.IModulLoader;
import de.master.manager.profStuff.Prof;

public class SupplementPanel extends Panel {

	public SupplementPanel(String id, IModulLoader courseLoader, IModel<Prof> profOfPanel1, IModel<Prof> profOfPanel2, IModul basicModul, IModul supplementModul, List<Prof> allProfs) {
		super(id);
		
		add(new SupplementCourseButtonPanel("supplementButtonPanel", supplementModul, courseLoader.loadModul("Supplement.txt"), allProfs));
		add(new InfoPanel("infoPanel", profOfPanel1, profOfPanel2, basicModul, supplementModul, allProfs));
	}


}
