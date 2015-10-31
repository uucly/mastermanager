package de.master.manager.noten;

import java.util.OptionalDouble;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import de.master.manager.profStuff.ModulCourse;
import de.master.manager.profStuff.Prof;

public class NoteInfoProfPanel extends Panel{

	private static final long serialVersionUID = 1L;

	public NoteInfoProfPanel(String id, IModel<Prof> profOfPanel) {
		super(id);
		
		ListView<ModulCourse> pflichtView = new ListView<ModulCourse>("pflichtList", profOfPanel.getObject().getSelectedPflichtModuls()) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<ModulCourse> item) {
				ModulCourse course = item.getModelObject();
				item.add(new Label("pflichtName", course.getName()));
				item.add(new Label("pflichtNote", course.getNote().isPresent() ? course.getNote().get().toString() : ""));
			}
		};
		OptionalDouble pflichtGrade = profOfPanel.getObject().calculateFinalPflichtGrade();
		add(new Label("pflichtAverage", pflichtGrade.isPresent() ? String.valueOf(pflichtGrade.getAsDouble()) : ""));
		
		ListView<ModulCourse> wahlListView = new ListView<ModulCourse>("wahlList", profOfPanel.getObject().getSelectedModuls()) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<ModulCourse> item) {
				ModulCourse course = item.getModelObject();
				item.add(new Label("wahlName", course.getName()));
				item.add(new Label("wahlNote", course.getNote().isPresent() ? course.getNote().get().toString() : ""));
			}
		};
		OptionalDouble wahlGrade = profOfPanel.getObject().calculateFinalWahlGrade();
		add(new Label("wahlAverage", wahlGrade.isPresent() ? String.valueOf(wahlGrade.getAsDouble()) : ""));
		
		add(wahlListView);
		add(pflichtView);
	}

}
