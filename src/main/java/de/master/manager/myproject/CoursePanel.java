package de.master.manager.myproject;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import de.master.manager.model.TransformationModel2;
import de.master.manager.profStuff.Prof;
import de.master.manager.profStuff.WahlPflichtModuleLoader;

public class CoursePanel extends Panel{

	private static final long serialVersionUID = 1L;

	public CoursePanel(String id, WahlPflichtModuleLoader courseLoader, Prof breunig, Prof hinz, Prof heck, Prof hennes){
		super(id);
		
		/* load the ui */
		List<Prof> allProfs = Arrays.asList(breunig, hinz, heck, hennes);
		IModel<Prof> profLeft = Model.of(breunig), profRight = Model.of(hinz);
		ModulButtonPanel panel1 = new ModulButtonPanel("buttonPanel1", profLeft, profRight,  allProfs, courseLoader);
		ModulButtonPanel panel2 = new ModulButtonPanel("buttonPanel2", profRight, profLeft, allProfs, courseLoader);
		
		InfoPanel infoPanel = new InfoPanel("infoPanel", new TransformationModel2<Prof, Prof, List<Prof>>(profLeft, profRight, Arrays::asList), allProfs);
		add(panel1, panel2, infoPanel);
	}

}
