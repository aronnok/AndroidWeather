package com.android.softart.janet;


/**
Ultimate Weather API
 */
public class WeatherCurrentCondition {

	// ===========================================================
	// Fields
	// ===========================================================

	private String dayofWeek = null;
	private Integer tempCelciusHigh = null;
	private Integer tempFahrenheitHigh = null;
	private Integer tempCelciusLow = null;
	private Integer tempFarenheitLow = null;
	private String condition = null;

	// ===========================================================
	// Constructors
	// ===========================================================

	public WeatherCurrentCondition() {

	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public String getDayofWeek() {
		return this.dayofWeek;
	}

	public void setDayofWeek(String dayofWeek) {
		this.dayofWeek = dayofWeek;
	}

	public Integer getTempCelciusHigh() {
		return tempCelciusHigh;
	}

	public void setTempCelciusHigh(Integer tempCelciusHigh) {
		this.tempCelciusHigh = tempCelciusHigh;
	}

	public Integer getTempFahrenheitHigh() {
		return tempFahrenheitHigh;
	}

	public void setTempFahrenheitHigh(Integer tempFahrenheitHigh) {
		this.tempFahrenheitHigh = tempFahrenheitHigh;
	}

	public Integer getTempCelciusLow() {
		return tempCelciusLow;
	}

	public void setTempCelciusLow(Integer tempCelciusLow) {
		this.tempCelciusLow = tempCelciusLow;
	}

	public Integer getTempFarenheitLow() {
		return tempFarenheitLow;
	}

	public void setTempFarenheitLow(Integer tempFarenheitLow) {
		this.tempFarenheitLow = tempFarenheitLow;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}
	
	
	
}
