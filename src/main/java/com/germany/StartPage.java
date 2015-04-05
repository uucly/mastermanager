package com.germany;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class StartPage extends WebPage{

	private static final long serialVersionUID = 1L;
	
	public StartPage(final PageParameters parameters){
		super(parameters);
		IModel<String> m = Model.of("");
		m.setObject("Hallo");
		add(new Label("test", m));
	}
}
