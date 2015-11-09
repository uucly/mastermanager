package de.master.manager.ui.panel;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import de.master.manager.profStuff.ICourseLoader;
import de.master.manager.profStuff.Prof;
import de.master.manager.ui.model.TransformationModel2;

public class SupplementPanel extends Panel {

	public SupplementPanel(String id, ICourseLoader courseLoader, IModel<Prof> profOfPanel1, IModel<Prof> profOfPanel2, List<Prof> allProfs) {
		super(id);
		
		add(new SupplementCourseButtonPanel("supplementButtonPanel"));
		add(new InfoPanel("infoPanel", new TransformationModel2<Prof, Prof, List<Prof>>(profOfPanel1, profOfPanel2, Arrays::asList), allProfs));
		
	}


}