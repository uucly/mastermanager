package dragAndDrop;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
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
	
	private Form<Object> form;
	private transient AbstractEvent profEvent;
	
	//TODO replace ProfCHangedEvent with EventInjector
	public ModulButtonPanel(String id, AbstractEvent profEvent, final IModel<Prof> prof) throws IOException {
		super(id);
		setOutputMarkupId(true);
		this.profEvent = profEvent;
		ListModel<Modul> moduleOfProf = new ListModel<Modul>();
		IModel<String> text = Model.of("");
		IModel<List<Modul>> selectedModuls = createSelectedModuls(text, moduleOfProf);
		
		MarkupContainer container = new WebMarkupContainer("container");
		form = new Form<Object>("form");
		
		TextField<String> textField = new TextField<String>("textField", text);
		textField.add(new OnChangeAjaxBehavior(){

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(form);
			}
			
			public void detach(Component component) {
				selectedModuls.detach();
			};
			
		});
		
		container.add(textField);
		container.add(createDropDown(moduleOfProf, prof, createModulParser(module), profEvent));
		form.add(new ButtonListView("listView", selectedModuls, prof));
		container.add(form);
		add(container);
		//add(form);
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
	
	private static IModel<List<Modul>> createSelectedModuls(IModel<String> text, ListModel<Modul> moduleOfProf){
		return new LoadableDetachableModel<List<Modul>>() {

			private static final long serialVersionUID = 1L;

			@Override
			protected List<Modul> load() {
				if(text.getObject()!= null){
					return moduleOfProf.getObject().stream().filter(m -> m.getName().toUpperCase().contains(text.getObject().toUpperCase())).collect(Collectors.toList());
				} else {
					return moduleOfProf.getObject();
				}
			}
		};
	}
}
