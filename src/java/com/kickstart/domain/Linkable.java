package com.kickstart.domain;

import java.util.List;

import com.strategicgains.restexpress.domain.XLink;

public interface Linkable
{
	public List<XLink> getLinks();
	public void setLinks(List<XLink> links);
	public void addLink(XLink link);
}