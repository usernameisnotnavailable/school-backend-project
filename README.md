# Tribes Backend

Dependencies:
Spring Boot DevTools
Lombok
Spring Web
Spring Data JPA
Spring Security
OAuth2 Resource Server
MySQL Driver

Do not forget to create environment variables to connect to database.
DATASOURCE_URL=jdbc:mysql://localhost/your database name
DATASOURCE_USERNAME=your local mysql username
DATASOURCE_PASSWORD=your local mysql password
HIBERNATE_DIALECT=org.hibernate.dialect.MySQL8Dialect
          
---
Rules:
---
---
Legend:
---
	feature: -
	theoretical feature: *

---
Map:
---
	-Square map NxN tiles
	-Towns are placed on the map randomly going outward from the middle, but at least 1 square between towns
	-armies can attack other towns on the map, after attack they will return at the same speed
	*NPC towns to raid or occupy?
---
Town buildings summary:
---

	Building name and function:									Leveling:

	-Castle:											
		-Main building									|	-Unlocks buildings / increase building speed
	-Production buildings:									|
		-Houses:									|	-worker count increase	
		-Woodcutter: produces wood							|	-increase worker slots
		-Quarry: produces stone								|	-increase worker slots
		-Iron mine: produces iron							|	-increase worker slots
		-Farm: produces food								|	-increase worker slots
		-Storage/stockpile: stores resources						|	-increase storage space for each resources
		*Production auxilary:								|	*increase production per worker
			*Windmill: increases the farm's food production				|
			*Prospector: increase iron production					|
			*Stonecutter: increase stone production					|
			*Forester or sawmill: increase wood production				|
	-military:										|
		-Barracks: train militaty units							|	-unlocks new units
		-Blacksmith:									|	-unlocks some units and upgrades
			*military upgrades (better weapons and/or armor)			|			
			*civil upgrades (better tools(plow, axe, pickaxe))			|	*unlock new resource upgrades
												|
		-Wall:										|	-increase defense bonus

---
General building info:
---
| Buildings       |                    Function                    | food | wood | stone | iron | starting lvl | build time | max lvl | Castle lvl needed |
|-----------------|:----------------------------------------------:|:----:|:----:|:-----:|:----:|:------------:|:----------:|:-------:|:-----------------:|
| Castle          |   unlocks buildings, decrease building time    |  20  |  50  |  50   |  20  |      0       |    30m     |    5    |         0         |
| Farm            |                 produces food                  |  5   |  10  |  20   |  10  |      1       |     5m     |   10    |         0         |
| Woodcutter      |                 produces wood                  |  20  |  5   |  10   |  10  |      1       |     5m     |   10    |         0         |
| Quarry          |                 produces stone                 |  10  |  10  |   5   |  20  |      1       |     5m     |   10    |         0         |
| Iron mine       |                 produces iron                  |  10  |  20  |  10   |  5   |      1       |     5m     |   10    |         0         |
| Housing         |             increases worker count             |  10  |  5   |   5   |  0   |      1       |     5m     |   10    |         0         |
| Stockpile       |            increases storage space             |  0   |  10  |  10   |  5   |      1       |    10m     |    5    |         0         |
| Barracks        |                trains soldiers                 |  5   |  25  |  20   |  5   |      0       |    15m     |   10    |         1         |
| Wall            |           defense bonus to defenders           |  0   |  25  |  50   |  5   |      0       |    30m     |    5    |         1         |
| Blacksmith      |               unlocks new units                |  10  |  25  |  50   |  25  |      0       |    20m     |    5    |         1         |
| ideas:          | --possible buildings in the future from here-- |      |      |       |      |              |            |         |                   |
| production aux. |  increase production of resources by 10%/lvl   |  5x  |  5x  |  5x   |  5x  |      0       |    30m     |    5    |         3         |
| market          |               exchange resources               |  10  |  50  |  25   |  25  |      0       |    20m     |    5    |         2         |

-building time and cost doubles for each lvl

-production rate: 10 units / h / worker

-unused workers increase building speed by 1% each up to 100%

