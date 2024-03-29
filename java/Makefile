# Makefile
# $Header: /home/cjm/cvs/autobooter/java/Makefile,v 0.27 2020-04-28 15:01:33 cjm Exp $ 
include ../../Makefile.common
include	../Makefile.common

# We need no later than Java 1.6 for Sprat
AB_JAVAC_OPTIONS= -source 1.6 -target 1.6
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
		ftspec1.autobooter.properties ftspec2.autobooter.properties \
		nuview1.autobooter.properties ratcam1.autobooter.properties \
		autoguider1.autobooter.properties autoguider2.autobooter.properties \
		estar6.autobooter.properties datared.autobooter.properties \
		rise.autobooter.properties ringo1.autobooter.properties ringo2.autobooter.properties \
		tiptilt1.autobooter.properties thor.autobooter.properties micc1.autobooter.properties \
		o1.autobooter.properties ringo3.autobooter.properties ringo3-1.autobooter.properties \
		ringo3-2.autobooter.properties iucaaag.autobooter.properties \
		frodospec1.frodospec.autobooter.properties frodospec1.sprat.autobooter.properties \
		sprat1.sprat.autobooter.properties ioi1.sprat.autobooter.properties ioi1.ioi.autobooter.properties \
		lotus1.lotus.autobooter.properties lotus2.lotus.autobooter.properties moptop1.autobooter.properties \
		moptop2.autobooter.properties moptop3.autobooter.properties moptop4.autobooter.properties \
		wms1.autobooter.properties liric1.autobooter.properties
#sprat1.autobooter.properties frodospec1.autobooter.properties ioi1.autobooter.properties 
CONFIG_OBJS	= $(CONFIG_SRCS:%.properties=$(BINDIR)/%.properties)
SCRIPT_SRCS  	= ltccd1.autobooter ltccd2.autobooter ltccd3.autobooter \
		ftccd1.autobooter ftccd2.autobooter \
		supircam1.autobooter supircam2.autobooter ftspec1.autobooter ftspec2.autobooter \
		spectrograph.autobooter nuview1.autobooter ratcam1.autobooter \
		autoguider1.autobooter autoguider2.autobooter \
		estar6.autobooter datared.autobooter rise.autobooter ringo1.autobooter \
		ringo2.autobooter tiptilt1.autobooter thor.autobooter micc1.autobooter o1.autobooter \
		ringo3.autobooter ringo3-1.autobooter ringo3-2.autobooter ioi1.autobooter iucaaag.autobooter \
		frodospec1.frodospec.autobooter frodospec1.sprat.autobooter sprat1.sprat.autobooter \
		ioi1.sprat.autobooter ioi1.ioi.autobooter lotus1.lotus.autobooter lotus2.lotus.autobooter \
		moptop1.autobooter moptop2.autobooter moptop3.autobooter moptop4.autobooter wms1.autobooter \
		liric1.autobooter
#frodospec1.autobooter sprat1.autobooter
SCRIPT_OBJS	= $(SCRIPT_SRCS:%=$(BINDIR)/%)

top: $(OBJS) config scripts docs

$(BINDIR)/%.class:%.java
	$(JAVAC) $(JAVAC_OPTIONS) $(AB_JAVAC_OPTIONS) -d $(BINDIR) $<

config: $(CONFIG_OBJS)

$(BINDIR)/%.properties: %.properties
	$(CP) -f $< $@

scripts: $(SCRIPT_OBJS)

$(BINDIR)/%.autobooter: %.autobooter
	$(CP) -f $< $@

docs: $(DOCS)

$(DOCS): $(SRCS)
	$(JAVADOC) -d $(DOCSDIR) $(DOCFLAGS) $(SRCS)

checkout:
	$(CO) $(CO_OPTIONS) $(SRCS)

checkin:
	-$(CI) $(CI_OPTIONS) $(SRCS) $(SCRIPT_SRCS) $(CONFIG_SRCS)

clean: tidy
	-$(RM) $(RM_OPTIONS) $(OBJS)

tidy:
	-$(RM) $(RM_OPTIONS) $(TIDY_OPTIONS)

backup: tidy

#
# $Log: not supported by cvs2svn $
# Revision 0.26  2015/06/09 13:40:35  cjm
# Added LOTUS configs.
#
# Revision 0.25  2013/11/27 12:16:32  cjm
# Addded iuaccaag.
#
# Revision 0.24  2012/11/12 15:50:21  cjm
# Added ringo3-1 and ringo3-2.
#
# Revision 0.23  2011/11/23 11:07:19  cjm
# Added o1 configs.
#
# Revision 0.22  2010/10/07 13:26:09  cjm
# Added thor.
#
# Revision 0.21  2010/03/24 10:47:57  cjm
# Added ringo2 / tiptilt1.
#
# Revision 0.20  2009/10/21 16:27:34  cjm
# Added rise.
#
# Revision 0.19  2009/02/26 14:46:48  cjm
# Added -f to autobooter copy.
#
# Revision 0.18  2008/12/01 15:33:30  cjm
# Added datared config files.
#
# Revision 0.17  2008/01/11 15:08:20  cjm
# Added estar6 for FrodoSpec testing.
#
# Revision 0.16  2007/07/19 11:28:23  cjm
# Added frodospec1 configs.
#
# Revision 0.15  2006/11/20 19:10:03  cjm
# Added CONFIG_SRCS to checkin.
#
# Revision 0.14  2006/11/20 19:07:43  cjm
# Added SCRIPT_SRCS to checkin.
#
# Revision 0.13  2006/08/09 15:08:06  cjm
# Changed cp option.
#
# Revision 0.12  2006/07/20 15:38:50  cjm
# Added autoguider1 config.
#
# Revision 0.11  2006/06/26 15:48:58  cjm
# Added FTSpecS config.
#
# Revision 0.10  2005/11/29 15:23:57  cjm
# Added ratcam1 configs.
#
# Revision 0.9  2004/10/13 19:56:19  cjm
# Added supircam2.
#
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
