#Created by: Mziwokholo Tshem
#Date: 20 April 2024
JAVAC=/usr/bin/javac
.SUFFIXES: .java .class

SRCDIR=src
BINDIR=bin
DOCDIR=doc

default:
	$(JAVAC) -d $(BINDIR) $(SRCDIR)/*.java $<

clean:
	rm -d  $(BINDIR)/*.class
	rm -Rf doc
	rm -d output-OS1
	
doc:
	javadoc -d $(DOCDIR) $(SRCDIR)/*.java
        
run:    
	java -cp $(BINDIR) VirtualToPhysicalAddress

