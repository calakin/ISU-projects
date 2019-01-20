#ifndef DUNGEONS_H
#define DUNGEONS_H

#include "Dungeon_Config.h"

typedef struct Room {
  int y_dim;
  int x_dim;
  int y_pos;
  int x_pos;
  int isConnected;
}Room;

typedef struct Dungeon {
  char map[DUNGEONHEIGHT][DUNGEONWIDTH];
  Room rooms[NUMROOMS];
}Dungeon;

Dungeon generateDungeon();
void printDungeon(Dungeon d);

#endif
