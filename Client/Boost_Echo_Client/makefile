CFLAGS:=-c -Wall -Weffc++ -g -std=c++11 -Iinclude
LDFLAGS:=-lboost_system -lboost_thread -pthread

all: EchoClient
	g++ -o bin/BGSclient bin/connectionHandler.o bin/main.o bin/Connection_thread.o bin/IO_thread.o $(LDFLAGS)
<<<<<<< HEAD

EchoClient: bin/connectionHandler.o bin/main.o bin/Connection_thread.o bin/IO_thread.o

=======

EchoClient: bin/connectionHandler.o bin/main.o bin/Connection_thread.o bin/IO_thread.o
	
>>>>>>> 69256156c96d94952837c6dbb78f5dfe366b0652
bin/connectionHandler.o: src/connectionHandler.cpp
	g++ $(CFLAGS) -o bin/connectionHandler.o src/connectionHandler.cpp

bin/main.o: src/main.cpp
	g++ $(CFLAGS) -o bin/main.o src/main.cpp

bin/Connection_thread.o: src/Connection_thread.cpp
	g++ $(CFLAGS) -o bin/Connection_thread.o src/Connection_thread.cpp
bin/IO_thread.o: src/IO_thread.cpp
	g++ $(CFLAGS) -o bin/IO_thread.o src/IO_thread.cpp
.PHONY: clean
clean:
	rm -f bin/*