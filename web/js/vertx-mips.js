//////////////////////////
//     Addresses       //
////////////////////////

var SERVER_IP_ADDRESS = $(location).attr('origin');
var CODE_REQUEST_ADDRESS = 'code.request';
var CODE_UPDATE_ADDRESS = 'code.update';
var REGISTER_REQUEST_ADDRESS = 'register.request_values';
var REGISTER_UPDATE_ADDRESS = 'register.update_values';
var MEMORY_REQUEST_ADDRESS = "memory.request_values";
var MEMORY_UPDATE_ADDRESS = "memory.update_values";

//////////////////////////
//      VERTX          //
////////////////////////

var eb = new EventBus(SERVER_IP_ADDRESS+"/eventbus");

eb.onopen = function(){
  $('button').removeClass('disabled');
  requestForCode();
  requestForRegisterValues();
  requestForMemoryValues('data');
};

eb.onclose = function(){
  window.location.reload(true);
}

//Used to close a connection to the event bus
function closeEbConnection(){
  if(eb)
    eb.close();
}

function validateEbState(){
  if(!eb){
    alert("Event bus has not loaded yet. Try refreshing if it still does not load after a while.");
    return false;
  }
  return true;
}

function initHandlers(){
  // register handlers for diff addresses here
}

//////////////////////////
//      Messages       //
////////////////////////

function sendCodeToBackend(){

  if(!validateEbState())
    return;
  
  $('#button-go').button('loading');

  eb.send(CODE_UPDATE_ADDRESS, '.text\r\n'.concat($('#textarea-code').val().trim()), function(err, msg){
    
    if(!msg.body){
      alert('Input code is invalid. Please double check your syntax.');
    }

    $('#button-go').button('reset');

    requestForCode();
  });

}

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
    if(err || !msg.body){
      alert("Invalid register value.");
    }
    requestForRegisterValues();

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
    if(err || !msg.body){
      alert("Invalid memory value.");
    }
    requestForMemoryValues('data');
  });
}


//////////////////////////
//      Utility        //
////////////////////////

function populateTable(tableID, data){

  var tableRows = d3.select(tableID).select('tbody').selectAll('tr').data(data);
  tableRows.remove();
  tableRows.exit().remove();

  tableRows = d3.select(tableID).select('tbody').selectAll('tr').data(data);

  tableRows.enter()
    .append('tr')
      .selectAll('td')
      .data(function(d){return d3.values(d);})
      .enter()
      .append('td')
        .text(function(d){return d;})
        .attr('class', 'text-nowrap')
        .attr('contenteditable', function(d){
          if(d.length == 16 && d.indexOf(' ') < 0)
            return true;
          return false;
        });
}

//////////////////////////
//   Event Listeners   //
////////////////////////
$('#table-r-registers').on('keydown', onRegTableTDChange);
$('#table-f-registers').on('keydown', onRegTableTDChange);
$('#table-memory').on('keydown', onMemTableTDChange);

function onMemTableTDChange(event) {
  var esc = event.which == 27,
      nl = event.which == 13,
      el = event.target,
      input = el.nodeName =='TD',
      data = {};

  if (input) {
    if (esc) {
      // restore state
      document.execCommand('undo');
      el.blur();
    } else if (nl) {
    
      var td = $(event.target);
      var col = td.index() + 1;
      var row = td.parent().index() + 1;
    
      var memAddress = td.parent().children()[1].innerHTML;
      var memValue = el.innerHTML;

      updateMemoryValue(memAddress, memValue);

      el.blur();
      event.preventDefault();
    }
  }
}


function onRegTableTDChange(event) {
  var esc = event.which == 27,
      nl = event.which == 13,
      el = event.target,
      input = el.nodeName =='TD',
      data = {};

  if (input) {
    if (esc) {
      // restore state
      document.execCommand('undo');
      el.blur();
    } else if (nl) {
    
      var td = $(event.target);
      var col = td.index() + 1;
      var row = td.parent().index() + 1;

      var regName = td.parent().children()[0].innerHTML;
      var regValue = el.innerHTML;

      updateRegisterValue(regName, regValue);

      el.blur();
      event.preventDefault();
    }
  }
}
