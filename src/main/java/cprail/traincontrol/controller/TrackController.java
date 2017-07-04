package cprail.traincontrol.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.camel.CamelContext;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jboss.logging.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.node.ObjectNode;

import cprail.traincontrol.interfaces.SessionMessageHandler;
import cprail.traincontrol.messaging.AppMessage;
import cprail.traincontrol.messaging.ChatMessage;
import cprail.traincontrol.model.Track;
import cprail.traincontrol.model.TrackView;

@Controller
public class TrackController {

	private List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

	private final String DB_TRACKS_SUBDIV_DUMMY = "tracks_subdiv_dummy";
	private final String DB_TRACKS_SEG_NODE_TYPE = "track_seg_node_type_config";

	@Autowired
	private CamelContext camelContext;

	@Autowired
	@Qualifier("sessionRegistry")
	private SessionRegistry sessionRegistry;

	@Autowired
	SessionMessageHandler msgHandler;

	private SimpMessageSendingOperations simpMessagingTemplate;

	@Autowired
	public TrackController(SimpMessageSendingOperations simpMessagingTemplate) {
		super();
		this.simpMessagingTemplate = simpMessagingTemplate;
	}

	@MessageMapping("/managetrack")
	// @SendTo( value={"/topic/postmessage", "/topic/otherdelegation"})
	public void manageTrack(@Payload Track track, SimpMessageHeaderAccessor headerAccessor)
	// public Track manageTrack(@Payload Track track, SimpMessageHeaderAccessor headerAccessor)
			throws Exception {

		// TODO option for camel
		ObjectMapper mapper = new ObjectMapper();
		String jsonStr = mapper.writeValueAsString(track);

		camelContext.createProducerTemplate().sendBody("activemq:track_mq", jsonStr);

		// if ("change_status".equals(track.getEvent())) {
		// String sessionId = "";
		// if (null != headerAccessor && null != headerAccessor.getSessionId()) {
		// sessionId = headerAccessor.getSessionId();
		//
		// headerAccessor.setSessionId(sessionId);
		//
		// msgHandler.addTrack(sessionId, track.getTrackMsg());
		// }
		//
		// // TODO should persist
		//
		// simpMessagingTemplate.convertAndSend("/topic/postmessage",
		// new Track(track.getEvent(), track.getTrackId(), track.getTrackMsg(), null,
		// null, null));
		// }

		// if ("imply".equals(track.getEvent())) {
		//
		// // get the revision of document based on document id.
		// String trackId = track.get_id();
		//
		//
		// CloseableHttpClient httpClient = null;
		// CloseableHttpResponse response = null;
		//
		// HttpGet get = null;
		//
		// String couchdbResp = null;
		// StringBuilder url = new StringBuilder("http://127.0.0.1:5984/");
		// StringBuilder db = new StringBuilder(DB_TRACKS_SUBDIV + "/");
		//
		// httpClient = HttpClients.createDefault();
		//
		// db.append(trackId);
		// url.append(db);
		// get = new HttpGet(url.toString());
		// get.setHeader("Content-Type", "application/json");
		// get.setHeader("Accept", "application/json");
		//
		// try {
		//
		// response = httpClient.execute(get);
		// couchdbResp = EntityUtils.toString(response.getEntity());
		//
		// } catch (ParseException | IOException e) {
		// e.printStackTrace();
		// } finally {
		// // try {
		// // response.close();
		// // httpClient.close();
		// // } catch (IOException e) {
		// // e.printStackTrace();
		// // }
		// }
		//
		// track = mapper.readValue(couchdbResp, Track.class);
		//
		// track.setState("IMPLIED");
		// track.getView().setPathFill("yellow");
		//
		// // update the document
		// HttpPut put = new HttpPut();
		// put = new HttpPut(url.toString());
		// put.setHeader("Content-Type", "application/json");
		// put.setHeader("Accept", "application/json");
		// put.setHeader("If-Match", track.get_rev());
		// String body = "";
		// try {
		//
		// body = mapper.writeValueAsString(track);
		// put.setEntity((HttpEntity) new StringEntity(body, "UTF-8"));
		//
		// response = httpClient.execute(put);
		// couchdbResp = EntityUtils.toString(response.getEntity());
		//
		// } catch (ParseException | IOException e) {
		// e.printStackTrace();
		// } finally {
		// }
		//
		// track = mapper.readValue(couchdbResp, Track.class);
		// track.setEvent("imply"); // need to set application setting
		// track.getView().setPathFill("yellow"); // TODO temp fix
		//
		// // client should pick up the changes from db
		// simpMessagingTemplate.convertAndSend("/topic/postmessage", track);
		// }

		// if ("change_state".equals(track.getEvent())) {
		//
		// track.setStroke("orange");
		//
		// simpMessagingTemplate.convertAndSend("/topic/postmessage",
		// new Track(track.getEvent(), track.getTrackId(), track.getTrackMsg(),
		// track.getTrackState(),
		// track.getStroke(), track.getSwitchTo()));
		// }
		//
		// if ("switch_track".equals(track.getEvent())) {
		//
		// // TODO should persist
		//
		//simpMessagingTemplate.convertAndSend("/topic/postmessage",
		//new Track(track.getEvent(), track.getTrackId(), null, null, null,
		// track.getSwitchTo()));
		// }

	}

