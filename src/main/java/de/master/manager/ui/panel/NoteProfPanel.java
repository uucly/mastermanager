package de.master.manager.ui.panel;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.google.common.base.Optional;

import de.master.manager.profStuff.ICourse;
import de.master.manager.profStuff.Prof;
import de.master.manager.ui.events.GradeChangedEvent;
import de.master.manager.ui.model.TransformationModel;

public class NoteProfPanel extends Panel {

	private static final long serialVersionUID = 1L;

	private static final List<Double> NOTEN_LIST = Arrays.asList(1.0, 1.3, 1.7, 2.0, 2.3, 2.7, 3.0, 3.3, 3.7, 4.0);

	public NoteProfPanel(String id, IModel<Prof> profOfPanel) {
		super(id);
		setOutputMarkupId(true);
		IModel<List<ICourse>> loadSelectedPflichtCourses = new TransformationModel<Prof, List<ICourse>>(profOfPanel,
				Prof::getSelectedPflichtModuls);
		IModel<List<ICourse>> loadSelectedWahlCourses = new TransformationModel<Prof, List<ICourse>>(profOfPanel,
				Prof::getSelectedModuls);

		add(createWahlCourseListView(loadSelectedWahlCourses));
		add(createPflichtCourseListView(loadSelectedPflichtCourses));
	}

	/* methods */

	private static ListView<ICourse> createWahlCourseListView(IModel<List<ICourse>> loadSelectedWahlCourses) {
		ListView<ICourse> wahlCourseListView = new ListView<ICourse>("wahlCourses", loadSelectedWahlCourses) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<ICourse> item) {
				ICourse currentCourse = item.getModelObject();
				item.add(new Label("wahlCourse", currentCourse.getName() + " (CP: " + currentCourse.getPoints() + ")"));
				item.add(createNotenDropDown("dropDownNotenWahl", item, currentCourse.getGrade()));
			}
		};
		return wahlCourseListView;
	}

	private static ListView<ICourse> createPflichtCourseListView(IModel<List<ICourse>> loadSelectedPflichtCourses) {
		ListView<ICourse> pflichtCourseListView = new ListView<ICourse>("pflichtCourses", loadSelectedPflichtCourses) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<ICourse> item) {
				ICourse currentCourse = item.getModelObject();
				item.add(new Label("pflichtCourse",
						currentCourse.getName() + " (CP: " + currentCourse.getPoints() + ")"));
				item.add(createNotenDropDown("dropDownNotenPflicht", item, currentCourse.getGrade()));
			}
		};
		return pflichtCourseListView;
	}

	private static DropDownChoice<Double> createNotenDropDown(String id, ListItem<ICourse> item,
			Optional<Double> note) {
		IModel<Double> noteModel = note.isPresent() ? Model.of(note.get()) : Model.of();
		DropDownChoice<Double> dropDownNoten = new DropDownChoice<Double>(id, noteModel, NOTEN_LIST);
		dropDownNoten.add(new OnChangeAjaxBehavior() {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				item.getModelObject().setGrade(noteModel.getObject());
				getComponent().send(getComponent().getPage(), Broadcast.DEPTH, new GradeChangedEvent(target));
			}
		});
		return dropDownNoten;
	}
}
