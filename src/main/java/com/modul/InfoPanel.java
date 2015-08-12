package com.modul;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;

import com.google.common.collect.Lists;
import com.professoren.Prof;

import de.agilecoders.wicket.core.markup.html.bootstrap.components.progress.ProgressBar;
import de.agilecoders.wicket.core.markup.html.bootstrap.components.progress.Stack;
import dragAndDrop.AbstractEvent;

public class InfoPanel extends Panel{

	private static final long serialVersionUID = 1L;
	private static final double MAX_POINTS = 23;
	
	private final ProgressBar progressBar;
	private final IModel<Integer> points = Model.of(0);
	private final IModel<Integer> pflichtPoints = Model.of(0);
	private IModel<List<Prof>> profs;
	private Form<Object> form;
	private IModel<List<Modul>> allCurrentSelectedModuls;
	private IModel allSelectedModuls;
	private ProgressBar pflichtProgressBar;
	
	public InfoPanel(String id, IModel<List<Prof>> profs) {
		super(id);
		setOutputMarkupId(true);
		this.profs = profs;
		form = new Form<Object>("form");
		points.setObject(calculatePoints(profs, Prof::calculateWahlPoints));
		pflichtPoints.setObject(calculatePoints(profs, Prof::calculatePflichtPoints));
		this.progressBar = createWahlProgressBar(points, profs);
		this.pflichtProgressBar = createPflichtProgressBar(pflichtPoints, profs);
		allSelectedModuls = new LoadableDetachableModel<List<Modul>>() {

			@Override
			protected List<Modul> load() {
				List<Modul> list = Lists.newArrayList();
				Arrays.asList(Prof.values()).stream().forEach(p->list.addAll(p.getSelectedModuls()));
				Arrays.asList(Prof.values()).stream().forEach(p->list.addAll(p.getSelectedPflichtModuls()));
				
				return list;
			}
		};
		allCurrentSelectedModuls = new LoadableDetachableModel<List<Modul>>() {
			@Override
			protected List<Modul> load() {
				List<Modul> list = Lists.newArrayList();
				profs.getObject().stream().forEach(m -> list.addAll(m.getSelectedModuls()));
				profs.getObject().stream().forEach(m -> list.addAll(m.getSelectedPflichtModuls()));
				
				return list;
			}
			
		};
		
        ListView<Modul> modulListView = createListView(allSelectedModuls, allCurrentSelectedModuls, profs);
		form.add(modulListView);
        form.add(progressBar);
        form.add(pflichtProgressBar);
		add(form);
			
	}
		
	private static ProgressBar createPflichtProgressBar(IModel<Integer> pflichtPoints, IModel<List<Prof>> profs) {
		ProgressBar progressBar = new ProgressBar("pflichtProgress"); 
		progressBar.setOutputMarkupId(true);
		Stack labeledStack = new Stack(progressBar.getStackId(), pflichtPoints) {
            private static final long serialVersionUID = 1L;

			@Override
            protected IModel<String> createLabelModel() {
                return new AbstractReadOnlyModel<String>() {
            		private static final long serialVersionUID = 1L;

					@Override
                    public String getObject() {
                    	return calculatePoints(profs, Prof::calculatePflichtPoints)+" von " + MAX_POINTS + " Punkten";
                    }
                };
            }
        };
        labeledStack.labeled(true).type(ProgressBar.Type.SUCCESS);
        progressBar.addStacks(labeledStack);
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
		} else if(event.getPayload() instanceof RemoveModulEvent){
			setPoints(summary, points);
			setPoints(summaryPflicht, pflichtPoints);
			((RemoveModulEvent) event.getPayload()).getTarget().add(form);
		}
		
	}
	
	private static void setPoints(int summary, IModel<Integer> points){
		if(summary < MAX_POINTS){
			points.setObject((int) Math.round(summary*(100/MAX_POINTS)));
		} else {
			points.setObject(100);
		}
	}

	private static ProgressBar createWahlProgressBar(IModel<Integer> points, IModel<List<Prof>> profs2){
		ProgressBar progressBar = new ProgressBar("progress"); 
		progressBar.setOutputMarkupId(true);
		Stack labeledStack = new Stack(progressBar.getStackId(), points) {
            private static final long serialVersionUID = 1L;

			@Override
            protected IModel<String> createLabelModel() {
                return new AbstractReadOnlyModel<String>() {
            		private static final long serialVersionUID = 1L;

					@Override
                    public String getObject() {
                    	return calculatePoints(profs2, Prof::calculateWahlPoints)+" von " + MAX_POINTS + " Punkten";
                    }
                };
            }
        };
        labeledStack.labeled(true).type(ProgressBar.Type.SUCCESS);
        progressBar.addStacks(labeledStack);
        return progressBar;
	}
	
	private static ListView<Modul> createListView(IModel<List<Modul>> allSelectedModuls, IModel<List<Modul>> allCurrentSelectedModuls, IModel<List<Prof>> profs){
		return new ListView<Modul>("verticalButtonGroup", allSelectedModuls) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<Modul> item) {
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
						Arrays.asList(Prof.values()).stream().forEach(prof -> prof.getSelectedModuls().removeIf(m -> m.getName().equals(modulName)));
						Arrays.asList(Prof.values()).stream().forEach(prof -> prof.getSelectedPflichtModuls().removeIf(m -> m.getName().equals(modulName)));
						
						send(getPage(), Broadcast.DEPTH, new RemoveModulEvent(target));
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
