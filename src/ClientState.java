package projet;

public enum ClientState {
	PASSIVE,
	ACTIVE;
	
	public ClientState inverse() {
		if(this == PASSIVE)
			return ACTIVE;
		
		return PASSIVE;
	}
}
