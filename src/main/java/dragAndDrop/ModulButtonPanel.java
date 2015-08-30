package dragAndDrop;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.management.RuntimeErrorException;

import models.TransformationModel;
import models.TransformationModel2;

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

import com.modul.Course;
import com.modul.CourseParser;
import com.modul.RemoveCourseEvent;
import com.modul.WahlPflichtModuleLoader;
import com.professoren.Prof;

public class ModulButtonPanel extends Panel {

	private static final long serialVersionUID = 1L;
	private static final String ALL_PATH = "src/main/resources/WahlPflichtModule.txt";
	private static final List<Prof> SEARCH_ENGINES = Arrays.asList(Prof.values());

	@SpringBean
	private WahlPflichtModuleLoader module;

	private Form<Object> formWahl;
	private transient AbstractEvent profEvent;
	private Form<?> formPflicht;

	// TODO replace ProfCHangedEvent with EventInjector
	public ModulButtonPanel(String id, AbstractEvent profEvent, final IModel<Prof> prof) throws IOException {
		super(id);
		setOutputMarkupId(true);
		this.profEvent = profEvent;
		IModel<List<Course>> moduleOfProf = new ListModel<Course>();
		IModel<String> text = Model.of("");
		IModel<List<Course>> selectedModuls = createSelectedModuls(text, moduleOfProf);

		formWahl = new Form<Object>("formWahl");
		
		MarkupContainer container = new WebMarkupContainer("container");
		container.add(createTextField(text, formWahl, selectedModuls));
		moduleOfProf.setObject(module.loadAllWahlCourseOfPath(prof.getObject().getPath()));
		
		container.add(createDropDown(moduleOfProf, prof, createModulParser(module), profEvent));
		
		formPflicht = new Form<Object>("formPflicht");
		formPflicht.add(createPflichListView(module, prof));
		formWahl.add(createListView(selectedModuls, prof));
		container.add(formPflicht);
		container.add(formWahl);
		
		add(container);
	}

	private static CourseParser createModulParser(WahlPflichtModuleLoader module) {
		try {
			List<Course> allModule = module.loadAllWahlCourseOfPath(ALL_PATH);
			return new CourseParser(allModule);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static DropDownChoice<Prof> createDropDown( final IModel<List<Course>> moduleOfProf, final IModel<Prof> selectedProf, CourseParser modulParser, AbstractEvent profEvent) {
		DropDownChoice<Prof> dropDown = new DropDownChoice<Prof>("dropDown", selectedProf, SEARCH_ENGINES);
		dropDown.add(new OnChangeAjaxBehavior() {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				moduleOfProf.setObject(modulParser.parse(selectedProf.getObject().getPath()));
				
				profEvent.setTarget(target);
				dropDown.send(dropDown.getPage(), Broadcast.DEPTH, profEvent);
			}
		});

		return dropDown;
	}


	@Override
	public void onEvent(IEvent<?> event) {
		if (event.getPayload() instanceof AbstractEvent && profEvent.getId() == ((AbstractEvent) event.getPayload()).getId()) {
			((AbstractEvent) event.getPayload()).getTarget().add(formWahl, formPflicht);
			
		} else if(event.getPayload() instanceof RemoveCourseEvent){
			((RemoveCourseEvent) event.getPayload()).getTarget().add(formWahl, formPflicht);			
		}
	}

	
	private static IModel<List<Course>> createPflichtCourseModel(WahlPflichtModuleLoader module, IModel<Prof> prof){
		return new LoadableDetachableModel<List<Course>>() {

			private static final long serialVersionUID = 1L;

			@Override
			protected List<Course> load() {
					return createModulParser(module).parse(prof.getObject().getPflichtModulPath());
			}
		};
	}
	
	
	private static TextField<String> createTextField(IModel<String> text, Form<Object> formWahl, IModel<List<Course>> selectedModuls){
		TextField<String> textField = new TextField<String>("textField", text);
		textField.add(new OnChangeAjaxBehavior() {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(formWahl);
			}

			public void detach(Component component) {
				selectedModuls.detach();
			};

		});
		return textField;
	}
	
	private static ListView<Course> createPflichListView(WahlPflichtModuleLoader module, IModel<Prof> prof){
		return new ListView<Course>("pflichtListView", createPflichtCourseModel(module, prof)){

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<Course> item) {
				item.add(new CoursePflichtButton("modulButton", item.getModelObject(), prof));
			}
			
		};
	}
	
	private static ListView<Course> createListView(IModel<List<Course>> selectedModuls, IModel<Prof> prof){
		return new ListView<Course>("listView", selectedModuls){

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<Course> item) {
				item.add(new CourseButton("modulButton", item.getModelObject(), prof));
			}
			
		};
	}
	
	private static IModel<List<Course>> createSelectedModuls(IModel<String> text, IModel<List<Course>> moduleOfProf) {
		
		return new LoadableDetachableModel<List<Course>>() {

			private static final long serialVersionUID = 1L;

			@Override
			protected List<Course> load() {
				if (text.getObject() != null) {
					return moduleOfProf.getObject().stream().filter(m -> m.getName().toUpperCase().contains(text.getObject().toUpperCase())).collect(Collectors.toList());
				} else {
					return moduleOfProf.getObject();
				}

			}
		};
	}
}
