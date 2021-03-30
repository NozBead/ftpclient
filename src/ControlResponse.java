package projet;

public class ControlResponse {
	private int code;
	private String msg;
	
	public ControlResponse(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	
	public ControlResponse(String rawResponse) {
		treatRawResponse(rawResponse);
	}
	
	public ControlResponse() {
		this(-1,null);
	}
	
	public boolean isNegative() {
		return code >= 400;
	}
	
	public void treatRawResponse(String rawResponse) {
		int idx = findIdxMsg(rawResponse);
		
		code = isolateCode(rawResponse, idx);
		msg = isolateMsg(rawResponse, idx);
	}
	
	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	private int findIdxMsg(String rawResponse) {
		return rawResponse.indexOf(' ');
	}
	
	private int isolateCode(String rawResponse, int idxMsg) {
		return Integer.parseInt(rawResponse.substring(0, idxMsg));
	}
	
	private String isolateMsg(String rawResponse, int idxMsg) {
		return rawResponse.substring(idxMsg);
	}
}
