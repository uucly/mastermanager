package com.germany;

import com.menueBar.BasePage;
import com.menueBar.MenuItemEnum;

public class HomePage extends BasePage {
	private static final long serialVersionUID = 6812852261202676179L;

		public HomePage() {

    }

    @Override
    public MenuItemEnum getActiveMenu() {
        return MenuItemEnum.NONE;
    }
}
