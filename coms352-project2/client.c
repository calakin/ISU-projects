#include <unistd.h>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <stdlib.h>
#include <string.h>
#define MAX_COMMAND_SIZE 1024
#define BUFFER_SIZE 10280
#define PORT 8080
#define IP_BYTES 15

char* const LOCALHOST = "127.0.0.1";

//returns the min between two integers
int min(int a, int b) { return ((a < b) ? a : b); }
static int encrypt(char* input, int cipher);

int main(int argc, char** argv) {
  int socketid, port, i = 1;
  struct sockaddr_in server_addr;
  char input[MAX_COMMAND_SIZE];
  char buffer[BUFFER_SIZE];
  char* usage = "Invalid input.\nUsage: 'client [-p <server port #>] [-a <ip address #>]'\n";
  char ip[IP_BYTES];

  //initialize buffers to 0
  memset(input, 0, sizeof(input));
  memset(buffer, 0, sizeof(buffer));
  
  //initialize default port and ip
  port = PORT;
  memcpy(ip, LOCALHOST, IP_BYTES);
  
  //parse command line arguments
  if(argc > 1) {
    if(((argc - 1) % 2) != 0) {
      printf("%s", usage);
      exit(-1);
    } else {
      while(i < argc) {
	char* option = (char*) argv[i];
	char* value = (char*) argv[i + 1];

	if(strcmp(option, "-p") == 0) {
	  port = atoi(value);
	} else if (strcmp(option, "-a") == 0) {
	  memcpy(ip, value, IP_BYTES);
	} else {
	  printf("%s", usage);
	  exit(-1);
	}
	
	i += 2;
      }
    }
  }
  
  printf("Remote Shell Client: Using port %d.\n", port);
  printf("Remote Shell Client: Using ip %s.\n", ip);	  

  //initialize port to try on server, address type
  server_addr.sin_family = AF_INET;
  server_addr.sin_port = htons(port);

  //open the socket file
  if( (socketid = socket(AF_INET, SOCK_STREAM, 0)) < 0) {
    printf("Remote Shell Client: Failed to create socket.\n");
    return -1;
  }
  
  //try connected to the localhost ip
  if( inet_pton(AF_INET, ip, &server_addr.sin_addr) < 0 ) {
    printf("Remote Shell Client: Invalid server address.\n");
    close(socketid);
    return -1;
  }

  //attempt to connect the socket to the server
  if( connect(socketid, (struct sockaddr *) &server_addr, sizeof(server_addr)) != 0) {
    printf("Remote Shell Client: Connection failed.\n");
    close(socketid);
    return -1;
  }
 
  printf("Remote Shell Client: Connected\n");
  printf("Remote Shell Client: Enter commands to be run on the server.\n");
  printf("Remote Shell Client: To exit, type \"quit\".\n");

  //request commands continuously
  int quit = 0;
  while(!quit) {
    if( fgets(input, MAX_COMMAND_SIZE, stdin) != NULL ) {

      //clear overflow from the buffer
      char* tmp;
      if( tmp = strchr(input, '\n') ) {
	*tmp = 0;
      } else {
	scanf("%*[^\n]");
	scanf("%*c");
      }

      if( strcmp( input, "quit" ) == 0 ) {
	quit = 1;
      }
      
      //encrypt and send the command
      encrypt(input, 9); //shifts by 9, the last digit of my university id
      write(socketid, input, strlen(input));

      //read and print the output from the server's stdout/stderr
      read(socketid, buffer, BUFFER_SIZE);
      printf("%s\n", buffer);
    }
  }
  
  close(socketid);
  return 0;
}

/*
  Encrypts a message by shifting the characters up by a specified amount.
  When 127 is reached, the characters will wrap back around to 0.
  input: the message to encrypt
  cipher: the amount to shift the message by
 */
static int encrypt(char* input, int cipher) {
  register char* c = input;
  
  while(*c != 0) {
    *c = (*c + cipher) % 127;
    c++;
  }
  
  return 0;
}
