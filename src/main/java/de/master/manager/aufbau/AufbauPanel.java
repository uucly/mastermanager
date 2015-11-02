package de.master.manager.aufbau;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import de.master.manager.model.TransformationModel2;
import de.master.manager.myproject.InfoPanel;
import de.master.manager.profStuff.Prof;
import de.master.manager.profStuff.WahlPflichtModuleLoader;

public class AufbauPanel extends Panel{

	public AufbauPanel(String id, WahlPflichtModuleLoader courseLoader, IModel<Prof> profOfPanel1, IModel<Prof> profOfPanel2, List<Prof> allProfs) {
		super(id);
		InfoPanel infoPanel = new InfoPanel("infoPanel", new TransformationModel2<Prof, Prof, List<Prof>>(profOfPanel1, profOfPanel2, Arrays::asList), allProfs);
		add(infoPanel);
	}

	
}
