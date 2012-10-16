package com.kickstart.controller;

import java.util.List;

import org.jboss.netty.handler.codec.http.HttpMethod;

import com.kickstart.Constants;
import com.kickstart.domain.Blog;
import com.kickstart.service.BlogService;
import com.strategicgains.restexpress.Request;
import com.strategicgains.restexpress.Response;
import com.strategicgains.restexpress.exception.BadRequestException;
import com.strategicgains.restexpress.query.QueryFilter;
import com.strategicgains.restexpress.query.QueryOrder;
import com.strategicgains.restexpress.query.QueryRange;
import com.strategicgains.restexpress.util.XLinkUtils;

public class BlogController
{
	private BlogService blogService;
	
	public BlogController(BlogService blogService)
	{
		super();
		this.blogService = blogService;
	}

	public String create(Request request, Response response)
	{
		Blog blog = request.getBodyAs(Blog.class, "Blog details not provided");
		Blog saved = blogService.create(blog);

		// Construct the response for create...
		response.setResponseCreated();

		// Include the Location header...
		String locationUrl = request.getNamedUrl(HttpMethod.GET, Constants.BLOG_READ_ROUTE);
		response.addLocationHeader(XLinkUtils.asLocationUrl(saved.getId(), Constants.BLOG_ID_PARAMETER, locationUrl));

		// Return the newly-created ID...
		return saved.getId();
	}

	public Blog read(Request request, Response response)
	{
		String id = request.getUrlDecodedHeader(Constants.BLOG_ID_PARAMETER, "No Blog ID supplied");
		return blogService.read(id);
	}

	public List<Blog> readAll(Request request, Response response)
	{
		QueryFilter filter = QueryFilter.parseFrom(request);
		QueryOrder order = QueryOrder.parseFrom(request);
		QueryRange range = QueryRange.parseFrom(request, 20);
		List<Blog> results = blogService.readAll(filter, range, order);
		response.addRangeHeader(range, blogService.count(filter));
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
		
		blogService.update(blog);
		response.setResponseNoContent();
	}

	public void delete(Request request, Response response)
	{
		String id = request.getUrlDecodedHeader(Constants.BLOG_ID_PARAMETER, "No Blog ID supplied");
		blogService.delete(id);
		response.setResponseNoContent();
	}
}
