package com.modul;

import java.io.Serializable;

public class Modul implements Serializable{
	private final String name;
	private final double points;
	private boolean inUse;
	
	public Modul(String name, double points) {
		this.name = name;
		this.points = points;
		this.setInUse(false);
	}

	public double getPoints() {
		return points;
	}

	public String getName() {
		return name;
	}

	public boolean isInUse() {
		return inUse;
	}
	
	public boolean isNotInUse() {
		return !inUse;
	}

	public void setInUse(boolean inUse) {
		this.inUse = inUse;
	}

}
