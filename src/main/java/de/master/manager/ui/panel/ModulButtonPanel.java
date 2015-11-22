package de.master.manager.ui.panel;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;

import de.master.manager.profStuff.ICourse;
import de.master.manager.profStuff.IModulLoader;
import de.master.manager.profStuff.Prof;
import de.master.manager.ui.button.CourseButton;
import de.master.manager.ui.button.CoursePflichtButton;
import de.master.manager.ui.events.ProfChangedEvent;
import de.master.manager.ui.events.RemoveCourseEvent;
import de.master.manager.ui.model.TransformationModel;
import de.master.manager.ui.model.TransformationModel2;


public class ModulButtonPanel extends Panel {

	private static final long serialVersionUID = 1L;
	
	private Form<Object> formWahl;
	private Form<?> formPflicht;
	
	public ModulButtonPanel(String id, final IModel<Prof> profOfThisPanel, IModel<Prof> profOfOtherPanel, List<Prof> allProfs, final IModulLoader courseLoader) {
		super(id);
		setOutputMarkupId(true);
		IModel<String> text = Model.of("");
		
		formWahl = new Form<Object>("formWahl");
		formPflicht = new Form<Object>("formPflicht");
		
		MarkupContainer container = new WebMarkupContainer("container");
		container.add(createTextField(text, formWahl));
		
		formPflicht.add(createPflichListView(new TransformationModel<>(profOfThisPanel, p -> p.getPflichtModul()), profOfThisPanel, allProfs));
		formWahl.add(createWahlListView(loadWahlCourses(text, profOfThisPanel, courseLoader), profOfThisPanel, profOfOtherPanel, allProfs));
		formPflicht.add(new Label("choosenProf", new TransformationModel<>(profOfThisPanel, p -> p.getName())));
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
	
	private static ListView<ICourse> createPflichListView(IModel<List<ICourse>> pflichtCourses, final IModel<Prof> prof, final List<Prof> allProfs){
		return new ListView<ICourse>("pflichtListView", pflichtCourses){
			private static final long serialVersionUID = 1L;
			@Override
			protected void populateItem(ListItem<ICourse> item) {
				ICourse currentCourse=item.getModelObject();
				item.add(new CoursePflichtButton("modulButton", currentCourse, prof.getObject().getPflichtModulSelected(), prof, allProfs));
				String points=String.valueOf(currentCourse.getPoints());
				item.add(new Button("modulPoints", new Model<String>(points)));
			}
			
		};
	}
	
	private static ListView<ICourse> createWahlListView(IModel<List<ICourse>> selectedModuls, final IModel<Prof> profLeft, final IModel<Prof> profRight, final List<Prof> allProfs){
		return new ListView<ICourse>("listView", selectedModuls){
			private static final long serialVersionUID = 1L;
			@Override
			protected void populateItem(ListItem<ICourse> item) {
				ICourse currentCourse=item.getModelObject();
				item.add(new CourseButton("modulButton", currentCourse, profLeft.getObject().getWahlModulSelected(), profLeft, profRight, allProfs));
				String points=String.valueOf(currentCourse.getPoints());
				item.add(new Button("modulPoints", new Model<String>(points)));
			}
			
		};
	}
	
	private static IModel<List<ICourse>> loadWahlCourses(final IModel<String> text, final IModel<Prof> prof, final IModulLoader courseLoader) {
		return new LoadableDetachableModel<List<ICourse>>() {
			private static final long serialVersionUID = 1L;
			@Override
			protected List<ICourse> load() {
				List<ICourse> coursesOfProf = courseLoader.loadModul(prof.getObject().getPath());
				coursesOfProf.sort((c1,c2) -> c1.getName().compareTo(c2.getName()));
				if (text.getObject() != null) {
					return coursesOfProf.stream().filter(m -> m.getName().toUpperCase().contains(text.getObject().toUpperCase())).collect(Collectors.toList());
				} else {
					return coursesOfProf;
				}
			}
		};
	}
}
