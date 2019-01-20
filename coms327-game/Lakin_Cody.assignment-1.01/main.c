#include "dungeons.h"

int main(int argc, char *argv[])
{
  Dungeon d;
  d = generateDungeon();
  printDungeon(d);

  return 0;
}
