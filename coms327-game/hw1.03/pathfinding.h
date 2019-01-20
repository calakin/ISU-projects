#ifndef PATHFINDING_H
#define PATHFINDING_H

#include "dungeon.h"
#include "heap.h"

/* This function finds the shortest path from position 1 to position 2 in the 
   given dungeon using Djikstra's algorithm */
void find_distance(pair_t from, pair_t to, dungeon_t d, int* distance, uint8_t tunneling);

/*This function uses findDistance() to map the distance to each point in a 
  dungeon from a single point. The distances are stored in an array with
  the dimensions of the dungeon map. */
void map_all_distances(pair_t position, dungeon_t d, 
		       char map[DUNGEON_Y][DUNGEON_X], uint8_t do_tunneling);

/*This function displays an 2D array of distances.*/
void display_distances(char map[DUNGEON_Y][DUNGEON_X]);

/* Uses pathfinding functions to display two distance maps from a given
   position, one for tunneling creatures and one for non-tunneling
   creatures.*/
void show_distances(pair_t given_position, dungeon_t d);

#endif
