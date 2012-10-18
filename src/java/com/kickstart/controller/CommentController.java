package com.kickstart.controller;

import java.util.List;

import org.jboss.netty.handler.codec.http.HttpMethod;

import com.kickstart.Constants;
import com.kickstart.domain.Comment;
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

public class CommentController
{
	private MongodbEntityRepository<Comment> comments;
	
	public CommentController(MongodbEntityRepository<Comment> commentRepository)
	{
		super();
		this.comments = commentRepository;
	}

	public String create(Request request, Response response)
	{
		Comment comment = request.getBodyAs(Comment.class, "Comment details not provided");
		String blogId = request.getUrlDecodedHeader(Constants.BLOG_ID_PARAMETER, "Blog ID not provided");
		String blogEntryId = request.getUrlDecodedHeader(Constants.BLOG_ENTRY_ID_PARAMETER, "Blog Entry ID not provided");
		comment.setBlogEntryId(blogEntryId);
		ValidationEngine.validateAndThrow(comment);
		Comment saved = comments.create(comment);

		// Construct the response for create...
		response.setResponseCreated();

		// Include the Location header...
		String locationUrl = request.getNamedUrl(HttpMethod.GET, Constants.COMMENT_READ_ROUTE);
		response.addLocationHeader(XLinkUtils.asLocationUrl(locationUrl,
				Constants.COMMENT_ID_PARAMETER, saved.getId(),
				Constants.BLOG_ID_PARAMETER, blogId,
				Constants.BLOG_ENTRY_ID_PARAMETER, blogEntryId));

		// Return the newly-created ID...
		return saved.getId();
	}

	public Comment read(Request request, Response response)
	{
		String id = request.getUrlDecodedHeader(Constants.COMMENT_ID_PARAMETER, "No Comment ID supplied");
		String blogId = request.getUrlDecodedHeader(Constants.BLOG_ID_PARAMETER, "No Blog ID supplied");
		Comment result = comments.read(id);

		// Add 'self' link
		String selfUrlPattern = request.getNamedUrl(HttpMethod.GET, Constants.COMMENT_READ_ROUTE);
		String selfUrl = XLinkUtils.asLocationUrl(selfUrlPattern,
				Constants.COMMENT_ID_PARAMETER, result.getId(),
				Constants.BLOG_ID_PARAMETER, blogId,
				Constants.BLOG_ENTRY_ID_PARAMETER, result.getBlogEntryId());
		result.addLink(new XLink("self", selfUrl));

		String parentUrlPattern = request.getNamedUrl(HttpMethod.GET, Constants.BLOG_ENTRY_READ_ROUTE);
		String parentUrl = XLinkUtils.asLocationUrl(parentUrlPattern,
				Constants.BLOG_ID_PARAMETER, blogId,
				Constants.BLOG_ENTRY_ID_PARAMETER, result.getBlogEntryId());
		result.addLink(new XLink("parent", parentUrl));

		return result;
	}

	public List<Comment> readAll(Request request, Response response)
	{
		String blogId = request.getUrlDecodedHeader(Constants.BLOG_ID_PARAMETER, "No Blog ID supplied");
		QueryFilter filter = QueryFilter.parseFrom(request);
		QueryOrder order = QueryOrder.parseFrom(request);
		QueryRange range = QueryRange.parseFrom(request, 20);
		List<Comment> results = comments.readAll(filter, range, order);
		response.setCollectionResponse(range, results.size(), comments.count(filter));
		
		// Add 'self' and 'parent' links
		String selfUrlPattern = request.getNamedUrl(HttpMethod.GET, Constants.COMMENT_READ_ROUTE);
		
		for (Comment comment : results)
		{
			String selfUrl = XLinkUtils.asLocationUrl(selfUrlPattern,
				Constants.COMMENT_ID_PARAMETER, comment.getId(),
				Constants.BLOG_ID_PARAMETER, blogId,
				Constants.BLOG_ENTRY_ID_PARAMETER, comment.getBlogEntryId());
			comment.addLink(new XLink("self", selfUrl));
		}

		return results;
	}

	public void update(Request request, Response response)
	{
		String id = request.getUrlDecodedHeader(Constants.COMMENT_ID_PARAMETER);
		Comment comment = request.getBodyAs(Comment.class, "Comment details not provided");
		
		if (!id.equals(comment.getId()))
		{
			throw new BadRequestException("ID in URL and ID in Comment must match");
		}
		
		ValidationEngine.validateAndThrow(comment);
		comments.update(comment);
		response.setResponseNoContent();
	}

	public void delete(Request request, Response response)
	{
		String id = request.getUrlDecodedHeader(Constants.COMMENT_ID_PARAMETER, "No Comment ID supplied");
		comments.delete(id);
		response.setResponseNoContent();
	}
}
