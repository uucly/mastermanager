package de.master.manager.noten;

import java.io.IOException;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;

import de.master.manager.myproject.CoursePanel;
import de.master.manager.myproject.NotePanel;

public class NavsPanel extends Panel{

	private Panel currentPanel;
	
	public NavsPanel(String id) throws IOException {
		super(id);
		currentPanel = new CoursePanel("panel");
		currentPanel.setOutputMarkupPlaceholderTag(true);
		AjaxLink cousePanelLink = new AjaxLink("courses") {

			@Override
			public void onClick(AjaxRequestTarget target) {
				try {
					Panel newPanel= new CoursePanel("panel");
					newPanel.setOutputMarkupId(true);
					currentPanel.replaceWith(newPanel);
					currentPanel = newPanel;
					target.add(newPanel);
				} catch (IOException e) {
					e.printStackTrace();
				}
				add(new AttributeAppender("class", "active"));
			}
		};
		
		AjaxLink notenLink = new AjaxLink("noten") {

			@Override
			public void onClick(AjaxRequestTarget target) {
					Panel newPanel= new NotePanel("panel");
					newPanel.setOutputMarkupId(true);
					currentPanel.replaceWith(newPanel);
					currentPanel = newPanel;
					target.add(currentPanel);
					add(new AttributeAppender("class", "active"));
				}
		};
		
		add(cousePanelLink);
		add(notenLink);
		add(currentPanel);
	}

}
