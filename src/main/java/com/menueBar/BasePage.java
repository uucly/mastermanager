package com.menueBar;

import org.apache.wicket.markup.html.WebPage;

import com.germany.HomePage;
import com.germany.StartPage;

import dragAndDrop.DragAndDropPage;

public abstract class BasePage extends WebPage{

	private static final long serialVersionUID = 1L;

	/**
	 * To configure navigation bar, change this, and wicket application
	 */
	public BasePage() {
		add(new TwitterBootstrapNavBarPanel.Builder("navBar", HomePage.class, "Example Web App", getActiveMenu())
	            .withMenuItem(MenuItemEnum.Start, StartPage.class)
	            .withMenuItemAsDropdown(MenuItemEnum.DRAG_DROP, DragAndDropPage.class, "Drop down")
	            .withMenuItemAsDropdown(MenuItemEnum.Start, StartPage.class, "Autocomplete")

	            .build());
	    }

	public abstract MenuItemEnum getActiveMenu();
}
