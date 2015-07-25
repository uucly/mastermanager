package com.modul;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.ListModel;

import com.google.common.collect.Lists;

public class SelectedModulContainer implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private ListModel<Modul> profModuls;
	private Collection<IModel<String>> selectedModulNames;

	public SelectedModulContainer(){
		selectedModulNames = Lists.newArrayList();
		profModuls = new ListModel<>(Lists.newArrayList());
	}
	
	public SelectedModulContainer(ListModel<Modul> profModuls, Collection<IModel<String>> selectedModulNames){
		this.profModuls = profModuls;
		this.selectedModulNames = selectedModulNames;
	}

	public List<Modul> getObject() {
		Predicate<Modul> filterModuls = m -> selectedModulNames.stream().filter(name -> m.getName().equals(name.getObject())).findFirst().isPresent();
		return profModuls.getObject().stream().filter(filterModuls).collect(Collectors.toList());
	}

	public void setProfModuls(ListModel<Modul> profModuls) {
		this.profModuls = profModuls;
	}

	public void setSelectedModulNames(Collection<IModel<String>> selectedModulNames) {
		this.selectedModulNames = selectedModulNames;
	}
	
	public void addSelectedModulName(IModel<String> selectedModulNames) {
		this.selectedModulNames.add(selectedModulNames);
	}
	
	public double calculatePoints(){
		return getObject().stream().mapToDouble(Modul::getPoints).sum();
	}

}
