OBJECTS = server.o client.o

all:
	make client
	make server

server: server.o
	$(CC) server.o -o server

client: client.o
	$(CC) client.o -o client

server.o:
client.o:
clean:
	-rm *.o *~ *#
