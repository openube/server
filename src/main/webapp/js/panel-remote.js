var speak = document.querySelector('#speak');
var disagree = document.querySelector('#disagree');
var agree = document.querySelector('#agree');
var voteLabel = document.querySelector('#vote-label');
var voted;
//todo make this unique user for session management/voter registration
//var ws = slidfast.ws.join('client:anonymous2');

disablePoll();

speak.onclick = function(event) {
  _gaq.push(['_trackEvent', 'onslyde-option1', 'vote']);
  ws.send('speak:' + JSON.stringify(userObject));

};

var agreeTimeout,
  disagreeTimeout;

disagree.onclick = function(event) {
  _gaq.push(['_trackEvent', 'onslyde-disagree', 'vote']);
  ws.send('props:disagree,' + userObject.name + "," + userObject.email);
  disagree.disabled = true;
  disagree.style.opacity = .4;
  disagree.value = "vote again in 30 seconds";
  clearTimeout(disagreeTimeout);
  disagreeTimeout = setTimeout(function(){
    disagree.disabled = false;
    disagree.style.opacity = 1;
    disagree.value = 'Thumbs down!'
  },30000);
  return false;
};

agree.onclick = function(event) {
  _gaq.push(['_trackEvent', 'onslyde-agree', 'vote']);
  ws.send('props:agree,' + userObject.name + "," + userObject.email);
  agree.disabled = true;
  agree.style.opacity = .4;
  agree.value = "vote again in 30 seconds";
  clearTimeout(agreeTimeout);
  agreeTimeout = setTimeout(function(){
    agree.disabled = false;
    agree.style.opacity = 1;
    agree.value = 'Thumbs up!'
  },30000);
  return false;
};

function disablePoll(){
  disagree.disabled = true;
  disagree.disabled = true;
  agree.style.opacity = .4;
  agree.style.opacity = .4;

  speak.disabled = true;
  speak.style.opacity = .4;
  voteLabel.innerHTML = 'Waiting...';
}

function enablePoll(){
  speak.disabled = false;
  disagree.disabled = false;
  agree.disabled = false;
  disagree.value = 'Thumbs Down!';
  agree.value = 'agree!';
  speak.style.opacity = 1;
  disagree.style.opacity = 1;
  agree.style.opacity = 1;
  voteLabel.innerHTML = 'Vote!';
  voted = false;
}

window.addEventListener('updateOptions', function(e) {
  console.log('updateOptions',e);
  enablePoll();
}, false);

//callback for pressing the speak button (managed server side)
window.addEventListener('speak', function(e) {
  if(e.position === '777'){
    speak.value = 'Thanks for speaking!';
    setTimeout(function(){
      speak.value = 'I want to speak';
    },20000);

  }else{
    speak.value = 'You are queued to speak';
  }

}, false);

window.addEventListener('remoteMarkup', function(e) {
//  console.log('e', typeof e.markup);

  if(e.markup !== ''){
    var markup = jQuery.parseJSON(e.markup);
    try {
      document.getElementById('from-slide').innerHTML = decodeURIComponent(markup.remoteMarkup);
    } catch (e) {
    }
  }

  //checking for type of object due to the way this response comes back from polling vs. ws clients
  //this code is also duped as a filler for polling clients ... todo unify
  if(typeof e.data !== 'object' && e.data !== ''){

    var data = jQuery.parseJSON(e.data);
//    console.log('data.position', data.position);
    if(data !== '' && localStorage['onslyde.attendeeIP'] === data.attendeeIP){
      if(data.position === 777){
        speak.value = 'Thanks for speaking!';
      }else{
        speak.value = 'You are queued to speak';
      }
    }else{
      speak.value = 'I want to speak';
    }
  }

}, false);


window.addEventListener('roulette', function(e) {
  var rouletteDiv = document.getElementById('roulette'),
    timer1,
    timer2;
  rouletteDiv.style.display = 'block';
  if(!e.winner){
    //simple state check for multiple raffles on the same session
    if(rouletteDiv.style.backgroundColor !== 'yellow'){
      rouletteDiv.innerHTML = "<p>calculating...</p>";
      timer1 = setTimeout(function(){rouletteDiv.innerHTML = "<p>sorry! maybe next time :)</p>";},5000);
    }

  }else if(e.winner){
    setTimeout(function(){
      rouletteDiv.style.backgroundColor = 'yellow';
      rouletteDiv.innerHTML = "<p>WINNER!!...</p>";
    },5000);
  }
}, false);

function getParameterByName(name) {
  var match = RegExp('[?&]' + name + '=([^&]*)').exec(window.location.search);
  return match && decodeURIComponent(match[1].replace(/\+/g, ' '));
}