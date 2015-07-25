package com.modul;

import java.util.List;

import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import com.professoren.Prof;

import de.agilecoders.wicket.core.markup.html.bootstrap.components.progress.ProgressBar;
import de.agilecoders.wicket.core.markup.html.bootstrap.components.progress.Stack;
import dragAndDrop.AbstractEvent;

public class InfoPanel extends Panel{

	private static final long serialVersionUID = 1L;
	private static final double MAX_POINTS = 23;
	
	private final ProgressBar progressBar;
	private final Model<Integer> points = Model.of(0);
	private List<IModel<Prof>> profs;
	
	public InfoPanel(String id, List<IModel<Prof>> profs) {
		super(id);
		this.profs = profs;
		setOutputMarkupId(true);
		Form<?> form = new Form<Object>("form");
		Label label = new Label("wahlLabel");
		
		this.progressBar = createProgressBar(points, profs);
        
        form.add(label);
		form.add(this.progressBar);
        
		add(form);
			
	}
	
	private static int calculatePoints(List<IModel<Prof>> profs){
		return (int)Math.round(profs.stream().mapToDouble(m -> m.getObject().calculatePoints()).sum());
	}
	
	@Override
	public void onEvent(IEvent<?> event) {
		super.onEvent(event);
		
		if(event.getPayload() instanceof SelectedEvent){
			if(calculatePoints(profs)<MAX_POINTS){
				points.setObject(calculatePoints(profs));
			} else {
				points.setObject(100);
			}
			
			((SelectedEvent) event.getPayload()).getTarget().add(progressBar);
		} else if(event.getPayload() instanceof AbstractEvent){
			if(calculatePoints(profs)<MAX_POINTS){
				points.setObject((int) Math.round(calculatePoints(profs)*(100/MAX_POINTS)));
			} else {
				points.setObject(100);
			}
			
			((AbstractEvent) event.getPayload()).getTarget().add(progressBar);
		}
		
		
	}

	private static ProgressBar createProgressBar(IModel<Integer> points, List<IModel<Prof>> profs2){
		ProgressBar progressBar = new ProgressBar("progress"); 
		progressBar.setOutputMarkupId(true);
		Stack labeledStack = new Stack(progressBar.getStackId(), points) {
            @Override
            protected IModel<String> createLabelModel() {
                return new AbstractReadOnlyModel<String>() {
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
	
}
