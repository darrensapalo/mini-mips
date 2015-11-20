//////////////////////////
//      Handlers       //
////////////////////////

function handleRegisterBroadcast(err, msg){

	// if this is false, it means there is no code
    if(msg.body){
	    var rArray = msg.body['r-registers'];
	    var fArray = msg.body['f-registers'];

	    populateTable('#table-r-registers', rArray);
	    populateTable('#table-f-registers', fArray);
	}
	else{
		populateTable('#table-r-registers', []);
	    populateTable('#table-f-registers', []);
	}

}

function handleMemoryBroadcast(err, msg){

	if(msg.body){
		var array = msg.body;
      	populateTable('#table-memory', array);
    }
    else
	    populateTable('#table-memory', []);

}