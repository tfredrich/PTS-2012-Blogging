package com.kickstart.persistence;

import java.util.List;

import com.kickstart.domain.Blog;
import com.strategicgains.repoexpress.Repository;
import com.strategicgains.restexpress.query.QueryFilter;
import com.strategicgains.restexpress.query.QueryOrder;
import com.strategicgains.restexpress.query.QueryRange;

/**
 * @author toddf
 * @since Oct 9, 2012
 */
public interface BlogRepository
extends Repository<Blog>
{
	public List<Blog> readAll(QueryFilter filter, QueryRange range, QueryOrder order);
	public long count(QueryFilter filter);
}
