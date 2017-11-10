const functions = require('firebase-functions');

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
 exports.helloWorld = functions.https.onRequest((request, response) => {
  response.send("Hello world from Firebase!");
 });
 
 exports.sendFriendRequestNotification = functions.database.ref('/friend_request/{pushId}')
 .onWrite(event => {
	 var friendRequestMessage = "Sent you a friend request";
	 
	 const friendRequest = event.data.current.val();
	 console.log(friendRequest);
	 
     const senderUid = friendRequest.sender.userId;
     const receiverUid = friendRequest.receiver.userId;
	 
	 
     const promises = [];

	 if (senderUid == receiverUid) {    
		//if sender is receiver, don't send notification
        promises.push(event.data.current.ref.remove());				
		return Promise.all(promises);
	 }

     const getInstanceIdPromise = admin.database().ref('/friend_request/{pushId}').once('value');
     const getReceiverUidPromise = admin.auth().getUser(receiverUid);

     return Promise.all([getInstanceIdPromise, getReceiverUidPromise]).then(results => {
		 const instanceId = results[0].val();
         const receiver = results[1];
         console.log('notifying ' + receiverUid + ' about ' + friendRequestMessage + ' from ' + senderUid);

         const payload = {
			 notification: {
				 title: receiver.displayName,
                 body: friendRequestMessage,
                 icon: receiver.photoURL
                }
        };
		
		admin.messaging().sendToDevice(instanceId, payload)
			.then(function (response) {
               console.log("Successfully sent message:", response);
            })
            .catch(function (error) {
               console.log("Error sending message:", error);
            });
 
	});
 });