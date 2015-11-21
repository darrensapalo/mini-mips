//////////////////////////
//      Handlers       //
////////////////////////

function handleCodeBroadcast(err, msg){

	 if(msg.body){
		var text = msg.body['text'];
		var array = msg.body['array'];

		$('#textarea-code').val(text);
		populateTable('#table-opcode', array);
    }
    else{
    	$('#textarea-code').val('');
		populateTable('#table-opcode', []);
    }
}

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

function handleCPUBroadcast(err, msg){

	console.log(msg.body);

	if(msg.body){
		var array = msg.body['registers'];
      	populateTable('#table-cpu-registers', array);

		var array = msg.body['pipeline'];
      	populateTable('#table-cpu-pipeline', array);

      	

    }
}