package com.hit.util;

public class RequestStatistics {

	private static RequestStatistics instance = null;
	private String algoName;
	private int capacity;
	private int modelsNum;
	private int swapNum;
	private int reqNum;

	// this singleton class will collect the requests data and will later send it
	// back to the client

	public static RequestStatistics getInstance() {

		if (instance == null) {
			instance = new RequestStatistics();
		}
		return instance;

	}

	public RequestStatistics() {
		modelsNum = 0;
		reqNum = 0;
	}
	
	//@SETTERS
	
	public void setAlgoName(String algoName) { //
		this.algoName = algoName;
	}

	public void setCapacity(int capacity) { //
		this.capacity = capacity;
	}

	public void addModels(int modelsNum) { //
		this.modelsNum +=modelsNum;
	}

	public void incrementSwapNum() { //
		this.swapNum++;
	}

	public void incrementReqNum(int reqNum) {
		this.reqNum += reqNum;
	}

	//@GETTERS
	
	public String getAlgoName() {
		return algoName;
	}

	public int getCapacity() {
		return capacity;
	}

	public int getModelsNum() {
		return modelsNum;
	}

	public int getSwapNum() {
		return swapNum;
	}

	public int getReqNum() {
		return reqNum;
	}

	@Override
	public String toString() {
		return "statistics,"+algoName+"," + capacity + "," + reqNum
				+ "," + modelsNum + "," + swapNum;
	}
	


}
