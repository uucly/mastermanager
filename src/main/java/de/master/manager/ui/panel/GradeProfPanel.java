package de.master.manager.ui.panel;

import java.util.Arrays;
import java.util.List;
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
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import com.google.common.base.Optional;

import de.master.manager.profStuff.ICourse;
import de.master.manager.profStuff.Prof;
import de.master.manager.ui.events.GradeChangedEvent;
import de.master.manager.ui.events.ProfChangedEvent;
import de.master.manager.ui.model.SerializableFunction;

public class GradeProfPanel extends Panel {

	private static final long serialVersionUID = 1L;

	private static final List<Double> NOTEN_LIST = Arrays.asList(1.0, 1.3, 1.7, 2.0, 2.3, 2.7, 3.0, 3.3, 3.7, 4.0);

	private final IModel<Prof> profOfPanel;

	public GradeProfPanel(String id, final IModel<Prof> profOfPanel) {
		super(id);
		setOutputMarkupId(true);
		this.profOfPanel = profOfPanel;
		add(new Label("averagePflicht",
				loadAverageGrade(profOfPanel, new SerializableFunction<Prof, Optional<Double>>() {

					@Override
					public Optional<Double> apply(Prof prof) {
						return prof.calculateFinalPflichtGrade();
					}
				})));
		add(new Label("averageWahl", loadAverageGrade(profOfPanel, new SerializableFunction<Prof, Optional<Double>>() {

			@Override
			public Optional<Double> apply(Prof prof) {
				return prof.calculateFinalWahlGrade();
			}
		})));
		add(createCourseListView("wahl", new LoadableDetachableModel<List<ICourse>>() {

			@Override
			protected List<ICourse> load() {
				return profOfPanel.getObject().getWahlModulSelected();
			}
		}));
		add(createCourseListView("pflicht",
				new LoadableDetachableModel<List<ICourse>>() {

			@Override
			protected List<ICourse> load() {
				return profOfPanel.getObject().getPflichtModulSelected();
			}
		}));
	}

	/* methods */
	private IModel<String> loadAverageGrade(final IModel<Prof> prof,
			final SerializableFunction<Prof, Optional<Double>> calcualteGrade) {
		return new LoadableDetachableModel<String>() {

			@Override
			protected String load() {
				return calcualteGrade.apply(prof.getObject()).isPresent()
						? String.valueOf(Math.round(calcualteGrade.apply(prof.getObject()).get() * 100.) / 100.)
						: "keine Note eingetragen";
			}
		};
	}

	private static ListView<ICourse> createCourseListView(final String courseType,
			IModel<List<ICourse>> selectedWahlCourses) {
		ListView<ICourse> wahlCourseListView = new ListView<ICourse>(courseType + "Courses", selectedWahlCourses) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<ICourse> item) {
				ICourse currentCourse = item.getModelObject();
				item.add(new Label(courseType + "Course",
						currentCourse.getName() + " (CP: " + currentCourse.getPoints() + ")"));
				item.add(createNotenDropDown(courseType + "DropDownGrade", item, currentCourse.getGrade()));
			}
		};
		return wahlCourseListView;
	}

	private static DropDownChoice<Double> createNotenDropDown(String id, final ListItem<ICourse> item,
			Optional<Double> note) {
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

	@Override
	protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);
		Prof prof = profOfPanel.getObject();
		if (prof.getWahlModulSelected().isEmpty() && prof.getPflichtModulSelected().isEmpty()) {
			tag.append("style", "display:none", " ");
		}
	}

	@Override
	public void onEvent(IEvent<?> event) {
		Object payload = event.getPayload();
		if (payload instanceof ProfChangedEvent) {
			((ProfChangedEvent) payload).getTarget().add(this);
		}
	}

}
