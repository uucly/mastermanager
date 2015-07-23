package com.menueBar;

import org.apache.wicket.markup.html.WebPage;

import com.germany.HomePage;
import com.germany.OpenStreetMapPage;
import com.germany.StartPage;

public abstract class BasePage extends WebPage{

	private static final long serialVersionUID = 1L;

	public BasePage() {
//		add(new TwitterBootstrapNavBarPanel.Builder("", HomePage.class, "",getActiveMenu()).withMenuItem(MenuItemEnum.CLIENTS, ClientsPage.class).build());
	  //  add(new TwitterBootstrapNavBarPanel.Builder("navBar", HomePage.class, "Example Web App", getActiveMenu())
	  ///          .withMenuItem(MenuItemEnum.Start, StartPage.class)
	   //         .withMenuItemAsDropdown(MenuItemEnum.MAP, OpenStreetMapPage.class, "Product One")
	            //.withMenuItem(MenuItemEnum.CONTACT, ContactPage.class)
	  //          .build());
	    }

	    public abstract MenuItemEnum getActiveMenu();
}
