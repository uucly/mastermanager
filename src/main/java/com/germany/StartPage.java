package com.germany;

import java.io.IOException;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.google.common.collect.Lists;
import com.menueBar.BasePage;
import com.menueBar.MenuItemEnum;
import com.modul.InfoPanel;
import com.modul.WahlPflichtPanel;
import com.professoren.Prof;

import de.agilecoders.wicket.core.Bootstrap;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeCssReference;
import dragAndDrop.ProfChangedEventLeft;
import dragAndDrop.ProfChangedEventRight;

public class StartPage extends BasePage{

	private static final long serialVersionUID = 1L;
	
	@SpringBean
	ProfChangedEventLeft leftEvent;
	
	@SpringBean
	ProfChangedEventRight rightEvent;
	
	public StartPage() throws IOException{
		
		//WebMarkupContainer address = new WebMarkupContainer("pflicht");
		//address.add(createForm(leftEvent, rightEvent));
		IModel<Prof> prof1 = Model.of(Prof.BREUNIG);
		IModel<Prof> prof2 = Model.of(Prof.HINZ);
		
		add(new WahlPflichtPanel("wahlPanel1", prof1, leftEvent));
		add(new WahlPflichtPanel("wahlPanel2", prof2, rightEvent));
		add(new InfoPanel("infoPanel", Lists.newArrayList(prof1, prof2)));
		//	add(HeaderContributor.forJavaScript("http://www.google.com/jsapi?key=ABCDEFG"));
	}
	
	private static Form<?> createForm(ProfChangedEventLeft leftEvent, ProfChangedEventRight rightEvent) throws IOException{
		IModel<Prof> prof1 = Model.of(Prof.BREUNIG);
		IModel<Prof> prof2 = Model.of(Prof.HINZ);
		
		Form<?> form = new Form<Object>("form");
		form.add(new WahlPflichtPanel("wahlPanel1", prof1, leftEvent));
		form.add(new WahlPflichtPanel("wahlPanel2", prof2, rightEvent));
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
