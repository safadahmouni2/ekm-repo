/*
 * Copyright (c) VW All Rights Reserved.
 *
 * This SOURCE CODE FILE, which has been provided by VW Software as part
 * of an VW Software product for use ONLY by licensed users of the product,
 * includes CONFIDENTIAL and PROPRIETARY information of VW Software.
 *
 * USE OF THIS SOFTWARE IS GOVERNED BY THE TERMS AND CONDITIONS
 * OF THE LICENSE STATEMENT AND LIMITED WARRANTY FURNISHED WITH
 * THE PRODUCT.
 *
 * IN PARTICULAR, YOU WILL INDEMNIFY AND HOLD INPRISE, ITS RELATED
 * COMPANIES AND ITS SUPPLIERS, HARMLESS FROM AND AGAINST ANY CLAIMS
 * OR LIABILITIES ARISING OUT OF THE USE, REPRODUCTION, OR DISTRIBUTION
 * OF YOUR PROGRAMS, INCLUDING ANY CLAIMS OR LIABILITIES ARISING OUT OF
 * OR RESULTING FROM THE USE, MODIFICATION, OR DISTRIBUTION OF PROGRAMS
 * OR FILES CREATED FROM, BASED ON, AND/OR DERIVED FROM THIS SOURCE
 * CODE FILE.
 */

package vwg.vw.km.presentation.client.administration.news;

import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ComponentSystemEvent;

import com.sun.faces.util.MessageFactory;

import vwg.vw.km.application.service.dto.NewsDTO;
import vwg.vw.km.application.service.logic.NewsService;
import vwg.vw.km.common.manager.Log;
import vwg.vw.km.common.manager.LogManager;
import vwg.vw.km.integration.persistence.model.NewsModel;
import vwg.vw.km.presentation.client.base.BaseDetailBean;

/**
 * <p>
 * Title: VW_KM
 * <p>
 * Description : User settings bean
 * </p>
 * <p>
 * Copyright: cl (c) 2011
 * </p>
 * 
 * @author hamdiABID
 */
public class NewsDetailBean extends BaseDetailBean<NewsDTO, NewsService> {

	private static final long serialVersionUID = 6562242179223540140L;

	private final Log log = LogManager.get().getLog(NewsDetailBean.class);

	public static final String DATE_FROM_TO_VALIDATION_MESSAGE_ID = "dateFromTo.validation.message";

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.base.BaseDetailBean#specificSave(javax.faces.event.ActionEvent)
	 */
	@Override
	public String specificSave(ActionEvent event) {
		NewsModel news = dto.getNews();
		
		if (news.getTitle() == null || news.getDescription() == null || news.getValidFrom() == null) {
			return getMessage("common.popup.UIInput.REQUIRED");
		}

		log.info("Try to save news " + news);

		service.save(dto);

		return "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.base.BaseDetailBean#getSaveMessage()
	 */
	@Override
	public String getSaveMessage() {
		return getMessage("action.news.save");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.base.BaseDetailBean#getDisplayText()
	 */
	@Override
	public String getDisplayText() {
		String newsLabel = dto.getNews().getTitle();
		return newsLabel;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.base.BaseDetailBean#getNodeId()
	 */
	@Override
	public Long getNodeId() {
		return dto.getNews().getNewsId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.base.BaseBean#specificCancelModificationMessage()
	 */
	@Override
	public String specificCancelModificationMessage() {
		return getMessage("action.news.reload");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.base.BaseBean#specificSearchMessage()
	 */
	@Override
	public String specificSearchMessage() {
		return getMessage("action.news.search");
	}

	/*
	 * 
	 * 
	 */
	public void validateDateComparator(ComponentSystemEvent event) {

		log.info("Try to validate Date Comparator newsDetail beans ");
		FacesContext fc = FacesContext.getCurrentInstance();
		UIComponent components = event.getComponent();

		// get DateFrom
		UIInput uiInputDateFrom = (UIInput) components.findComponent("news_date_from");

		String dateFrom = uiInputDateFrom.getLocalValue() == null ? "" : uiInputDateFrom.getLocalValue().toString();
		log.info("Try to validateDateComparator dateFrom" + dateFrom);

		// get dateTo
		UIInput uiInputDateTo = (UIInput) components.findComponent("news_date_of_expiry");

		String dateTo = uiInputDateTo.getLocalValue() == null ? "" : uiInputDateTo.getLocalValue().toString();

		String dateToId = uiInputDateTo.getClientId();
		log.info("Try to validateDateComparator dateTo " + dateTo);

		if (dateFrom.isEmpty() || dateTo.isEmpty()) {
			return;
		}

		Date dateFromTime = (Date) uiInputDateFrom.getLocalValue();
		Date dateToTime = (Date) uiInputDateTo.getLocalValue();

		if ((dateToTime.compareTo(dateFromTime)) < 0 || (dateToTime.equals(dateFromTime))) {

			FacesMessage message = MessageFactory.getMessage(fc, DATE_FROM_TO_VALIDATION_MESSAGE_ID);
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			fc.addMessage(dateToId, message);
			fc.renderResponse();

		}

		return;
	}
}
