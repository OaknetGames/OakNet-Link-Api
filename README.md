# OakNet-Link-New
This Minecraft Mod allows the creation of peer to peer multiplayer networks 

# Basic Concept
The Mod consist of two parts:

- the Wrapper: The actual mod (loaded by forge), different for each version of the game

- the Api: This is the same for each version of game, independent

The Communication between the two parts is made possible with two classes:

- IMinecraft.java: This interface has to be implemented by the wrapper; Api -> Game

- MinecraftHooks.java: The methods which are contained in this class need to be called by the Wrapper; Game -> Api

# Network Concepts:
The general idea of the communication is the following:

- Game Traffic:

  Host's game <-> McServer <-> API <-> UDP PeerToPeer <-> API <-> Peer's game.

- Other Traffic
  
  Api <-> Master-Server
  
In detail:

- TCP: Minecraft interface

  Here the Api communicates with either the McServer or the McClient

- TCP: Between Master-Server and Client

  Here is the communication for ServerLists, OakNet-Link Chat, PeerToPeer endpoint exchange, Server-Passwords, etc

- UDP: Between Master-Server and Client || Endpoint and Endpoint

  Here the Master-Server is used for the UDP hole punching. The endpoints are using UDP for their PeerToPeer stuff.

# How to setup the IDE

TODO

# License
You are allowed to modify the code as you wish and to create pull requests to contribute to the Project.

You may NOT redestribute builds of the code without my permission.

Furthermore you are NOT allowed to host your own Master-Server.
