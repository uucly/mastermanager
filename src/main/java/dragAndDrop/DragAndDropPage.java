package dragAndDrop;

import java.io.IOException;
import java.util.Arrays;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.menueBar.BasePage;
import com.menueBar.MenuItemEnum;
import com.modul.InfoPanel;
import com.modul.SelectedModulContainer;
import com.modul.WahlPflichtModule;

public class DragAndDropPage extends BasePage{

	private static final long serialVersionUID = 1L;
	
	
	@SpringBean
	private WahlPflichtModule module;
	
	public DragAndDropPage() throws IOException {
		SelectedModulContainer modulContainer1 = new SelectedModulContainer();
		SelectedModulContainer modulContainer2 = new SelectedModulContainer();
		
		ModulButtonPanel panel1 = new ModulButtonPanel("buttonPanel1", modulContainer1);
		ModulButtonPanel panel2 = new ModulButtonPanel("buttonPanel2", modulContainer2);
		InfoPanel infoPanel = new InfoPanel("infoPanel", Arrays.asList(modulContainer1, modulContainer2));
		
		add(panel1, panel2, infoPanel);
	}
	
	@Override
	public MenuItemEnum getActiveMenu() {
		return MenuItemEnum.DRAG_DROP;
	}
	
}
