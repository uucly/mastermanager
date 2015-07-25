package com.germany;

import java.io.IOException;
import java.util.Arrays;

import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.google.common.collect.Lists;
import com.menueBar.BasePage;
import com.menueBar.MenuItemEnum;
import com.modul.InfoPanel;
import com.modul.SelectedModulContainer;
import com.modul.WahlPflichtPanel;

import de.agilecoders.wicket.core.Bootstrap;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeCssReference;

public class StartPage extends BasePage{

	private static final long serialVersionUID = 1L;
	
	public StartPage() throws IOException{
		
		WebMarkupContainer address = new WebMarkupContainer("pflicht");
		address.add(createForm());
		add(address);
		//	add(HeaderContributor.forJavaScript("http://www.google.com/jsapi?key=ABCDEFG"));
	}
	
	private static Form<?> createForm() throws IOException{
		SelectedModulContainer selectedModuls1 = new SelectedModulContainer();
		SelectedModulContainer selectedModuls2 = new SelectedModulContainer();
		
		Form<?> form = new Form<Object>("form");
		form.add(new WahlPflichtPanel("wahlPanel1", selectedModuls1));
		form.add(new WahlPflichtPanel("wahlPanel2", selectedModuls2));
		form.add(new InfoPanel("infoPanel", Lists.newArrayList()));
		return form;
	}
	
	@Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        Bootstrap.renderHead(response);
        response.render(CssHeaderItem.forReference(FontAwesomeCssReference.instance()));
    }

	@Override
	public MenuItemEnum getActiveMenu() {
		return MenuItemEnum.Start;
	}
	
}
