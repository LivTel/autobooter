# Makefile -*- mode: Fundamental;-*-
# $Header: /home/cjm/cvs/autobooter/java/Makefile,v 0.1 2000-07-06 10:31:39 cjm Exp $ 
include ../../Makefile.common
include	../Makefile.common

BINDIR 		= $(AB_BIN_HOME)/java
DOCSDIR 	= $(AB_DOC_HOME)/javadocs
DOCFLAGS 	= -version -author -private

SRCS 		= AutoBooter.java AutoBooterProcessThread.java AutoBooterProcessParentInterface.java \
		AutoBooterStreamThread.java
OBJS 		= $(SRCS:%.java=$(BINDIR)/%.class)
DOCS 		= $(SRCS:%.java=$(DOCSDIR)/%.html)

CONFIG_SRCS	= ltccd1.autobooter.properties ltccd2.autobooter.properties
CONFIG_OBJS	= $(CONFIG_SRCS:%.properties=$(BINDIR)/%.properties)

top: $(OBJS) config docs

$(BINDIR)/%.class:%.java
	$(JAVAC) $(JAVAC_OPTIONS) -d $(BINDIR) $<

config: $(CONFIG_OBJS)

$(BINDIR)/%.properties: %.properties
	$(CP) $< $@

docs: $(DOCS)

$(DOCS): $(SRCS)
	$(JAVADOC) -d $(DOCSDIR) $(DOCFLAGS) $(SRCS)

checkout:
	$(CO) $(CO_OPTIONS) $(SRCS)

checkin:
	-$(CI) $(CI_OPTIONS) $(SRCS)

clean: tidy
	-$(RM) $(RM_OPTIONS) $(OBJS)

tidy:
	-$(RM) $(RM_OPTIONS) $(TIDY_OPTIONS)

backup: tidy

#
# $Log: not supported by cvs2svn $
#
