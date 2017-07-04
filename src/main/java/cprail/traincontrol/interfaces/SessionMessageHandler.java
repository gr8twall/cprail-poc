package cprail.traincontrol.interfaces;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;

import cprail.traincontrol.model.Track;
import cprail.traincontrol.model.TrackId;

@Component
public class SessionMessageHandler {

	private final Set<StompHeaderAccessor> headerAccessors = new HashSet<>();

	private final List<String> trackMsgs = new ArrayList<>();

	public void addHeaderAccessor(StompHeaderAccessor headerAccessor) {
		// TODO Auto-generated method stub

		headerAccessors.add(headerAccessor);

		// tracks.add(addTrack(headerAccessor));

	}

//	private Track addTrack(StompHeaderAccessor headerAccessor) {
//		// TODO Auto-generated method stub
//
//		Track track = new Track(null, headerAccessor.getSessionId(), headerAccessor.getMessage(), null, null, null);
//
//		return track;
//	}

	private void sendToConnectedClient(Track track) {
		// TODO Auto-generated method stub

		// TODO should have a message factory
//		System.out.println("SESSION ID : " + track.getTrackId());
//		System.out.println("MESSAGE : " + track.getTrackMsg());
	}

	protected Set<StompHeaderAccessor> getHeaderAccessors() {
		return headerAccessors;
	}

	public void addTrack(String sessionId, String trackMsg) {
		// TODO Auto-generated method stub

		trackMsgs.add(trackMsg);
	}

	public String getTrackMessages() {

		return trackMsgs.toString();
	}

}
