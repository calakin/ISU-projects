#include <unistd.h>
#include <errno.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netdb.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/wait.h>
#include <signal.h>
#include <ctype.h>

#define MAX_COMMAND_SIZE 1024
#define PORT 8080
#define HISTORY_SIZE 10
#define MAX_OUTPUT_SIZE 10280 //(MAX_COMMAND_SIZE + 4) * HISTORY_SIZE

static int decrypt(char* input, int cipher);
static char* trim(char* str);
char** parse_command(char* command);
int init_history(char history_arr[HISTORY_SIZE][MAX_COMMAND_SIZE]);
int history_to_string(char history_arr[HISTORY_SIZE][MAX_COMMAND_SIZE], char* history);
int add_to_history(char history_arr[HISTORY_SIZE][MAX_COMMAND_SIZE], char* command);
static void reaper(int signum);
void do_command(char command[MAX_COMMAND_SIZE], char output[MAX_OUTPUT_SIZE]);
int bang(char history[HISTORY_SIZE][MAX_COMMAND_SIZE], char command[MAX_COMMAND_SIZE], char output[MAX_OUTPUT_SIZE]);

int main(int argc, char** argv) {
  struct sockaddr_in server_addr;
  int socketid;
  int port, i;
  char* usage = "Invalid input.\nUsage: './server [-p <server port #>]'\n";
  struct sigaction new_action;

  //reap zombie processes
  new_action.sa_handler = reaper;
  sigemptyset (&new_action.sa_mask);
  new_action.sa_flags = 0;
  sigaction (SIGCHLD, &new_action, NULL);

  //initialize port to defined PORT
  port = PORT;
  i = 1;
  
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
	} else {
	  printf("%s", usage);
	  exit(-1);
	}
	
	i += 2;
      }
    }
  }
  
  printf("Server: Using port %d.\n", port);
  
  server_addr.sin_port = htons(port);
  server_addr.sin_addr.s_addr = INADDR_ANY;
  server_addr.sin_family = AF_INET;
  
  if( (socketid = socket(AF_INET, SOCK_STREAM, 0)) < 0) {
    printf("Server: Failed to create socket.\n");
    return -1;
  }

  if( bind(socketid, (struct sockaddr *) &server_addr, sizeof(server_addr)) != 0) { 
      printf("Server: Cannot bind socket.\n");
      close(socketid);
      return -1;
  }
  
  if( listen(socketid, 10) != 0 ) {
    printf("Server: Cannot listen on port\n");
    close(socketid);
    return -1;
  }

  //Loop and accept connections from clients, start a new process for each one
  printf("Server: Listening for clients\n");
  while(1) {
    struct sockaddr_in client_address;
    int client, client_size;
    pid_t pid;
    
    client_size = sizeof(client_address);

    //accept the client's connection
    if( (client = accept(socketid, (struct sockaddr *) &client_address, (socklen_t *) &client_size)) < 0 ) {
      printf("Server: Could not accept client.\n");
      continue;
    }

    //create the new process
    if( (pid = fork()) == 0) {
      printf("Server: executing client %d commands in a new process\n", client);
      char history[HISTORY_SIZE][MAX_COMMAND_SIZE];
      char command[MAX_COMMAND_SIZE];
      char commands[100][MAX_COMMAND_SIZE];
      char output[MAX_OUTPUT_SIZE];
      int parent_pid = getpid(); //the children of this PID will be returned for "jobs". Not sure if it is wanted here or on the main server instance.
      int commands_length = 0;

      init_history(history);
      
      while(1) {
	//clear buffers
	memset(command, 0, sizeof(command));
	memset(output, 0, sizeof(output));

	//reap zombies
	while( waitpid(0, NULL, WNOHANG) != -1 );
	
	//read, decrypt, trim command from client
	read(client, command, MAX_COMMAND_SIZE);
	decrypt(command, 9); //using last digit of my ID as cipher, 9
        strcpy(command, trim(command));

	//handle multiple commands
	if(strchr(command, ';')) {
	  add_to_history(history, command);
	  char* token;
	  const char d[2] = ";";
	  token = strtok(command, d);

	  while( token != NULL && commands_length < 100) {
	    strcpy(commands[commands_length], token);
	    strcpy(commands[commands_length], trim(commands[commands_length]));
	    commands_length++;
	    token = strtok(NULL, d);
	  }
	}

	int quit = 0;
	if(commands_length > 0) {
	  int i;
	  for( i = 0; i < commands_length; i++ ) {
	    //perform command as long is it is not an invalid bang operation
	    if( commands[i][0] != '!' || bang(history, commands[i], output) == 0) {

	      //handle pre-defined commands
	      if( strcmp(commands[i], "quit") == 0 ) {
		quit = 1;
		break;
	      } else if( strcmp( commands[i], "History" ) == 0  || strcmp( commands[i], "history" ) == 0 ) {
		history_to_string(history, output);
	      } else {
		if( strcmp( commands[i], "jobs" ) == 0 ) {
		  sprintf(commands[i], "pgrep -P %d", parent_pid);
		}
	    
		do_command(commands[i], output);
	      }
	    }
	  }
	} else {
	  //perform command as long is it is not an invalid bang operation
	  if( command[0] != '!' || bang(history, command, output) == 0) {
	    add_to_history(history, command);

	    //handle pre-defined commands
	    if( strcmp(command, "quit") == 0 ) {
	      break;
	    } else if( strcmp( command, "History" ) == 0  || strcmp( command, "history" ) == 0 ) {
	      history_to_string(history, output);
	    } else {
	      if( strcmp( command, "jobs" ) == 0 ) {
		sprintf(command, "pgrep -P %d", parent_pid);
	      }
	    
	      do_command(command, output);
	    }
	  }
	}

	if(quit) break;
	
	//write the stdout/stderr to the client
	write(client, output, MAX_OUTPUT_SIZE);
	commands_length = 0;
      }
 
      strcat(output, "Remote Shell Client: Connection closed.\n");
      write(client, output, sizeof(output));
      printf("Server: exiting process for client %d\n", client);
      close(client);
      exit(0);
    }	
  }
  
  close(socketid);
  printf("Server: Finished listening\n");
  return 0;
}

