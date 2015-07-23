package com.germany;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;

import com.menueBar.ModulPage;
import com.menueBar.MapPage;

import de.agilecoders.wicket.core.Bootstrap;
import de.agilecoders.wicket.core.settings.BootstrapSettings;
import de.agilecoders.wicket.core.settings.IBootstrapSettings;
import de.agilecoders.wicket.core.settings.ThemeProvider;
import de.agilecoders.wicket.less.BootstrapLess;
import de.agilecoders.wicket.themes.markup.html.bootswatch.BootswatchTheme;
import de.agilecoders.wicket.themes.markup.html.bootswatch.BootswatchThemeProvider;

/**
 * Application object for your web application. If you want to run this
 * application without deploying, run the Start class.
 * 
 * @see com.germany.Start#main(String[])
 */
public class WicketApplication extends WebApplication {
	/**
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	@Override
	public Class<? extends WebPage> getHomePage() {
		return OpenStreetMapPage.class;
	}

	/**
	 * @see org.apache.wicket.Application#init()
	 */
	@Override
	protected void init() {
		super.init();
		configureBootstrap();
		getComponentInstantiationListeners().add( new SpringComponentInjector(this));
		
		//mountPage("com/germany", StartPage.class);
		//mountPage("com/menueBar", StartPage.class);
		//mountPage("com/germany", OpenStreetMapPage.class);
/*		mountPage("product-two", ProductTwoPage.class);
		mountPage("product-three", ProductThreePage.class);
		mountPage("team", TeamPage.class);
		mountPage("skills", OurSkillsPage.class);
		mountPage("contact", ContactPage.class);
*/
		getMarkupSettings().setStripWicketTags(true);
		setExternalHtmlDirIfSystemPropertyIsPresent();
	}

	private void setExternalHtmlDirIfSystemPropertyIsPresent() {
		String externalDir = System.getProperty("wicket.html.dir");
		if (externalDir != null) {
//			getResourceSettings().addResourceFolder(externalDir);
		}
	}

	private void configureBootstrap() {

		final IBootstrapSettings settings = new BootstrapSettings();
		settings.useCdnResources(true);

		final ThemeProvider themeProvider = new BootswatchThemeProvider(
				BootswatchTheme.Spacelab);
		settings.setThemeProvider(themeProvider);

		Bootstrap.install(this, settings);
		BootstrapLess.install(this);
	}
}