	@SuppressWarnings("deprecation")
	@MessageMapping("/implystate")
	public void reactState(@Payload AppMessage msg, SimpMessageHeaderAccessor headerAccessor) throws Exception {

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.setVisibilityChecker(mapper.getVisibilityChecker().withFieldVisibility(Visibility.ANY));
		ObjectNode node = null;
		Map<String, Object> resultMap = null; 
		
		Track dataTrack  = new Track();
		
		//String jsonStr = mapper.writeValueAsString(track);

		// camelContext.createProducerTemplate().sendBody("activemq:track_mq", jsonStr);

		// get the revision of document based on document id.
		String reqTrackId = msg.getId();
		String opCode = msg.getOpCode().toUpperCase();

		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;

		HttpGet get = null;

		String couchdbResp = null;
		StringBuilder url = new StringBuilder("http://127.0.0.1:5984/");
		StringBuilder db = new StringBuilder(DB_TRACKS_SUBDIV_DUMMY + "/");

		httpClient = HttpClients.createDefault();

		db.append(reqTrackId);
		url.append(db);
		get = new HttpGet(url.toString());
		get.setHeader("Content-Type", "application/json");
		get.setHeader("Accept", "application/json");
		
		//TrackView view = new TrackView();

		try {

			response = httpClient.execute(get);
			couchdbResp = EntityUtils.toString(response.getEntity());
			dataTrack = mapper.readValue(couchdbResp, Track.class);
//			node = mapper.readValue(couchdbResp, ObjectNode.class);

		} catch (ParseException | IOException e) {
			e.printStackTrace();
		} finally {
			// try {
			// response.close();
			// httpClient.close();
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
		}
		
//		resultMap = mapper.convertValue(node, Map.class);
//		Map trackMap = (Map) resultMap.get("view");
//		Map trackProps = (Map)trackMap.get("TRACK");
				
		if("REQ".equals(opCode)) {
			dataTrack.setState("REQ");
			dataTrack.setFill("#F7DC6F");
			dataTrack.setLimits(msg.getLimits());
		} else if("NACK".equals(opCode)) {
			dataTrack.setState("NACK");
			dataTrack.setFill("#2ECC71");
			dataTrack.setTopType(msg.getTopType());
		} else if("ACK".equals(opCode)) {
			dataTrack.setState("ACK");
			dataTrack.setFill("#EC7063");
		} else if("DEN".equals(opCode)) {
			dataTrack.setState("ACTIVE");
			dataTrack.setFill("#D7DBDD");
		}
		
		
		// update the document
		HttpPut put = new HttpPut();
		put = new HttpPut(url.toString());
		put.setHeader("Content-Type", "application/json");
		put.setHeader("Accept", "application/json");
		put.setHeader("If-Match", getRevision(dataTrack));
		String body = "";
		try {

			body = mapper.writeValueAsString(dataTrack);
			put.setEntity((HttpEntity) new StringEntity(body, "UTF-8"));

			response = httpClient.execute(put);
			couchdbResp = EntityUtils.toString(response.getEntity());
			

		} catch (ParseException | IOException e) {
			e.printStackTrace();
		} finally {
		}

		// state update successful
		msg.setOpCode(dataTrack.getState());
		msg.setMsg(" " + msg.getId() + " by Howard");

		Thread.sleep(2000);
		
		// TODO client should pick up the changes from db
		simpMessagingTemplate.convertAndSend("/topic/postmessage", msg);

		//String user = "";
		//simpMessagingTemplate.convertAndSendToUser(user, "/topic/postmessage", msg);

	}

	private String getRevision(Track track) {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		HttpGet get = null;

		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = null;

		StringBuilder url = new StringBuilder("http://127.0.0.1:5984/");
		StringBuilder db = new StringBuilder(DB_TRACKS_SUBDIV_DUMMY);
		db.append("/");
		db.append(track.get_id());
		db.append("/");

		httpClient = HttpClients.createDefault();
		String couchdbResp = "";

		url.append(db);
		get = new HttpGet(url.toString());
		get.setHeader("Content-Type", "application/json");
		get.setHeader("Accept", "application/json");

		try {

			response = httpClient.execute(get);
			couchdbResp = EntityUtils.toString(response.getEntity());
			node = mapper.readValue(couchdbResp, ObjectNode.class);

		} catch (ParseException | IOException e) {
			e.printStackTrace();
		} finally {
		}

		return node.get("_rev").toString();
	}
	
	@MessageMapping("/chatmessage")
	//public void manageChat(@Payload ChatMessage chatMsg, SimpMessageHeaderAccessor headerAccessor) {
	public void manageChat(@Payload ChatMessage chatMsg, Principal principal) {
		
		
		//simpMessagingTemplate.convertAndSendToUser(principal.getName(),"/queue/postchat", chatMsg);
		
		
		if(null != chatMsg.getReceiver() && !("").equals(chatMsg.getReceiver().trim())) {
			simpMessagingTemplate.convertAndSendToUser(chatMsg.getReceiver(), "/topic/postchat", chatMsg);
		} else {
			simpMessagingTemplate.convertAndSend("/topic/postmessage", chatMsg);
		}
		
	}

}
