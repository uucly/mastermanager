package de.master.manager.myproject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.wicketstuff.annotation.mount.MountPath;

import de.master.manager.menueBar.BasePage;
import de.master.manager.model.TransformationModel2;
import de.master.manager.profStuff.Prof;
import de.master.manager.profStuff.WahlPflichtModuleLoader;

@MountPath(value = "/", alt = "/home")
public class DragAndDropPage extends BasePage{

	private static final long serialVersionUID = 1L;
	
	
	public DragAndDropPage() throws IOException{
		/* load all needed wahlcourses and pflichtcourses */
		InputStream resource = getClass().getResourceAsStream("WahlPflichtModule.txt");
		InputStream breunigPflichtResource = getClass().getResourceAsStream("BreunigPflicht.txt");
		InputStream hinzPflichtResource = getClass().getResourceAsStream("HinzPflicht.txt");
		InputStream heckPflichtResource = getClass().getResourceAsStream("HeckPflicht.txt");
		InputStream hennesPflichtResource = getClass().getResourceAsStream("HennesPflicht.txt");
		
		WahlPflichtModuleLoader courseLoader = new WahlPflichtModuleLoader(IOUtils.toString(resource, "UTF-8"));
		Prof breunig = new Prof("Breunig", courseLoader.loadCourseOfProf(IOUtils.toString(breunigPflichtResource, "UTF-8"))), 
				hinz = new Prof("Hinz", courseLoader.loadCourseOfProf(IOUtils.toString(hinzPflichtResource, "UTF-8"))), 
				heck = new Prof("Heck", courseLoader.loadCourseOfProf(IOUtils.toString(heckPflichtResource, "UTF-8"))), 
				hennes = new Prof("Hennes", courseLoader.loadCourseOfProf(IOUtils.toString(hennesPflichtResource, "UTF-8")));
		
		/* load the ui */
		List<Prof> allProfs = Arrays.asList(breunig, hinz, heck, hennes);
		IModel<Prof> profLeft = Model.of(breunig), profRight = Model.of(hinz);
		ModulButtonPanel panel1 = new ModulButtonPanel("buttonPanel1", profLeft, profRight,  allProfs, courseLoader);
		ModulButtonPanel panel2 = new ModulButtonPanel("buttonPanel2", profRight, profLeft, allProfs, courseLoader);
		
		InfoPanel infoPanel = new InfoPanel("infoPanel", new TransformationModel2<Prof, Prof, List<Prof>>(profLeft, profRight, Arrays::asList), allProfs);
		add(panel1, panel2, infoPanel);
	}
	
}
