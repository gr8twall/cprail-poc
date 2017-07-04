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

	stompClient.connect({"ptc-user": "EIC"}, function(frame) {
		setConnected(true);
		console.log('Connected: ' + frame);
		stompClient.subscribe('/topic/postmessage', function(appMsg) {
			
			if(JSON.parse(appMsg.body).opCode === 'change_status') {
				showTrackInfo(JSON.parse(appMsg.body).msg);
			}
			if(JSON.parse(appMsg.body).opCode === 'change_state') {
				updateTrack(appMsg);
			}
			if(JSON.parse(appMsg.body).opCode == 'eic_message') {
				eicMessage(appMsg);
			}
			
			
			if(JSON.parse(appMsg.body).opCode === 'send_bulletin') {
				bulletin(JSON.parse(appMsg.body).msg);
			}
			if(JSON.parse(appMsg.body).opCode === 'REQ' 
				|| JSON.parse(appMsg.body).opCode === 'ACTIVE'
				|| JSON.parse(appMsg.body).opCode === 'ACK'
				|| JSON.parse(appMsg.body).opCode === 'NACK'
					) {
				getSubdivTracks(); 
				
				if(JSON.parse(appMsg.body).opCode === 'REQ') {
					bulletin(JSON.parse(appMsg.body).msg);
				}
			}
			if(JSON.parse(appMsg.body).opCode === 'alert') {
				alert(JSON.parse(appMsg.body).msg);
			}
			
			if(JSON.parse(appMsg.body).opCode === 'chat') {
				showChat(JSON.parse(appMsg.body).msg); 
			}			
			
		});
		
		stompClient.subscribe('/topic/postchat', function(msg) {
			console.log(msg);
			//if(JSON.parse(appMsg.body).opCode === 'chat') {
				showChat(msg.body); 
			//}
		});
	});
	
	
}

function disconnect() {
	if (stompClient != null) {
		stompClient.disconnect({"ptc-user": "EIC"}, function(frame){
		});
	}
	setConnected(false);
	console.log("Disconnected");
}

function bulletin(msg) {
	//$("#bulletin").append("<tr><td>" + msg + "</td></tr>");
	
	$("#bulletin").append("<div class=\"alert alert-success alert-dismissable\">" +
			"<a href=\"#\" class=\"close\" data-dismiss=\"alert\" aria-label=\"close\">X</a><strong>TOP Request!</strong>" + msg + "</div>")
}

function alert(msg) {
	$("#bulletin").html("<div class=\"alert alert-danger alert-dismissable\">" +
			"<a href=\"#\" class=\"close\" data-dismiss=\"alert\" aria-label=\"close\">X</a><strong></strong>" + msg + "</div>")
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


function eicMessage(trackMsg) {
	$("#postmessage").append("<tr><td>" + trackMsg + "</td></tr>");
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
	$("#subdivheader").html(tracks);
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
			"<div class=\"modal-content\">" +
			"<div class=\"modal-header\"><button type=\"button\" class=\"close\" data-dismiss=\"modal\">&times;</button>" +
			"<h4 class=\"modal-title\">" + trackdoc._id + 
			"</h4></div>" +
			"<div class=\"modal-body\" >" +
			
			"<div><label class=\"col-xs-5 control-label\">Time Required (HH) : "+ trackdoc.limits + "</label></div>" +
			
			"<div class=\"modal-body\">" +
			//"<button class=\"btn btn-default dropdown-toggle\" type=\"button\" data-toggle=\"dropdown\">TOP Type<span class=\"caret\"></span></button>" +
			//"<label class=\"col-xs-3 control-label\">TOP Type</label>" +
			"<h4>TOP Type</h4>" +
			"<select id=\"top_type\" class=\"selectpicker\" >" +
			"<option value=\"REG\">Regular</option>" +
			"<option value=\"FFUP\">Follow-Up</option>" +
			"</select>" +
			"</div>" +
		
	        "</div>" +
			"<div class=\"modal-footer\">" +
			"<button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\" onClick=\"approve(\'" + trackdoc._id + "\')\" > APPROVE </button>" +
			"<button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\" onClick=\"deny(\'" + trackdoc._id + "\')\" > DENY </button>" +
			"</div>" +
			"</div></div></div>";
		
	});
	
	$("#state_ui").html(stateModals);
}

