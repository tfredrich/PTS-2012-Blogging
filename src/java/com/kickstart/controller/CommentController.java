package com.kickstart.controller;

import java.util.List;

import org.jboss.netty.handler.codec.http.HttpMethod;

import com.kickstart.Constants;
import com.kickstart.domain.Comment;
import com.kickstart.service.CommentService;
import com.strategicgains.restexpress.Request;
import com.strategicgains.restexpress.Response;
import com.strategicgains.restexpress.exception.BadRequestException;
import com.strategicgains.restexpress.query.QueryFilter;
import com.strategicgains.restexpress.query.QueryOrder;
import com.strategicgains.restexpress.query.QueryRange;
import com.strategicgains.restexpress.util.XLinkUtils;

public class CommentController
{
	private CommentService commentService;
	
	public CommentController(CommentService commentService)
	{
		super();
		this.commentService = commentService;
	}

	public String create(Request request, Response response)
	{
		Comment comment = request.getBodyAs(Comment.class, "Comment details not provided");
		String blogId = request.getUrlDecodedHeader(Constants.BLOG_ID_PARAMETER, "Blog ID not provided");
		String blogEntryId = request.getUrlDecodedHeader(Constants.BLOG_ENTRY_ID_PARAMETER, "Blog Entry ID not provided");
		comment.setBlogEntryId(blogEntryId);
		Comment saved = commentService.create(comment);

		// Construct the response for create...
		response.setResponseCreated();

		// Include the Location header...
		String locationUrl = request.getNamedUrl(HttpMethod.GET, Constants.COMMENT_READ_ROUTE);
		response.addLocationHeader(XLinkUtils.asLocationUrl(saved.getId(), Constants.COMMENT_ID_PARAMETER, locationUrl,
				Constants.BLOG_ID_PARAMETER, blogId,
				Constants.BLOG_ENTRY_ID_PARAMETER, blogEntryId));

		// Return the newly-created ID...
		return saved.getId();
	}

	public Comment read(Request request, Response response)
	{
		String id = request.getUrlDecodedHeader(Constants.COMMENT_ID_PARAMETER, "No Comment ID supplied");
		return commentService.read(id);
	}

	public List<Comment> readAll(Request request, Response response)
	{
		QueryFilter filter = QueryFilter.parseFrom(request);
		QueryOrder order = QueryOrder.parseFrom(request);
		QueryRange range = QueryRange.parseFrom(request, 20);
		List<Comment> results = commentService.readAll(filter, range, order);
		response.addRangeHeader(range, commentService.count(filter));
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
		
		commentService.update(comment);
		response.setResponseNoContent();
	}

	public void delete(Request request, Response response)
	{
		String id = request.getUrlDecodedHeader(Constants.COMMENT_ID_PARAMETER, "No Comment ID supplied");
		commentService.delete(id);
		response.setResponseNoContent();
	}
}
