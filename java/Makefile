# Makefile -*- mode: Fundamental;-*-
# $Header: /home/cjm/cvs/autobooter/java/Makefile,v 0.3 2001-07-11 15:28:49 cjm Exp $ 
include ../../Makefile.common
include	../Makefile.common

BINDIR 		= $(AB_BIN_HOME)/java
DOCSDIR 	= $(AB_DOC_HOME)/javadocs
DOCFLAGS 	= -version -author -private

SRCS 		= AutoBooter.java AutoBooterProcessThread.java AutoBooterProcessParentInterface.java \
		AutoBooterStreamThread.java
OBJS 		= $(SRCS:%.java=$(BINDIR)/%.class)
DOCS 		= $(SRCS:%.java=$(DOCSDIR)/%.html)

CONFIG_SRCS	= ltccd1.autobooter.properties ltccd2.autobooter.properties spectrograph.autobooter.properties
CONFIG_OBJS	= $(CONFIG_SRCS:%.properties=$(BINDIR)/%.properties)

STARTUP_SCRIPT  = S55autobooter

top: $(OBJS) config startupscript docs
	
$(BINDIR)/%.class:%.java
	$(JAVAC) $(JAVAC_OPTIONS) -d $(BINDIR) $<

config: $(CONFIG_OBJS)

$(BINDIR)/%.properties: %.properties
	$(CP) $< $@

startupscript: $(BINDIR)/S55autobooter

$(BINDIR)/S55autobooter: S55autobooter
	$(CP) S55autobooter $@

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
# Revision 0.2  2000/07/14 16:33:04  cjm
# Backup.
#
# Revision 0.1  2000/07/06 10:31:39  cjm
# initial revision.
#
#
