package com.germany;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import professoren.Prof;

public class WahlPflichtPanel extends Panel{

	private static final long serialVersionUID = 1L;

	private static final String ALL_PATH= "src/main/resources/WahlPflichtModule.txt";
	
	@SpringBean
	private WahlPflichtModule module;
	private ModulParser modulParser;
	
	private static final List<Prof> SEARCH_ENGINES = Arrays.asList(Prof.values());
	
	public WahlPflichtPanel(String id) {
		super(id);
		
		try {
			List<Modul> allModule = module.parse(ALL_PATH);
			modulParser = new ModulParser(allModule);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		
		add(createForm(modulParser));
	}
	
	private static Form createForm(final ModulParser modulParser){
		IModel<Collection<Modul>> moduleOfProf = new Model();
		
		Form form = new Form("form");
		form.add(new ModulAutoCompleteTextField("auto1", Model.of(""), moduleOfProf));
		form.add(new ModulAutoCompleteTextField("auto2", Model.of(""), moduleOfProf));
		form.add(new ModulAutoCompleteTextField("auto3", Model.of(""), moduleOfProf));
		form.add(new ModulAutoCompleteTextField("auto4", Model.of(""), moduleOfProf));
		form.add(createDropDown(moduleOfProf, modulParser));
		return form;
	}
	
	private static DropDownChoice<Prof> createDropDown(final IModel<Collection<Modul>> moduleOfProf, ModulParser modulParser){
		IModel<Prof> selected = Model.of(Prof.BREUNIG);
		DropDownChoice<Prof> dropDown = new DropDownChoice<Prof>("dropDown",selected, SEARCH_ENGINES){
			@Override
			protected void onConfigure() {
				super.onConfigure();
				setModulOfProf(moduleOfProf, modulParser, selected);
			}
		};
		dropDown.add(new OnChangeAjaxBehavior() {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				setModulOfProf(moduleOfProf, modulParser, selected);
			}
		});
		
		return dropDown;
	}
	
	
	
	private static void setModulOfProf( IModel<Collection<Modul>> moduleOfProf, ModulParser modulParser, IModel<Prof> selected){
		try{
			moduleOfProf.setObject(modulParser.parse(selected.getObject().getPath()));
		}catch(IOException ex){
			throw new RuntimeException(ex);
		}
	}
}
