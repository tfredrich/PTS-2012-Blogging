package com.kickstart.persistence;

import java.util.List;

import com.kickstart.domain.BlogEntry;
import com.mongodb.Mongo;
import com.strategicgains.repoexpress.mongodb.MongodbEntityRepository;
import com.strategicgains.restexpress.query.QueryFilter;
import com.strategicgains.restexpress.query.QueryOrder;
import com.strategicgains.restexpress.query.QueryRange;

/**
 * @author toddf
 * @since Oct 10, 2012
 */
public class MongodbBlogEntryRepository
extends MongodbEntityRepository<BlogEntry>
implements BlogEntryRepository
{
	@SuppressWarnings("unchecked")
	public MongodbBlogEntryRepository(Mongo mongo, String databaseName)
	{
		super(mongo, databaseName, BlogEntry.class);
	}

	@Override
	public List<BlogEntry> readAll(QueryFilter filter, QueryRange range, QueryOrder order)
	{
		return query(BlogEntry.class, range, filter, order);
	}

	@Override
	public long count(QueryFilter filter)
	{
		return count(BlogEntry.class, filter);
	}
}
