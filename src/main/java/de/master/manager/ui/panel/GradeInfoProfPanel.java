package de.master.manager.ui.panel;

import java.util.OptionalDouble;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import de.master.manager.profStuff.ICourse;
import de.master.manager.profStuff.Prof;

public class GradeInfoProfPanel extends Panel{

	private static final long serialVersionUID = 1L;

	public GradeInfoProfPanel(String id, IModel<Prof> profOfPanel) {
		super(id);
		
		OptionalDouble pflichtGrade = profOfPanel.getObject().calculateFinalPflichtGrade();
		OptionalDouble wahlGrade = profOfPanel.getObject().calculateFinalWahlGrade();
		
		add(new Label("wahlAverage", wahlGrade.isPresent() ? String.valueOf(wahlGrade.getAsDouble()) : ""));
		add(new Label("pflichtAverage", pflichtGrade.isPresent() ? String.valueOf(pflichtGrade.getAsDouble()) : ""));
		
		add(createWahlListView(profOfPanel));
		add(createPflichtListView(profOfPanel));
	}

	/* methods */
	private ListView<ICourse> createPflichtListView(IModel<Prof> profOfPanel) {
		ListView<ICourse> pflichtView = new ListView<ICourse>("pflichtList", profOfPanel.getObject().getSelectedPflichtModuls()) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<ICourse> item) {
				ICourse course = item.getModelObject();
				item.add(new Label("pflichtName", course.getName()));
				item.add(createNoteLabel("pflichtNote", course));
				
			}
		};
		return pflichtView;
	}

	private ListView<ICourse> createWahlListView(IModel<Prof> profOfPanel) {
		ListView<ICourse> wahlListView = new ListView<ICourse>("wahlList", profOfPanel.getObject().getSelectedModuls()) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<ICourse> item) {
				ICourse course = item.getModelObject();
				item.add(new Label("wahlName", course.getName()));
				item.add(createNoteLabel("wahlNote", course));
			}
		};
		return wahlListView;
	}

	private static final Label createNoteLabel(String id, ICourse course){
		return new Label(id, course.getGrade().isPresent() ? course.getGrade().get().toString() : "");
	}
}
