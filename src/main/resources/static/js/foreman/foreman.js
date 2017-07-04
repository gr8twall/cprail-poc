var stompClient = null;

function setConnected(connected) {
	$("#connect").prop("disabled", connected);
	$("#disconnect").prop("disabled", !connected);
	if (connected) {
		$("#conversation").show();
		$("#chatmsg").show();
	} else {
		$("#conversation").hide();
		$("#chatmsg").hide();
	}
	$("#postmessage").html("");
}

function connect() {
	var socket = new SockJS('/cprail-websocket-endpt');
	
	stompClient = Stomp.over(socket);

	stompClient.connect({}, function(frame) {
		setConnected(true);
		console.log('Connected: ' + frame);
		stompClient.subscribe('/topic/postmessage', function(appMsg) {
			
			if(JSON.parse(appMsg.body).opCode === 'change_status') {
				showTrackInfo(JSON.parse(appMsg.body).msg);
			}
			if(JSON.parse(appMsg.body).opCode === 'change_state') {
				updateTrack(appMsg);
			}
			if(JSON.parse(appMsg.body).opCode == 'switch_track') {
				viewSwitchedTrack(appMsg);
			}
			
			
			if(JSON.parse(appMsg.body).opCode === 'send_bulletin') {
				bulletin(JSON.parse(appMsg.body).msg);
			}
			if(JSON.parse(appMsg.body).opCode === 'REQ' || JSON.parse(appMsg.body).opCode === 'ACTIVE') {
				getSubdivTracks(); 
			}
			if(JSON.parse(appMsg.body).opCode === 'ACK') {
				getSubdivTracks(); 
			}
			if(JSON.parse(appMsg.body).opCode === 'NACK') {
				getSubdivTracks(); 
			}
			
			if(JSON.parse(appMsg.body).opCode === 'chat') {
				showChat(JSON.parse(appMsg.body).msg); 
			}
			
			
		});
		
		stompClient.subscribe('/topic/postchat', function(appMsg) {
			console.log(msg);
			//if(JSON.parse(appMsg.body).opCode === 'chat') {
				showChat(msg.body); 
			//}
		});
	});
	
	
}

function disconnect() {
	if (stompClient != null) {
		stompClient.disconnect();
	}
	setConnected(false);
	console.log("Disconnected");
}

function bulletin(msg) {
	$("#bulletin").append("<tr><td>" + msg + "</td></tr>");
}


function sendChat() {
	var receiver = document.getElementById("receiver").value;
	var msg = document.getElementById("msg").value;
	stompClient.send("/cprail/chatmessage", {}, JSON.stringify({
		'receiver': receiver,
		'msg' : msg,
		'opCode': 'chat'
	}));
	
}
function showChat(msg) {
	$("#postmessage").append("<tr><td>" + msg + "</td></tr>");
}

function sendMessage() {
	stompClient.send("/cprail/managetrack", {}, JSON.stringify({
		'event':'change_status',
		'trackMsg' : $("#trackmsg").val()
	}));
	
}


function showTrackInfo(trackMsg) {
	$("#postmessage").append("<tr><td>" + trackMsg + "</td></tr>");
}


function changeColor() {
	// document.getElementById('track2').style.stroke = '#F44336';
	document.getElementById("track_altawan_2").setAttribute("stroke", "#F44336");
}


$(function() {
	$("form").on('submit', function(e) {
		e.preventDefault();
	});
	$("#connect").click(function() {
		connect();
	});
	$("#disconnect").click(function() {
		disconnect();
	});
	$("#send").click(function() {
		sendMessage();
	});
	$("#sendchat").click(function() {
		sendChat();
	});


});





function getSubdivTracks() {
	$("#subdivheader").text("Subdiv Tracks");
	var pouchdb = new PouchDB('tracks_subdiv_dummy');
	var couchdb = 'http://localhost:5984/tracks_subdiv_dummy';
	
	var opts = {live : true};
	pouchdb.sync(couchdb, opts);
	
	pouchdb.allDocs({
		include_docs : true,
		descending : true
	}, function(err, doc) {
		showSubdivTracks(doc.rows);
	});
}

function showSubdivTracks(tracks) {
	
	$("#track_subdiv_dummy").html("");
	var subdivTracks = "";
	//subdivTracks += "<table height=\"300\"><tr>";
	tracks.forEach(function(track){

		var trackdoc = track.doc;
		//var viewmap = trackdoc.view.TRACK;
		
		subdivTracks += "<td>";
		subdivTracks += "<svg id=\""+ trackdoc._id +"\" width=\"80\" height=\"80\" >";
		
		
		subdivTracks += "<a xlink:href=\"#p"+ trackdoc._id +"\" data-toggle=\"modal\" data-target=\"#stateui_"+ trackdoc._id  +"\">";
		subdivTracks += "<path id=\"p" + trackdoc._id +"\" d=\"" + trackdoc.dim + "\"";
		subdivTracks += " fill=\"" + trackdoc.fill + "\"";
		subdivTracks += " stroke=\"" + trackdoc.stroke + "\"";
		subdivTracks += " stroke-width=\"" + trackdoc.strokeWidth + "\" />";
		subdivTracks += "<text font-family=\"Verdana\" font-size=\"10\">" +
				"<textPath xlink:href=\"#p"+ trackdoc._id +"\">" + trackdoc._id +"</textPath></text>"
		subdivTracks += "</a>"			
		
		subdivTracks += "</svg>"
		subdivTracks += "</td>";
			
	});
	
	//subdivTracks += "</tr></table>";
	$("#track_subdiv_dummy").html(subdivTracks);
	
	setStateModal(tracks);
}