---
Buildings lvl info:
---
| lvl | prod building worker slot | Housing worker amount | Stockpile storage space | Castle build time reduction | wall defense bonus | Barracks unlocks |
|:---:|---------------------------|:---------------------:|:-----------------------:|:---------------------------:|:------------------:|------------------|
|  1  | 1                         |           3           |           100           |             10%             |         5%         | militia          |
|  2  | 2                         |           6           |           400           |             20%             |        10%         | spearman         |
|  3  | 3                         |           9           |          1600           |             30%             |        15%         | archer           |
|  4  | 5                         |          15           |          6400           |             40%             |        20%         | swordsman        |
|  5  | 8                         |          24           |          25000          |             50%             |        25%         | scout            |
|  6  | 13                        |          39           |           n/a           |             n/a             |        n/a         | crossbowman      |
|  7  | 21                        |          61           |           n/a           |             n/a             |        n/a         | light cavalry    |
|  8  | 34                        |          102          |           n/a           |             n/a             |        n/a         | longbow man      |
|  9  | 55                        |          165          |           n/a           |             n/a             |        n/a         | knight           |
| 10  | 100                       |          300          |           n/a           |             n/a             |        n/a         | heavy cavalry    |



---
Unit info
---
| Unit name     | HP  | attack | defense | food | wood | stone | iron | speed | carry capacity | train time | barrack lvl | *WIP* (blacksmith lvl) | *WIP* (tech needed) |
|---------------|-----|:------:|--------:|------|------|-------|------|-------|----------------|------------|-------------|------------------------|---------------------|
| militia       | 10  |   1    |       0 | 5    | 0    | 0     | 0    | 25    | 20             | 1m         | 1           | 0                      | n/a                 |
| spearman      | 15  |   2    |       1 | 10   | 5    | 0     | 0    | 25    | 20             | 2m         | 2           | 0                      | n/a                 |
| archer        | 10  |   2    |       1 | 10   | 10   | 0     | 0    | 25    | 15             | 4m         | 3           | 1                      | arrowheads          |
| swordsman     | 30  |   4    |       3 | 15   | 5    | 0     | 10   | 20    | 15             | 8m         | 4           | 2                      | anvil               |
| scout         | 25  |   2    |       1 | 40   | 0    | 0     | 5    | 100   | 50             | 10m        | 5           | 1                      | saddle              |
| crossbowman   | 25  |   5    |       2 | 25   | 25   | 0     | 5    | 20    | 10             | 15m        | 6           | 3                      | bolts               |
| light cavalry | 40  |   5    |       3 | 60   | 10   | 0     | 15   | 75    | 30             | 20m        | 7           | 2                      | reins               |
| longbow man   | 25  |   8    |       2 | 25   | 50   | 0     | 10   | 25    | 15             | 30m        | 8           | 2                      | fletcher            |
| knight        | 50  |   10   |       5 | 25   | 10   | 0     | 25   | 15    | 5              | 45m        | 9           | 4                      | bellow              |
| heavy cavalry | 100 |   15   |      10 | 100  | 25   | 0     | 75   | 35    | 25             | 60m        | 10          | 5                      | stirrups            |

- Speed means map gird squares per hour
- army speed is the speed of the slowest units (an army of 100 militia, 100 archer, 100 swordsman, 100 scouts has a speed of 20)


	-eg.:
	Town 1 coordinates: (7,4)
	Town 2 coordinates: (13,11)
  	distance is calculated as: ((Town1.x - Town2.x)^2 + (Town1.y - Town2.y)^2)^(1/2) = distance
				   ((7-13)^2 + (4-11)^2)^(1/2) = ~9,22 squares
	after this we divide the distance with the army speed. Using the army above(spped = 20):
	distance of 9,22 / 20 = 0,46 hours = 27,66 minutes = 27 minutes and 40 seconds

---
Battle
---

attacker:

- militia: 10
- spearman: 10
- archer: 10
- swordsman: 10
- scout: 10
- crossbowman: 10
- light cavalry: 10
- longbow man: 10
- knight: 10
- heavy cavalry: 10
- attack value: 540


defender:

- militia: 10
- spearman: 10
- archer: 10
- swordsman: 10
- scout: 10
- crossbowman: 10
- light cavalry: 10
- longbow man: 10
- knight: 10
- heavy cavalry: 10
- attack value: 540


damage done evenly distributed between unit types

-> 540 dmg distributed to 10 types

-> 54 dmg each

-> damage - def value to first soldier

-> if hp left is 0 or less unit dies and remaining dmg (if any) goes to next soldier

-> if last soldier of the type dies remaining dmg added to the next type

