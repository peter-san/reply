(function() {
  'use strict';
  var stompClient = null;
  
  function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
      $("#conversation").show();
    } else {
      $("#conversation").hide();
    }
    $("#greetings").html("");
  }
  
  $(document).ready(function() {
    var socket = new SockJS('/replay-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
      setConnected(true);
      console.log('Connected to Websocket: ' + frame);
      
      stompClient.subscribe('/topic/position-change', function(operationRoomMsg) {
        var event = JSON.parse(operationRoomMsg.body);
   
        $('#car_'+event.carId).css('left', event.currentPosition+'%');
        $('#bar_'+event.carId).css('width', event.aggregatedValue+'px');
        $('#bar_'+event.carId).text(event.aggregatedValue);
        
        console.log("Received position-change:" + event.carId);    
      });
    });
  });
  
})();