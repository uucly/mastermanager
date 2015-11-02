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

import de.master.manager.events.ProfChangedEvent;
import de.master.manager.events.RemoveCourseEvent;
import de.master.manager.model.TransformationModel;
import de.master.manager.profStuff.Course;
import de.master.manager.profStuff.Prof;
import de.master.manager.profStuff.WahlPflichtModuleLoader;


public class ModulButtonPanel extends Panel {

	private static final long serialVersionUID = 1L;
	
	private Form<Object> formWahl;
	private Form<?> formPflicht;
	
	public ModulButtonPanel(String id, final IModel<Prof> profOfThisPanel, IModel<Prof> profOfOtherPanel, List<Prof> allProfs, final WahlPflichtModuleLoader courseLoader) {
		super(id);
		setOutputMarkupId(true);
		IModel<String> text = Model.of("");
		
		formWahl = new Form<Object>("formWahl");
		formPflicht = new Form<Object>("formPflicht");
		
		MarkupContainer container = new WebMarkupContainer("container");
		container.add(createTextField(text, formWahl));
		
		formPflicht.add(createPflichListView(new TransformationModel<Prof, List<Course>>(profOfThisPanel, p -> p.getPflichtCourse()), profOfThisPanel, allProfs));
		formWahl.add(createWahlListView(loadWahlCourses(text, profOfThisPanel, courseLoader), profOfThisPanel, profOfOtherPanel, allProfs));
		container.add(formPflicht);
		container.add(formWahl);
		
		add(container);
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
				Course currentCourse=item.getModelObject();
				item.add(new CoursePflichtButton("modulButton", currentCourse, prof, allProfs));
				String points=String.valueOf(currentCourse.getPoints());
				item.add(new Button("modulPoints", new Model<String>(points)));
			}
			
		};
	}
	
	private static ListView<Course> createWahlListView(IModel<List<Course>> selectedModuls, final IModel<Prof> profLeft, final IModel<Prof> profRight, final List<Prof> allProfs){
		return new ListView<Course>("listView", selectedModuls){
			private static final long serialVersionUID = 1L;
			@Override
			protected void populateItem(ListItem<Course> item) {
				Course currentCourse=item.getModelObject();
				item.add(new CourseButton("modulButton", currentCourse, profLeft, profRight, allProfs));
				String points=String.valueOf(currentCourse.getPoints());
				item.add(new Button("modulPoints", new Model<String>(points)));
				
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
