package vwg.vw.km.integration.persistence.model;

public enum EnumCostgroup {	
	P("P"), // Produkt
	N("N"); // Produktnah

	void setValue(String desc) {
		this.desc = desc;
	}

	public String value() {
		return desc;
	}

	private EnumCostgroup(String desc) {
		this.desc = desc;
	}

	private String desc;


}
