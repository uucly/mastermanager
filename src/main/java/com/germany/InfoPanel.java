package com.germany;

import java.util.Collection;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AbstractAjaxTimerBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.AjaxIndicatorAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.time.Duration;

import de.agilecoders.wicket.core.markup.html.bootstrap.components.progress.ProgressBar;

public class InfoPanel extends Panel{

	private static final long serialVersionUID = 1L;

	private final ProgressBar progressBar;
	
	public InfoPanel(String id, Collection<SelectedModulContainer> allSelectedModuls) {
		super(id);
		Form form = new Form("form");
		Label label = new Label("wahlLabel");
		form.add(label);

		this.progressBar = new ProgressBar("progress", Model.of(36), ProgressBar.Type.SUCCESS); 
		//progressBar.add(new AttributeModifier("class", new Model<String>("progress-bar-success")));
		form.add(this.progressBar);
		add(form);
		
	}

}
