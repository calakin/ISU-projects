#include "utility.h"
#include "dungeon.h"
#include "time.h"

void spawnPC(dungeon_t* d, pair_t player_pos)
{
  //if no command line position given
  if(player_pos[dim_y] == -1 && player_pos[dim_x] == -1){

    int seed = time(NULL);
    srand(seed);

    int room = (rand() % d->num_rooms);

    int x_offset, y_offset;
    x_offset = rand() % d->rooms[room].size[dim_x];
    y_offset = rand() % d->rooms[room].size[dim_y];

    player_pos[dim_x] = d->rooms[room].position[dim_x] + x_offset;
    player_pos[dim_y] = d->rooms[room].position[dim_y] + y_offset;
  }

  d->map[player_pos[dim_y]][player_pos[dim_x]] = ter_player_character;
}
