package dragAndDrop;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.menueBar.BasePage;
import com.menueBar.MenuItemEnum;
import com.modul.Modul;
import com.modul.ModulParser;
import com.modul.WahlPflichtModule;
import com.professoren.Prof;

public class DragAndDropPage extends BasePage{

	private static final long serialVersionUID = 1L;
	//TODO outsource in own class and inject
	private static final String ALL_PATH= "src/main/resources/WahlPflichtModule.txt";
	
	@SpringBean
	private WahlPflichtModule module;
	
	public DragAndDropPage() throws IOException {
		
		ModulParser modulParser = createModulParser(module);
		
		List<Modul> moduls = modulParser.parse(Prof.BREUNIG.getPath());
		Form form = new Form("form");
		ListView<Modul> modulList = new ListView<Modul>("listView",moduls) {

			@Override
			protected void populateItem(ListItem<Modul> item) {
				Button b = new ModulButton("modulButton", Model.of(item.getModelObject().getName()));
				item.add(b);
			}
		};
		
		form.add(modulList);
		add(form);
	}
	
	@Override
	public MenuItemEnum getActiveMenu() {
		return MenuItemEnum.DRAG_DROP;
	}
	
	private static ModulParser createModulParser(WahlPflichtModule module){
		try {
			List<Modul> allModule = module.parse(ALL_PATH);
			return new ModulParser(allModule);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
