package dragAndDrop;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import com.menueBar.BasePage;
import com.menueBar.MenuItemEnum;
import com.modul.InfoPanel;
import com.professoren.Prof;

public class DragAndDropPage extends BasePage{

	private static final long serialVersionUID = 1L;
	
	
	public DragAndDropPage() throws IOException {
		Arrays.asList(Prof.values()).stream().forEach(Prof::clearAll);
		IModel<Prof> prof1 = Model.of(Prof.BREUNIG), prof2= Model.of(Prof.HINZ);
		Form form = new Form("form");
		ModulButtonPanel panel1 = new ModulButtonPanel("buttonPanel1",new ProfChangedEventLeft(), prof1);
		ModulButtonPanel panel2 = new ModulButtonPanel("buttonPanel2",new ProfChangedEventRight(), prof2);
		InfoPanel infoPanel = new InfoPanel("infoPanel", new LoadableDetachableModel<List<Prof>>() {

			@Override
			protected List<Prof> load() {
				return Arrays.asList(prof1.getObject(), prof2.getObject());
			}
		});

		//InfoPanel infoPanel = new InfoPanel("infoPanel", new TransformationModel2<Prof, Prof, List<Prof>>(prof1, prof2, (p1, p2) -> Arrays.asList(p1,p2))/*Arrays.asList(prof1, prof2)*/);
		add(panel1, panel2, infoPanel);
	}
	
	@Override
	public MenuItemEnum getActiveMenu() {
		return MenuItemEnum.DRAG_DROP;
	}
	
}
