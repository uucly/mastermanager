package de.master.manager.noten;


import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import de.master.manager.model.TransformationModel;
import de.master.manager.profStuff.Course;
import de.master.manager.profStuff.Prof;

public class NoteProfPanel extends Panel{

	private static final long serialVersionUID = 1L;

	public NoteProfPanel(String id, IModel<Prof> profOfPanel) {
		super(id);
		IModel<List<Course>> loadSelectedPflichtCourses = new TransformationModel<Prof, List<Course>>(profOfPanel, Prof::getPflichtCourse);
		
		ListView<Course> pflichtCourseListView = new ListView<Course>("pflichtCourses", loadSelectedPflichtCourses) {

			@Override
			protected void populateItem(ListItem<Course> item) {
				item.add(new Label("pflichtCourse", item.getModelObject().getName()));
			}
		};
		
		TransformationModel<Prof, List<Course>> loadSelectedWahlCourses = new TransformationModel<Prof, List<Course>>(profOfPanel, Prof::getSelectedModuls);
		ListView<Course> wahlCourseListView = new ListView<Course>("wahlCourses", loadSelectedWahlCourses) {

			@Override
			protected void populateItem(ListItem<Course> item) {
				item.add(new Label("wahlCourse", item.getModelObject().getName()));
			}
		};
		add(wahlCourseListView);
		add(pflichtCourseListView);
	}

}
