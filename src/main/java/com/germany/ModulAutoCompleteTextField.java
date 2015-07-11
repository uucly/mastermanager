package com.germany;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import org.apache.wicket.model.IModel;

import com.google.common.base.Strings;

public class ModulAutoCompleteTextField extends AutoCompleteTextField<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final List<Modul> moduls;
	private String selected;
	
	public ModulAutoCompleteTextField(String id, IModel<String> model, List<Modul> moduls) {
		super(id, model);
		this.moduls = moduls;
		selected = "";
		
		add(new OnChangeAjaxBehavior(){

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				moduls.stream().filter(m -> m.getName().equals(selected)).forEach(m -> m.setInUse(false));
				selected = getModelObject();
				moduls.stream().filter(m -> m.getName().equals(getModelObject())).forEach(m -> m.setInUse(true));
			}
			
		});
	}

	@Override
	protected Iterator<String> getChoices(String input) {
		if (Strings.isNullOrEmpty(input)) {
			return Collections.<String>emptyList().iterator();
		}

		return moduls.stream().filter(Modul::isNotInUse).filter(m -> m.getName().toUpperCase().startsWith(input.toUpperCase())).map(m -> m.getName()).collect(Collectors.toList()).iterator();
	}
	
}
