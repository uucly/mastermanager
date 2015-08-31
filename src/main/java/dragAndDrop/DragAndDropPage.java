package dragAndDrop;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;

import com.menueBar.BasePage;
import com.menueBar.MenuItemEnum;
import com.modul.InfoPanel;
import com.modul.WahlPflichtModuleLoader;
import com.professoren.Prof;

public class DragAndDropPage extends BasePage{

	private static final long serialVersionUID = 1L;
	
	
	public DragAndDropPage(){
		WahlPflichtModuleLoader courseLoader = new WahlPflichtModuleLoader("src/main/resources/WahlPflichtModule.txt");
		Prof breunig = new Prof("Breunig", courseLoader.loadCourseOfProf(getCoursePath("BreunigPflicht.txt"))), 
				hinz = new Prof("Hinz", courseLoader.loadCourseOfProf(getCoursePath("HinzPflicht.txt"))), 
				heck = new Prof("Heck", courseLoader.loadCourseOfProf(getCoursePath("HeckPflicht.txt"))), 
				hennes = new Prof("Hennes", courseLoader.loadCourseOfProf(getCoursePath("HennesPflicht.txt")));
		
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

		//InfoPanel infoPanel = new InfoPanel("infoPanel", new TransformationModel2<Prof, Prof, List<Prof>>(prof1, prof2, (p1, p2) -> Arrays.asList(p1,p2))/*Arrays.asList(prof1, prof2)*/);
		add(panel1, panel2, infoPanel);
	}
	
	
	@Override
	public MenuItemEnum getActiveMenu() {
		return MenuItemEnum.DRAG_DROP;
	}
	
	private static String getCoursePath(String fileName) {
		return "src/main/resources/" + fileName;
	}
	
}
