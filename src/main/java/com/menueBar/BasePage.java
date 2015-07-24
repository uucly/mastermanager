package com.menueBar;

import map.OpenStreetMapPage;

import org.apache.wicket.markup.html.WebPage;

import com.germany.HomePage;
import com.germany.StartPage;

public abstract class BasePage extends WebPage{

	private static final long serialVersionUID = 1L;

	/**
	 * To configure navigation bar, change this, and wicket application
	 */
	public BasePage() {
		add(new TwitterBootstrapNavBarPanel.Builder("navBar", HomePage.class, "Example Web App", getActiveMenu())
	            .withMenuItem(MenuItemEnum.Start, StartPage.class)
	            .withMenuItemAsDropdown(MenuItemEnum.MAP, OpenStreetMapPage.class, "Product One")
	            .build());
	    }

	    public abstract MenuItemEnum getActiveMenu();
}
