package com.kickstart.controller;

import java.util.List;

import org.jboss.netty.handler.codec.http.HttpMethod;

import com.kickstart.Constants;
import com.kickstart.domain.Blog;
import com.strategicgains.repoexpress.mongodb.MongodbEntityRepository;
import com.strategicgains.restexpress.Request;
import com.strategicgains.restexpress.Response;
import com.strategicgains.restexpress.domain.XLink;
import com.strategicgains.restexpress.exception.BadRequestException;
import com.strategicgains.restexpress.query.QueryFilter;
import com.strategicgains.restexpress.query.QueryOrder;
import com.strategicgains.restexpress.query.QueryRange;
import com.strategicgains.restexpress.util.XLinkUtils;
import com.strategicgains.syntaxe.ValidationEngine;

public class BlogController
{
	private MongodbEntityRepository<Blog> blogs;
	
	public BlogController(MongodbEntityRepository<Blog> blogRepository)
	{
		super();
		this.blogs = blogRepository;
	}

	public String create(Request request, Response response)
	{
		Blog blog = request.getBodyAs(Blog.class, "Blog details not provided");
		ValidationEngine.validateAndThrow(blog);
		Blog saved = blogs.create(blog);

		// Construct the response for create...
		response.setResponseCreated();

		// Include the Location header...
		String locationUrl = request.getNamedUrl(HttpMethod.GET, Constants.BLOG_READ_ROUTE);
		response.addLocationHeader(XLinkUtils.asLocationUrl(locationUrl, Constants.BLOG_ID_PARAMETER, saved.getId()));

		// Return the newly-created ID...
		return saved.getId();
	}

	public Blog read(Request request, Response response)
	{
		String id = request.getUrlDecodedHeader(Constants.BLOG_ID_PARAMETER, "No Blog ID supplied");
		Blog result = blogs.read(id);

		// Add 'self' link
		String selfUrlPattern = request.getNamedUrl(HttpMethod.GET, Constants.BLOG_READ_ROUTE);
		String selfUrl = XLinkUtils.asLocationUrl(selfUrlPattern, Constants.BLOG_ID_PARAMETER, result.getId());
		result.addLink(new XLink("self", selfUrl));

		// Add 'entries' link
		String entriesUrlPattern = request.getNamedUrl(HttpMethod.GET, Constants.BLOG_ENTRIES_READ_ROUTE);
		String entriesUrl = XLinkUtils.asLocationUrl(entriesUrlPattern, Constants.BLOG_ID_PARAMETER, result.getId());
		result.addLink(new XLink("entries", entriesUrl));
		return result;
	}

	public List<Blog> readAll(Request request, Response response)
	{
		QueryFilter filter = QueryFilter.parseFrom(request);
		QueryOrder order = QueryOrder.parseFrom(request);
		QueryRange range = QueryRange.parseFrom(request, 20);
		List<Blog> results = blogs.readAll(filter, range, order);
		response.setCollectionResponse(range, results.size(), blogs.count(filter));
		
		// Add 'self' links
		String urlPattern = request.getNamedUrl(HttpMethod.GET, Constants.BLOG_READ_ROUTE);
		
		for (Blog blog : results)
		{
			String selfUrl = XLinkUtils.asLocationUrl(urlPattern, Constants.BLOG_ID_PARAMETER, blog.getId());
			blog.addLink(new XLink("self", selfUrl));
		}

		return results;
	}

	public void update(Request request, Response response)
	{
		String id = request.getUrlDecodedHeader(Constants.BLOG_ID_PARAMETER);
		Blog blog = request.getBodyAs(Blog.class, "Blog details not provided");
		
		if (!id.equals(blog.getId()))
		{
			throw new BadRequestException("ID in URL and ID in Blog must match");
		}
		
		ValidationEngine.validateAndThrow(blog);
		blogs.update(blog);
		response.setResponseNoContent();
	}

	public void delete(Request request, Response response)
	{
		String id = request.getUrlDecodedHeader(Constants.BLOG_ID_PARAMETER, "No Blog ID supplied");
		blogs.delete(id);
		response.setResponseNoContent();
	}
}
