package de.master.manager.aufbau;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import de.master.manager.profStuff.ICourseLoader;
import de.master.manager.profStuff.Prof;
import de.master.manager.profStuff.SupplementCourses;
import de.master.manager.ui.model.TransformationModel2;
import de.master.manager.ui.panel.InfoPanel;

public class AufbauPanel extends Panel{

	private static final long serialVersionUID = 1L;

	public AufbauPanel(String id, ICourseLoader courseLoader, IModel<Prof> profOfPanel1, IModel<Prof> profOfPanel2, SupplementCourses supplements, List<Prof> allProfs) {
		super(id);
		InfoPanel infoPanel = new InfoPanel("infoPanel", new TransformationModel2<Prof, Prof, List<Prof>>(profOfPanel1, profOfPanel2, Arrays::asList), supplements, allProfs);
		add(infoPanel);
	}

	
}