function buildTrackView(subdiv) {
	
	$("#subdivheader").text(subdiv);
	
//	var pouchdb= new PouchDB('tracks');
//	var remotesyncdb = 'http://localhost:5984/sync_tmp'
	
	var remote_db;
	var in_db;
	
	if (subdiv === 'Hoadley') {
		// TODO should be pointing to pouchdb
		remote_db = 'http://localhost:5984/tracks_hoadley';
		in_db = new PouchDB('tracks_hoadley');
	}
	
	if (subdiv === 'Altawan') {
		// TODO should be pointing to pouchdb
		remote_db = 'http://localhost:5984/tracks_altawan';
		in_db = new PouchDB('tracks_altawan');
	}
	
	var opts = {live : true};
	in_db.sync(remote_db, opts);
	in_db.allDocs({
		include_docs : true,
		descending : true
	}, function(err, doc) {
		buildTrackUI(doc.rows);
	});
	

	

}

function buildTrackUI(tracks) {
	$("#subdivision").html("");
	$("#state_ui").html("");
	
	var htmltxt = "<svg height=\"400\" width=\"100%\">";
	//$("#subdivision").append("<svg height=\"400\" width=\"100%\">");
	
	tracks.forEach(function(track){
		var trackdoc = track.doc;
		//console.log(trackdoc.track_id);
		
		htmltxt = htmltxt +"<a xlink:href=\"#\" class=\"btn btn-info btn-lg\" data-toggle=\"modal\" data-target=\"#stateui_" 
						+ trackdoc.track_id  + "\">";
		htmltxt = htmltxt + "<polyline id=\"" + trackdoc.track_id + "\" points=\"" + trackdoc.points + "\" stroke=\"" + trackdoc.stroke + "\" stroke-width=\"" 
				+ trackdoc.stroke_width + "\" " +	"stroke-linecap=\""+ trackdoc.stroke_linecap + "\" stroke-dasharray=\"" 
				+ trackdoc.stroke_dasharray + "\" />";
		htmltxt = htmltxt + "</a>";	
		
		// switch
		var cy = Number(trackdoc.y1) +10;
		if(trackdoc.switch) {
			htmltxt = htmltxt + "<circle cx=\"" + trackdoc.x1 + "\" cy=\""+ cy +"\" r=\"5\" stroke=\"black\" stroke-width=\"1\" fill=\"red\" " +
					"onClick=\"switchTrack('" + trackdoc.track_id +"','"+ trackdoc.switchto + "')\" />";
		}
		
		
		$("#state_ui").append(
				"<div class=\"modal fade\" id=\"stateui_" + trackdoc.track_id +"\" role=\"dialog\">" +
				"<div class=\"modal-dialog modal-md\">" +
				"<div class=\"modal-content\">" +
				"<div class=\"modal-header\"><button type=\"button\" class=\"close\" data-dismiss=\"modal\">&times;</button>" +
				"<h4 class=\"modal-title\">" + trackdoc.track_id + "</h4></div>" +
				"<div class=\"modal-body\"><a href=\"#\" onClick=\"stateChange(\'" + trackdoc.track_id + "\')\">Change State</a></div>" +
				"<div class=\"modal-footer\"><button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\">Close</button></div>" +
				"</div></div></div>");
		
		
	});
	
	htmltxt = htmltxt + "Sorry, your browser does not support inline SVG. </svg>";
	
	$("#subdivision").html(htmltxt);
	
	
	
}



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


function approve(track) {
	
	var topType = $("#stateui_"+track+" #top_type").val();
	
	var track_id = track;
	stompClient.send("/cprail/implystate", {}, JSON.stringify({
		'opCode':'NACK',
		'id' : track_id,
		'topType': topType
	}));
	
}

function deny(track) {
	var track_id = track;
	stompClient.send("/cprail/implystate", {}, JSON.stringify({
		'opCode':'DEN',
		'id' : track_id
	}));
	
}












































