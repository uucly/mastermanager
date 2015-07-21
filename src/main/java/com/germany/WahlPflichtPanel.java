package com.germany;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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
	
	private static final List<String> SEARCH_ENGINES = Arrays.asList(new String[] {
			"Breunig", "Hinz", "Hennes", "Heck" });
	
	public WahlPflichtPanel(String id, IModel<String> selected) {
		super(id, selected);
		
		IModel<Collection<Modul>> moduleOfProf = new Model();
		try {
			List<Modul> allModule = module.parse(ALL_PATH);
			modulParser = new ModulParser(allModule);
			List<Modul> breunigModule = modulParser.parse(Prof.BREUNIG.getPath());
			moduleOfProf.setObject(breunigModule);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		
		add(createForm(selected, moduleOfProf));
	}
	
	private static Form createForm(IModel<String> selected, IModel<Collection<Modul>> moduleOfProf){
		Form form = new Form("form");
		ModulAutoCompleteTextField textField1 = new ModulAutoCompleteTextField("auto1", selected, moduleOfProf);
		ModulAutoCompleteTextField textField2 = new ModulAutoCompleteTextField("auto2", selected,moduleOfProf);
		ModulAutoCompleteTextField textField3 = new ModulAutoCompleteTextField("auto3", selected,moduleOfProf);
		ModulAutoCompleteTextField textField4 = new ModulAutoCompleteTextField("auto4", selected,moduleOfProf);

		DropDownChoice<String> dropDown = new DropDownChoice<String>("dropDown",selected, SEARCH_ENGINES);
		form.add(textField1);
		form.add(textField2);
		form.add(textField3);
		form.add(textField4);
		form.add(dropDown);
		return form;
	}
}
