package de.master.manager.noten;


import java.util.Arrays;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
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

	private static final List<Double> NOTEN_LIST = Arrays.asList(1.0, 1.3, 1.7, 2.0, 2.3, 2.7, 3.0, 3.3, 3.7, 4.0);

	public NoteProfPanel(String id, IModel<Prof> profOfPanel) {
		super(id);
		IModel<List<Course>> loadSelectedPflichtCourses = new TransformationModel<Prof, List<Course>>(profOfPanel, Prof::getSelectedPflichtModuls);
		
		ListView<Course> pflichtCourseListView = new ListView<Course>("pflichtCourses", loadSelectedPflichtCourses) {

			@Override
			protected void populateItem(ListItem<Course> item) {
				item.add(new Label("pflichtCourse", item.getModelObject().getName()));
				DropDownChoice<Double> dropDownNoten = new DropDownChoice<Double>("dropDownNotenPflicht", NOTEN_LIST);
				item.add(dropDownNoten);
			}
		};
		
		TransformationModel<Prof, List<Course>> loadSelectedWahlCourses = new TransformationModel<Prof, List<Course>>(profOfPanel, Prof::getSelectedModuls);
		ListView<Course> wahlCourseListView = new ListView<Course>("wahlCourses", loadSelectedWahlCourses) {

			@Override
			protected void populateItem(ListItem<Course> item) {
				item.add(new Label("wahlCourse", item.getModelObject().getName()));
				DropDownChoice<Double> dropDownNoten = new DropDownChoice<Double>("dropDownNotenWahl", NOTEN_LIST);
				item.add(dropDownNoten);
			}
		};
		
		
		add(wahlCourseListView);
		add(pflichtCourseListView);
	}

}
