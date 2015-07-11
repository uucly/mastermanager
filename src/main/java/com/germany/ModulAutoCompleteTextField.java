package com.germany;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import org.apache.wicket.model.IModel;

import com.google.common.base.Strings;

public class ModulAutoCompleteTextField extends AutoCompleteTextField<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final List<Modul> moduls;
	
	public ModulAutoCompleteTextField(String id, IModel<String> model, List<Modul> moduls) {
		super(id, model);
		this.moduls = moduls;
	}

	@Override
	protected Iterator<String> getChoices(String input) {
		 if (Strings.isNullOrEmpty(input))
         {
             List<String> emptyList = Collections.emptyList();
             return emptyList.iterator();
         }

         List<String> choices = new ArrayList<String>(moduls.size());
         for (final Modul m : moduls)
         {
             if (!m.isInUse() && m.getName().toUpperCase().startsWith(input.toUpperCase()))
             {
            	 choices.add(m.getName());
             }
         }

         return choices.iterator();
     }
	

}
