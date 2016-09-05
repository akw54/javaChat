.PHONY : build genkey server client keycrack clean

build : 
	javac *.java
genkey :
	java GenerateKeys $(MPRIME) $(NPRIME)
server :
	java ServerImpl $(PORT) $(E) $(C) $(D) $(DC)
client :
	java ClientImpl $(SERVER) $(PORT) $(E) $(C) $(D) $(DC)
keycrack : 
	echo "i didn't do this"
clean :
	rm *.class
