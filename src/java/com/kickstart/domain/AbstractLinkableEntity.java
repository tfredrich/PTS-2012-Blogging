package com.kickstart.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.code.morphia.annotations.NotSaved;
import com.strategicgains.repoexpress.mongodb.AbstractMongodbEntity;
import com.strategicgains.restexpress.domain.XLink;

public class AbstractLinkableEntity
extends AbstractMongodbEntity
implements Linkable
{
	@NotSaved
	private List<XLink> links;

	@Override
	public List<XLink> getLinks()
	{
		return Collections.unmodifiableList(links);
	}

	@Override
	public void setLinks(List<XLink> links)
	{
		this.links = new ArrayList<XLink>(links);
	}
	
	@Override
	public void addLink(XLink link)
	{
		if (links == null)
		{
			links = new ArrayList<XLink>();
		}
		
		links.add(link);
	}
}
