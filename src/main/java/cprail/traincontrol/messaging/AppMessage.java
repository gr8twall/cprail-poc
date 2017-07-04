package cprail.traincontrol.messaging;

public class AppMessage {
	
	private String id;
	private String opCode;
	private String msg;
	private String limits;
	private String topType;

	public AppMessage() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	public AppMessage(String id, String opCode, String msg, String limits, String topType) {
		super();
		this.id = id;
		this.opCode = opCode;
		this.msg = msg;
		this.limits = limits;
		this.topType = topType;
	}




	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOpCode() {
		return opCode;
	}

	public void setOpCode(String opCode) {
		this.opCode = opCode;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getLimits() {
		return limits;
	}

	public void setLimits(String limits) {
		this.limits = limits;
	}

	public String getTopType() {
		return topType;
	}

	public void setTopType(String topType) {
		this.topType = topType;
	}




	
	
}