function setStateModal(tracks) {
	
	$("#state_ui").html("");
	var stateModals = "";
	var trackdoc;
	tracks.forEach(function(track){
		trackdoc = track.doc;
		stateModals += 
			"<div class=\"modal fade\" id=\"stateui_" + trackdoc._id +
			"\" role=\"dialog\"><div class=\"modal-dialog modal-md\">" +
			"<div class=\"modal-content\"><div class=\"modal-header\">" +
			"<button type=\"button\" class=\"close\" data-dismiss=\"modal\">&times;</button>" +
			"<h4 class=\"modal-title\">" + trackdoc._id + "</h4>" +
			"</div>" +

			
			"<div class=\"modal-body\" >";
			
			if(trackdoc.state=="ACTIVE") {
				stateModals +=	"<label class=\"col-xs-3 control-label\">Time Required</label>" +
				"<input type=\"text\" id=\"limits\" />" +
				"<label class=\"col-xs-1 control-label\">(HH)</label>";
			}
			if(trackdoc.state==="NACK") {
				stateModals += "<label class=\"col-xs-6 control-label\">Time Required (HH) : "+ trackdoc.limits +"</label>" +
				"<label class=\"col-xs-6 control-label\">TOP Type : "+ trackdoc.topType +"</label>";
			}
			
			stateModals += "</div>" +
			
			"<div class=\"modal-footer\">" +
			 "<button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\" onClick=\"request(\'" + trackdoc._id + "\')\" > REQUEST </button>" +
			 "<button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\" onClick=\"acknowledge(\'" + trackdoc._id + "\')\" > ACKNOWLEDGE </button>" +
			 "</div>" +
			"</div></div></div>";
		
	});
	
	$("#state_ui").html(stateModals);
}


function request(track) {
	
	var limits = $("#stateui_"+track+" #limits").val();
	
	stompClient.send("/cprail/implystate", {}, JSON.stringify({
		'opCode':'REQ',
		'id' : track,
		'limits': limits
	}));
	
}

$(function(){
	var limits;
	console.log('into submit');
	$('#TOPForm').on('submit', function(e){
		console.log('in submit');
		e.preventDefault();
		console.log('limits->');
		limits = $('#TOPForm').find('[name="n_limits"]').val();
		console.log('limits ---->');
		console.log(limits);
	});
	
//	var track_id = $('#TOPForm').find('[name="f_trackId"]').val();;
//	stompClient.send("/cprail/implystate", {}, JSON.stringify({
//		'opCode':'REQ',
//		'id' : track_id,
//		'msg' : limits
//	}));
//		
		
		
});




function stateChange(track) {
	var track_id = track;
	stompClient.send("/cprail/managetrack", {}, JSON.stringify({
		'event':'change_state',
		'trackId' : track_id
		/*,
		'stroke' : document.getElementById(track_id).getAttribute("stroke")*/
	}));
	
}

function updateTrack(track) {
		
//		var trackid = JSON.parse(track.body).trackId;
//		var trackstroke = JSON.parse(track.body).stroke;
//		document.getElementById(trackid).setAttribute("stroke", trackstroke);
		
		var trackid = JSON.parse(track.body)._id;
		var fillcolor  = JSON.parse(track.body).view.pathFill;
		document.getElementById(trackid).setAttribute("pathFill", fillcolor);
	
}


function switchTrack(fromtrack, totrack) {
	var fmtrack = fromtrack;
	var totrack = totrack;
	
	stompClient.send("/cprail/managetrack", {}, JSON.stringify({
		'event':'switch_track',
		'trackId' : fmtrack,
		'switchTo' : totrack
	}));
	
}

function viewSwitchedTrack(track) {
		
		var fromTrack = JSON.parse(track.body).trackId;
		var toTrack = JSON.parse(track.body).switchTo;
		
		document.getElementById(fromTrack).setAttribute("stroke-width", 3);
		document.getElementById(toTrack).setAttribute("stroke-width", 7);
}


function acknowledge(track) {
	var track_id = track;
	stompClient.send("/cprail/implystate", {}, JSON.stringify({
		'opCode':'ACK',
		'id' : track_id
	}));
	
}
































