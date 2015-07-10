package com.germany;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import org.apache.wicket.model.IModel;

import com.google.common.base.Strings;

public class ModulAutoCompleteTextField extends AutoCompleteTextField<String>{

	public ModulAutoCompleteTextField(String id, IModel<String> model) {
		super(id, model);
	}

	@Override
	protected Iterator<String> getChoices(String input) {
		 if (Strings.isNullOrEmpty(input))
         {
             List<String> emptyList = Collections.emptyList();
             return emptyList.iterator();
         }

         List<String> choices = new ArrayList<String>(10);

         Locale[] locales = Locale.getAvailableLocales();

         for (final Locale locale : locales)
         {
             final String country = locale.getDisplayCountry();

             if (country.toUpperCase().startsWith(input.toUpperCase()))
             {
                 choices.add(country);
                 if (choices.size() == 10)
                 {
                     break;
                 }
             }
         }

         return choices.iterator();
     }
	

}
