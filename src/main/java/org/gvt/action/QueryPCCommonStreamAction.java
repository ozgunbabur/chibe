package org.gvt.action;

import org.biopax.paxtools.io.pathwayCommons.PathwayCommons2Client;
import org.biopax.paxtools.io.pathwayCommons.util.PathwayCommonsException;
import org.biopax.paxtools.model.Model;
import org.gvt.ChisioMain;
import org.gvt.gui.AbstractQueryParamDialog;
import org.gvt.gui.CommonStreamQueryParamWithEntitiesDialog;

import java.util.List;

/**
 * @author Ozgun Babur
 *
 */
public class QueryPCCommonStreamAction extends QueryPCAction
{
	public QueryPCCommonStreamAction(ChisioMain main, boolean useSelected)
	{
		super(main, "Query Common Stream", useSelected);
	}

	public void run()
	{
		execute();
	}

	@Override
	protected Model doQuery() throws PathwayCommonsException
	{
		List<String> sourceSymbols = options.getFormattedSourceList();

		PathwayCommons2Client pc2 = new PathwayCommons2Client();
		pc2.setGraphQueryLimit(options.getLengthLimit());
		return pc2.getCommonStream(sourceSymbols, (options.isDirection() ==
			AbstractQueryParamDialog.DOWNSTREAM ?
			PathwayCommons2Client.STREAM_DIRECTION.DOWNSTREAM :
			PathwayCommons2Client.STREAM_DIRECTION.UPSTREAM));
	}

	@Override
	protected AbstractQueryParamDialog getDialog()
	{
		return new CommonStreamQueryParamWithEntitiesDialog(main, null);
	}

	@Override
	protected boolean canQuery()
	{
		return !options.getFormattedSourceList().isEmpty();
	}
}