eg.:

	-militia(no def): 54 dmg 
	-> first soldier receives dmg, hp remained -44, unit dies 
	-> remaining dmg goes to next soldier (repeate 4 times, next 4 soldiers die) 
	-> last soldier damaged have 6 hp left
	-> next type

	spearman: 54 dmg 
	-> first soldier receives dmg - def value(1), so 53 dmg 
	-> hp remaining -38 
	-> next soldier 38 dmg - def value(1) = 37 dmg 
	-> hp remaining  -22 
	-> next soldier 22 dmg - def value(1) = 21 dmg
	-> hp remaining -6
	-> next soldier 6 dmg - def value(1) = 5 dmg
	-> hp remaining 10
	-> next type
	...
	knight: 54 dmg 
	-> dmg(54) - def value(5) = 49 dmg 
	-> no casualties, hp remained = 1




---
theoretical Features (just ignore these):
---

		-Lookout tower:
			-increase vision radius of town					-increase radius

	-Wall:										-increase defense bonus
	*upgrades to add special defenses (boiling oil, holes for archers, towers, moat, draw bridge, secret gate, torches for fire arrows)

	*civilian:
		-Market:								*increase the amount of resources to trade
			-trade resources						*better exchange rates
			*mercanaries							*better/more units 
			(**unique/non trainable units(assasin, marksman, hunter, bandit))			

		-Builder's guild:
			-decrease building time						*bigger decrease

		*Academy:
			-upgrades							*unlock new upgrades
			**Recruit Lords							*Lords with better bonuses

		*Temple:
			-increase moral -> increase all production			*bigger bonus

		(**Tavern:
			-Recruit Heroes							*Better heroes)

	Units:
	-Melee:
		-Militia: cheap costs only food, no armor, improvised weapons/tools, weak against everything but dangerous in great numbers
		-Spearman: cheap but costs wood, light gambeson armor, spear, good against cavalry, weak against foot melee units
		-Swordsman: costs medium food few iron few wood, heavy gambeson armor & shield, shortsword, good against foot soldiers weak against archers
		-Knight: costs a lot of food, high iron and a few wood, heavy armor and longsword, good against foot soldiers, *weak against crossbows*

	-Ranged:
		-Archer: costs food and medium wood, no armor, bow and arrows, good against foot soldiers, weak against cavalry
		-Crossbowman: costs medium food medium wood and few iron, Leather armor, crossbow and bolts,*slow, short range* good againsr armored units
		-Longbowman: costs medium food medium wood few iron, light  gambeson, longbow and arrows, *fast, long range* good agains unarmored units 

	-Cavalry:
		-scout: costs medium food, few iron, light gambeson, shortsword, very fast and relatively cheap but weak
		-light cavalry: costs more food and iron than scout and few iron, leather armor, spear, fast and good against archers and foot soldiers except spears
		-heavy cavalry: costs a lot of food and iron medium wood, heavy armor on horse and fighter, lance and warhammer, slowest cavalry but highest damage and armor
		*horse archer: costs a lot of food and medium wood few iron, light gambeson, bow and arrows, second to scout in speed, low armor but good for quick skirmeshes

	*Siege:
		*Catapult: costs a lot of wood medium iron medium stone, good against building especially walls, very slow *can reduce building levels*
		*Battering ram: costs a lot of wood medium iron, good against breaching walls, very slow
		*Trebutche: costs a more wood iron and stone than the catapult but stronger and slower *can reduce building levels*
		**Ladders: costs a lot of wood, allows melee units to attack walls **Can be an army upgrade and not a unit**

	siege units can be army upgrades, each one costs a lot of resources but negates 1 wall level 
	(eg.: enemy wall lvl: 3, so you add catapult, ladders and battering ram to your army, negating his/her def bonus for a big resources investment(maybe even at cost of speed))
	
	Battle:
	*WIP*

	*Hero*s*:
	-Hero:
		(-Heroes needed to move armies)
		-the heroes have atributes to add to troops:
			-attack modifier
			-defence modifier
			-speed modifier
			*unit speciality
			**Unique units
	*Lord:
		-Lords add modifiers to towns:
			-increase production
			-decrease building time/cost
			-decrease unit recruitment time/cost
			-better market rates

	**Heros and Lords with better bonuses but with drawbacks:
		-Lord John Doe:
			- +5% attack
			- +5% deffense

		-Lady Jane Doe:
			- +15% attack
			- -5% deffense**		