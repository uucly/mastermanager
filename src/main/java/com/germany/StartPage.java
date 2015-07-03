package com.germany;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class StartPage extends WebPage{

	private static final long serialVersionUID = 1L;
	private static final List<String> SEARCH_ENGINES = Arrays.asList(new String[] {
			"Google", "Bing", "Baidu" });
 
	//make Google selected by default
	private String selected = "Google";
	
	public StartPage(final PageParameters parameters){
		super(parameters);
		
		/*add(new FeedbackPanel("feedback"));
		 
		final IModel<String> prop = new PropertyModel<String>(this, "selected");
		DropDownChoice<String> listSites = new DropDownChoice<String>(
			"sites", prop, SEARCH_ENGINES);
 
		Form form = new Form("form") {
			@Override
			protected void onSubmit() {
 
				info("Selected search engine : " + prop.getObject());
				warn("Bla");
			}
		};
		Label label = new Label("label", prop);
		
		IModel<String> m = new AbstractReadOnlyModel<String>() {

			@Override
			public String getObject() {
				return "color:rgb(255,150,"+(int)(Math.random()*100)+")";
			}
		};
		
		Button button = new Button("testButton", m);
		label.add(new AttributeAppender("style", m));
		form.add(listSites);
		form.add(label);
		form.add(button);
		
		add(form);*/
		WebMarkupContainer address = new WebMarkupContainer("address");
		Form form = new Form("form");
		
		form.add(new Label("street", "Wiesenstraße"));
		form.add(new Label("number", "14"));
		form.add(new Label("zipcode", "35390"));
		form.add(new Label("city", "Gießen"));
		address.add(form);
		add(address);

	}
}
