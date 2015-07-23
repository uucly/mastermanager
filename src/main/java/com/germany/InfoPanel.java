package com.germany;

import java.util.Collection;

import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import de.agilecoders.wicket.core.markup.html.bootstrap.components.progress.ProgressBar;
import de.agilecoders.wicket.core.markup.html.bootstrap.components.progress.Stack;

public class InfoPanel extends Panel{

	private static final long serialVersionUID = 1L;

	private final ProgressBar progressBar;
	private final Collection<SelectedModulContainer> allSelectedModuls;
	private final Model<Integer> points = Model.of(0);
	
	public InfoPanel(String id, Collection<SelectedModulContainer> allSelectedModuls) {
		super(id);
		
		this.allSelectedModuls = allSelectedModuls;
		setOutputMarkupId(true);
		Form<?> form = new Form<Object>("form");
		Label label = new Label("wahlLabel");
		
		this.progressBar = createProgressBar(points);
        
        form.add(label);
		form.add(this.progressBar);
        
		add(form);
			
	}
	
	private static int calculatePoints(Collection<SelectedModulContainer> allSelectedModuls){
		return (int)allSelectedModuls.stream().mapToDouble(SelectedModulContainer::calculatePoints).sum();
	}
	
	@Override
	public void onEvent(IEvent<?> event) {
		super.onEvent(event);
		
		if(event.getPayload() instanceof SelectedEvent){
			points.setObject(calculatePoints(allSelectedModuls));
			SelectedEvent selectedEvent = (SelectedEvent) event.getPayload();
			selectedEvent.getTarget().add(progressBar);
		}
	}

	private static ProgressBar createProgressBar(IModel<Integer> points){
		ProgressBar progressBar = new ProgressBar("progress"); 
		progressBar.setOutputMarkupId(true);
		Stack labeledStack = new Stack(progressBar.getStackId(), points) {
            @Override
            protected IModel<String> createLabelModel() {
                return new AbstractReadOnlyModel<String>() {
                    @Override
                    public String getObject() {
                    	return "The progress is: "+getModelObject()+" %";
                    }
                };
            }
        };
        labeledStack.labeled(true).type(ProgressBar.Type.SUCCESS);
        progressBar.addStacks(labeledStack);
        return progressBar;
	}
}
