package org.intercom.service;

import java.util.List;

import org.intercom.model.ErrorBean;
import org.intercom.model.VerificationBean;
import org.intercom.service.exceptions.BusinessLayerException;

public interface VerificationService {
	public List<ErrorBean> detectErros(VerificationBean verificationBean)
			throws BusinessLayerException;

	public List<ErrorBean> getPrimaryKeyNullErrors(List<ErrorBean> errorList)
			throws BusinessLayerException;

	public List<ErrorBean> getPKRedundancyErrors(List<ErrorBean> errorList)
			throws BusinessLayerException;

	public List<ErrorBean> getTypeMismatchErrors(List<ErrorBean> errorList)
			throws BusinessLayerException;

	public List<ErrorBean> getMaxLenghtErrors(List<ErrorBean> errorList)
			throws BusinessLayerException;

	public List<ErrorBean> getOutofIntervalErrors(List<ErrorBean> errorList)
			throws BusinessLayerException;
}
