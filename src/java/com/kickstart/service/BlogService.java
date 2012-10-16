package com.kickstart.service;

import java.util.List;

import com.kickstart.domain.Blog;
import com.kickstart.persistence.BlogRepository;
import com.strategicgains.restexpress.query.QueryFilter;
import com.strategicgains.restexpress.query.QueryOrder;
import com.strategicgains.restexpress.query.QueryRange;
import com.strategicgains.syntaxe.ValidationEngine;

/**
 * @author toddf
 * @since Oct 9, 2012
 */
public class BlogService
{
	private BlogRepository blogs;

	public BlogService(BlogRepository blogRepository)
	{
		super();
		this.blogs = blogRepository;
	}

	public Blog create(Blog blog)
	{
		ValidationEngine.validateAndThrow(blog);
		return blogs.create(blog);
	}

	public Blog read(String id)
	{
		return blogs.read(id);
	}

	public void update(Blog blog)
	{
		ValidationEngine.validateAndThrow(blog);
		blogs.update(blog);
	}

	public void delete(String id)
	{
		blogs.delete(id);
	}

	public List<Blog> readAll(QueryFilter filter, QueryRange range, QueryOrder order)
	{
		return blogs.readAll(filter, range, order);
	}

	public long count(QueryFilter filter)
	{
		return blogs.count(filter);

	}
}
