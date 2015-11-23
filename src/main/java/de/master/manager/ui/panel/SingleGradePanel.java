package de.master.manager.ui.panel;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;

import com.google.common.base.Optional;

import de.master.manager.profStuff.ICourse;
import de.master.manager.profStuff.IModul;
import de.master.manager.ui.events.GradeChangedEvent;

public abstract class SingleGradePanel extends Panel{

	private static final List<Double> NOTEN_LIST = Arrays.asList(1.0, 1.3, 1.7, 2.0, 2.3, 2.7, 3.0, 3.3, 3.7, 4.0);
	private final IModul modul;

	public SingleGradePanel(String id, String name, final IModul modul) {
		super(id);
		this.modul = modul;
		setOutputMarkupId(true);
		
		add(new Label("panelName", Model.of(name)));
		add(createBasicGradeListView(modul));
		
		IModel<String> loadGrade = new LoadableDetachableModel<String>() {
			@Override
			protected String load() {
				return calculateFinalGrade(modul);
			}
		};
		add(new Label("average", loadGrade));
	}
	
	
	/* methods */
	private ListView<ICourse> createBasicGradeListView(IModul modul) {
		ListView<ICourse> basicCourses = new ListView<ICourse>("courses", modul) {
			
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<ICourse> item) {
				ICourse currentCourse = item.getModelObject();
				item.add(new Label("course", currentCourse.getName() + " (CP: " + currentCourse.getPoints() + ")"));
				item.add(createNotenDropDown("dropDownGrade", item, currentCourse.getGrade()));
			
			}
		};
		return basicCourses;
	}
	
	
	private static DropDownChoice<Double> createNotenDropDown(String id, final ListItem<ICourse> item, Optional<Double> note) {
		final IModel<Double> noteModel = note.isPresent() ? Model.of(note.get()) : Model.<Double>of();
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

	private static final String calculateFinalGrade(IModul modul){
		Optional<Double> grade = modul.calculateGrade();
		return grade.isPresent() ? String.valueOf(round(grade.get())) : "keine Note eingetragen";
	}
	
	private static final double round(double number){
		return Math.round(number*100.0)/100.0;
	}
	
	@Override
	protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);
		if(modul.isEmpty()){
			tag.append("style", "display:none", " ");
		}
	}
}