//decrypts a message, shifting each character down by the cipher
static int decrypt(char* input, int cipher) {
  register char* c = input;
  
  while(*c != 0) {
    *c = (*c - cipher);
    if(*c < 0) *c += 127;
    c++;
  }
  
  return 0;
}

/*
  This function trims all leading and trailing spaces
  from str. It allocates a new string and returns its
  pointer.
 */
char* trim(char* str) {
  int start, end, length, i = 0;

  while(str[i] == ' ') i++;
  start = i;
  i = strlen(str) - 1;
  while(str[i] == ' ') i--;
  end = i;
  length = (end - start) + 1;
  
  char* new_str = malloc(sizeof('a') * length);
  memset(new_str, 0, sizeof(new_str));
  strncpy(new_str, str + start, length);

  return new_str;
}


/**
   This function takes a command intended for the shell.
   The command is parsed to remove unnecessary whitespace
   and turned into an array of char*, where each word
   delimited by spaces becomes a char* in the array. The
   array ends with a NULL pointer. The pointer to the array
   is returned.
 */
char** parse_command(char* command) {
  //find the number of non-consecutive spaces in command
  int i;
  int len = strlen(command);
  int count = 0;
  char prev = 'a'; //arbitrary non-space character
  for(i = 0; i < len; i++) {
    if(command[i] == ' ' && prev != ' ') {
      count++;
    }
    prev = command[i];
  }

  //Allocate the array to hold all tokens (count + 1) and an ending NULL
  char** arr = malloc(sizeof(char*) * (count + 2));

  //Add tokens to array
  i = 0;
  char *ptr = strtok(command, " ");
  while(ptr != NULL) {
    arr[i++] = ptr;
    ptr = strtok(NULL, " ");
  }

  arr[count + 1] = (char *) NULL;
  
  return arr;
}

