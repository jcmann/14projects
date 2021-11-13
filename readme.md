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
which can also take a couple hours. They may be writing the story,
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

### Minimum Viable Product

- [ ] As a Dungeon Master, I want to be able to create an account that saves my information in a database, and not just local browser storage, so my data persists across logins.
- [ ] As a DM, I should be able to sign in and sign out of my account, and my data
      should persist across these logins.
- [ ] As a DM, I want to be able to create, modify, and delete
      various encounters from my saved list of encounters.
- [ ] As a DM writing an encounter, I should be able to add characters and monsters to the encounter so that I can track the difficulty of said encounter.
- [ ] As a DM writing an encounter, I should be able to calculate the difficulty rating of an encounter based on character and monster levels.
- [ ] As a DM writing an encounter, I should be able to write a long text description of the encounter so I can store all the information that doesn't conveniently boil down into characters and monsters.
- [ ] As a DM, I should be able to track (add, modify, and delete) player characters. Specifically, I need their current health, armor class, primary stats, and hit dice.

### Stretch Goals

- [ ] As a DM, I would like to be able to see the current health of both characters and monsters in an encounter so it's easier for me to keep track of.
- [ ] As a DM, I would like to be able to easily see expanded stats for monsters in an encounter such as: AC, stats, hit dice, attacks, or vulnerabilities.
- [ ] As a DM, I would like to be able to click on a monster in an encounter and see an expanded view of their full monster page.
- [ ] As a DM, I should be able to roll any die in the standard DND gaming set from any screen.
- [ ] As a DM, I want to be able to add custom monsters to the monster roster that can be added to encounters just like monsters that come from the API, so I'm not as restricted creatively.

### Dev-related Stretch Goals

- [ ] Fix Amplify/Redux so the Redux Toolkit reducer does not manually reload the page for changes in login status to take effect
- [ ] Implement APIFormatUtility for cleaner code

## Project Plan

### Technologies

- Frontend: React with Redux Toolkit
- Backend: Maven structured as an API (Java project)

### Database Tables

- Users
- Characters
- Encounters
- Characters_Encounters

### Technological Overview

The backend will be structured as an API with endpoints representing the above tables, as well as endpoints representing endpoints from the external API (the DND API) that the app will use: namely, monsters and some character data.

#### **"Middleman" endpoints**

The application will use data from the DND API to perform calculations, and provide the user with a list of monsters they can add to an encounter.

The DND API will provide data about classes, races, and monsters that the user can use while building encounters. Rather than the React frontend interacting with this API directly, it will send a request to our Java/Maven backend, which will then send the request to the DND API.

#### **Regular endpoints**

The React frontend will also send more standard requests to our backend that will get app-specific data about users, characters, and encounters.

The User endpoint will be used to verify that the user making the request is valid, as authentication is handled in the frontend using AWS Amplify, and will also associate validated users to character and encounter data. Currently, I anticipate storing user-related data in Redux state.

- 1 User: Many Characters
- 1 User: Many Encounters

The Characters endpoint will be used to request data for characters belonging to that user, to be displayed and interacted with in the React frontend.

The Encounters endpoint will be used similarly.

### Rough Schedule Estimate

In addition to following exercise and checkpoint goals:

- Week 10: Have frontend at least mostly built, but not connected to any backend, study React, Redux Toolkit, especially asynchronous usages
- Week 11-13: Have endpoints/backend mostly working
- Week 13-16: Connect frontend to backend
