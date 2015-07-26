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
	
	public WahlPflichtPanel(String id, IModel<Prof> prof) {
		super(id);
		add(createForm(createModulParser(module), prof));
	}
	
	private static Form<?> createForm(final ModulParser modulParser, IModel<Prof> prof){
		IModel<Modul> selectedModul1 = Model.of(new Modul("",0)), selectedModul2 = Model.of(new Modul("",0)), selectedModul3 = Model.of(new Modul("",0)), selectedModul4 = Model.of(new Modul("",0));
		ListModel<Modul> moduleOfProf = new ListModel<Modul>();
		prof.getObject().addSelectedModul(selectedModul1.getObject());
		prof.getObject().addSelectedModul(selectedModul2.getObject());
		prof.getObject().addSelectedModul(selectedModul3.getObject());
		prof.getObject().addSelectedModul(selectedModul4.getObject());
		
		
		
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
		moduleOfProf.setObject(loadModulsOfProf(modulParser, selected));
		DropDownChoice<Prof> dropDown = new DropDownChoice<Prof>("dropDown",selected, SEARCH_ENGINES);
		dropDown.add(new OnChangeAjaxBehavior() {
			
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				moduleOfProf.setObject(loadModulsOfProf(modulParser, selected));
			}
		});
		
		return dropDown;
	}
	
	
	private static List<Modul> loadModulsOfProf(ModulParser modulParser, IModel<Prof> selected){
		try{
			return modulParser.parse(selected.getObject().getPath());
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
