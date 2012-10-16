package com.kickstart.persistence;

import java.util.List;

import com.kickstart.domain.Comment;
import com.mongodb.Mongo;
import com.strategicgains.repoexpress.mongodb.MongodbEntityRepository;
import com.strategicgains.restexpress.query.QueryFilter;
import com.strategicgains.restexpress.query.QueryOrder;
import com.strategicgains.restexpress.query.QueryRange;

/**
 * @author toddf
 * @since Oct 10, 2012
 */
public class MongodbCommentRepository
extends MongodbEntityRepository<Comment>
implements CommentRepository
{
	@SuppressWarnings("unchecked")
	public MongodbCommentRepository(Mongo mongo, String databaseName)
	{
		super(mongo, databaseName, Comment.class);
	}

	@Override
	public List<Comment> readAll(QueryFilter filter, QueryRange range, QueryOrder order)
	{
		return query(Comment.class, range, filter, order);
	}

	@Override
	public long count(QueryFilter filter)
	{
		return count(Comment.class, filter);
	}
}
