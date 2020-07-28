package com.stockies.model;


public class KeyStats {

	public KeyStats() {
		// TODO Auto-generated constructor stub
	}
	
	private String username;
	private String shortname;
	private String mktcap;
	private String lastvalue;
	private String chg;
	private String percentchange;
	private String yearlyhigh;
	private String yearlylow;
	private String volume;
	private String fdayavgvol;
	private String todaysopen;
	private String timestamp1;
	private String timestamp_to_display;




	public KeyStats(String username, String shortname, String mktcap, String lastvalue, String chg,
			String percentchange, String yearlyhigh, String yearlylow, String volume, String fdayavgvol,
			String todaysopen, String timestamp1,String timestamp_to_display) {
		super();
		this.username = username;
		this.shortname = shortname;
		this.mktcap = mktcap;
		this.lastvalue = lastvalue;
		this.chg = chg;
		this.percentchange = percentchange;
		this.yearlyhigh = yearlyhigh;
		this.yearlylow = yearlylow;
		this.volume = volume;
		this.fdayavgvol = fdayavgvol;
		this.todaysopen = todaysopen;
		this.timestamp1 = timestamp1;
		this.timestamp_to_display=timestamp_to_display;
	}



	public KeyStats(String shortname, String mktcap, String lastvalue, String chg, String percentchange,
			String yearlyhigh, String yearlylow, String volume, String fdayavgvol, String todaysopen) {
		super();
		this.shortname = shortname;
		this.mktcap = mktcap;
		this.lastvalue = lastvalue;
		this.chg=chg;
		this.percentchange = percentchange;
		this.yearlyhigh = yearlyhigh;
		this.yearlylow = yearlylow;
		this.volume = volume;
		this.fdayavgvol = fdayavgvol;
		this.todaysopen = todaysopen;
	}



	public String getUsername() {
		return username;
	}



	public void setUsername(String username) {
		this.username = username;
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



	public String getLastvalue() {
		return lastvalue;
	}



	public void setLastvalue(String lastvalue) {
		this.lastvalue = lastvalue;
	}



	public String getChg() {
		return chg;
	}



	public void setChg(String chg) {
		this.chg = chg;
	}



	public String getPercentchange() {
		return percentchange;
	}



	public void setPercentchange(String percentchange) {
		this.percentchange = percentchange;
	}



	public String getYearlyhigh() {
		return yearlyhigh;
	}



	public void setYearlyhigh(String yearlyhigh) {
		this.yearlyhigh = yearlyhigh;
	}



	public String getYearlylow() {
		return yearlylow;
	}



	public void setYearlylow(String yearlylow) {
		this.yearlylow = yearlylow;
	}



	public String getVolume() {
		return volume;
	}



	public void setVolume(String volume) {
		this.volume = volume;
	}



	public String getFdayavgvol() {
		return fdayavgvol;
	}



	public void setFdayavgvol(String fdayavgvol) {
		this.fdayavgvol = fdayavgvol;
	}



	public String getTodaysopen() {
		return todaysopen;
	}



	public void setTodaysopen(String todaysopen) {
		this.todaysopen = todaysopen;
	}



	public String getTimestamp1() {
		return timestamp1;
	}



	public void setTimestamp1(String timestamp1) {
		this.timestamp1 = timestamp1;
	}
	
	



	public String getTimestamp_to_display() {
		return timestamp_to_display;
	}



	public void setTimestamp_to_display(String timestamp_to_display) {
		this.timestamp_to_display = timestamp_to_display;
	}



	@Override
	public String toString() {
		return "KeyStats [username=" + username + ", shortname=" + shortname + ", mktcap=" + mktcap + ", lastvalue="
				+ lastvalue + ", chg=" + chg + ", percentchange=" + percentchange + ", yearlyhigh=" + yearlyhigh
				+ ", yearlylow=" + yearlylow + ", volume=" + volume + ", fdayavgvol=" + fdayavgvol + ", todaysopen="
				+ todaysopen + ", timestamp1=" + timestamp1 + ", timestamp_to_display=" + timestamp_to_display + "]";
	}



	
	
	
	

}
	


