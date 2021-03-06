package org.resthub.web.service.impl;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.core.service.GenericServiceImpl;
import org.resthub.web.dao.WebSampleResourceDao;
import org.resthub.web.model.WebSampleResource;
import org.resthub.web.service.WebSampleResourceService;

@Named("webSampleResourceService")
public class WebSampleResourceServiceImpl extends
		GenericServiceImpl<WebSampleResource, Long, WebSampleResourceDao> implements
		WebSampleResourceService {

	@Inject
	@Named("webSampleResourceDao")
	@Override
	public void setDao(WebSampleResourceDao resourceDao) {
		super.setDao(resourceDao);
	}

}
