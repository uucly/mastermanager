package com.germany;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;

public class InfoPanel extends Panel{

	public InfoPanel(String id) {
		super(id);
		Form form = new Form("form");
		Label label = new Label("wahlLabel");
		
	}

}
