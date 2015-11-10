package de.master.manager.ui.panel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;

import com.google.common.collect.Lists;

import de.agilecoders.wicket.core.markup.html.bootstrap.components.progress.ProgressBar;
import de.master.manager.profStuff.ICourse;
import de.master.manager.profStuff.Prof;
import de.master.manager.profStuff.SupplementCourses;
import de.master.manager.ui.events.AbstractEvent;
import de.master.manager.ui.events.PanelChangedEvent;
import de.master.manager.ui.events.RemoveCourseEvent;
import de.master.manager.ui.events.SelectedEvent;
import de.master.manager.ui.model.TransformationModel;



public class InfoPanel extends Panel{

	private static final long serialVersionUID = 1L;
	private static final double MAX_POINTS = 23;
	
	private final IModel<Integer> wahlPoints = Model.of(0);
	private final IModel<Integer> pflichtPoints = Model.of(0);
	private final IModel<Integer> aufbauPoints = Model.of(0);
	private final IModel<Integer> supplementPoints = Model.of(0);
	
	private final IModel<List<Prof>> profs;
	private final Form<Object> form;
	private final IModel<List<ICourse>> allCurrentSelectedModuls;
	private final IModel<List<ICourse>> allSelectedModuls;
	private final IModel<List<ICourse>> loadSupplementCourses;
	
	public InfoPanel(String id, final IModel<List<Prof>> profs,  final List<Prof> allProfs) {
		super(id);
		setOutputMarkupId(true);
		this.profs = profs;
		
		loadSupplementCourses = new TransformationModel<List<Prof>, List<ICourse>>(profs, list -> {
			Optional<Prof> optionalProf = list.stream().findFirst();
			return optionalProf.isPresent() ? optionalProf.get().getSupplementCourses().getAllCourses() : Collections.emptyList();
		});

		form = new Form<Object>("form");
		wahlPoints.setObject(calculatePoints(profs, Prof::calculateWahlPoints));
		pflichtPoints.setObject(calculatePoints(profs, Prof::calculatePflichtPoints));
		
		supplementPoints.setObject((int)Math.round(loadSupplementPoints(profs)));
		
		ProgressBar wahlProgressBar = createProgressBar("wahlProgress", wahlPoints);
		ProgressBar pflichtProgressBar = createProgressBar("pflichtProgress", pflichtPoints);
		ProgressBar aufbauProgressBar = createProgressBar("aufbauProgress", aufbauPoints);
		ProgressBar supplementProgressBar = createProgressBar("supplementProgress", supplementPoints);
		
		allSelectedModuls = new LoadableDetachableModel<List<ICourse>>() {

			private static final long serialVersionUID = 1L;

			@Override
			protected List<ICourse> load() {
				List<ICourse> list = Lists.newArrayList();
				allProfs.stream().forEach(p->list.addAll(p.getSelectedModuls()));
				allProfs.stream().forEach(p->list.addAll(p.getSelectedPflichtModuls()));
				list.addAll(loadSupplementCourses.getObject());
				return list;
			}
		};
		allCurrentSelectedModuls = new LoadableDetachableModel<List<ICourse>>() {
			
			private static final long serialVersionUID = 1L;

			@Override
			protected List<ICourse> load() {
				List<ICourse> list = Lists.newArrayList();
				profs.getObject().stream().forEach(m -> list.addAll(m.getSelectedModuls()));
				profs.getObject().stream().forEach(m -> list.addAll(m.getSelectedPflichtModuls()));
				list.addAll(loadSupplementCourses.getObject());
				
				return list;
			}
			
		};
		
        ListView<ICourse> modulListView = createListView(allSelectedModuls, allCurrentSelectedModuls, profs, allProfs);
		form.add(modulListView);
        form.add(wahlProgressBar);
        form.add(pflichtProgressBar);
        form.add(aufbauProgressBar);
        form.add(supplementProgressBar);
		add(form);
			
	}

	private double loadSupplementPoints(final IModel<List<Prof>> profs) {
		Optional<Double> optionalSupplementPoints = profs.getObject().stream().findFirst().map(p -> p.calculateSupplementPoints());
		double points = optionalSupplementPoints.isPresent() ? optionalSupplementPoints.get() : 0;
		return points;
	}

