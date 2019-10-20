public class TCP {
	
	public static enum States
	{
		CLOSED,
		LISTEN,
		SYN_RCVD,
		SYN_SENT,
		ESTABLISHED,
		FIN_WAIT_1,
		CLOSING,
		FIN_WAIT_2,
		TIME_WAIT,
		CLOSE_WAIT,
		LAST_ACK
	}
	
	public static enum Action
	{
		APP_PASSIVE_OPEN,
		APP_ACTIVE_OPEN,
		APP_SEND,
		APP_CLOSE,
		APP_TIMEOUT,
		RCV_ACK,
		RCV_SYN,
		RCV_SYN_ACK,
		RCV_FIN,
		RCV_FIN_ACK
	}

	
	public static String traverseStates(String[] sa) {
		
		States currentState = States.LISTEN;
		for (String s : sa) {
			
			currentState = getNextState(currentState, Action.valueOf(s));
		}


		
	}
	
	public static States getNextState(States currentState, Action action) throws Exception {
		
		switch(currentState) {
		case CLOSED :
			if (action == Action.APP_PASSIVE_OPEN) return States.LISTEN;
			if (action == Action.APP_ACTIVE_OPEN) return States.SYN_SENT; 
			break;
		case LISTEN :
			if (action == Action.RCV_SYN) return States.SYN_RCVD;
			if (action == Action.APP_SEND) return States.SYN_SENT;
			if (action == Action.APP_CLOSE) return States.CLOSED;
			break;
		case SYN_RCVD :
			if (action == Action.APP_CLOSE) return States.FIN_WAIT_1;
			if (action == Action.RCV_ACK) return States.ESTABLISHED;
			break;
		case SYN_SENT :
			if (action == Action.RCV_SYN) return States.SYN_RCVD;
			
			if (action == Action.RCV_SYN_ACK) return States.ESTABLISHED;
			if (action == Action.APP_CLOSE) return States.CLOSED;
			break;
		case ESTABLISHED :
			if (action == Action.APP_CLOSE) return States.FIN_WAIT_1;
			if (action == Action.RCV_FIN) return States.CLOSE_WAIT;
			break;
		case FIN_WAIT_1 :
			if (action == Action.RCV_FIN) return States.FIN_WAIT_1;
			if (action == Action.RCV_FIN_ACK) return States.CLOSE_WAIT;
			if (action == Action.RCV_ACK) return States.CLOSING;
			break;
		case CLOSING :
			if (action == Action.RCV_ACK) return States.TIME_WAIT;
			break;
		case FIN_WAIT_2 :
			if (action == Action.RCV_FIN) return States.TIME_WAIT;
			break;
		case TIME_WAIT :
			if (action == Action.APP_TIMEOUT) return States.CLOSED;
			break;
		case CLOSE_WAIT :
			if (action == Action.APP_CLOSE) return States.LAST_ACK;
			break;
		case LAST_ACK :
			if (action == Action.RCV_ACK) return States.CLOSED;
			break;
		
		default:
			break;
		}

		
		throw new Exception("Invalid state/action combination");
	}

}
