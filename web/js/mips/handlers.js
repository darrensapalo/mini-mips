//////////////////////////
//      Handlers       //
////////////////////////

function handleCodeBroadcast(err, msg){

	 if(msg.body){
		var text = msg.body['text'];
		var array = msg.body['array'];

		$('#textarea-code').val(text);
		populateTable('#table-opcode', array, opcodeColumns);
    }
    else{
    	$('#textarea-code').val('');
		populateTable('#table-opcode', [], opcodeColumns);
    }
}

function handleRegisterBroadcast(err, msg){


	// if this is false, it means there is no code
    if(msg.body){
	    var rArray = msg.body['r-registers'];
	    var fArray = msg.body['f-registers'];

	    populateTable('#table-r-registers', rArray, registerColumns);
	    populateTable('#table-f-registers', fArray, registerColumns);
	}
	else{
		populateTable('#table-r-registers', [], registerColumns);
	    populateTable('#table-f-registers', [], registerColumns);
	}

}

function handleMemoryBroadcast(err, msg){

	if(msg.body){
		var array = msg.body;
      	populateTable('#table-memory', array, memoryColumns);
    }
    else
	    populateTable('#table-memory', [], memoryColumns);

}

function handleCPUBroadcast(err, msg){

	console.log(msg.body);

	if(msg.body){
		var array = msg.body['registers'];
      	populateTable('#table-cpu-registers', array, registerColumns);

		var array = msg.body['pipeline'];
      	// populateTable('#table-cpu-pipeline', array, pipelineColumns);
      	populatePipelineTable(array);
    }
}