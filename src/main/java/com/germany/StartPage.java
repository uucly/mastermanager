package com.germany;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import de.agilecoders.wicket.core.Bootstrap;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeCssReference;

public class StartPage extends WebPage{

	private static final long serialVersionUID = 1L;
	
	private static final String BREUNIG_PATH= "src/main/resources/Breunig_Wahl.txt";
	private static final String ALL_PATH= "src/main/resources/WahlPflichtModule.txt";
	
	
	public StartPage(final PageParameters parameters) throws IOException{
		super(parameters);
		
		WebMarkupContainer address = new WebMarkupContainer("pflicht");
		address.add(createForm());
		add(address);
	}
	
	private static Form createForm() throws IOException{
		SelectedModulContainer selectedModuls1 = new SelectedModulContainer();
		SelectedModulContainer selectedModuls2 = new SelectedModulContainer();
		
		Form form = new Form("form");
		form.add(new WahlPflichtPanel("wahlPanel1", selectedModuls1));
		form.add(new WahlPflichtPanel("wahlPanel2", selectedModuls2));
		form.add(new InfoPanel("infoPanel", Arrays.asList(selectedModuls1, selectedModuls2)));
		return form;
	}
	
	@Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        Bootstrap.renderHead(response);
        response.render(CssHeaderItem.forReference(FontAwesomeCssReference.instance()));
    }
	
}
