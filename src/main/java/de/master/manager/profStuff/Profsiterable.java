package de.master.manager.profStuff;

import java.util.List;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;

public class Profsiterable {

	private final List<Prof> profs;
	
	private Profsiterable(List<Prof> profs){
		this.profs = profs;
	}
	
	
	public boolean contains(final ICourse course){
		return FluentIterable.from(profs).anyMatch(new Predicate<Prof>() {

			@Override
			public boolean apply(Prof input) {
				return input.getWahlModulSelected().contains(course) || input.getPflichtModulSelected().contains(course);
			}
		});
	}
	
	public static Profsiterable from(List<Prof> profs){
		return new Profsiterable(profs);
	}
	
	
}
