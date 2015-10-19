package de.master.manager.noten;

import java.io.IOException;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;

import de.master.manager.myproject.DragAndDropPage;

public class NavsPanel extends Panel{

	public NavsPanel(String id) {
		super(id);
		
		Link cousePanelLink = new Link("courses") {

			@Override
			public void onClick() {
				try {
					this.setResponsePage(new DragAndDropPage());
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		};
		
		Link notenLink = new Link("noten") {

			@Override
			public void onClick() {
					this.setResponsePage(new NotenPage());
			}
		};
		
		add(cousePanelLink);
		add(notenLink);
	}

}
