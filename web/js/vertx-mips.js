//////////////////////////
//     Addresses       //
////////////////////////

var SERVER_IP_ADDRESS = $(location).attr('origin');
var CODE_INPUT_ADDRESS = 'code.input';
var REGISTER_REQUEST_ADDRESS = 'register.request_values';
var REGISTER_UPDATE_ADDRESS = 'register.update_values';


//////////////////////////
//      VERTX          //
////////////////////////

var eb = new EventBus(SERVER_IP_ADDRESS+"/eventbus");

eb.onopen = function(){
  $('button').removeClass('disabled');
  requestForRegisterValues();
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

  eb.send(CODE_INPUT_ADDRESS, '.text\r\n'.concat($('#textarea-code').val().trim()), function(err, msg){

    $('#button-go').button('reset');

    if(!msg.body){
      alert('Input code is invalid. Please double check your syntax.');
    }

    populateTable('#table-opcode', msg.body);

  });

}

function requestForRegisterValues(){

  if(!validateEbState())
    return;

  eb.send(REGISTER_REQUEST_ADDRESS, '', function(err, msg){

    if(err){
      alert("Failed to get register values from server.");
    }

    console.log(msg.body);

    var rArray = msg.body['r-registers'];
    var fArray = msg.body['f-registers'];

    populateTable('#table-r-registers', rArray);
    populateTable('#table-f-registers', fArray);
    
  });

}

function updateRegisterValue(regName , regValue){

  data = {}
  data['name'] = regName;
  data['value'] = regValue;

  eb.send(REGISTER_UPDATE_ADDRESS, data, function(err, msg){
    if(err || !msg.body){
      alert("Invalid register value.");
    }
    requestForRegisterValues();

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
            if(d[0] != 'R' && d[0] !='F')
              return true;
            else
              return false;
        });
}

//////////////////////////
//   Event Listeners   //
////////////////////////
$('#table-r-registers').on('keydown', onRTableTDChange);
$('#table-f-registers').on('keydown', onFTableTDChange);


function onRTableTDChange(event) {
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
    
      var regName = $("#table-r-registers tr:nth-child("+row+") td:first-child").text();
      var regValue = el.innerHTML;

      updateRegisterValue(regName, regValue);

      el.blur();
      event.preventDefault();
    }
  }
}

function onFTableTDChange(event) {
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
    
      var regName = $("#table-f-registers tr:nth-child("+row+") td:first-child").text();
      var regValue = el.innerHTML;

      updateRegisterValue(regName, regValue);

      el.blur();
      event.preventDefault();
    }
  }
}
