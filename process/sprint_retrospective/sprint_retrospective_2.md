# Simone

## Sprint Retrospective (2nd)

A brief analysis concerning issues faced during the second sprint of development.


Multiplayer:

- We are storing data into the devices using Realm and Pubnub. This solution is completely distributed, but it is not the right choice. We are planning to move to Firebase during the third sprint.

Push Notification:

- Right now push notification are triggered as local notification when a new match request is received. A good choice would be to use a push notification service.
