package dragAndDrop;

import java.beans.Transient;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;

import com.modul.Course;
import com.modul.RemoveCourseEvent;
import com.modul.WahlPflichtModuleLoader;
import com.professoren.Prof;

public class ModulButtonPanel extends Panel {

	private static final long serialVersionUID = 1L;
	
	private Form<Object> formWahl;
	private Form<?> formPflicht;
	private AbstractEvent profEvent;
	
	// TODO replace ProfCHangedEvent with EventInjector
	public ModulButtonPanel(String id, AbstractEvent profEvent, final IModel<Prof> prof, IModel<Prof> profRight, List<Prof> allProfs, final WahlPflichtModuleLoader courseLoader) {
		super(id);
		setOutputMarkupId(true);
		this.profEvent = profEvent;
		IModel<String> text = Model.of("");
		
		formWahl = new Form<Object>("formWahl");
		
		IModel<List<Course>> moduleOfProf = new LoadableDetachableModel<List<Course>>() {

			@Override
			protected List<Course> load() {
				return courseLoader.loadCourseOfProf(prof.getObject().getPath());
			}
		};
		IModel<List<Course>> selectedModuls = createSelectedModuls(text, moduleOfProf);

		MarkupContainer container = new WebMarkupContainer("container");
		container.add(createTextField(text, formWahl, selectedModuls));
		container.add(createDropDown(prof, profEvent, allProfs));
		
		formPflicht = new Form<Object>("formPflicht");
		formPflicht.add(createPflichListView(courseLoader, prof, allProfs));
		formWahl.add(createListView(selectedModuls, prof, allProfs));
		container.add(formPflicht);
		container.add(formWahl);
		
		add(container);
	}
	
	
	private static DropDownChoice<Prof> createDropDown(final IModel<Prof> selectedProf, AbstractEvent profEvent, List<Prof> allProfs) {
		DropDownChoice<Prof> dropDown = new DropDownChoice<Prof>("dropDown", selectedProf, allProfs);
		dropDown.add(new OnChangeAjaxBehavior() {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
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
					return module.loadCourseOfProf(prof.getObject().getPflichtModulPath());
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
	
	private static ListView<Course> createPflichListView(WahlPflichtModuleLoader module, IModel<Prof> prof, List<Prof> allProfs){
		return new ListView<Course>("pflichtListView", createPflichtCourseModel(module, prof)){
			private static final long serialVersionUID = 1L;
			@Override
			protected void populateItem(ListItem<Course> item) {
				item.add(new CoursePflichtButton("modulButton", item.getModelObject(), prof, allProfs));
			}
			
		};
	}
	
	private static ListView<Course> createListView(IModel<List<Course>> selectedModuls, IModel<Prof> prof, List<Prof> allProfs){
		return new ListView<Course>("listView", selectedModuls){
			private static final long serialVersionUID = 1L;
			@Override
			protected void populateItem(ListItem<Course> item) {
				item.add(new CourseButton("modulButton", item.getModelObject(), prof, allProfs));
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
