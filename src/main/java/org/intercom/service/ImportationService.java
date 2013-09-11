package org.intercom.service;

import org.intercom.model.ImportationBean;
import org.intercom.service.exceptions.BusinessLayerException;

public interface ImportationService {
	public Boolean mappingErrors(ImportationBean importationBean);

	public void importToDatabaseTable(ImportationBean importationBean)
			throws BusinessLayerException;
}
