package com.kickstart.persistence;

import java.util.List;

import com.kickstart.domain.Blog;
import com.mongodb.Mongo;
import com.strategicgains.repoexpress.mongodb.MongodbEntityRepository;
import com.strategicgains.restexpress.query.QueryFilter;
import com.strategicgains.restexpress.query.QueryOrder;
import com.strategicgains.restexpress.query.QueryRange;

/**
 * @author toddf
 * @since Oct 10, 2012
 */
public class MongodbBlogRepository
extends MongodbEntityRepository<Blog>
implements BlogRepository
{
	@SuppressWarnings("unchecked")
	public MongodbBlogRepository(Mongo mongo, String databaseName)
	{
		super(mongo, databaseName, Blog.class);
	}

	@Override
	public List<Blog> readAll(QueryFilter filter, QueryRange range, QueryOrder order)
	{
		return query(Blog.class, range, filter, order);
	}

	@Override
	public long count(QueryFilter filter)
	{
		return count(Blog.class, filter);
	}
}
