// The Cloud Functions for Firebase SDK to create Cloud Functions and setup triggers.
const functions = require('firebase-functions');

// The Firebase Admin SDK to access the Firebase Realtime Database. 
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);


// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
 exports.helloWorld = functions.https.onRequest((request, response) => {
  response.send("Hello world from Firebase!");
 });
 
  exports.generateGuitarPart = functions.https.onRequest((request, response) => {
	  //Use math to generate notes per instrument, eventually would like to generate these instrument peices through machine learning
	  var guitarPart = new Object();
	  var noteList = new Array(0,1,2,3,4,5,6,7);
	  guitarPart.notes = noteList;
	
	  response.send(guitarPart)
 });
 
 exports.sendFriendRequestNotification = functions.database.ref('/friend_request/{pushId}')
 .onWrite(event => {
	 
	 var friendRequestMessage = "Sent you a friend request";
	 
	 const friendRequest = event.data.current;
	 console.log(friendRequest);

	 const senderUid = friendRequest.child("sender").child("userId").val();
	 const receiverUid = friendRequest.child("receiver").child("userId").val();
	 
	 const statusMessage = friendRequest.child("status").val();
	 
	 const senderFirebaseId = friendRequest.child("sender").child("fireBaseToken").val()
	 const receiverFirebaseId = friendRequest.child("receiver").child("fireBaseToken").val();
	 console.log(receiverFirebaseId);
	 
	 //const senderName = friendRequest.child("sender").child("artistName").val()
	 //const receiverName = friendRequest.child("receiver").child("artistName").val()

 
	 console.log(friendRequest.val());
	 console.log(senderUid);
	 console.log(receiverUid);
	 console.log(statusMessage);
	 
     const promises = [];

	 if (senderUid == receiverUid) {    
		//if sender is receiver, don't send notification
        promises.push(event.data.current.ref.remove());				
		return Promise.all(promises);
	 }
	 
	 const getInstanceIdPromise = admin.database().ref('/friend_request/{pushId}').once('value');
     const getReceiverUidPromise = admin.auth().getUser(receiverUid);

     return Promise.all([getInstanceIdPromise, getReceiverUidPromise]).then(results => {
         console.log('notifying ' + receiverUid + ' about ' + friendRequestMessage + ' from ' + senderUid);

         const payload = {
			notification: {
				title: "Friend Request From Fake ",
				body: " Fake wants to be your friend on Brain Beats !"
			}
         };
		
	admin.messaging().sendToDevice("d5gbZX35us0:APA91bE3-GDI58bA_a-4jsq8_4UEPgOFf689CWCtJObhPGN1UgOaZyYzf17sY-VL72PFjO8vvLA57jaoRWpz0xc8HaOncyKwlSGhE5HgF0XX6TgcT56_78yahRpiYe8bQ_suZ6ZH-hQw", payload)
		.then(function (response) {
              console.log("Successfully sent message:", response);
        })
        .catch(function (error) {
              console.log("Error sending message:", error);
        });
	});
 });
 
 