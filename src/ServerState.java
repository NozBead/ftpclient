package projet;

public enum ServerState {
	WAITING_INFOS,
	WAITING_CMD,
	OCCUPIED;
	
	public static ServerState getStateFromCode(int code) {
		if(code >= 300 && code < 400) {
			return WAITING_INFOS;
		}
		else if(code >= 100 && code < 200) {
			return OCCUPIED;
		}
		else {
			return WAITING_CMD;
		}
	}
}
