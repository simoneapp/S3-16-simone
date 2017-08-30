# Simone

## Sprint Retrospective (3nd)

A brief analysis concerning issues faced during the third sprint of development.


Multiplayer (Firebase):

- Even if Firebase is the right solution we had spent few hours trying to understand how to compute data on the server side. Cloud Functions provided by Firebase are the best solution, but we have never seen them before. Some hours of coding were requested in order to get familiar. Despite this, replacing Pubnub and Realm with Firebase was a key point: now Simone has a centralized database which stores all user data. Moreover, Firebase provides also a functionality for sending push notification without triggered them locally (Google Cloud Messaging). 


Fragments (GUI Android):

- The multiplayer user interface is now composed by two tabs. On the first tab it is possible to see Facebook's friend, while on the second tab there are all the on going/previous matches. This refactoring was not easy at all, but now we have a sort of example that we can reuse at anytime we need it.

