package de.master.manager.profStuff;

import java.io.Serializable;

public class Course implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private final String name;
	private final double points;
	
	public Course(String name, double points) {
		this.name = name;
		this.points = points;
	}

	public double getPoints() {
		return points;
	}

	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		Course other = (Course) obj;
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
