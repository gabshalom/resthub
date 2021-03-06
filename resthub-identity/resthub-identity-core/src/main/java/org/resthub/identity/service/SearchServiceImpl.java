package org.resthub.identity.service;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.identity.dao.SearchDao;
import org.resthub.identity.model.AbstractPermissionsOwner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Search service implementation.
 */
@Named("searchService")
public class SearchServiceImpl implements SearchService {

	/**
	 * Classe's logger
	 */
	protected final static Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);

	/**
	 * DAO used to performs searches.
	 */
	@Inject
	protected SearchDao searchDao;
	
	/**
	 * Initialization method : resetIndexes.
	 */
	@PostConstruct
	public void init() {
		logger.info("[init] Refresh search indexes");
		resetIndexes();
	} // init().
	
	/**
	 * {@inheritDoc}
	 */
	public List<AbstractPermissionsOwner> search(String query, boolean withUsers, boolean withGroups) {
		if (logger.isDebugEnabled()) {
			logger.debug("[search] Search with query '"+query+"'"+(withUsers ? " on users" : "")+
					(withGroups ? " on groups" : ""));
		}
		// Argument validation.
		if (query == null) {
			throw new IllegalArgumentException("query must not be null");
		}
		// Adds * after each terms if terms are simple (no keywords, no quotes)
		if (query.trim().length() > 0 && !query.matches("\\+|\"|-|~|\\?|\\*|\\[|\\(") && !query.contains("NOT") && 
				!query.contains("AND") && !query.contains("OR")) {
			String[] terms = query.split(" ");
			query = "";
			for (String term : terms) {
				query += term + "* ";
			}
			logger.debug("[search] Query adapted to '"+query+"'");
		}
		return searchDao.search(query, withUsers, withGroups);
	} // search().
	
	/**
	 * {@inheritDoc}
	 */
	public void resetIndexes() {
		logger.info("[init] Users and groups re-indexation in progress...");
		searchDao.resetIndexes();
		logger.info("[init] Users and groups re-indexation finished");
	} // resetIndexes

} // class SearchService
