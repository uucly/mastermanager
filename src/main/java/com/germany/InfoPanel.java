package com.germany;

import java.util.Collection;

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
	
	public InfoPanel(String id, Collection<SelectedModulContainer> allSelectedModuls) {
		super(id);
		Form form = new Form("form");
		Label label = new Label("wahlLabel");
		form.add(label);

		this.progressBar = new ProgressBar("progress"); 
		Stack labeledStack = new Stack(progressBar.getStackId(), Model.of(10)) {
            @Override
            protected IModel<String> createLabelModel() {
                return new AbstractReadOnlyModel<String>() {
                    @Override
                    public String getObject() {
                        return "The progress is: 45 Prozent";
                    }
                };
            }
        };
        labeledStack.labeled(true).type(ProgressBar.Type.SUCCESS);
        progressBar.addStacks(labeledStack);
        form.add(this.progressBar);
        
		//form.add(labeledStack);
		add(form);
		
	}

}
