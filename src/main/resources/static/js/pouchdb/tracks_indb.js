(function() {

	'use strict';

	var ENTER_KEY = 13;
	var newTodoDom = document.getElementById('new-todo');
	var syncDom = document.getElementById('sync-wrapper');

	// EDITING STARTS HERE (you dont need to edit anything above this line)

	var db = new PouchDB('tracks');
	var remoteCouch = 'http://10.188.219.110:5984/tracks';

//	db.changes({
//		since : 'now',
//		live : true
//	}).on('change', showTodos);

	// We have to create a new todo document and enter it in the database
	function addTrack(trackid) {
		var track = {
			// _id: new Date().toISOString(),
			_id : trackid,
			state : false
		};
		db.put(track, function callback(err, result) {
			if (!err) {
				console.log('track added!');
			}
		});
	}

	function buildTrackView() {
		
		var opts = {
				live : true
			};

		db.sync(remoteCouch, opts, syncError);
		
		db.allDocs({
			include_docs : true,
			descending : true
		}, function(err, doc) {
			buildTrackUI(doc.rows);
		});
	}

	function buildTrackUI(tracks) {
		$("#subdivision").html(
			
			"<svg height=\"400\" width=\"100%\">" 
				
			//tracks.forEach(function(track) {});
				//ul.appendChild(buildSubdivTracksUI(track.doc));
				
				+"<line id=\"track_altawan_1\" x1=\"50\" y1=\"100\" x2=\"150\" y2=\"100\" stroke=\"#D6DBDF\" stroke-width=\"7\" stroke-linecap=\"round\" stroke-dasharray=\"5,5\" />"
				
			//});
		
			+"Sorry, your browser does not support inline SVG."
			+"</svg>"
			
		);
	}
	
	// Given an object representing a todo, this will create a list item
	// to display it.
	function buildSubdivTracksUI(todo) {
		var checkbox = document.createElement('input');
		checkbox.className = 'toggle';
		checkbox.type = 'checkbox';
		checkbox.addEventListener('change', checkboxChanged.bind(this, todo));

		var label = document.createElement('label');
		label.appendChild(document.createTextNode(todo.title));
		label.addEventListener('dblclick', todoDblClicked.bind(this, todo));

		var deleteLink = document.createElement('button');
		deleteLink.className = 'destroy';
		deleteLink.addEventListener('click', deleteButtonPressed.bind(this,
				todo));

		var divDisplay = document.createElement('div');
		divDisplay.className = 'view';
		divDisplay.appendChild(checkbox);
		divDisplay.appendChild(label);
		divDisplay.appendChild(deleteLink);

		var inputEditTodo = document.createElement('input');
		inputEditTodo.id = 'input_' + todo._id;
		inputEditTodo.className = 'edit';
		inputEditTodo.value = todo.title;
		inputEditTodo.addEventListener('keypress', todoKeyPressed.bind(this,
				todo));
		inputEditTodo.addEventListener('blur', todoBlurred.bind(this, todo));

		var li = document.createElement('li');
		li.id = 'li_' + todo._id;
		li.appendChild(divDisplay);
		li.appendChild(inputEditTodo);

		if (todo.completed) {
			li.className += 'complete';
			checkbox.checked = true;
		}

		return li;
	}

	function checkboxChanged(todo, event) {
		todo.completed = event.target.checked;
		db.put(todo);
	}

	// User pressed the delete button for a todo, delete it
	function deleteButtonPressed(todo) {
		db.remove(todo);
	}

	// The input box when editing a todo has blurred, we should save
	// the new title or delete the todo if the title is empty
	function todoBlurred(todo, event) {
		var trimmedText = event.target.value.trim();
		if (!trimmedText) {
			db.remove(todo);
		} else {
			todo.title = trimmedText;
			db.put(todo);
		}
	}

	// Initialise a sync with the remote server
	function sync() {
		syncDom.setAttribute('data-sync-state', 'syncing');
		var opts = {
			live : true
		};

		db.sync(remoteCouch, opts, syncError);

	}

	// EDITING STARTS HERE (you dont need to edit anything below this line)

	// There was some form or error syncing
	function syncError() {
		syncDom.setAttribute('data-sync-state', 'error');
	}

	// User has double clicked a todo, display an input so they can edit the
	// title
	function todoDblClicked(todo) {
		var div = document.getElementById('li_' + todo._id);
		var inputEditTodo = document.getElementById('input_' + todo._id);
		div.className = 'editing';
		inputEditTodo.focus();
	}

	// If they press enter while editing an entry, blur it to trigger save
	// (or delete)
	function todoKeyPressed(todo, event) {
		if (event.keyCode === ENTER_KEY) {
			var inputEditTodo = document.getElementById('input_' + todo._id);
			inputEditTodo.blur();
		}
	}


	function newTodoKeyPressHandler(event) {
		if (event.keyCode === ENTER_KEY) {
			addTodo(newTodoDom.value);
			newTodoDom.value = '';
		}
	}

	function addEventListeners() {
		newTodoDom.addEventListener('keypress', newTodoKeyPressHandler, false);
	}

//	addEventListeners();
//	showTodos();

//	if (remoteCouch) {
//		sync();
//	}

})();
