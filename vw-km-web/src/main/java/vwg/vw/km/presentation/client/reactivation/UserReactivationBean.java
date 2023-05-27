/**
 * 
 */
package vwg.vw.km.presentation.client.reactivation;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

import vwg.vw.km.application.service.dto.UserDTO;
import vwg.vw.km.application.service.logic.UserService;
import vwg.vw.km.common.manager.Log;
import vwg.vw.km.common.manager.LogManager;
import vwg.vw.km.common.type.BaseDateTime;
import vwg.vw.km.integration.persistence.model.UserModel;

/**
 * @author abidh
 * @since 29 jan 2020
 * 
 */
public class UserReactivationBean {

	public static final String REACTIVATION_SUCCESS = "reactivationSuccess";

	public static final String REACTIVATION_FAILURE = "reactivationFailure";

	public static final String REACTIVATION_FAILURE_ANONYMIZED = "reactivationFailureAnonymized";

	private final Log log = LogManager.get().getLog(UserReactivationBean.class);

	private UserService userService = null;

	private String message = null;

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	protected Map<String, ?> getRequestParameterMap() {
		return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
	}

	@PostConstruct
	public void init() {
		if (log.isInfoEnabled()) {
			log.info("***Server Start Time: Reactivation service of user account ");

		}
		setMessage(getDoReactivationAccount());
		log.info("message is " + getMessage());
	}

	/**
	 * reactivation action:check success or failure
	 */
	public String getDoReactivationAccount() {
		if (log.isInfoEnabled()) {
			log.info("Try to reactivate account user...");
		}
		String activationCode_ = (String) getRequestParameterMap().get("activationCode");

		log.info("the activationCode is ... " + activationCode_);

		if (activationCode_ != null) {

			UserModel user = userService.getUserByActivationCode(activationCode_);

			if (user != null) {
				log.info("the USER is ... " + user.getUserId());

				// check if account is not Anonymous
				if (Boolean.FALSE.equals(user.getAnonymized())) {

					// validation of account reactivation
					log.info("START Reactivation user Account");
					user.setActive(Boolean.TRUE);
					user.setLastLogInTime(BaseDateTime.getCurrentDateTime());
					user.setDeactivationDate(null);
					user.setActivationCode(null);
					user.setActivationCodeSentDate(null);

					UserDTO dto = new UserDTO();
					dto.setUser(user);
					userService.save(dto);
					log.info("END Reactivation user Account : success");
					return REACTIVATION_SUCCESS;

				} else {
					log.info("END Reactivation user Account : Your account no longer exists.");
					return REACTIVATION_FAILURE_ANONYMIZED;
				}

			} else {
				// the activation code not exist : user account could be activated
				// or Wrong code
				if (log.isInfoEnabled()) {
					log.info("END Reactivation user Account : failure");
				}

				return REACTIVATION_FAILURE;
			}
		} else {
			// the activation Code is null
			return REACTIVATION_FAILURE;
		}

	}
}
