const functions = require('firebase-functions');

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
 exports.helloWorld = functions.https.onRequest((request, response) => {
  response.send("Hello world from Firebase!");
 });
 
 exports.sendFriendRequestNotification = functions.database.ref('/friend_request/{pushId}/user')
 .onWrite(event => {
       // Grab the current value of what was written to the Realtime Database.
      const original = event.data.val();
	  console.log('Friend Request Received', event.params.pushId, original);
	  
	  return null
 });