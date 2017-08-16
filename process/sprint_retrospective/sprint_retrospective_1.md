# Simone

## Sprint Retrospective (1st)

A brief analysis concerning issues faced during the first sprint of development.

Single Player mode:

- Handling errors: Akka does not log errors, so it is not easy to find bugs. 

Facebook integration:

- Initial setup of Facebook API: we have never developed any kind of Facebook integration.

Android:

- It is difficult to show data from the realm database into the view.

Networking:

- Actors (Akka) are definetely not suitable for mobile apps. PubNub is the right choice. Instisting on using Akka was a wasting of time. 

Other:

- Travis CI: travis.yml file is hard to configure it
