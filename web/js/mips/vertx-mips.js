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

var REGISTER_BROADCAST_ADDRESS = "register.broadcast";
var MEMORY_BROADCAST_ADDRESS = "memory.broadcast";
var CODE_BROADCAST_ADDRESS = "code.broadcast";

//////////////////////////
//      VERTX          //
////////////////////////

var eb = new EventBus(SERVER_IP_ADDRESS+"/eventbus");

eb.onopen = function(){
  $('button').removeClass('disabled');
  requestForCode();
  requestForRegisterValues();
  requestForMemoryValues('data');
  initHandlers();
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
  eb.registerHandler(REGISTER_BROADCAST_ADDRESS, handleRegisterBroadcast);
  eb.registerHandler(MEMORY_BROADCAST_ADDRESS, handleMemoryBroadcast);
  
}