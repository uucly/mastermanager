package com.germany;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.menueBar.BasePage;
import com.menueBar.MenuItemEnum;

public class HomePage extends BasePage {
	/*private static final long serialVersionUID = 1L;

	public HomePage(final PageParameters parameters) {
		super(parameters);

		add(new Label("version", getApplication().getFrameworkSettings().getVersion()));
		
    }*/
	public HomePage() {

    }

    @Override
    public MenuItemEnum getActiveMenu() {
        return MenuItemEnum.NONE;
    }
}
