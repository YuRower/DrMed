package model;


public class VaccineTypeLocation {

	private int index;
	private String name;
	private String resource;

	public VaccineTypeLocation(int index, String name, String resource) {
		this.name = name;
		this.index = index;
		this.resource = resource;
	}


	public String getResource() {
		return resource;
	}

	
	public void setResource(String resource) {
		this.resource = resource;
	}

	
	public int getIndex() {
		return index;
	}

	
	public void setIndex(int index) {
		this.index = index;
	}

	public String getName() {
		return name;
	}

	
	public void setName(String name) {
		this.name = name;
	}

	
	@Override
	public String toString() {
		return name;
	}

}
