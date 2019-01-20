DungeonGenerator: main.o dungeons.o
	gcc main.o dungeons.o -o DungeonGenerator

main.o: main.c dungeons.h
	gcc -Wall -Werror -ggdb main.c -c

dungeons.o: dungeons.c dungeons.h Dungeon_Config.h
	gcc -Wall -Werror -ggdb dungeons.c -c

clean:
	rm -f *.o DungeonGenerator *~ core
