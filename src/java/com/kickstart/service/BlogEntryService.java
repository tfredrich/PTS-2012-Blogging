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

import com.kickstart.domain.BlogEntry;
import com.kickstart.persistence.BlogEntryRepository;
import com.strategicgains.restexpress.query.QueryFilter;
import com.strategicgains.restexpress.query.QueryOrder;
import com.strategicgains.restexpress.query.QueryRange;
import com.strategicgains.syntaxe.ValidationEngine;

/**
 * @author toddf
 * @since Oct 9, 2012
 */
public class BlogEntryService
{
	private BlogEntryRepository blogEntries;

	public BlogEntryService(BlogEntryRepository blogEntryRepository)
	{
		super();
		this.blogEntries = blogEntryRepository;
	}

	public BlogEntry create(BlogEntry blogEntry)
	{
		ValidationEngine.validateAndThrow(blogEntry);
		return blogEntries.create(blogEntry);
	}

	public BlogEntry read(String id)
	{
		return blogEntries.read(id);
	}

	public void update(BlogEntry blogEntry)
	{
		ValidationEngine.validateAndThrow(blogEntry);
		blogEntries.update(blogEntry);
	}

	public void delete(String id)
	{
		blogEntries.delete(id);
	}

	public List<BlogEntry> readAll(QueryFilter filter, QueryRange range, QueryOrder order)
	{
		return blogEntries.readAll(filter, range, order);
	}

	public long count(QueryFilter filter)
	{
		return blogEntries.count(filter);

	}
}
