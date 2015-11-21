//////////////////////////
//      Messages       //
////////////////////////

function requestForCode(){
  if(!validateEbState())
    return;

  eb.send(CODE_REQUEST_ADDRESS, '', function(err, msg){

    if(err){
      alert("Failed to get code from server.");
    }

    // if this is false, it means there is no code yet
    if(msg.body){
      var text = msg.body['text'];
      var array = msg.body['array'];

      $('#textarea-code').val(text);
      populateTable('#table-opcode', array);
    }

  });
}

function updateCode(code){

  if(!validateEbState())
    return;
  
  $('#button-go').button('loading');

  eb.send(CODE_UPDATE_ADDRESS, code, function(err, msg){
    
    // $('#button-go').button('reset');

    // if(!msg.body || !msg.body['isSuccessful']){
    //   $('#span-code-error').html(msg.body['errors']);
    //   $('#div-code-error').show();    
    // }
    // else{
    //   $('#div-code-error').hide();   
    //   requestForCode(); // to refresh the opcode table
    // }

  });

}

function requestForRegisterValues(){

  if(!validateEbState())
    return;

  eb.send(REGISTER_REQUEST_ADDRESS, '', function(err, msg){

    if(err || !msg.body){
      alert("Failed to get register values from server.");
    }

    var rArray = msg.body['r-registers'];
    var fArray = msg.body['f-registers'];

    populateTable('#table-r-registers', rArray);
    populateTable('#table-f-registers', fArray);
    
  });

}

function updateRegisterValue(regName , regValue){

  data = {}
  data['name'] = regName;
  data['value'] = regValue.trim();

  eb.send(REGISTER_UPDATE_ADDRESS, data, function(err, msg){
    // if(err || !msg.body){
    //   alert("Invalid register value.");
    // }
    // requestForRegisterValues();

  });

}

function requestForMemoryValues(type){
  if(!validateEbState())
    return;

  eb.send(MEMORY_REQUEST_ADDRESS, type, function(err, msg){

    if(err || !msg.body){
      alert("Failed to get data memory values from server.");
    }

    var array = msg.body;

    populateTable('#table-memory', array);
  });
}

function updateMemoryValue(memAddress, memValue){
  data = {}
  data['address'] = memAddress;
  data['value'] = memValue.trim();

  eb.send(MEMORY_UPDATE_ADDRESS, data, function(err, msg){
    // if(err || !msg.body){
    //   alert("Invalid memory value.");
    // }
    // requestForMemoryValues('data');
  });
}