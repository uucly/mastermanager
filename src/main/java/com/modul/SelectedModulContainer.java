package com.modul;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.ListModel;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.professoren.Prof;

public class SelectedModulContainer implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private ListModel<Modul> profModuls;
	private Map<Prof,IModel<String>> selectedModulNames;

	public SelectedModulContainer(){
		selectedModulNames = Maps.newEnumMap(Prof.class);
		profModuls = new ListModel<>(Lists.newArrayList());
	}
	
	/*public SelectedModulContainer(ListModel<Modul> profModuls, Collection<IModel<String>> selectedModulNames){
		this.profModuls = profModuls;
		this.selectedModulNames = selectedModulNames;
	}*/

/*	public List<Modul> getSelectedModuls() {
		Predicate<Modul> filterModuls = m -> selectedModulNames.stream().filter(name -> m.getName().equals(name.getObject())).findFirst().isPresent();
		return profModuls.getObject().stream().filter(filterModuls).collect(Collectors.toList());
	}

	public void setProfModuls(ListModel<Modul> profModuls) {
		this.profModuls = profModuls;
	}

	public Collection<IModel<String>> getSelectedModulNames(){
		return this.selectedModulNames;
	}
	
	//public void setSelectedModulNames(Collection<IModel<String>> selectedModulNames) {
	//	this.selectedModulNames = selectedModulNames;
	//}
	
	public void addSelectedModulName(Prof prof, IModel<String> selectedModulName) {
		this.selectedModulNames.put(prof, selectedModulName);
	}
	
	public void addSelectedModulName(List<IModel<String>> selectedModulNames) {
		this.selectedModulNames.addAll(selectedModulNames);
	}
	
	public double calculatePoints(){
		return getSelectedModuls().stream().mapToDouble(Modul::getPoints).sum();
	}*/

}
