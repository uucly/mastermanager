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
	
	public WahlPflichtPanel(String id, SelectedModulContainer selectedContainer) {
		super(id);
		
		try {
			List<Modul> allModule = module.parse(ALL_PATH);
			modulParser = new ModulParser(allModule);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		add(createForm(modulParser, selectedContainer));
	}
	
	private static Form<?> createForm(final ModulParser modulParser, SelectedModulContainer selectedModuls){
		IModel<String> selectedModul1 = Model.of();
		IModel<String> selectedModul2 = Model.of();
		IModel<String> selectedModul3 = Model.of();
		IModel<String> selectedModul4 = Model.of();
		IModel<Collection<Modul>> moduleOfProf = new Model(); 
		
		selectedModuls.setProfModuls(moduleOfProf);
		selectedModuls.setSelectedModulNames(Arrays.asList(selectedModul1, selectedModul2, selectedModul3, selectedModul4));
		
		Form<?> form = new Form<Object>("form");
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
			private static final long serialVersionUID = 1L;

			@Override
			protected void onConfigure() {
				super.onConfigure();
				setModulOfProf(moduleOfProf, modulParser, selected);
			}
		};
		dropDown.add(new OnChangeAjaxBehavior() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

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
