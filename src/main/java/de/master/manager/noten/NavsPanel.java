package de.master.manager.noten;

import java.io.IOException;
import java.util.function.Supplier;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.panel.Panel;

import de.master.manager.myproject.CoursePanel;
import de.master.manager.myproject.NotePanel;
import de.master.manager.profStuff.Prof;
import de.master.manager.profStuff.WahlPflichtModuleLoader;

public class NavsPanel extends Panel{

	private static final long serialVersionUID = 1L;
	private Panel currentPanel;
	
	public NavsPanel(String id, WahlPflichtModuleLoader courseLoader, Prof breunig, Prof hinz, Prof heck, Prof hennes) throws IOException {
		super(id);
		currentPanel = new CoursePanel("panel", courseLoader, breunig, hinz, heck, hennes);
		currentPanel.setOutputMarkupPlaceholderTag(true);
		AjaxLink<?> cousePanelLink = createLink("courses",  new CoursePanel("panel", courseLoader, breunig, hinz, heck, hennes));
		AjaxLink<?> notenLink = createLink("noten",  new NotePanel("panel"));
				
		add(cousePanelLink);
		add(notenLink);
		add(currentPanel);
	}
	
	private AjaxLink<?> createLink(String id, Panel newPanel){
		return new AjaxLink<Object>(id) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				newPanel.setOutputMarkupId(true);
				currentPanel.replaceWith(newPanel);
				currentPanel = newPanel;
				target.add(currentPanel);
			}
		};
	}
	/*private AjaxLink<?> createLink(String id, Supplier<Panel> newPanelSupplier){
		return new AjaxLink<Object>(id) {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				Panel newPanel= newPanelSupplier.get();
				newPanel.setOutputMarkupId(true);
				currentPanel.replaceWith(newPanel);
				currentPanel = newPanel;
				target.add(currentPanel);
			}
		};
	}*/

}
