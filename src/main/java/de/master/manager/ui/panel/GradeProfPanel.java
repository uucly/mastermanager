package de.master.manager.ui.panel;

import java.util.Arrays;
import java.util.List;
import java.util.OptionalDouble;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;

import com.google.common.base.Optional;

import de.master.manager.profStuff.ICourse;
import de.master.manager.profStuff.Prof;
import de.master.manager.ui.events.GradeChangedEvent;
import de.master.manager.ui.events.ProfChangedEvent;
import de.master.manager.ui.model.SerializableFunction;
import de.master.manager.ui.model.TransformationModel;

public class GradeProfPanel extends Panel {

	private static final long serialVersionUID = 1L;

	private static final List<Double> NOTEN_LIST = Arrays.asList(1.0, 1.3, 1.7, 2.0, 2.3, 2.7, 3.0, 3.3, 3.7, 4.0);

	private final IModel<Prof> profOfPanel;

	public GradeProfPanel(String id, IModel<Prof> profOfPanel) {
		super(id);
		setOutputMarkupId(true);
		this.profOfPanel = profOfPanel;
		add(new Label("averagePflicht", loadAverageGrade(profOfPanel, Prof::calculateFinalPflichtGrade)));
		add(new Label("averageWahl", loadAverageGrade(profOfPanel, Prof::calculateFinalWahlGrade)));
		add(createCourseListView("wahl", new TransformationModel<Prof, List<ICourse>>(profOfPanel, Prof::getWahlModulSelected)));
		add(createCourseListView("pflicht", new TransformationModel<Prof, List<ICourse>>(profOfPanel, Prof::getPflichtModulSelected)));
	}

	/* methods */
	private IModel<String> loadAverageGrade(IModel<Prof> prof, SerializableFunction<Prof, OptionalDouble> calcualteGrade) {
		return new TransformationModel<Prof, String>(prof, p -> calcualteGrade.apply(p).isPresent()? String.valueOf(Math.round(calcualteGrade.apply(p).getAsDouble()*100.)/100.) : "keine Note eingetragen");
	}

	private static ListView<ICourse> createCourseListView(String courseType, IModel<List<ICourse>> selectedWahlCourses) {
		ListView<ICourse> wahlCourseListView = new ListView<ICourse>(courseType + "Courses", selectedWahlCourses) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<ICourse> item) {
				ICourse currentCourse = item.getModelObject();
				item.add(new Label(courseType + "Course", currentCourse.getName() + " (CP: " + currentCourse.getPoints() + ")"));
				item.add(createNotenDropDown(courseType + "DropDownGrade", item, currentCourse.getGrade()));
			}
		};
		return wahlCourseListView;
	}

	private static DropDownChoice<Double> createNotenDropDown(String id, ListItem<ICourse> item, Optional<Double> note) {
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
	
	@Override
	protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);
		Prof prof = profOfPanel.getObject();
		if(prof.getWahlModulSelected().isEmpty() && prof.getPflichtModulSelected().isEmpty()){
			tag.append("style" , "display:none", " ");
		}
	}
	
	@Override
	public void onEvent(IEvent<?> event) {
		Object payload = event.getPayload();
		if(payload instanceof ProfChangedEvent){
			((ProfChangedEvent) payload).getTarget().add(this);
		}
	}

}