//sets all strings to be empty
int init_history(char history_arr[HISTORY_SIZE][MAX_COMMAND_SIZE]) {
  int i;
  for(i = 0; i < 10; i++) {
    memset(history_arr[i], 0, sizeof(history_arr[i]));
  }

  return 0;
}

//turns the history array into a single char*
int history_to_string(char history_arr[HISTORY_SIZE][MAX_COMMAND_SIZE], char* history) {
  int i;
  for(i = 0; i < HISTORY_SIZE; i++) {
    if( history_arr[i][0] == '\0' ) break;
    char tmp[MAX_COMMAND_SIZE + 4];
    sprintf(tmp, "%d) %s\n", i + 1, history_arr[i]);
    strcat(history, tmp);
  }

  return 0;
}

//adds an entry to history
int add_to_history(char history_arr[HISTORY_SIZE][MAX_COMMAND_SIZE], char* command) {
  int i;
  for( i = 0; i < HISTORY_SIZE; i++) {
    if( history_arr[i][0] == '\0' ) {
      strcpy(history_arr[i], command);
      return i;
    }
  }

  for( i = 0; i < HISTORY_SIZE - 1; i++) {
    strcpy(history_arr[i], history_arr[i + 1]);
  }

  strcpy(history_arr[HISTORY_SIZE - 1], command);
}

//handler for SIGCHILD, reaps exit code from child
static void reaper(int signum) 
{ 
  wait(NULL); 
} 

//takes a command, performs execvp on the command, appends the stdout/stderr to output
void do_command(char command[MAX_COMMAND_SIZE],
		char output[MAX_OUTPUT_SIZE]) {
  //parse command into char* array
  char** arr = parse_command(command);

  //create a pipe to get stdout & stderr from a new process
  int fd[2];
  pipe(fd);

  //create the execvp child process
  pid_t pid2;
  pid2 = fork();

  //in child, bind stdout and stderr to the pipe, execute the commands
  if(pid2 == 0) {
    close(fd[0]);
    while ((dup2(fd[1], 1) == -1) && (errno == EINTR)) {}
    while ((dup2(fd[1], 2) == -1) && (errno == EINTR)) {}
    close(fd[1]);
    execvp(arr[0], arr);
    exit(-1);
  }

  //wait for the child to finish, then read the piped data
  close(fd[1]);
  waitpid(pid2, NULL, 0);
  char tmp[MAX_OUTPUT_SIZE];
  memset(tmp, 0, sizeof(tmp));
  read(fd[0], tmp, MAX_OUTPUT_SIZE);
  strcat(output, tmp);
  close(fd[0]);
}

//takes in a bang command, retrieves the desired command from history,
//and gives output if the command was invalid
int bang(char history[HISTORY_SIZE][MAX_COMMAND_SIZE],
	  char command[MAX_COMMAND_SIZE],
	  char output[MAX_OUTPUT_SIZE]) {
  //!!
  if( command[1] == '!') {
    if ( strcmp(command, "!!") == 0 ) {
      if(history[0][0] == '\0') {
	strcat(output, "No commands in history\n");
	return -1;
      } else {
	int i = 0;
	while(history[i][0] != '\0' && i < HISTORY_SIZE) i++;
	strcpy(command, history[i - 1]);
	printf("command: %s\n", command);
      }
    } else {
      strcpy(output, "Invalid usage\n");
      return -3;
    }
  }
  //!N
  else {
    int len = strlen(command);
    char strnum[len - 1];
    memcpy(strnum, &command[1], sizeof(strnum));
    int num = atoi(strnum);

    if(num > 10 || num <= 0) {
      strcat(output, "Invalid usage\n");
      return -3;
    } else if(history[num - 1][0] == '\0') {
      strcat(output, "No such command in history\n");
      return -2;
    }

    strcpy(command, history[num - 1]);
  }

  return 0;
}
	    
