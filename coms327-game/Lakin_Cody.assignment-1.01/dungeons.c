#include "dungeons.h"
#include "Dungeon_Config.h"
#include <time.h>
#include <stdlib.h>
#include <stdio.h>
#include <math.h>


Room generateRoom()
{
  Room r;

  srand(time(NULL));

  int y_dim = (rand() % (MAXROOMHEIGHT + 1 - MINROOMHEIGHT)) + MINROOMHEIGHT;
  int x_dim = (rand() % (MAXROOMWIDTH + 1 - MINROOMWIDTH)) + MINROOMWIDTH;
  int x_pos = (rand() % (DUNGEONWIDTH - 4 - x_dim)) + 2; 
  int y_pos = (rand() % (DUNGEONHEIGHT - 4 - y_dim)) + 2; 
	       
  r.y_dim = y_dim;
  r.x_dim = x_dim;
  r.y_pos = y_pos;
  r.x_pos = x_pos;

  r.isConnected = 1;
  
  return r;
}

void printDungeon(Dungeon d)
{
  int i, j;

  for(i = 0; i < DUNGEONHEIGHT; i++){
    for(j = 0; j < DUNGEONWIDTH; j++){
      printf("%c", d.map[i][j]);
    }
    printf("\n");
  }
  printf("\n");
}


int placeRoom(Room r, Dungeon *d)
{
  //extremeties of the given room
  int yTop = r.y_pos;
  int xLeft = r.x_pos;
  int yBot = yTop + r.y_dim;
  int xRight = xLeft + r.x_dim;

  int i, j;

  //check if room can fit in proposed space with 1 space border
  //if so, place it
  for(i = yTop - 1; i <= yBot + 1; i++){      
    for(j = xLeft - 1; j <= xRight + 1; j++){
      if(d->map[i][j] != ' ') return 1;
    }
  }

  for(i = yTop; i <= yBot; i++){
    for(j = xLeft; j < xRight; j++){
      d->map[i][j] = '.';
    }
  }
  
  return 0; //if successful
}

double findDistance(int x1, int x2, int y1, int y2)
{
  double distance;

  distance = sqrt((y2-y1)*(y2-y1) + (x2-x1)*(x2-x1));

  return distance;
}

int findClosestRoom(Room r, Room rooms[])
{
  double current;
  int closestIndex;
  double shortestDistance = DUNGEONWIDTH + 1.0; //start at highest distance
  int i;

  for(i = 0; i < NUMROOMS; i++){

    //if rooms[i] and r are not the same, find the distance between them
    //if it is the shortest so far, make it the new shortestDistance
    if(r.x_pos != rooms[i].x_pos && r.y_pos != rooms[i].y_pos){
      current = findDistance(r.x_pos, rooms[i].x_pos, r.y_pos, rooms[i].y_pos);
	
      if(findDistance(r.x_pos, rooms[i].x_pos, r.y_pos, rooms[i].y_pos)
	 < shortestDistance){

	//only connect to a room already connected to others
	if(rooms[i].isConnected == 0){
	  shortestDistance = current;
	  closestIndex = i;
	}
      }
    }
  }

  //if no rooms are connected yet, return -1
  if(shortestDistance == DUNGEONWIDTH + 1.0){
    return -1;
  } else {
     return closestIndex;
  }
}

void connectRooms(Room *r1, int closestIndex, Dungeon *d)
{
  Room* r2 = &(d->rooms[closestIndex]);
  int x_distance, y_distance;

  x_distance = r1->x_pos - r2->x_pos;
  y_distance = r1->y_pos - r2->y_pos;

  int i;

  //the following code in the if statements draws straight lines
  //left/right and up/down to the other room's position
  if(x_distance < 0){
    for(i = 0; i <= abs(x_distance); i++){
      char current = d->map[r1->y_pos][r1->x_pos + i];

      if(current == ' ') d->map[r1->y_pos][r1->x_pos + i] = '#';
    }
  }

  if(x_distance > 0){
   for(i = 0; i <= abs(x_distance); i++){
     char current = d->map[r1->y_pos][r1->x_pos - i];

     if(current == ' ') d->map[r1->y_pos][r1->x_pos - i] = '#';
   }
  }

  if(y_distance < 0){
    for(i = 0; i <= abs(y_distance); i++){
      char current = d->map[r1->y_pos + i][r1->x_pos - x_distance];

      if(current == ' ') d->map[r1->y_pos + i][r1->x_pos - x_distance] = '#';
    }
  }

  if(y_distance > 0){
    for(i = 0; i <= abs(y_distance); i++){
      char current = d->map[r1->y_pos - i][r1->x_pos - x_distance];

      if(current == ' ') d->map[r1->y_pos - i][r1->x_pos - x_distance] = '#';
    }
  }
  
  r1->isConnected = 0;
}

void addCorridors(Dungeon *d)
{
  int i;

  for(i = 0; i < NUMROOMS; i++){
    Room *currentRoom = &(d->rooms[i]);
    
    if(currentRoom->isConnected != 0){
      int closestIndex = findClosestRoom(*currentRoom, d->rooms);

      if(closestIndex == -1){
	currentRoom->isConnected = 0;
      } else {
	connectRooms(currentRoom, closestIndex, d);
      }
    }
  }
}

void addBorder(Dungeon *d)
{
  int i;

  for(i = 0; i < DUNGEONWIDTH/2; i++){
    d->map[0][i] = BORDERCHAR;
    d->map[20][i] = BORDERCHAR;
    d->map[0][DUNGEONWIDTH - 1 - i] = BORDERCHAR;
    d->map[20][DUNGEONWIDTH - 1 - i] = BORDERCHAR;

    if(i < DUNGEONHEIGHT){
      d->map[i][0] = BORDERCHAR;
      d->map[i][79] = BORDERCHAR;
    }
  }
}
 
Dungeon generateDungeon()
{
  Dungeon d;

  int i, j;

  //initialize map to all solid rock
  for(i = 0; i < DUNGEONHEIGHT; i++){
    for(j = 0; j < DUNGEONWIDTH; j++){
      d.map[i][j] = ' ';
    }
  }

  addBorder(&d);
  
  //generate rooms
  Room r;
  int roomCount = 0;
  
  while(roomCount < NUMROOMS){ //TODO: add other criteria like at least %7 rooms

    r = generateRoom();
    
    if(placeRoom(r, &d) == 0){
      d.rooms[roomCount] = r;
      roomCount++;
    }
  }

  //generate corridors
  addCorridors(&d);
  
  return d;
}
