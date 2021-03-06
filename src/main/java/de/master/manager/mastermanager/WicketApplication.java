package de.master.manager.mastermanager;

import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;

import de.agilecoders.wicket.core.Bootstrap;
import de.agilecoders.wicket.core.settings.BootstrapSettings;
import de.agilecoders.wicket.core.settings.IBootstrapSettings;
import de.agilecoders.wicket.core.settings.ThemeProvider;
import de.agilecoders.wicket.less.BootstrapLess;
import de.agilecoders.wicket.themes.markup.html.bootswatch.BootswatchTheme;
import de.agilecoders.wicket.themes.markup.html.bootswatch.BootswatchThemeProvider;
import de.master.manager.ui.HomePage;

/**
 * Application object for your web application. If you want to run this
 * application without deploying, run the Start class.
 * 
 * @see de.master.manager.mastermanager.Start#main(String[])
 */
public class WicketApplication extends WebApplication {
	/**
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	@Override
	public Class<? extends WebPage> getHomePage() {
		return HomePage.class;
	}
	
	/**
	 * @see org.apache.wicket.Application#init()
	 */
	@Override
	public void init() {
		super.init();
		configureBootstrap();
		getComponentInstantiationListeners().add(new SpringComponentInjector(this));
	}

	/**
	 * configures wicket-bootstrap and installs the settings.
	 */
	private void configureBootstrap() {

		final IBootstrapSettings settings = new BootstrapSettings();
		final ThemeProvider themeProvider = new BootswatchThemeProvider(
				BootswatchTheme.Flatly);
		// settings.setJsResourceFilterName("footer-container")
		// .setThemeProvider(themeProvider);

		Bootstrap.install(this, settings);
		BootstrapLess.install(this);
	}
	
	@Override
	public RuntimeConfigurationType getConfigurationType() {
		return RuntimeConfigurationType.DEPLOYMENT;
	}

}
