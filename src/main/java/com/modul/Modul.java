package com.modul;

import java.io.Serializable;

public class Modul implements Serializable{
	private static final long serialVersionUID = 1L;
	
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (inUse ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		long temp;
		temp = Double.doubleToLongBits(points);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Modul other = (Modul) obj;
		if (inUse != other.inUse)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (Double.doubleToLongBits(points) != Double
				.doubleToLongBits(other.points))
			return false;
		return true;
	}
	
	

}
