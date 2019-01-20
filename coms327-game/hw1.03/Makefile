OBJS = dungeon.o heap.o pathfinding.o utility.o main.o
CC = gcc
DEBUG = -g
COBJ = -c
CFLAGS = -Wall -Werror $(DEBUG) $(COBJ)
LFLAGS = -Wall -Werror $(DEBUG)
HFILES = pathfinding.h utility.h dungeon.h heap.h


rlg327: $(OBJS)
	$(CC) $(LFLAGS) $(OBJS) -o rlg327

main.o: main.c $(HFILES)
	$(CC) $(CFLAGS) main.c	

dungeon.o: dungeon.c dungeon.h heap.h
	$(CC) $(CFLAGS) dungeon.c

pathfinding.o: pathfinding.c pathfinding.h
	$(CC) $(CFLAGS) pathfinding.c

heap.o: heap.c heap.h macros.h
	$(CC) $(CFLAGS) heap.c

utility.o: utility.c utility.h
	$(CC) $(CFLAGS) utility.c

clean:
	echo Removing all generated files
	rm -f *.o *.d TAGS core vgcore.* gmon.out rlg327 

clobber: clean
	echo Removing backup files
	rm -f *~ \#* *pgm
