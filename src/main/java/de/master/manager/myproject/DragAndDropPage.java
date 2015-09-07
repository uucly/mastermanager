package de.master.manager.myproject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.wicket.core.util.resource.ClassPathResourceFinder;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.file.File;

import de.master.manager.myproject.menueBar.BasePage;
import de.master.manager.myproject.menueBar.MenuItemEnum;


public class DragAndDropPage extends WebPage{

	private static final long serialVersionUID = 1L;
	
	
	public DragAndDropPage() throws IOException{
		//InputStream t = getClass().getResourceAsStream("C://Users/uucly.SvenWeisker/mastermanager/target/mastermanager-1.0-SNAPSHOT/WEB-INF/classes/WahlPflichtModule.txt");
		//InputStream te = getClass().getResourceAsStream("WahlPflichtModule.txt");
		
		// C://Users/uucly.SvenWeisker/mastermanager/target/mastermanager-1.0-SNAPSHOT/WEB-INF/classes/WahlPflichtModule.txt
		/*File f = new File("C://Users/uucly.SvenWeisker/mastermanager/target/mastermanager-1.0-SNAPSHOT/WEB-INF/classes/WahlPflichtModule.txt");
		if(f.exists()){
			System.out.println(true);
		}
		File fe = new File("WahlPflichtModule.txt");
		if(fe.exists()){
			System.out.println(true);
		}*/
		String bla = getClassRelativePath();
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
		
		List<Prof> allProfs = Arrays.asList(breunig, hinz, heck, hennes);
		IModel<Prof> profLeft = Model.of(breunig), profRight = Model.of(hinz);
		Form form = new Form("form");
		ModulButtonPanel panel1 = new ModulButtonPanel("buttonPanel1", profLeft, profRight,  allProfs, courseLoader);
		ModulButtonPanel panel2 = new ModulButtonPanel("buttonPanel2", profRight, profLeft, allProfs, courseLoader);
		InfoPanel infoPanel = new InfoPanel("infoPanel", new LoadableDetachableModel<List<Prof>>() {

			@Override
			protected List<Prof> load() {
				return Arrays.asList(profLeft.getObject(), profRight.getObject());
			}
		}, allProfs);

		//InfoPanel infoPanel = new InfoPanel("infoPanel", new TransformationModel2<Prof, Prof, List<Prof>>(prof1, prof2, (p1, p2) -> Arrays.asList(p1,p2))/*Arrays.asList(prof1, prof2));
		add(panel1, panel2, infoPanel);
	}
	
	
	/*@Override
	public MenuItemEnum getActiveMenu() {
		return MenuItemEnum.DRAG_DROP;
	}*/
	
	private static String getCoursePath(String fileName) {
		return "src/main/resources/" + fileName;
	}
	
}
