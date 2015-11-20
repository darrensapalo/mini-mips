//////////////////////////
//      Handlers       //
////////////////////////

function handleRegisterBroadcast(err, msg){

	console.log(msg.body);

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