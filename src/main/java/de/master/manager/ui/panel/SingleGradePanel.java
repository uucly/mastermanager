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
import org.apache.wicket.model.Model;

import com.google.common.base.Optional;

import de.master.manager.profStuff.ICourse;
import de.master.manager.ui.events.GradeChangedEvent;

public class SingleGradePanel extends Panel{

	private static final List<Double> NOTEN_LIST = Arrays.asList(1.0, 1.3, 1.7, 2.0, 2.3, 2.7, 3.0, 3.3, 3.7, 4.0);
	private final List<ICourse> courses;

	public SingleGradePanel(String id, List<ICourse> courses) {
		super(id);
		this.courses = courses;
		setOutputMarkupId(true);
		add(createBasicGradeListView(courses));
	}
	
	
	/* methods */
	private ListView<ICourse> createBasicGradeListView(List<ICourse> basicCourseList) {
		ListView<ICourse> basicCourses = new ListView<ICourse>("courses", basicCourseList) {
			
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

	@Override
	protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);
		if(courses.isEmpty()){
			tag.append("style", "display:none", " ");
		}
	}
}
