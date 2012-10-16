package com.kickstart.service;

import java.util.List;

import com.kickstart.domain.BlogEntry;
import com.kickstart.persistence.BlogEntryRepository;
import com.strategicgains.restexpress.query.QueryFilter;
import com.strategicgains.restexpress.query.QueryOrder;
import com.strategicgains.restexpress.query.QueryRange;
import com.strategicgains.syntaxe.ValidationEngine;

/**
 * @author toddf
 * @since Oct 9, 2012
 */
public class BlogEntryService
{
	private BlogEntryRepository blogEntries;

	public BlogEntryService(BlogEntryRepository blogEntryRepository)
	{
		super();
		this.blogEntries = blogEntryRepository;
	}

	public BlogEntry create(BlogEntry blogEntry)
	{
		ValidationEngine.validateAndThrow(blogEntry);
		return blogEntries.create(blogEntry);
	}

	public BlogEntry read(String id)
	{
		return blogEntries.read(id);
	}

	public void update(BlogEntry blogEntry)
	{
		ValidationEngine.validateAndThrow(blogEntry);
		blogEntries.update(blogEntry);
	}

	public void delete(String id)
	{
		blogEntries.delete(id);
	}

	public List<BlogEntry> readAll(QueryFilter filter, QueryRange range, QueryOrder order)
	{
		return blogEntries.readAll(filter, range, order);
	}

	public long count(QueryFilter filter)
	{
		return blogEntries.count(filter);

	}
}
