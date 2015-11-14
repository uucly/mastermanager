package de.master.manager.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

import de.agilecoders.wicket.core.Bootstrap;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapExternalLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.DropDownButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.MenuBookmarkablePageLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.MenuDivider;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.MenuHeader;
import de.agilecoders.wicket.core.markup.html.bootstrap.html.HtmlTag;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.GlyphIconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.ImmutableNavbarComponent;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarComponents;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarDropDownButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarExternalLink;
import de.agilecoders.wicket.core.settings.IBootstrapSettings;
import de.agilecoders.wicket.core.settings.ITheme;

public abstract class BasePage extends WebPage{

	private static final long serialVersionUID = 1L;

	/**
	 * To configure navigation bar, change this, and wicket application
	 */
	public BasePage() {
		add(new HtmlTag("html"));

           add(newNavbar("navbar"));
        /*add(newNavigation("navigation"));
        add(new Footer("footer"));

        add(new Code("code-internal"));

        add(new HeaderResponseContainer("footer-container", "footer-container"));

        // add new relic RUM scripts.
        add(new Label("newrelic", Model.of(NewRelic.getBrowserTimingHeader()))
                .setEscapeModelStrings(false)
                .setRenderBodyOnly(true)
                .add(new AttributeModifier("id", "newrelic-rum-header")));
        add(new Label("newrelic-footer", Model.of(NewRelic.getBrowserTimingFooter()))
                .setEscapeModelStrings(false)
                .setRenderBodyOnly(true)
                .add(new AttributeModifier("id", "newrelic-rum-footer")));*/
	    }
	
	
	protected Navbar newNavbar(String markupId) {
        Navbar navbar = new Navbar(markupId);

        navbar.setPosition(Navbar.Position.TOP);
        navbar.setInverted(true);
        
        //ResourceReference resource = new PackageResourceReference(getClass(), "gik_logo.png");
       // navbar.setBrandImage(resource, Model.of("Master Manager"));
        navbar.setBrandName(Model.of("Master Manager"));
        // show brand name
       
        navbar.addComponents(NavbarComponents.transform(Navbar.ComponentPosition.LEFT,
                        new NavbarButton<Void>(HomePage.class, Model.of("Overview")).setIconType(GlyphIconType.home),
                        new NavbarButton<Void>(DragAndDropPage.class, Model.of("Moduls")),
                       // new NavbarButton<Void>(ComponentsPage.class, Model.of("Components")),
                        new NavbarExternalLink(Model.of("http://gug.bgu.kit.edu/downloads/Master_Modulhandbuch_GuG_Oktober_2015.pdf"))
                                .setLabel(Model.of("Modulhandbuch"))
                                .setTarget(BootstrapExternalLink.Target.blank)
                                .setIconType(GlyphIconType.export))
        );
     //   navbar.addComponents(new NavbarText(navbar.newExtraItemId(), "Plain text").position(Navbar.ComponentPosition.RIGHT));

        DropDownButton dropdown = new NavbarDropDownButton(Model.of("Themes")) {
            @Override
            public boolean isActive(Component item) {
                return false;
            }

            @Override
            protected List<AbstractLink> newSubMenuButtons(final String buttonMarkupId) {
                final List<AbstractLink> subMenu = new ArrayList<AbstractLink>();
                subMenu.add(new MenuHeader(Model.of("all available themes:")));
                subMenu.add(new MenuDivider());

                final IBootstrapSettings settings = Bootstrap.getSettings(getApplication());
                final List<ITheme> themes = settings.getThemeProvider().available();

                for (final ITheme theme : themes) {
                    PageParameters params = new PageParameters();
                    params.set("theme", theme.name());

                    subMenu.add(new MenuBookmarkablePageLink<Void>(getPageClass(), params, Model.of(theme.name())));
                }

                return subMenu;
            }
        }.setIconType(GlyphIconType.book);

        navbar.addComponents(new ImmutableNavbarComponent(dropdown));

        return navbar;
    }
	
	
	/**
	 * mal sehen ob man es noch brauch
	 */
/*	@Override
    protected void onConfigure() {
        super.onConfigure();

        configureTheme(getPageParameters());
    }*/

	
	private void configureTheme(PageParameters pageParameters) {
        StringValue theme = pageParameters.get("theme");

        if (!theme.isEmpty()) {
            IBootstrapSettings settings = Bootstrap.getSettings(getApplication());
            settings.getActiveThemeProvider().setActiveTheme(theme.toString(""));
        }
    }

    protected ITheme activeTheme() {
        IBootstrapSettings settings = Bootstrap.getSettings(getApplication());

        return settings.getActiveThemeProvider().getActiveTheme();
    }
}
