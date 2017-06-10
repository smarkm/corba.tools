package org.smark.corba.tools.core;

public class RegistEntity {
	private String name;
	private String ior;
	public RegistEntity(){}
	public RegistEntity(String name, String ior) {
		super();
		this.name = name;
		this.ior = ior;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIor() {
		return ior;
	}
	public void setIor(String ior) {
		this.ior = ior;
	}
	@Override
	public String toString() {
		return "RegistEntity [name=" + name + ", ior=" + ior + "]";
	}
	
}
