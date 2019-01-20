#include "pathfinding.h"
#include "dungeon.h"
#include "heap.h"

//review djikstra's, then write psuedo code for it
//each cell has a weight depending on its hardness (hardness 255 has infinite):
// weight = hardness / 85 [1-254] and 1 [0].

//add switch to specify spawn point (optional)

typedef struct path {
  heap_node_t *hn;
  uint8_t pos[2];
  uint8_t from[2];
  int32_t cost;
} path_t;

static int32_t path_cmp(const void *key, const void *with) {
  return ((path_t *) key)->cost - ((path_t *) with)->cost;
}

void find_distance(pair_t from, pair_t to, dungeon_t d, int* distance, uint8_t tunneling)
{

  static path_t paths[DUNGEON_Y][DUNGEON_X], *currentPath;
  static uint32_t initialized = 0;
  heap_t h;
  uint32_t x, y;

  //initializes the coordinates of each path in the paths array
  if (!initialized) {
    for (y = 0; y < DUNGEON_Y; y++) {
      for (x = 0; x < DUNGEON_X; x++) {
        paths[y][x].pos[dim_y] = y;
        paths[y][x].pos[dim_x] = x;
      }
    }
    initialized = 1;
  }

  //initialize cost of each path to the max value
  for (y = 0; y < DUNGEON_Y; y++) {
    for (x = 0; x < DUNGEON_X; x++) {
      paths[y][x].cost = INT_MAX;
    }
  }

  //initialize cost to from from itself as 0
  paths[from[dim_y]][from[dim_x]].cost = 0;

  //initialize the priority queue using our heap and path comparator
  heap_init(&h, path_cmp, NULL);

  //adds all paths to the heap, except the immutable walls & walls if not tunneling
  for (y = 0; y < DUNGEON_Y; y++) {
    for (x = 0; x < DUNGEON_X; x++) {
      if (!tunneling && d.map[y][x] == ter_wall) {
	paths[y][x].hn = NULL;
      } else if (d.map[y][x] != ter_wall_immutable || d.hardness[y][x] != 255) {
        paths[y][x].hn = heap_insert(&h, &paths[y][x]);
      } else {
        paths[y][x].hn = NULL;
      }
    }
  }

  if(paths[to[dim_y]][to[dim_x]].hn == NULL){
    *distance = -1;
    heap_delete(&h);
    return;
  }

  int cost;

  //keep removing the minimum paths from the queue as long as there are paths in the queue
  while ((currentPath = heap_remove_min(&h))) {
    //sets the paths heap node to null (visits it)
    currentPath->hn = NULL;

    //returns if there is no path
    if(currentPath->cost == INT_MAX) {
     *distance = -1;
     heap_delete(&h);
     return;
    }

    if(tunneling){
      cost = ( d.hardness[currentPath->pos[dim_y]][currentPath->pos[dim_x]] / 85 ) + 1;
    } else{
      cost = 1;
    }

    //if this current path is equal to the destination (to)
    if ((currentPath->pos[dim_y] == to[dim_y]) && currentPath->pos[dim_x] == to[dim_x]) {
      *distance = currentPath->cost;
      heap_delete(&h);
      return;
    }

    int i,j;
    for(i = -1; i < 2; i++){
      for(j = -1; j < 2; j++){
	
	//no need to check itself
	if(i == 0 && j == 0) continue;

	//if not visited / has path && the cost of that path is more than the cost
	//of this path plus the cost to get to the neighbor
	if ((paths[currentPath->pos[dim_y] + i][currentPath->pos[dim_x] + j].hn) &&
	    (paths[currentPath->pos[dim_y] + i][currentPath->pos[dim_x] + j].cost >
	     (currentPath->cost + cost))) {

	  //update the neighbors cost to this path's cost and the cost to get to the neighbor
	    paths[currentPath->pos[dim_y] + i][currentPath->pos[dim_x] + j].cost =
	      (currentPath->cost + cost);

	    //change neighbor's "from" to this path
	    paths[currentPath->pos[dim_y] + i][currentPath->pos[dim_x] + j].from[dim_y] = currentPath->pos[dim_y];
	    paths[currentPath->pos[dim_y] + i][currentPath->pos[dim_x] + j].from[dim_x] = currentPath->pos[dim_x];

	    //decrease the neighbor's cost in the queue
	    heap_decrease_key_no_replace(&h, paths[currentPath->pos[dim_y] + i]
					 [currentPath->pos[dim_x] + j].hn);
	}
      }
    }
  }
}


void map_all_distances(pair_t position, dungeon_t d, char map[DUNGEON_Y][DUNGEON_X],
		       uint8_t do_tunneling)
{
  int i,j;

  //initialize all to spaces
  for(i = 0; i < DUNGEON_Y; i++){
    for(j = 0; j < DUNGEON_X; j++){
      map[i][j] = ' ';
    }
  }


  //Map for tunneling creatures
  if(do_tunneling) {

    int distance;
    pair_t tmp;
    for(i = 1; i < DUNGEON_Y - 1; i++){
      for(j = 1; j < DUNGEON_X - 1; j++){

	tmp[dim_y] = i;
	tmp[dim_x] = j;

	find_distance(position, tmp, d, &distance, do_tunneling);

	if(distance == -1) {
          map[i][j] = ' ';
        } else {
          map[i][j] = distance + '0';
        }
      }
    }

  //Map for nontunneling creatures
  } else {

    int distance;
    pair_t tmp;
    for(i = 1; i < DUNGEON_Y - 1; i++){
      for(j = 1; j < DUNGEON_X - 1; j++){
	if(d.map[i][j] == ter_wall){
	  map[i][j] = ' ';
	} else{
	  tmp[dim_y] = i;
	  tmp[dim_x] = j;

	  find_distance(position, tmp, d, &distance, do_tunneling);

	  if(distance == -1) {
	    map[i][j] = ' ';
	  } else {
	    map[i][j] = distance + '0';
	  }
	}
      }
    }
  }
}

void display_distance_map(char map[DUNGEON_Y][DUNGEON_X], pair_t player_position)
{
  int i, j;

  for(i = 0; i<DUNGEON_Y; i++){
    for(j = 0; j < DUNGEON_X; j++){
      if(i == player_position[dim_y] && j == player_position[dim_x]){
	printf("@");
      } else if(map[i][j] == ' '){
	printf("%c", map[i][j]);
      } else{
	int distance = (map[i][j] - '0');
	distance = distance % 10;
	printf("%c", distance + '0');
      }
    }
    printf("\n");
  }
  printf("\n");
}

void show_distances(pair_t given_position, dungeon_t d)
{
  char distance_map_nontunneling[DUNGEON_Y][DUNGEON_X];
  char distance_map_tunneling[DUNGEON_Y][DUNGEON_X];

  map_all_distances(given_position, d, distance_map_nontunneling, 0);
  map_all_distances(given_position, d, distance_map_tunneling, 1);

  display_distance_map(distance_map_nontunneling, given_position);
  display_distance_map(distance_map_tunneling, given_position);
}
