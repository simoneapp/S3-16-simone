# Simone - Sprint Backlog table

## 1st sprint backlog

| Details |
|---|
| At the end of the first sprint, a prototype Simone app was developed. SinglePlayer mode was almost completed, meaning that a user would be able to play in single mode following a sequence of colors chosen by the computer. Highscore, Achivements, Multiplayer are still missing. | 
## 2st sprint backlog

| Details |
|---|
| After 50 hours of coding, Simone app was capable of handling a single player match considering: score, mode (classic or hard?), highscore and achievements. Moreover, it is possible to play a match again another Facebook friend. Right now all the communication is implemented as message passing: each match is stored into a local DB and there is a continuous synchronization between data stored into the devices.| 
## 3st sprint backlog

| Details |
|---|
| We understood that a distributed solution (P2P) storing data into the single devices is not a good choice. Here comes the big change: we moved from message passing (Pubnub library) to a cloud solution: Firebase. In this way all data are store in a central server. Each device connects to the cloud server and download all data to create/play a match as multiplayer mode. Technically speaking, Realm (databse) + Pubnub (message passing) are replaced by Firebase, a cloud solution for mobile devices.|
## 4st sprint backlog

| Details |
|---|---|---|---|---|---|
| Simone app is completed! Now it is possible to play as a Single Player, both classic and hard mode, Multiplayer (classic or nearby). Moreover, settings let the user to enable/disable music and push notifications. After reaching a prefixed threshold achivements are unlocked.|