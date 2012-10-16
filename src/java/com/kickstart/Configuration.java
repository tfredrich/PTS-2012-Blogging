package com.kickstart;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.kickstart.controller.BlogController;
import com.kickstart.controller.BlogEntryController;
import com.kickstart.controller.CommentController;
import com.kickstart.persistence.BlogEntryRepository;
import com.kickstart.persistence.BlogRepository;
import com.kickstart.persistence.CommentRepository;
import com.kickstart.persistence.MongodbBlogEntryRepository;
import com.kickstart.persistence.MongodbBlogRepository;
import com.kickstart.persistence.MongodbCommentRepository;
import com.kickstart.service.BlogEntryService;
import com.kickstart.service.BlogService;
import com.kickstart.service.CommentService;
import com.mongodb.Mongo;
import com.mongodb.ServerAddress;
import com.strategicgains.restexpress.Format;
import com.strategicgains.restexpress.RestExpress;
import com.strategicgains.restexpress.exception.ConfigurationException;
import com.strategicgains.restexpress.util.Environment;

public class Configuration
extends Environment
{
	private static final String NAME_PROPERTY = "name";
	private static final String PORT_PROPERTY = "port";
	private static final String DEFAULT_FORMAT_PROPERTY = "default.Format";
	private static final String MONGODB_BOOTSTRAPS_PROPERTY = "mongodb.bootstraps";
	private static final String MONGODB_DATABASE_PROPERTY = "mongodb.database";
	private static final String MONGODB_USERNAME_PROPERTY = "mongodb.user";
	private static final String MONGODB_PASSWORD_PROPERTY = "mongodb.password";

	private int port;
	private String name;
	private String defaultFormat;

	private BlogController blogController;
	private BlogEntryController blogEntryController;
	private CommentController commentController;

	@Override
	protected void fillValues(Properties p)
	{
		this.name = p.getProperty(NAME_PROPERTY, RestExpress.DEFAULT_NAME);
		this.port = Integer.parseInt(p.getProperty(PORT_PROPERTY, String.valueOf(RestExpress.DEFAULT_PORT)));
		this.defaultFormat = p.getProperty(DEFAULT_FORMAT_PROPERTY, Format.JSON);
		String dbName = p.getProperty(MONGODB_DATABASE_PROPERTY);

		if (dbName == null)
		{
			throw new ConfigurationException("Please define a database name for property: " + MONGODB_DATABASE_PROPERTY);
		}

		String dbUser = p.getProperty(MONGODB_USERNAME_PROPERTY);
		String dbPassword = p.getProperty(MONGODB_PASSWORD_PROPERTY);
		List<ServerAddress> bootstraps = null;

		try
		{
			String bootstrapString = p.getProperty(MONGODB_BOOTSTRAPS_PROPERTY);
			bootstraps = parseBootstrapString(bootstrapString);
		}
		catch (Exception e)
		{
			throw new ConfigurationException(e);
		}

		Mongo mongo = new Mongo(bootstraps);
		initialize(mongo, dbName, dbUser, dbPassword);
	}

	private void initialize(Mongo mongo, String dbName, String dbUser, String dbPassword)
	{
		if (dbUser != null && dbPassword != null && dbPassword.length() > 0)
		{
			if (!mongo.getDB(dbName).authenticate(dbUser, dbPassword.toCharArray()))
			{
				throw new ConfigurationException("User not authenticated against database: " + dbName);
			}
		}

		BlogRepository blogRepository = new MongodbBlogRepository(mongo, dbName);
		BlogEntryRepository blogEntryRepository = new MongodbBlogEntryRepository(mongo, dbName);
		CommentRepository commentRepository = new MongodbCommentRepository(mongo, dbName);
		
		BlogService blogService = new BlogService(blogRepository);
		BlogEntryService blogEntryService = new BlogEntryService(blogEntryRepository);
		CommentService commentService = new CommentService(commentRepository);
		
		blogController = new BlogController(blogService);
		blogEntryController = new BlogEntryController(blogEntryService);
		commentController = new CommentController(commentService);
	}

	/**
	 * @param bootstrapString
	 * @return
	 * @throws UnknownHostException
	 * @throws NumberFormatException
	 */
	private List<ServerAddress> parseBootstrapString(String bootstrapString)
	throws NumberFormatException,
	    UnknownHostException
	{
		if (bootstrapString == null || bootstrapString.trim().isEmpty())
		{
			throw new ConfigurationException("Please set MongoDB bootstrap servers in property: " + MONGODB_BOOTSTRAPS_PROPERTY);
		}

		String[] serverAndPorts = bootstrapString.split(",");
		List<ServerAddress> results = new ArrayList<ServerAddress>(serverAndPorts.length);

		for (String sp : serverAndPorts)
		{
			String[] server = sp.split(":");

			if (server.length == 2)
			{
				results.add(new ServerAddress(server[0], Integer.parseInt(server[1])));
			}
			else if (server.length == 1)
			{
				results.add(new ServerAddress(server[0]));
			}
		}

		return results;
	}

	public String getDefaultFormat()
	{
		return defaultFormat;
	}

	public int getPort()
	{
		return port;
	}

	public String getName()
	{
		return name;
	}
	
	public BlogController getBlogController()
	{
		return blogController;
	}
	
	public BlogEntryController getBlogEntryController()
	{
		return blogEntryController;
	}
	
	public CommentController getCommentController()
	{
		return commentController;
	}
}
