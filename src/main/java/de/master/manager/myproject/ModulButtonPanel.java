package de.master.manager.myproject;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
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


public class ModulButtonPanel extends Panel {

	private static final long serialVersionUID = 1L;
	
	private Form<Object> formWahl;
	private Form<?> formPflicht;
	
	// TODO replace ProfCHangedEvent with EventInjector
	public ModulButtonPanel(String id, final IModel<Prof> profLeft, IModel<Prof> profRight, List<Prof> allProfs, final WahlPflichtModuleLoader courseLoader) {
		super(id);
		setOutputMarkupId(true);
		IModel<String> text = Model.of("");
		
		formWahl = new Form<Object>("formWahl");
		formPflicht = new Form<Object>("formPflicht");
		
		MarkupContainer container = new WebMarkupContainer("container");
		container.add(createTextField(text, formWahl));
		container.add(createDropDown(profLeft, allProfs));
		
		formPflicht.add(createPflichListView(loadPflichtCourses(profLeft), profLeft, allProfs));
		formWahl.add(createWahlListView(loadWahlCourses(text, profLeft, courseLoader), profLeft, profRight, allProfs));
		container.add(formPflicht);
		container.add(formWahl);
		
		add(container);
	}
	
	
	private IModel<List<Course>> loadPflichtCourses(final IModel<Prof> prof) {
		return new LoadableDetachableModel<List<Course>>() {

			@Override
			protected List<Course> load() {
				return prof.getObject().getPflichtCourse();
			}
		};
	}


	private static DropDownChoice<Prof> createDropDown(final IModel<Prof> selectedProf, List<Prof> allProfs) {
		final DropDownChoice<Prof> dropDown = new DropDownChoice<Prof>("dropDown", selectedProf, allProfs);
		dropDown.add(new OnChangeAjaxBehavior() {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				dropDown.send(dropDown.getPage(), Broadcast.DEPTH, new ProfChangedEvent(target));
			}
		});

		return dropDown;
	}


	@Override
	public void onEvent(IEvent<?> event) {
		if (event.getPayload() instanceof ProfChangedEvent) {
			((ProfChangedEvent) event.getPayload()).getTarget().add(formWahl, formPflicht);
		} else if(event.getPayload() instanceof RemoveCourseEvent){
			((RemoveCourseEvent) event.getPayload()).getTarget().add(formWahl, formPflicht);			
		}
	}
	
	private static TextField<String> createTextField(IModel<String> text, final Form<Object> formWahl){
		TextField<String> textField = new TextField<String>("textField", text);
		textField.add(new OnChangeAjaxBehavior() {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(formWahl);
			}
		});
		return textField;
	}
	
	private static ListView<Course> createPflichListView(IModel<List<Course>> pflichtCourses, final IModel<Prof> prof, final List<Prof> allProfs){
		return new ListView<Course>("pflichtListView", pflichtCourses){
			private static final long serialVersionUID = 1L;
			@Override
			protected void populateItem(ListItem<Course> item) {
				item.add(new CoursePflichtButton("modulButton", item.getModelObject(), prof, allProfs));
			}
			
		};
	}
	
	private static ListView<Course> createWahlListView(IModel<List<Course>> selectedModuls, final IModel<Prof> profLeft, final IModel<Prof> profRight, final List<Prof> allProfs){
		return new ListView<Course>("listView", selectedModuls){
			private static final long serialVersionUID = 1L;
			@Override
			protected void populateItem(ListItem<Course> item) {
				item.add(new CourseButton("modulButton", item.getModelObject(), profLeft, profRight, allProfs));
			}
			
		};
	}
	
	private static IModel<List<Course>> loadWahlCourses(final IModel<String> text, final IModel<Prof> prof, final WahlPflichtModuleLoader courseLoader) {
		return new LoadableDetachableModel<List<Course>>() {
			private static final long serialVersionUID = 1L;
			@Override
			protected List<Course> load() {
				InputStream resource = getClass().getResourceAsStream(prof.getObject().getPath());
				List<Course> coursesOfProf;
				try {
					coursesOfProf = courseLoader.loadCourseOfProf(IOUtils.toString(resource,"UTF-8"));
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				
				if (text.getObject() != null) {
					return coursesOfProf.stream().filter(m -> m.getName().toUpperCase().contains(text.getObject().toUpperCase())).collect(Collectors.toList());
				} else {
					return coursesOfProf;
				}
			}
		};
	}
}
