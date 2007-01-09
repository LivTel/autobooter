# Makefile 
# $Header: /home/cjm/cvs/autobooter/Makefile,v 1.1 2007-01-09 15:00:18 cjm Exp $ 

include ../Makefile.common

MAKE = make
DIRS = java latex

top:
	@for i in $(DIRS); \
	do \
		(echo making in $$i...; $(MAKE) -C $$i); \
	done;

checkin:
	-@for i in $(DIRS); \
	do \
		(echo checkin in $$i...; $(MAKE) -C $$i checkin; cd $$i; $(CI) $(CI_OPTIONS) Makefile); \
	done;

checkout:
	@for i in $(DIRS); \
	do \
		(echo checkout in $$i...; cd $$i; $(CO) $(CO_OPTIONS) Makefile; $(MAKE) checkout); \
	done;

depend:
	echo autobooter package has no depend at the moment.

clean:
	-$(RM) $(RM_OPTIONS) $(TIDY_OPTIONS)
	@for i in $(DIRS); \
	do \
		(echo clean in $$i...; $(MAKE) -C $$i clean); \
	done;

tidy:
	-$(RM) $(RM_OPTIONS) $(TIDY_OPTIONS)
	@for i in $(DIRS); \
	do \
		(echo tidy in $$i...; $(MAKE) -C $$i tidy); \
	done;

backup: checkin
	@for i in $(DIRS); \
	do \
		(echo backup in $$i...; $(MAKE) -C $$i backup); \
	done;
	-$(RM) $(RM_OPTIONS) $(TIDY_OPTIONS)
	tar cvf $(BACKUP_DIR)/autobooter.tar .
	compress $(BACKUP_DIR)/autobooter.tar

# $Log: not supported by cvs2svn $
