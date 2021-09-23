# Jen Mann Individual Project

## Problem Statement

Dungeons and Dragons (DND) is a longstanding and increasingly popular tabletop 
roleplaying game. Currently on its 5th edition (aka “5e”), it has an expansive 
world filled with monsters and magic. The game consists of a handful of players 
(usually 3-5) and a dungeon master (DM). Players make characters and are the 
ones actually roleplaying. The DM is the one who’s building the game world, 
playing NPCs and enemies, and generally guiding the players. 
The DM effectively is the computer, and naturally, being the computer is a lot of work!

Finding a DM is often difficult for players, much less finding a good one. 
Game sessions themselves often last somewhere around three hours. 
DMs are also responsible for prepping the story world for the next session, 
which can also take a couple hours. They may be writing the story themself, 
or they have have to organize prewritten materials.

There are a variety of tools DMs use to keep themselves organized and to get 
their jobs done. Many use notebooks, but there are also various online tools 
like D&D Beyond, encounter builders, map creator tools, campaign wiki creators, etc.. 
D&D Beyond is great but has some weird limitations around paywall. 
I’m not really trying to reinvent the wheel with this project as much as I want 
to make my own spin on encounter builders and campaign tools.

Overall, I want to focus on creating tooling that helps DMs during encounters 
especially, but that can also help just during regular non-combat gameplay. 
All of this revolves around information tracking and display and I believe the 
most important part is a good UI. 

## User Stories

MVP: As a Dungeon Master, I want to be able to create an account that saves my 
information in a database, and not just local browser storage, so my data 
persists across long periods of time.

MVP: As a Dungeon Master, I want to be able to create, modify, and delete 
various encounters from my saved list of encounters.

MVP: As a DM, I should be able to track (add, modify, and delete) player 
characters to encounters. Specifically, I need to track their current health, 
armor class, primary stats, and hit dice.

MVP: As a DM, I should be able to track enemies in the encounters as well. 
At a quick glance, I need to see their health, AC, stats, hit dice, 
vulnerabilities, and attacks. I should be able to expand this view to see 
their full monster page.

As a DM, I should be able to roll any die in the standard dice set on any screen.

As a DM, I want to be able to add custom monsters to the monster roster that 
can be added to encounters like official monsters. 

## Project Plan

I have two different ideas for the project, which I'd like some insight on to decide between!

### General Info

The DND API would provide data about classes and races, as well as a list of monsters
that the user could scroll through. The project is intended to be a supplemental
tool, not one that would replace the physical books. The information
I'm hoping to display will be what DMs might need to glance at to keep game flow going.

Database Tables
* Users
* Characters
* Encounters
* UsersCharacters
* UsersEncounters
* CharactersEncounters

### Route 1: All Java (Servlets, JSPs)

(Disclaimer: I think that this is technically more in line with the project requirements)

The servlets would handle not only the database requests, but also any contact
with the API.

Week 4: Checkpoint 1
Week 5: Design database + DAO
Week 6: Work on design feedback and build homepage
Week 7: Add authentication (and enough to log in)
Week 8: Work on deploying to AWS and checkpoint 3
Week 9 onwards: General work continuing

### Route 2: Java backend, React frontend

I think this kind of also is a win for the rubric because it would definitely 
count as using a complex other technology! But, if I did this I’d need some 
guidance on how to best do this because it’d kind of be two codebases.

I would imagine basically writing two project directories, one just for the Java 
side and having all the React files live in their own directory. I’m not really 
sure how it would work to have the Java project also serve up the entire React project.

The Java code would then be structured sort of as both its own API and a sort of
middleware. It would handle all the data that would be stored in the database 
(user info, saved encounters, saved characters, custom monsters). 
I feel like it would also need to be what makes the requests to the API, 
rather than that coming directly from the frontend -- i.e., the frontend would 
make a request to my Java project which would then make the request to the DND API.

While I use React at work, we have so many of our own structures that a lot of 
just the regular React/Redux/etc is abstracted away pretty heavily. It would 
take me quite a bit of time to really figure out exactly what line of code fires
off actual requests, because we don’t just use simple Fetch.

I’ll be working on a Udemy class to learn the React side of things.

Week 4: Checkpoint 1, start React studying
Week 5: Design database + DAO
Week 6: Work on design + start building homepage
Week 7: Start building authentication
Week 8: Start working on deploying to AWS/Checkpoint 3
Week 9: Checkpoint 3, continue working on frontend
Week 10:
Goal: Have frontend built but not connected to anything
Start applying REST to Java project
Week 11-13: Have endpoints/backend mostly working
Week 13-16: Connect frontend to backend (gives me time to work on async React/etc.)