package com.germany;

public class Modul {
	private final String name;
	private final int points;
	private boolean inUse;
	
	public Modul(String name, int points) {
		this.name = name;
		this.points = points;
		this.setInUse(false);
	}

	public int getPoints() {
		return points;
	}

	public String getName() {
		return name;
	}

	public boolean isInUse() {
		return inUse;
	}

	public void setInUse(boolean inUse) {
		this.inUse = inUse;
	}

}
