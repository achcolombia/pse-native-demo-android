package com.browser2app.pse.psenativedemo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AutomatonRequestResponse {
	@SerializedName("success")
	@Expose
	private Boolean success;
	@SerializedName("AutomatonRequestId")
	@Expose
	private String automatonRequestId;

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getAutomatonRequestId() {
		return automatonRequestId;
	}

	public void setAutomatonRequestId(String automatonRequestId) {
		this.automatonRequestId = automatonRequestId;
	}

	@Override
	public String toString() {
		return "AutomatonRequestResponse{" +
				"success=" + success +
				", automatonRequestId='" + automatonRequestId + '\'' +
				'}';
	}
}
