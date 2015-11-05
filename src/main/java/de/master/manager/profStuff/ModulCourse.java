package de.master.manager.profStuff;

import javax.annotation.Nullable;

import com.google.common.base.Optional;

public class ModulCourse implements ICourse{

	private static final long serialVersionUID = 1L;
	
	private Optional<Double> grade;
	private final String name;
	private final double points;

	public ModulCourse(String name, double points) {
		this.name = name;
		this.points = points;
		grade = Optional.absent();
	}

	public Optional<Double> getGrade() {
		return grade;
	}

	/**
	 * @param grade can be null
	 */
	public void setGrade(@Nullable Double grade) {
		this.grade = grade == null ? Optional.absent() : Optional.of(grade);
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
		ModulCourse other = (ModulCourse) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (Double.doubleToLongBits(points) != Double.doubleToLongBits(other.points))
			return false;
		return true;
	}

	

}
