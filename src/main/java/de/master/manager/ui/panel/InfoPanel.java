package de.master.manager.ui.panel;

import java.util.List;
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
import de.master.manager.ui.events.AbstractEvent;
import de.master.manager.ui.events.RemoveCourseEvent;
import de.master.manager.ui.events.SelectedEvent;



public class InfoPanel extends Panel{

	private static final long serialVersionUID = 1L;
	private static final double MAX_POINTS = 23;
	
	private final ProgressBar progressBar;
	private final IModel<Integer> points = Model.of(0);
	private final IModel<Integer> pflichtPoints = Model.of(0);
	private IModel<List<Prof>> profs;
	private Form<Object> form;
	private IModel<List<ICourse>> allCurrentSelectedModuls;
	private IModel<List<ICourse>> allSelectedModuls;
	private ProgressBar pflichtProgressBar;
	
	public InfoPanel(String id, final IModel<List<Prof>> profs, final List<Prof> allProfs) {
		super(id);
		setOutputMarkupId(true);
		this.profs = profs;
		form = new Form<Object>("form");
		points.setObject(calculatePoints(profs, Prof::calculateWahlPoints));
		pflichtPoints.setObject(calculatePoints(profs, Prof::calculatePflichtPoints));
		this.progressBar = createWahlProgressBar(points, profs);
		this.pflichtProgressBar = createPflichtProgressBar(pflichtPoints, profs);
		allSelectedModuls = new LoadableDetachableModel<List<ICourse>>() {

			private static final long serialVersionUID = 1L;

			@Override
			protected List<ICourse> load() {
				List<ICourse> list = Lists.newArrayList();
				allProfs.stream().forEach(p->list.addAll(p.getSelectedModuls()));
				allProfs.stream().forEach(p->list.addAll(p.getSelectedPflichtModuls()));
				
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
				
				return list;
			}
			
		};
		
        ListView<ICourse> modulListView = createListView(allSelectedModuls, allCurrentSelectedModuls, profs, allProfs);
		form.add(modulListView);
        form.add(progressBar);
        form.add(pflichtProgressBar);
		add(form);
			
	}
		
	private static ProgressBar createPflichtProgressBar(IModel<Integer> pflichtPoints, final IModel<List<Prof>> profs) {
		ProgressBar progressBar = new ProgressBar("pflichtProgress", pflichtPoints, ProgressBar.Type.SUCCESS); 
		progressBar.setOutputMarkupId(true);
		return progressBar;
	}

	private static int calculatePoints(IModel<List<Prof>> profs, Function<Prof, Double> calculate){
		return (int)Math.round(profs.getObject().stream().mapToDouble(p -> calculate.apply(p)).sum());
	}
	
	
	@Override
	public void onEvent(IEvent<?> event) {
		super.onEvent(event);
		int summary = calculatePoints(profs, Prof::calculateWahlPoints);
		int summaryPflicht = calculatePoints(profs, Prof::calculatePflichtPoints);
		if(event.getPayload() instanceof SelectedEvent){
			setPoints(summary, points);
			setPoints(summaryPflicht, pflichtPoints);
			((SelectedEvent) event.getPayload()).getTarget().add(form);
		} else if(event.getPayload() instanceof AbstractEvent){
			setPoints(summary, points);
			setPoints(summaryPflicht, pflichtPoints);
			((AbstractEvent) event.getPayload()).getTarget().add(form);
		} else if(event.getPayload() instanceof RemoveCourseEvent){
			setPoints(summary, points);
			setPoints(summaryPflicht, pflichtPoints);
			((RemoveCourseEvent) event.getPayload()).getTarget().add(form);
		}
		
	}
	
	private static void setPoints(int summary, IModel<Integer> points){
		if(summary < MAX_POINTS){
			points.setObject((int) Math.round(summary*(100/MAX_POINTS)));
		} else {
			points.setObject(100);
		}
	}

	private static ProgressBar createWahlProgressBar(IModel<Integer> points, final IModel<List<Prof>> profs2){
		ProgressBar progressBar = new ProgressBar("progress", points, ProgressBar.Type.SUCCESS); 
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
					button.add(new AttributeModifier("class", Model.of("btn btn-xs btn-success btn-block")));//new AttributeAppender("class", " btn-success"));
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
