package com.kickstart.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.code.morphia.annotations.NotSaved;
import com.strategicgains.hyperexpress.domain.Link;
import com.strategicgains.hyperexpress.domain.Linkable;
import com.strategicgains.repoexpress.mongodb.AbstractMongodbEntity;

public class AbstractLinkableEntity
extends AbstractMongodbEntity
implements Linkable
{
	@NotSaved
	private List<Link> links;

	@Override
	public List<Link> getLinks()
	{
		return Collections.unmodifiableList(links);
	}

	@Override
	public void setLinks(List<Link> links)
	{
		this.links = new ArrayList<Link>(links);
	}
	
	@Override
	public void addLink(Link link)
	{
		if (links == null)
		{
			links = new ArrayList<Link>();
		}
		
		links.add(link);
	}
}
