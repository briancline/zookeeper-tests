JFLAGS = 
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	ConnectionWatcher.java \
	CreateGroup.java \
	JoinGroup.java \
	ListGroup.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
