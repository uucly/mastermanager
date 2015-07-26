package com.germany;

import java.io.IOException;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.google.common.collect.Lists;
import com.menueBar.BasePage;
import com.menueBar.MenuItemEnum;
import com.modul.InfoPanel;
import com.modul.SelectedModulContainer;
import com.modul.WahlPflichtPanel;
import com.professoren.Prof;

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
		IModel<Prof> prof1 = Model.of(Prof.BREUNIG);
		IModel<Prof> prof2 = Model.of(Prof.HINZ);
		
		Form<?> form = new Form<Object>("form");
		form.add(new WahlPflichtPanel("wahlPanel1", prof1));
		form.add(new WahlPflichtPanel("wahlPanel2", prof2));
		form.add(new InfoPanel("infoPanel", Lists.newArrayList(prof1, prof2)));
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
