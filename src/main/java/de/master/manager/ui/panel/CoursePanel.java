package de.master.manager.ui.panel;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import de.master.manager.profStuff.BasicModul;
import de.master.manager.profStuff.ICourseLoader;
import de.master.manager.profStuff.Prof;
import de.master.manager.profStuff.SupplementCourses;
import de.master.manager.profStuff.SupplementModul;
import de.master.manager.ui.model.TransformationModel2;

public class CoursePanel extends Panel{

	private static final long serialVersionUID = 1L;

	public CoursePanel(String id, ICourseLoader courseLoader, IModel<Prof> profOfPanel1, IModel<Prof> profOfPanel2, BasicModul basicModul, SupplementModul supplementModul, List<Prof> allProfs){
		super(id);
		
		/* load the ui */
		ModulButtonPanel panel1 = new ModulButtonPanel("buttonPanel1", profOfPanel1, profOfPanel2,  allProfs, courseLoader);
		ModulButtonPanel panel2 = new ModulButtonPanel("buttonPanel2", profOfPanel2, profOfPanel1, allProfs, courseLoader);
		
		InfoPanel infoPanel = new InfoPanel("infoPanel", new TransformationModel2<Prof, Prof, List<Prof>>(profOfPanel1, profOfPanel2, Arrays::asList), basicModul, supplementModul, allProfs);
		add(panel1, panel2, infoPanel);
	}

	
}
