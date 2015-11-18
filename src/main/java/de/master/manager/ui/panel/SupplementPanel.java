package de.master.manager.ui.panel;

import java.util.List;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import de.master.manager.profStuff.BasicModul;
import de.master.manager.profStuff.ICourseLoader;
import de.master.manager.profStuff.Prof;
import de.master.manager.profStuff.SupplementModul;

public class SupplementPanel extends Panel {

	public SupplementPanel(String id, ICourseLoader courseLoader, IModel<Prof> profOfPanel1, IModel<Prof> profOfPanel2, BasicModul basicModul, SupplementModul supplementModul, List<Prof> allProfs) {
		super(id);
		
		add(new SupplementCourseButtonPanel("supplementButtonPanel", supplementModul, courseLoader.loadCourses("Supplement.txt")));
		add(new InfoPanel("infoPanel", profOfPanel1, profOfPanel2, basicModul, supplementModul, allProfs));
	}


}
