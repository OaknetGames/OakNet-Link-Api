# OakNet-Link-New
This Minecraft Mod allows the creation of peer to peer multiplayer networks 

https://www.curseforge.com/minecraft/mc-mods/oaknetlink

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

Note: You need a working installation of a JDK which is added to the path enviroment variables.
Also you need to install Eclipse as IDE.

- Clone this Repository
- Run PrepareWorkspace.bat and enter your Minecraft credentials when asked.
- Open the Folder in Eclipse
- Import the projects

![Import Project 1](http://daten.oaknetwork.de/data/onl/ImportIntoEclipse.png)

1. Click on "Import projects..."
2. Click on "Existing Projects into Workspace"
3. Click on Next

![Import Project 2](http://daten.oaknetwork.de/data/onl/ImportIntoEclipse2.png)

1. Click on "Browse..." and choose the root of the repository
2. Click on "Select All"
3. Click on Finish

- For each Wrapper Project you need to add Run Configurations

![Run Configuration 1](http://daten.oaknetwork.de/data/onl/RunConfigurationMinecraft.png)

1. Click on "Configure External Tools" 
2. Choose a Name
3. Location; Click on "Browse Workspace"
4. Choose the desired Wrapper Project
5. Choose the RunClient batch file and click OK

![Run Configuration 2](http://daten.oaknetwork.de/data/onl/RunConfigurationMinecraft2.png)

1. Working Directory; Click on "Browse Workspace"
2. Choose the desired Wrapper Project
3. Click OK
3. Click Apply
3. Click Close

Now you can make the desired changes and open a pull request.

Thanks for contributing.


# License
You are allowed to modify the code as you wish and to create pull requests to contribute to the Project.

You may NOT redistribute builds of the code without my permission.

Furthermore you are NOT allowed to host your own Master-Server.
