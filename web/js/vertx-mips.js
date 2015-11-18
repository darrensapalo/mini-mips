//////////////////////////
//     Addresses       //
////////////////////////

var SERVER_IP_ADDRESS = $(location).attr('origin');
var CODE_INPUT_ADDRESS = 'code.input';


//////////////////////////
//      VERTX          //
////////////////////////

var eb = new EventBus(SERVER_IP_ADDRESS+"/eventbus");

eb.onopen = function(){
  $('button').removeClass('disabled');
};

//Used to close a connection to the event bus
function closeConn(){
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
//      On Click       //
////////////////////////

function sendCodeToBackend(){

  if(!validateEbState())
    return;
  
  $("#button-go").button('loading');

  eb.send(CODE_INPUT_ADDRESS, $('#textarea-code').val().trim(), function(err, msg){

    if(!msg.body){
      alert("Problem sending code to server. Please try again.");
    }

    $("#button-go").button('reset');

  });

}