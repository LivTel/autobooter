# Makefile
# $Header: /home/cjm/cvs/autobooter/java/Makefile,v 0.9 2004-10-13 19:56:19 cjm Exp $ 
include ../../Makefile.common
include	../Makefile.common

BINDIR 		= $(AB_BIN_HOME)/java
DOCSDIR 	= $(AB_DOC_HOME)/javadocs
DOCFLAGS 	= -version -author -private

SRCS 		= AutoBooterProcessStatusInterface.java AutoBooterLogInterface.java AutoBooterConstants.java \
		AutoBooterStatus.java AutoBooterStreamThread.java AutoBooterProcessThread.java AutoBooter.java 
OBJS 		= $(SRCS:%.java=$(BINDIR)/%.class)
DOCS 		= $(SRCS:%.java=$(DOCSDIR)/%.html)

CONFIG_SRCS	= ltccd1.autobooter.properties ltccd2.autobooter.properties ltccd3.autobooter.properties \
		ftccd1.autobooter.properties ftccd2.autobooter.properties \
		spectrograph.autobooter.properties supircam1.autobooter.properties supircam2.autobooter.properties \
		ftspec1.autobooter.properties nuview1.autobooter.properties spectrograph.autobooter.properties
CONFIG_OBJS	= $(CONFIG_SRCS:%.properties=$(BINDIR)/%.properties)
SCRIPT_SRCS  	= ltccd1.autobooter ltccd2.autobooter ltccd3.autobooter \
		ftccd1.autobooter ftccd2.autobooter \
		supircam1.autobooter supircam2.autobooter ftspec1.autobooter spectrograph.autobooter nuview1.autobooter
SCRIPT_OBJS	= $(SCRIPT_SRCS:%=$(BINDIR)/%)

top: $(OBJS) config scripts docs

$(BINDIR)/%.class:%.java
	$(JAVAC) $(JAVAC_OPTIONS) -d $(BINDIR) $<

config: $(CONFIG_OBJS)

$(BINDIR)/%.properties: %.properties
	$(CP) $< $@

scripts: $(SCRIPT_OBJS)

$(BINDIR)/%.autobooter: %.autobooter
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
# Revision 0.8  2004/08/03 09:58:06  cjm
# S55autobooter files now just .autobooter.
#
# Revision 0.7  2004/07/06 18:13:46  cjm
# Added new instrument machines.
#
# Revision 0.6  2004/05/24 18:22:13  cjm
# Added ftccd2.
#
# Revision 0.5  2004/04/19 14:48:23  cjm
# Added spectrograph configs.
#
# Revision 0.4  2004/03/23 17:45:17  cjm
# Created multi host startup script.
# Added ftccd1, supircam1, ftspec1 properties.
#
# Revision 0.3  2001/07/11 15:28:49  cjm
# Config changes.
#
# Revision 0.2  2000/07/14 16:33:04  cjm
# Backup.
#
# Revision 0.1  2000/07/06 10:31:39  cjm
# initial revision.
#
#
