package dragAndDrop;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.modul.Modul;
import com.modul.ModulParser;
import com.modul.WahlPflichtModule;
import com.professoren.Prof;

public class ModulButtonPanel extends Panel{

	private static final long serialVersionUID = 1L;
	private static final String ALL_PATH= "src/main/resources/WahlPflichtModule.txt";
	private static final List<Prof> SEARCH_ENGINES = Arrays.asList(Prof.values());
	
	@SpringBean
	private WahlPflichtModule module;
	
	private ListModel<Modul> moduleOfProf;
	private Form<Object> form;
	private transient AbstractEvent profEvent;
	
	//TODO replace ProfCHangedEvent with EventInjector
	public ModulButtonPanel(String id, AbstractEvent profEvent, final IModel<Prof> prof) throws IOException {
		super(id);
		setOutputMarkupId(true);
		this.profEvent = profEvent;
		ModulParser modulParser = createModulParser(module);
		moduleOfProf = new ListModel<Modul>();
		
		form = new Form<Object>("form");
		
		ListView<Modul> modulList = new ListView<Modul>("listView",moduleOfProf) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<Modul> item) {				
				Button b = new ModulButton("modulButton", item.getModelObject(), prof);
				item.add(b);
			}
		};
		
		form.add(createDropDown(moduleOfProf, prof, modulParser, profEvent));
		form.add(modulList);
		
		add(form);
	}
	
	
	/*	*/
	private static ModulParser createModulParser(WahlPflichtModule module){
		try {
			List<Modul> allModule = module.parse(ALL_PATH);
			return new ModulParser(allModule);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private static DropDownChoice<Prof> createDropDown(final IModel<List<Modul>> moduleOfProf, final IModel<Prof> selectedProf, ModulParser modulParser, AbstractEvent profEvent){
		moduleOfProf.setObject(loadModulsOfProf(modulParser, selectedProf));
		DropDownChoice<Prof> dropDown = new DropDownChoice<Prof>("dropDown",selectedProf, SEARCH_ENGINES);
		dropDown.add(new OnChangeAjaxBehavior() {
			
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				moduleOfProf.setObject(loadModulsOfProf(modulParser, selectedProf));
				profEvent.setTarget(target);
				dropDown.send(dropDown.getPage(), Broadcast.DEPTH, profEvent);
			}
		});
		
		return dropDown;
	}
	
	private static List<Modul> loadModulsOfProf(ModulParser modulParser, IModel<Prof> selected){
		try{
			return modulParser.parse(selected.getObject().getPath());
		}catch(IOException ex){
			throw new RuntimeException(ex);
		}
	}

	@Override
	public void onEvent(IEvent<?> event) {
		super.onEvent(event);
		if(event.getPayload() instanceof AbstractEvent && profEvent.getId() == ((AbstractEvent)event.getPayload()).getId()){
			((AbstractEvent)event.getPayload()).getTarget().add(form);
		}
	}
}
