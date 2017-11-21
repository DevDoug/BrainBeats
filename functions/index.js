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
 
 exports.sendFriendRequestNotification = functions.database.ref('/friend_request/{pushId}')
 .onWrite(event => {
	 
	 var friendRequestMessage = "Sent you a friend request";
	 
	 const friendRequest = event.data.current;
	 console.log(friendRequest);

	 const senderUid = friendRequest.child("sender").child("userId").val();
	 const receiverUid = friendRequest.child("receiver").child("userId").val();
	 const statusMessage = friendRequest.child("status").val();
 
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
	 
	  console.log(getInstanceIdPromise);
	  console.log(getReceiverUidPromise);

	 

     return Promise.all([getInstanceIdPromise, getReceiverUidPromise]).then(results => {
		 const instanceId = results[0].val();
		 console.log(instanceId);
         const receiver = results[1];
		 console.log(receiver);
         console.log('notifying ' + receiverUid + ' about ' + friendRequestMessage + ' from ' + senderUid);

         const payload = {
			 notification: {
				 title: receiver.displayName,
                 body: friendRequestMessage,
                }
        };
		
		
    const tokens = Object.keys(instanceId.val());
	console.log(tokens);
	console.log(payload);
	
	
	admin.messaging().sendToDevice(tokens, payload)
		.then(function (response) {
              console.log("Successfully sent message:", response);
        })
        .catch(function (error) {
              console.log("Error sending message:", error);
        });
	});
 });