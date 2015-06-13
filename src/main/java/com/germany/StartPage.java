package com.germany;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class StartPage extends WebPage{

	private static final long serialVersionUID = 1L;
	
	public StartPage(final PageParameters parameters){
		super(parameters);
		
		Form form = new Form("form");
		String test = "bla";
		List<String> SEARCH_ENGINES = Arrays.asList(new String[] {
				"Google", "Bing", "Baidu" });
		 
		//variable to hold the selected value from dropdown box,
		//and also make "Google" is selected by default
		String selected = "Google";
		 
		DropDownChoice<String> listSites = new DropDownChoice<String>(
				"sites", new PropertyModel<String>(this, "selected"), SEARCH_ENGINES);
		//form.add(listSites);
		add(form);
		
	}
}
