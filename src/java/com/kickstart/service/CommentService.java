/*
    Copyright 2012, Strategic Gains, Inc.

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

		http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
 */
package com.kickstart.service;

import java.util.List;

import com.kickstart.domain.Comment;
import com.kickstart.persistence.CommentRepository;
import com.strategicgains.restexpress.query.QueryFilter;
import com.strategicgains.restexpress.query.QueryOrder;
import com.strategicgains.restexpress.query.QueryRange;
import com.strategicgains.syntaxe.ValidationEngine;

/**
 * @author toddf
 * @since Oct 9, 2012
 */
public class CommentService
{
	private CommentRepository comments;

	public CommentService(CommentRepository commentRepository)
	{
		super();
		this.comments = commentRepository;
	}

	public Comment create(Comment comment)
	{
		ValidationEngine.validateAndThrow(comment);
		return comments.create(comment);
	}

	public Comment read(String id)
	{
		return comments.read(id);
	}

	public void update(Comment comment)
	{
		ValidationEngine.validateAndThrow(comment);
		comments.update(comment);
	}

	public void delete(String id)
	{
		comments.delete(id);
	}

	public List<Comment> readAll(QueryFilter filter, QueryRange range, QueryOrder order)
	{
		return comments.readAll(filter, range, order);
	}

	public long count(QueryFilter filter)
	{
		return comments.count(filter);

	}
}
