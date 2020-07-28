package com.stockies.model;


public class Nifty50 {
	public Nifty50() {
		// TODO Auto-generated constructor stub
	}

	
	



	@Override
	public String toString() {
		return "Nifty50 [id=" + id + ", shortname=" + shortname + ", mktcap=" + mktcap + ", lastval=" + lastval
				+ ", change=" + change + ", percentagechange=" + percentagechange + ", direction=" + direction
				+ ", volume=" + volume + "]";
	}


	private String id;
	private String shortname;

	

	private String mktcap;



	private String lastval;

	

	private String change;

	

	private String percentagechange;

	

	private String direction;

	

	

	private String volume;
	
	

	

	
	
	public Nifty50(String id, String shortname, String mktcap, String lastval, String change, String percentagechange,
			String direction, String volume) {
		super();
		this.id = id;
		this.shortname = shortname;
		this.mktcap = mktcap;
		this.lastval = lastval;
		this.change = change;
		this.percentagechange = percentagechange;
		this.direction = direction;
		this.volume = volume;
	}






	public String getId() {
		return id;
	}






	public void setId(String id) {
		this.id = id;
	}






	public String getShortname() {
		return shortname;
	}






	public void setShortname(String shortname) {
		this.shortname = shortname;
	}






	public String getMktcap() {
		return mktcap;
	}






	public void setMktcap(String mktcap) {
		this.mktcap = mktcap;
	}






	public String getLastval() {
		return lastval;
	}






	public void setLastval(String lastval) {
		this.lastval = lastval;
	}






	public String getChange() {
		return change;
	}






	public void setChange(String change) {
		this.change = change;
	}






	public String getPercentagechange() {
		return percentagechange;
	}






	public void setPercentagechange(String percentagechange) {
		this.percentagechange = percentagechange;
	}






	public String getDirection() {
		return direction;
	}






	public void setDirection(String direction) {
		this.direction = direction;
	}






	public String getVolume() {
		return volume;
	}






	public void setVolume(String volume) {
		this.volume = volume;
	}





	




	

	
}
