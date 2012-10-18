package com.kickstart.controller;

import java.util.List;

import org.jboss.netty.handler.codec.http.HttpMethod;

import com.kickstart.Constants;
import com.kickstart.domain.BlogEntry;
import com.strategicgains.repoexpress.mongodb.MongodbEntityRepository;
import com.strategicgains.restexpress.Request;
import com.strategicgains.restexpress.Response;
import com.strategicgains.restexpress.exception.BadRequestException;
import com.strategicgains.restexpress.query.QueryFilter;
import com.strategicgains.restexpress.query.QueryOrder;
import com.strategicgains.restexpress.query.QueryRange;
import com.strategicgains.restexpress.util.XLinkUtils;
import com.strategicgains.syntaxe.ValidationEngine;

public class BlogEntryController
{
	private MongodbEntityRepository<BlogEntry> blogEntries;
	
	public BlogEntryController(MongodbEntityRepository<BlogEntry> blogEntryRepository)
	{
		super();
		this.blogEntries = blogEntryRepository;
	}

	public String create(Request request, Response response)
	{
		BlogEntry blogEntry = request.getBodyAs(BlogEntry.class, "BlogEntry details not provided");
		String blogId = request.getUrlDecodedHeader(Constants.BLOG_ID_PARAMETER, "No Blog ID provided");
		blogEntry.setBlogId(blogId);
		ValidationEngine.validateAndThrow(blogEntry);
		BlogEntry saved = blogEntries.create(blogEntry);

		// Construct the response for create...
		response.setResponseCreated();

		// Include the Location header...
		String locationUrl = request.getNamedUrl(HttpMethod.GET, Constants.BLOG_ENTRY_READ_ROUTE);
		response.addLocationHeader(XLinkUtils.asLocationUrl(locationUrl,
				Constants.BLOG_ENTRY_ID_PARAMETER, saved.getId(),
				Constants.BLOG_ID_PARAMETER, blogId));

		// Return the newly-created ID...
		return saved.getId();
	}

	public BlogEntry read(Request request, Response response)
	{
		String id = request.getUrlDecodedHeader(Constants.BLOG_ENTRY_ID_PARAMETER, "No BlogEntry ID supplied");
		return blogEntries.read(id);
	}

	public List<BlogEntry> readAll(Request request, Response response)
	{
		QueryFilter filter = QueryFilter.parseFrom(request);
		QueryOrder order = QueryOrder.parseFrom(request);
		QueryRange range = QueryRange.parseFrom(request, 20);
		List<BlogEntry> results = blogEntries.readAll(filter, range, order);
		response.setCollectionResponse(range, results.size(), blogEntries.count(filter));
		return results;
	}

	public void update(Request request, Response response)
	{
		String id = request.getUrlDecodedHeader(Constants.BLOG_ENTRY_ID_PARAMETER);
		BlogEntry blogEntry = request.getBodyAs(BlogEntry.class, "BlogEntry details not provided");
		
		if (!id.equals(blogEntry.getId()))
		{
			throw new BadRequestException("ID in URL and ID in BlogEntry must match");
		}
		
		ValidationEngine.validateAndThrow(blogEntry);
		blogEntries.update(blogEntry);
		response.setResponseNoContent();
	}

	public void delete(Request request, Response response)
	{
		String id = request.getUrlDecodedHeader(Constants.BLOG_ENTRY_ID_PARAMETER, "No BlogEntry ID supplied");
		blogEntries.delete(id);
		response.setResponseNoContent();
	}
}
