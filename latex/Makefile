# $Header: /home/cjm/cvs/autobooter/latex/Makefile,v 0.2 2000-10-03 16:39:48 cjm Exp $

include ../../Makefile.common
include ../Makefile.common

DOCSDIR 	= $(AB_DOC_HOME)/latex

TEXSRCS 	= autobooter.tex
FIGSRCS 	= autobooter_dirtree.fig

DVIOBJS		= $(TEXSRCS:%.tex=%.dvi)
PSOBJS		= $(TEXSRCS:%.tex=$(DOCSDIR)/%.ps)

EPSOBJS		= $(FIGSRCS:%.fig=%.eps)

top: $(PSOBJS)

$(DOCSDIR)/%.ps: %.dvi
	$(DVIPS) $< -o
	$(MV) $*.ps $(DOCSDIR)

$(DVIOBJS): $(EPSOBJS)

%.dvi: %.tex
	$(LATEX) $<
	$(LATEX) $<
	$(LATEX) $<

%.eps: %.fig
	$(FIG2DEV) $(FIG2DEV_OPTIONS) $< $@

depend:
	echo "NO depend rule for latex files."

clean:
	$(RM) $(RM_OPTIONS) $(DVIOBJS) $(PSOBJS) $(EPSOBJS) $(LATEX_TIDY_OPTIONS) $(TIDY_OPTIONS)

tidy:
	-$(RM) $(RM_OPTIONS) $(LATEX_TIDY_OPTIONS) $(TIDY_OPTIONS)

backup: tidy
	-$(RM) $(RM_OPTIONS) $(DVIOBJS) $(EPSOBJS)

checkin:
	-$(CI) $(CI_OPTIONS) $(TEXSRCS) $(FIGSRCS)

checkout:
	-$(CO) $(CO_OPTIONS) $(TEXSRCS) $(FIGSRCS)

#
# $Log: not supported by cvs2svn $
# Revision 0.1  2000/08/08 11:21:38  cjm
# initial revision.
#
#
