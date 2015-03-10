package org.gvt;

import org.biopax.paxtools.controller.Completer;
import org.biopax.paxtools.controller.SimpleEditorMap;
import org.biopax.paxtools.io.SimpleIOHandler;
import org.biopax.paxtools.model.BioPAXElement;
import org.biopax.paxtools.model.Model;
import org.biopax.paxtools.model.level3.Control;
import org.biopax.paxtools.model.level3.Conversion;
import org.biopax.paxtools.model.level3.ProteinReference;
import org.biopax.paxtools.model.level3.Xref;
import org.junit.Test;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Ozgun Babur
 */
public class ReactionToGeneFilePreparer
{
	@Test
	public void prepare() throws IOException
	{
		SimpleIOHandler io = new SimpleIOHandler();
		Model model = io.convertFromOWL(new FileInputStream(
			"/home/ozgun/Projects/biopax-pattern/Pathway Commons.5.Detailed_Process_Data.BIOPAX.owl"));

		Completer cpt = new Completer(SimpleEditorMap.L3);

		BufferedWriter writer = new BufferedWriter(new FileWriter("/home/ozgun/Projects/chibe/resources/reaction2gene.txt"));

		boolean first = true;

		for (Conversion conv : model.getObjects(Conversion.class))
		{
			Set<BioPAXElement> set = new HashSet<BioPAXElement>();
			set.add(conv);

			set.addAll(conv.getControlledOf());

			set = cpt.complete(set, model);

			Set<String> symbols = new HashSet<String>();
			Set<Control> controls = new HashSet<Control>();
			for (BioPAXElement ele : set)
			{
				if (ele instanceof ProteinReference)
				{
					Set<String> symbolSet = getSymbols((ProteinReference) ele);
					if (symbolSet != null) symbols.addAll(symbolSet);
				}
				else if (ele instanceof Control)
				{
					controls.add((Control) ele);
				}
			}

			if (!symbols.isEmpty())
			{
				if (first) first = false;
				else writer.write("\n");

				writer.write(conv.getRDFId());

				for (Control control : controls)
				{
					writer.write(" " + control.getRDFId());
				}

				for (String symbol : symbols)
				{
					writer.write("\t" + symbol);
				}
			}

		}
		writer.close();
	}

	private Set<String> getSymbols(ProteinReference prot)
	{
		Set<String> set = new HashSet<String>();
		for (Xref xref : prot.getXref())
		{
			if (xref.getDb() != null && xref.getDb().equals("HGNC Symbol"))
			{
				set.add(xref.getId());
			}
		}
		return set;
	}
}
