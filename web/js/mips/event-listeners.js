//////////////////////////
//   Event Listeners   //
////////////////////////
$('#table-r-registers').on('keydown', onRegTableTDChange);
$('#table-f-registers').on('keydown', onRegTableTDChange);
$('#table-memory').on('keydown', onMemTableTDChange);
$('#button-go').on('click', onExecuteClick);

$('#button-clock-once').on('click', onClockOnceClick);

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
    
      var memAddress = td.parent().children()[0].innerHTML;
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

function onExecuteClick(){
  updateCode($('#textarea-code').val().trim());
}

function onClockOnceClick(){
  clock(false);
}