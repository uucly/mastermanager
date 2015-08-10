package com.modul;

import java.util.ArrayList;
import java.util.List;

import models.TransformationModel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;

import com.google.common.collect.Lists;
import com.professoren.Prof;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.ButtonBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.ButtonGroup;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.ButtonList;
import de.agilecoders.wicket.core.markup.html.bootstrap.components.progress.ProgressBar;
import de.agilecoders.wicket.core.markup.html.bootstrap.components.progress.Stack;
import de.agilecoders.wicket.jquery.util.Generics2;
import dragAndDrop.AbstractEvent;

public class InfoPanel extends Panel{

	private static final long serialVersionUID = 1L;
	private static final double MAX_POINTS = 23;
	
	private final ProgressBar progressBar;
	private final Model<Integer> points = Model.of(0);
	private IModel<List<Prof>> profs;
	private Form<Object> form;
	private LoadableDetachableModel<List<Modul>> allCurrentSelectedModuls;
	
	public InfoPanel(String id, IModel<List<Prof>> profs) {
		super(id);
		this.profs = profs;
		setOutputMarkupId(true);
		form = new Form<Object>("form");
		Label label = new Label("wahlLabel");
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
        ListView<Modul> modulListView = new ListView<Modul>("verticalButtonGroup", allCurrentSelectedModuls) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<Modul> item) {
				AjaxButton button = new AjaxButton("selectedModulButton", Model.of(item.getModelObject().getName())) {
					private static final long serialVersionUID = 1L;

					public void onSubmit(AjaxRequestTarget target, Form<?> form) {
						profs.getObject().stream().forEach(prof -> prof.getSelectedModuls().removeIf(m -> m.getName().equals(getModelObject())));
						send(getPage(), Broadcast.DEPTH, new RemoveModulEvent(target));
					};
				};
				item.add(button);
			}
		};
		
		form.add(modulListView);
        form.add(label);
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
			if(calculatePoints(profs)<MAX_POINTS){
				points.setObject((int) Math.round(calculatePoints(profs)*(100/MAX_POINTS)));
			} else {
				points.setObject(100);
			}
			
			((SelectedEvent) event.getPayload()).getTarget().add(form);
		} else if(event.getPayload() instanceof AbstractEvent){
			if(calculatePoints(profs)<MAX_POINTS){
				points.setObject((int) Math.round(calculatePoints(profs)*(100/MAX_POINTS)));
			} else {
				points.setObject(100);
			}
			
			((AbstractEvent) event.getPayload()).getTarget().add(form);
		} else if(event.getPayload() instanceof RemoveModulEvent){
			((RemoveModulEvent) event.getPayload()).getTarget().add(form);
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
	@Override
	protected void onDetach() {
		super.onDetach();
		allCurrentSelectedModuls.detach();
		profs.detach();
	}
}
