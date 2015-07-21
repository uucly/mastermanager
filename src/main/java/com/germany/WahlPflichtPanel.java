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
	private static final List<Prof> SEARCH_ENGINES = Arrays.asList(Prof.values());
	
	@SpringBean
	private WahlPflichtModule module;
	private ModulParser modulParser;
	
	private final IModel<Modul> selectedModul1,selectedModul2,selectedModul3,selectedModul4;
	
	public WahlPflichtPanel(String id) {
		super(id);
		
		try {
			List<Modul> allModule = module.parse(ALL_PATH);
			modulParser = new ModulParser(allModule);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		selectedModul1 = Model.of();
		selectedModul2 = Model.of();
		selectedModul3 = Model.of();
		selectedModul4 = Model.of();
		
		add(createForm(modulParser, selectedModul1, selectedModul2, selectedModul3, selectedModul4));
	}
	
	private static Form createForm(final ModulParser modulParser, IModel<Modul> selectedModul1, IModel<Modul> selectedModul2, IModel<Modul> selectedModul3, IModel<Modul> selectedModul4){
		IModel<Collection<Modul>> moduleOfProf = new Model();
		Form form = new Form("form");
		form.add(new ModulAutoCompleteTextField("auto1", selectedModul1, moduleOfProf));
		form.add(new ModulAutoCompleteTextField("auto2", selectedModul2, moduleOfProf));
		form.add(new ModulAutoCompleteTextField("auto3", selectedModul3, moduleOfProf));
		form.add(new ModulAutoCompleteTextField("auto4", selectedModul4, moduleOfProf));
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
	
	public List<IModel<Modul>> getAllSelectedModuls(){
		return Arrays.asList(selectedModul1,selectedModul2, selectedModul3, selectedModul4);
	}
}