	private static int calculatePoints(IModel<List<Prof>> profs, Function<Prof, Double> calculate){
		return (int)Math.round(profs.getObject().stream().mapToDouble(p -> calculate.apply(p)).sum());
	}
	
	private static int calculatePoints(double points){
		return (int)Math.round(points);
	}
	
	
	
	@Override
	public void onEvent(IEvent<?> event) {
		super.onEvent(event);
		int summary = calculatePoints(profs, Prof::calculateWahlPoints);
		int summaryPflicht = calculatePoints(profs, Prof::calculatePflichtPoints);
		int summarySupplement = calculatePoints(loadSupplementCourses.getObject().stream().mapToDouble(ICourse::getPoints).sum());
		if(event.getPayload() instanceof SelectedEvent){
			setPoints(summary, wahlPoints);
			setPoints(summaryPflicht, pflichtPoints);
			setPoints(summarySupplement, supplementPoints);
			((SelectedEvent) event.getPayload()).getTarget().add(form);
		} else if(event.getPayload() instanceof AbstractEvent){
			setPoints(summary, wahlPoints);
			setPoints(summaryPflicht, pflichtPoints);
			setPoints(summarySupplement, supplementPoints);
			((AbstractEvent) event.getPayload()).getTarget().add(form);
		} else if(event.getPayload() instanceof RemoveCourseEvent){
			setPoints(summary, wahlPoints);
			setPoints(summaryPflicht, pflichtPoints);
			setPoints(summarySupplement, supplementPoints);
			((RemoveCourseEvent) event.getPayload()).getTarget().add(form);
		} else if(event.getPayload() instanceof PanelChangedEvent){
			setPoints(summary, wahlPoints);
			setPoints(summaryPflicht, pflichtPoints);
			setPoints(summarySupplement, supplementPoints);
			((PanelChangedEvent) event.getPayload()).getTarget().add(form);
		}
		
	}
	
	private static void setPoints(int summary, IModel<Integer> points){
		if(summary < MAX_POINTS){
			points.setObject((int) Math.round(summary*(100/MAX_POINTS)));
		} else {
			points.setObject(100);
		}
	}

	private static ProgressBar createProgressBar(String id, IModel<Integer> points){
		ProgressBar progressBar = new ProgressBar(id, points, ProgressBar.Type.SUCCESS); 
		progressBar.setOutputMarkupId(true);
		return progressBar;
	}
	
	private static ListView<ICourse> createListView(IModel<List<ICourse>> allSelectedModuls, final IModel<List<ICourse>> allCurrentSelectedModuls, IModel<List<Prof>> profs, final List<Prof> allProfs){
		return new ListView<ICourse>("verticalButtonGroup", allSelectedModuls) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<ICourse> item) {
				AjaxButton button = createButton(item.getModelObject().getName());
				if(allCurrentSelectedModuls.getObject().contains(item.getModel().getObject())){
					button.add(new AttributeModifier("class", Model.of("btn btn-xs btn-success btn-block")));
				}
				button.add(new Label("label", item.getModelObject().getName()));
				item.add(button);
			}
			
			/* local private method*/
			private AjaxButton createButton(String modulName){
				return new AjaxButton("selectedModulButton") {
					private static final long serialVersionUID = 1L;

					public void onSubmit(AjaxRequestTarget target, Form<?> form) {
						allProfs.stream().forEach(prof -> prof.getSelectedModuls().removeIf(m -> m.getName().equals(modulName)));
						allProfs.stream().forEach(prof -> prof.getSelectedPflichtModuls().removeIf(m -> m.getName().equals(modulName)));
						allProfs.stream().findFirst().ifPresent(p -> p.getSupplementCourses().getAllCourses().removeIf(c -> c.getName().equals(modulName)));
						send(getPage(), Broadcast.DEPTH, new RemoveCourseEvent(target));
					};
				};
			}
			
		};
	}
	
	@Override
	protected void onDetach() {
		super.onDetach();
		allCurrentSelectedModuls.detach();
		allSelectedModuls.detach();
		profs.detach();
	}
}
