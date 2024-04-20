JAVAC = javac
JAVA = java
SRC = ./Estore
BIN = ./Estore/bin

CLIENT_MAIN = client.EstoreClient
SERVER_MAIN = server.EstoreServer

CLIENT_JAR = $(BIN)/EstoreClient.jar
SERVER_JAR = $(BIN)/EstoreServer.jar

# Collect all Java files in the subdirectories
SOURCES = $(shell find $(SRC) -name '*.java')
CLASSES = $(SOURCES:.java=.class)

all: compile jars

compile: $(CLASSES)

$(CLASSES): $(SOURCES)
	$(JAVAC) -d $(BIN) $(SOURCES)

jars: $(CLIENT_JAR) $(SERVER_JAR)

$(CLIENT_JAR): $(CLASSES)
	echo "Main-Class: $(CLIENT_MAIN)" > $(BIN)/client_manifest.mf
	jar -cfm $(CLIENT_JAR) $(BIN)/client_manifest.mf -C $(BIN) .

$(SERVER_JAR): $(CLASSES)
	echo "Main-Class: $(SERVER_MAIN)" > $(BIN)/server_manifest.mf
	jar -cfm $(SERVER_JAR) $(BIN)/server_manifest.mf -C $(BIN) .

runserver: $(SERVER_JAR)
	$(JAVA) -jar $(SERVER_JAR)

runclient: $(CLIENT_JAR)
	$(JAVA) -jar $(CLIENT_JAR)

clean:
	find $(BIN) -type f -name '*.class' -delete
	rm -f $(CLIENT_JAR) $(SERVER_JAR) $(BIN)/client_manifest.mf $(BIN)/server_manifest.mf

.PHONY: all clean runserver runclient compile jars