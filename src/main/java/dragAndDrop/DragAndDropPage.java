package dragAndDrop;

import java.io.IOException;
import java.util.Arrays;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.menueBar.BasePage;
import com.menueBar.MenuItemEnum;
import com.modul.InfoPanel;
import com.modul.WahlPflichtModule;
import com.professoren.Prof;

public class DragAndDropPage extends BasePage{

	private static final long serialVersionUID = 1L;
	
	
	@SpringBean
	private WahlPflichtModule module;
	@SpringBean
	private ProfChangedEventLeft profEventLeft;
	@SpringBean
	private ProfChangedEventRight profEventRight;
	
	
	public DragAndDropPage() throws IOException {
		IModel<Prof> prof1 = Model.of(Prof.BREUNIG), prof2= Model.of(Prof.HINZ);
		Form form = new Form("form");
		ModulButtonPanel panel1 = new ModulButtonPanel("buttonPanel1",profEventLeft, prof1);
		ModulButtonPanel panel2 = new ModulButtonPanel("buttonPanel2",profEventRight, prof2);
		InfoPanel infoPanel = new InfoPanel("infoPanel", Arrays.asList(prof1, prof2));
		add(panel1, panel2, infoPanel);
		//form.add(panel1,panel2,infoPanel);
		//add(form);
	}
	
	@Override
	public MenuItemEnum getActiveMenu() {
		return MenuItemEnum.DRAG_DROP;
	}
	
}
