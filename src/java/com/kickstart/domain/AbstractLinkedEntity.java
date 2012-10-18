package com.kickstart.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.code.morphia.annotations.NotSaved;
import com.strategicgains.repoexpress.mongodb.AbstractMongodbEntity;
import com.strategicgains.restexpress.domain.XLink;

public class AbstractLinkedEntity extends AbstractMongodbEntity
{
	@NotSaved
	private List<XLink> links;

	public List<XLink> getLinks()
	{
		return Collections.unmodifiableList(links);
	}

	public void setLinks(List<XLink> links)
	{
		this.links = links;
	}
	
	public void addLink(XLink link)
	{
		if (links == null)
		{
			links = new ArrayList<XLink>();
		}
		
		links.add(link);
	}
}
