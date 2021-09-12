# Jen Mann Individual Project

## Problem Statement

Final Fantasy 14 (FF14) offers players a wide variety of content to progress through, which means players have a lot of
progress to keep track of. This data is all available in the game, but it’s often spread across multiple user interfaces
and menus, and can be difficult to track. Some often involve using external online sources to find additional
information about -- such as quest walkthroughs, or locations of certain gathering items. In general, players trying to
get the most out of the game have a lot of menus to click through to keep track of their game goals.

There are several helper apps that exist to fulfill various FF14 “needs” for players, thanks to a player-maintained API,
XIVAPI. Some allow users to create lists of all the items they need to craft, gather, or purchase. Some allow users to
create lists of gear. Some provide location data for various in-game events, items, etc. The most prominent exist to
provide information and allow users to create various lists of in-game items. Several of these do allow you to save user
data, but a lot of them use browser storage and thus lose your data easily.

For this project, I am going to make an application that uses game data, through XIVAPI, as well as user input to allow
users to track various in-game “projects.” For example, progression through the main story quests, completion of the
crafting and gathering logs, completion of minion or riding mount collections. Ideally, users will be able to customize
what they’re working on by choosing from certain supported projects. 
