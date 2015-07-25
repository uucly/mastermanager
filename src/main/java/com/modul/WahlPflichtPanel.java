package com.modul;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.professoren.Prof;

public class WahlPflichtPanel extends Panel{

	private static final long serialVersionUID = 1L;

	private static final String ALL_PATH= "src/main/resources/WahlPflichtModule.txt";
	private static final List<Prof> SEARCH_ENGINES = Arrays.asList(Prof.values());
	
	@SpringBean
	private WahlPflichtModule module;
	
	public WahlPflichtPanel(String id, SelectedModulContainer selectedContainer) {
		super(id);
		add(createForm(createModulParser(module), selectedContainer));
	}
	
	private static Form<?> createForm(final ModulParser modulParser, SelectedModulContainer selectedModuls){
		IModel<String> selectedModul1 = Model.of(""), selectedModul2 = Model.of(""), selectedModul3 = Model.of(""), selectedModul4 = Model.of("");
		ListModel<Modul> moduleOfProf = new ListModel<Modul>();
		
		//selectedModuls.setProfModuls(moduleOfProf);
		//selectedModuls.addSelectedModulName(Arrays.asList(selectedModul1, selectedModul2, selectedModul3, selectedModul4));
		
		Form<?> form = new Form<Object>("form");
		form.add(new ModulAutoCompleteTextField("auto1", selectedModul1, moduleOfProf));
		form.add(new ModulAutoCompleteTextField("auto2", selectedModul2, moduleOfProf));
		form.add(new ModulAutoCompleteTextField("auto3", selectedModul3, moduleOfProf));
		form.add(new ModulAutoCompleteTextField("auto4", selectedModul4, moduleOfProf));
		form.add(createDropDown(moduleOfProf, modulParser));
		return form;
	}
	
	private static DropDownChoice<Prof> createDropDown(final IModel<List<Modul>> moduleOfProf, ModulParser modulParser){
		IModel<Prof> selected = Model.of(Prof.BREUNIG);
		setModulOfProf(moduleOfProf, modulParser, selected);
		DropDownChoice<Prof> dropDown = new DropDownChoice<Prof>("dropDown",selected, SEARCH_ENGINES);
		dropDown.add(new OnChangeAjaxBehavior() {
			
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				setModulOfProf(moduleOfProf, modulParser, selected);
			}
		});
		
		return dropDown;
	}
	
	
	private static void setModulOfProf( IModel<List<Modul>> moduleOfProf, ModulParser modulParser, IModel<Prof> selected){
		try{
			moduleOfProf.setObject(modulParser.parse(selected.getObject().getPath()));
		}catch(IOException ex){
			throw new RuntimeException(ex);
		}
	}
	
	private static ModulParser createModulParser(WahlPflichtModule module){
		try {
			List<Modul> allModule = module.parse(ALL_PATH);
			return new ModulParser(allModule);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
}
