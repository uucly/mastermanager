package com.modul;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
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
	private IModel<List<Prof>> profs;
	private Form<Object> form;
	private IModel<List<Modul>> allCurrentSelectedModuls;
	
	public InfoPanel(String id, IModel<List<Prof>> profs) {
		super(id);
		setOutputMarkupId(true);
		this.profs = profs;
		form = new Form<Object>("form");
		points.setObject(calculatePoints(profs));
		this.progressBar = createProgressBar(points, profs);
		
		allCurrentSelectedModuls = new LoadableDetachableModel<List<Modul>>() {
			@Override
			protected List<Modul> load() {
				List<Modul> list = Lists.newArrayList();
				profs.getObject().stream().forEach(m -> list.addAll(m.getSelectedModuls()));
				return list;
			}
			
		};
		/*IModel<List<Modul>> allCurrentSelectedModuls = new TransformationModel<List<Prof>, List<Modul>>(profs, p -> {
			List<Modul> list = Lists.newArrayList();
			p.stream().forEach(m -> list.addAll(m.getSelectedModuls()));
			return list;
		});*/
		/*ListView<Modul> selectedButtons = new ListView<Modul>("verticalButtonGroup", allCurrentSelectedModuls) {

			@Override
			protected void populateItem(ListItem<Modul> item) {
				item.add(new AjaxLink<Modul>("selectedModulButton", Model.of(item.getModelObject())) {

					@Override
					public void onClick(AjaxRequestTarget target) {
						profs.getObject().stream().filter(prof -> prof.getSelectedModuls().remove(item.getModelObject()));
						remove(item);
						send(getPage(), Broadcast.DEPTH, new RemoveModulEvent(target));
					}});
			}
		};
		
		selectedButtons.setOutputMarkupId(true);*/
        ListView<Modul> modulListView = createListView(allCurrentSelectedModuls, profs);
		form.add(modulListView);
        form.add(new Label("wahlLabel"));
		form.add(progressBar);
        
		add(form);
			
	}
		
	private static int calculatePoints(IModel<List<Prof>> profs2){
		return (int)Math.round(profs2.getObject().stream().mapToDouble(Prof::calculatePoints).sum());
	}
	
	@Override
	public void onEvent(IEvent<?> event) {
		super.onEvent(event);
		
		if(event.getPayload() instanceof SelectedEvent){
			setPoints(profs, points);
			((SelectedEvent) event.getPayload()).getTarget().add(form);
		} else if(event.getPayload() instanceof AbstractEvent){
			setPoints(profs, points);
			
			((AbstractEvent) event.getPayload()).getTarget().add(form);
		} else if(event.getPayload() instanceof RemoveModulEvent){
			setPoints(profs, points);
			((RemoveModulEvent) event.getPayload()).getTarget().add(form);
		}
		
	}
	
	private static void setPoints(IModel<List<Prof>> profs, IModel<Integer> points){
		int summary = calculatePoints(profs);
		if(summary < MAX_POINTS){
			points.setObject((int) Math.round(calculatePoints(profs)*(100/MAX_POINTS)));
		} else {
			points.setObject(100);
		}
	}

	private static ProgressBar createProgressBar(IModel<Integer> points, IModel<List<Prof>> profs2){
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
                    	return calculatePoints(profs2)+" von " + MAX_POINTS + " Punkten";
                    }
                };
            }
        };
        labeledStack.labeled(true).type(ProgressBar.Type.SUCCESS);
        progressBar.addStacks(labeledStack);
        return progressBar;
	}
	
	private static ListView<Modul> createListView(IModel<List<Modul>> allCurrentSelectedModuls, IModel<List<Prof>> profs){
		return new ListView<Modul>("verticalButtonGroup", allCurrentSelectedModuls) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<Modul> item) {
				AjaxButton button = createButton(item.getModelObject().getName());
				button.add(new Label("label", item.getModelObject().getName()));
				item.add(button);
			}
			
			/* local private method*/
			private AjaxButton createButton(String modulName){
				return new AjaxButton("selectedModulButton") {
					private static final long serialVersionUID = 1L;

					public void onSubmit(AjaxRequestTarget target, Form<?> form) {
						profs.getObject().stream().forEach(prof -> prof.getSelectedModuls().removeIf(m -> m.getName().equals(modulName)));
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
		profs.detach();
	}
}
