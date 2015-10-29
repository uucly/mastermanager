package de.master.manager.noten;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

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




import de.master.manager.model.TransformationModel;
import de.master.manager.profStuff.ModulCourse;
import de.master.manager.profStuff.Prof;

public class NoteProfPanel extends Panel{

	private static final long serialVersionUID = 1L;

	private static final List<Double> NOTEN_LIST = Arrays.asList(1.0, 1.3, 1.7, 2.0, 2.3, 2.7, 3.0, 3.3, 3.7, 4.0);

	public NoteProfPanel(String id, IModel<Prof> profOfPanel) {
		super(id);
		setOutputMarkupId(true);
		IModel<List<ModulCourse>> loadSelectedPflichtCourses = new TransformationModel<Prof, List<ModulCourse>>(profOfPanel, Prof::getSelectedPflichtModuls);
		
		ListView<ModulCourse> pflichtCourseListView = new ListView<ModulCourse>("pflichtCourses", loadSelectedPflichtCourses) {

			@Override
			protected void populateItem(ListItem<ModulCourse> item) {
				ModulCourse currentCourse = item.getModelObject();
				item.add(new Label("pflichtCourse", currentCourse.getName()));
				item.add(createNotenDropDown("dropDownNotenPflicht", item, currentCourse.getNote()));
			}

			
		};
		
		TransformationModel<Prof, List<ModulCourse>> loadSelectedWahlCourses = new TransformationModel<Prof, List<ModulCourse>>(profOfPanel, Prof::getSelectedModuls);
		ListView<ModulCourse> wahlCourseListView = new ListView<ModulCourse>("wahlCourses", loadSelectedWahlCourses) {

			@Override
			protected void populateItem(ListItem<ModulCourse> item) {
				ModulCourse currentCourse = item.getModelObject();
				item.add(new Label("wahlCourse", currentCourse.getName()));
				item.add(createNotenDropDown("dropDownNotenWahl", item, currentCourse.getNote()));
			}
		};
		
		
		add(wahlCourseListView);
		add(pflichtCourseListView);
	}

	
	/* methods */
	private static DropDownChoice<Double> createNotenDropDown(String id, ListItem<ModulCourse> item, Optional<Double> note) {
		IModel<Double> noteModel = note.isPresent() ? Model.of(note.get()) : Model.of(); 
		DropDownChoice<Double> dropDownNoten = new DropDownChoice<Double>(id, noteModel, NOTEN_LIST);
		dropDownNoten.add(new OnChangeAjaxBehavior() {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				Double selectedGrade = noteModel.getObject();
				Optional<Double> grade = selectedGrade == null ? Optional.empty() : Optional.of(selectedGrade);
				item.getModelObject().setNote(grade);
				getComponent().send(getComponent().getPage(), Broadcast.DEPTH, new GradeChangedEvent(target));
			}
		});
		return dropDownNoten;
	}
}
