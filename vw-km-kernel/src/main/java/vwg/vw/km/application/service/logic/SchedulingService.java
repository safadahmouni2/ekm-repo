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
package vwg.vw.km.application.service.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;

import vwg.vw.km.application.implementation.ComponentElementManager;
import vwg.vw.km.application.implementation.ComponentVersionManager;
import vwg.vw.km.application.implementation.ComponentVersionStandManager;
import vwg.vw.km.application.implementation.ComponentVersionUsersManager;
import vwg.vw.km.application.implementation.ElementVersionManager;
import vwg.vw.km.application.implementation.ElementVersionUsersManager;
import vwg.vw.km.application.implementation.EnumStatusManager;
import vwg.vw.km.application.implementation.UserManager;
import vwg.vw.km.application.implementation.impl.ComponentStandChangeManagerImpl;
import vwg.vw.km.application.implementation.impl.ElementVersionChangeManagerImpl;
import vwg.vw.km.application.service.base.BaseService;
import vwg.vw.km.application.service.dto.CostElementDTO;
import vwg.vw.km.common.manager.Log;
import vwg.vw.km.common.manager.LogManager;
import vwg.vw.km.common.type.BaseDateTime;
import vwg.vw.km.common.type.ChangeTypeValue;
import vwg.vw.km.common.type.RandomString;
import vwg.vw.km.integration.persistence.model.BrandModel;
import vwg.vw.km.integration.persistence.model.ComponentElementModel;
import vwg.vw.km.integration.persistence.model.ComponentStandModel;
import vwg.vw.km.integration.persistence.model.ComponentStandUsersModel;
import vwg.vw.km.integration.persistence.model.ComponentVersionModel;
import vwg.vw.km.integration.persistence.model.ElementModel;
import vwg.vw.km.integration.persistence.model.ElementVersionModel;
import vwg.vw.km.integration.persistence.model.ElementVersionUsersModel;
import vwg.vw.km.integration.persistence.model.EnumStatusModel;
import vwg.vw.km.integration.persistence.model.UserModel;

/**
 * <p>
 * Title: EKM
 * <p>
 * Description : Class description goes here
 * </p>
 * <p>
 * Copyright: VW (c) 2011
 * </p>
 * .
 * 
 * @author Sebri Zouhaier changed by $Author: saidi $
 * @version $Revision: 1.26 $ $Date: 2022/01/19 09:37:04 $
 */
public class SchedulingService extends BaseService<CostElementDTO> {
	private final Log log = LogManager.get().getLog(SchedulingService.class);

	private ElementVersionManager elementVersionManager;

	private EnumStatusManager enumStatusManager;

	private ComponentElementManager componentElementManager;

	private ComponentVersionManager componentVersionManager;

	private ComponentVersionStandManager componentVersionStandManager;

	private ElementVersionUsersManager elementVersionUsersManager;

	private ComponentVersionUsersManager componentVersionUsersManager;

	private UserManager userManager;

	private MailService mailService;

	private final static int CHECKSUM_LENGTH = 14;

	// start transactional methods

	public void saveInUseStatus() {
		List<Long> createdStandIds = new ArrayList<Long>();
		saveInUseStatusForElements(createdStandIds);
		saveActivationForElementVersionsUsers(createdStandIds);
		saveActivationComponentStandUsers();
		saveInUseStatusForComponents();
	}

	/**
	 * method called by Spring cron job to release (set inuse) all approved element versions
	 */
	private void saveInUseStatusForElements(List<Long> createdStandIds) {
		BaseDateTime validFrom = new BaseDateTime(BaseDateTime.getCurrentDateTime().getDateSeparatedPt());
		log.info("START SET IN USE ELEMENT VERSION JOB : " + BaseDateTime.getCurrentDateTime());
		log.info("SET IN USE STATUS IS RUNNING FOR DAY : " + validFrom);
		List<ElementVersionModel> elementVersions = elementVersionManager.getElementVersionToBeTraitedByBatch();
		if (elementVersions != null && !elementVersions.isEmpty()) {
			EnumStatusModel enumStatus = enumStatusManager.getObject(EnumStatusManager.Status.IN_USE.value());
			Map<BrandModel, List<ElementVersionModel>> oldElementVersionInUseByBrand = new HashMap<BrandModel, List<ElementVersionModel>>();
			Map<BrandModel, List<ElementVersionModel>> newElementVersionInUseByBrand = new HashMap<BrandModel, List<ElementVersionModel>>();
			for (ElementVersionModel elementVersion : elementVersions) {
				// 1- stop the last version in use
				ElementVersionModel lastElementVersionInUse = elementVersionManager.getLastElementVersionByStatus(
						elementVersion.getElement().getElementId(), EnumStatusManager.Status.IN_USE.value());
				Long versionNumber = 1L;
				if (lastElementVersionInUse != null) {
					log.info("Stop the last version in use :: " + lastElementVersionInUse);
					lastElementVersionInUse.setValidTo(validFrom.addSeconds(-1));
					elementVersionManager.saveObject(lastElementVersionInUse);
					versionNumber = lastElementVersionInUse.getNumber() + 1;
					if (oldElementVersionInUseByBrand.containsKey(lastElementVersionInUse.getElement().getOwner())) {
						oldElementVersionInUseByBrand.get(lastElementVersionInUse.getElement().getOwner())
								.add(lastElementVersionInUse);
					} else {
						List<ElementVersionModel> elemetVersions = new ArrayList<ElementVersionModel>();
						elemetVersions.add(lastElementVersionInUse);
						oldElementVersionInUseByBrand.put(lastElementVersionInUse.getElement().getOwner(),
								elemetVersions);
					}
				}
				// 2-change elementVersion from 3 to 5 status (APPROVED to IN USE)
				log.info("SET ELEMENT VERSION " + elementVersion + " FROM APPROVED TO IN USE");
				elementVersion.setNumber(versionNumber);
				elementVersion.setEnumStatus(enumStatus);
				elementVersion.setValidFrom(validFrom);
				elementVersion.setValidTo(validFrom.addSeconds(59 * 60 + 23 * 60 * 60 + 59).addYears(200));
				elementVersionManager.saveObject(elementVersion);
				if (newElementVersionInUseByBrand.containsKey(elementVersion.getElement().getOwner())) {
					newElementVersionInUseByBrand.get(elementVersion.getElement().getOwner()).add(elementVersion);
				} else {
					List<ElementVersionModel> elemetVersions = new ArrayList<ElementVersionModel>();
					elemetVersions.add(elementVersion);
					newElementVersionInUseByBrand.put(elementVersion.getElement().getOwner(), elemetVersions);
				}
				// add change entry
				if (elementVersion.getNumber() > 1) {
					ElementVersionChangeManagerImpl.get().updateChangeTypeForVersion(
							elementVersion.getElementVersionId(), ChangeTypeValue.IN_PROCESS,
							ChangeTypeValue.NEW_STAND);
				}
			}
			// create new stand for component when element change status to in use for same owner
			createNewStandAfterElementVersionChange(newElementVersionInUseByBrand, oldElementVersionInUseByBrand,
					createdStandIds);

		}
		log.info("END SET IN USE ELEMENT VERSION JOB : " + BaseDateTime.getCurrentDateTime());
	}

	private void createNewStandAfterElementVersionChange(
			Map<BrandModel, List<ElementVersionModel>> newElementVersionInUseByBrand,
			Map<BrandModel, List<ElementVersionModel>> oldElementVersionInUseByBrand, List<Long> createdStandIds) {
		// create new stand for linked element version
		if (!oldElementVersionInUseByBrand.isEmpty()) {
			log.info("START CREATE STAND AFTER CHANGE ELEMNT VERSION: " + BaseDateTime.getCurrentDateTime());
			List<ComponentStandModel> componentStandsLinked = componentVersionStandManager
					.getComponentVersionStandByLinkedElementVersion(oldElementVersionInUseByBrand);
			if (componentStandsLinked != null && !componentStandsLinked.isEmpty()) {
				for (ComponentStandModel oldStand : componentStandsLinked) {
					// new stand can be created only in last version in use
					ComponentVersionModel lastComponentVersion = componentVersionManager
							.getLastComponentVersionByComponentId(
									oldStand.getComponentVersion().getComponent().getComponentId());

					log.info("Last Component version to be treated: " + lastComponentVersion);
					log.info("Component version of old Stand: " + oldStand.getComponentVersion());
					// get the stand to be updated with the new element version from the component working version
					ComponentStandModel updatedStand = oldStand;
					if (!oldStand.getComponentVersion().equals(lastComponentVersion)) {
						lastComponentVersion.getComponentVersionStands().size();
						updatedStand = new ArrayList<ComponentStandModel>(
								lastComponentVersion.getComponentVersionStands()).get(0);
					}
					log.info("Stand to be updated: " + updatedStand);

					// if last component version is approved : controller will create a normal stand
					if (EnumStatusManager.Status.APPROVED.value()
							.equals(lastComponentVersion.getEnumStatus().getEnumStatusId())) {
						updateStandRelation(updatedStand, newElementVersionInUseByBrand, Boolean.TRUE);
					}
					// update the stand the working version and create a new stand in the last component version in use
					else {
						if (!EnumStatusManager.Status.IN_USE.value()
								.equals(lastComponentVersion.getEnumStatus().getEnumStatusId())) {
							updateStandRelation(updatedStand, newElementVersionInUseByBrand, Boolean.TRUE);
						}
						// check if it is last component version in use
						if (componentVersionManager.isTheLastComponentVersion(
								oldStand.getComponentVersion().getComponentVersionId(),
								oldStand.getComponentVersion().getComponent().getComponentId(),
								EnumStatusManager.Status.IN_USE.value())
								&& (oldStand.getComponentVersion().getEnumStatus().getEnumStatusId()
										.equals(EnumStatusManager.Status.IN_USE.value()))) {
							// save new a stand for the version
							Long newStandId = saveNewStandFromOldOne(oldStand, newElementVersionInUseByBrand,
									Boolean.TRUE);
							createdStandIds.add(newStandId);
						}
					}
				}
			} else {
				log.info("ELEMENTS NOT USED ...");
			}
			log.info("END CREATE STAND AFTER CHANGE ELEMNT VERSION: " + BaseDateTime.getCurrentDateTime());
		}
	}

	/**
	 * Activation of Element Versions Users
	 */
	private void saveActivationForElementVersionsUsers(List<Long> createdStandIds) {
		BaseDateTime startFrom = new BaseDateTime(BaseDateTime.getCurrentDateTime().getDateSeparatedPt());
		log.info("START ACTIVATION ELEMENT VERSION USERS JOB : " + BaseDateTime.getCurrentDateTime());
		log.info("ACTIVATION ELEMENT VERSION USERS IS RUNNING FOR DAY : " + startFrom);

		List<ElementVersionUsersModel> inactivatedElementVersionUsers = elementVersionUsersManager
				.getInactivatedElementVersionUsers();
		Map<BrandModel, List<ElementVersionModel>> elementVersionByBrand = new HashMap<BrandModel, List<ElementVersionModel>>();
		for (ElementVersionUsersModel elementVersionUser : inactivatedElementVersionUsers) {
			elementVersionUser.setActive(true);
			log.info("ELEMENT VERSION USERS ACTIVATED : " + elementVersionUser.getId());
			elementVersionUsersManager.saveObject(elementVersionUser);
			if (elementVersionByBrand.containsKey(elementVersionUser.getBrand())) {
				elementVersionByBrand.get(elementVersionUser.getBrand()).add(elementVersionUser.getElementVersion());
			} else {
				List<ElementVersionModel> elemetVersions = new ArrayList<ElementVersionModel>();
				elemetVersions.add(elementVersionUser.getElementVersion());
				elementVersionByBrand.put(elementVersionUser.getBrand(), elemetVersions);
			}
		}
		createComponentStandAfterUseAnElementVersion(elementVersionByBrand, createdStandIds);
	}

	private void createComponentStandAfterUseAnElementVersion(
			Map<BrandModel, List<ElementVersionModel>> elementVersionByBrand, List<Long> createdStandIds) {
		log.info("END ACTIVATION ELEMENT VERSION USES JOB : " + BaseDateTime.getCurrentDateTime());
		// create new stand for linked element version
		if (!elementVersionByBrand.isEmpty()) {
			Map<BrandModel, List<ElementModel>> elementByBrand = new HashMap<BrandModel, List<ElementModel>>();
			for (Map.Entry<BrandModel, List<ElementVersionModel>> entry : elementVersionByBrand.entrySet()) {
				List<ElementModel> elemets = new ArrayList<ElementModel>();
				for (ElementVersionModel elementVersion : entry.getValue()) {
					elemets.add(elementVersion.getElement());
				}
				elementByBrand.put(entry.getKey(), elemets);
			}

			log.info("START CREATE STAND AFTER USE ELEMNT VERSION: " + BaseDateTime.getCurrentDateTime());
			List<ComponentStandModel> componentStandsLinked = componentVersionStandManager
					.getComponentVersionStandByNotOwnerLinkedElement(elementByBrand);
			if (componentStandsLinked != null && !componentStandsLinked.isEmpty()) {
				for (ComponentStandModel oldStand : componentStandsLinked) {
					// new stand can be created only in last version in use
					ComponentVersionModel lastComponentVersion = componentVersionManager
							.getLastComponentVersionByComponentId(
									oldStand.getComponentVersion().getComponent().getComponentId());
					// if last component version is approved : controller will create a normal stand
					if (EnumStatusManager.Status.APPROVED.value()
							.equals(lastComponentVersion.getEnumStatus().getEnumStatusId())) {
						// updateStandRelation(oldStand, elementVersionByBrand, Boolean.FALSE);
					} else {
						if ((!EnumStatusManager.Status.IN_USE.value()
								.equals(lastComponentVersion.getEnumStatus().getEnumStatusId()))
								&& oldStand.getComponentVersion().equals(lastComponentVersion)) {
							// updateStandRelation(oldStand, elementVersionByBrand, Boolean.FALSE);
						}
						if (componentVersionManager.isTheLastComponentVersion(
								oldStand.getComponentVersion().getComponentVersionId(),
								oldStand.getComponentVersion().getComponent().getComponentId(),
								EnumStatusManager.Status.IN_USE.value())) {
							// save new a stand for the version
							if (!createdStandIds.contains(oldStand.getId())) {
								Long newStandId = saveNewStandFromOldOne(oldStand, elementVersionByBrand,
										Boolean.FALSE);
								createdStandIds.add(newStandId);
							} else {
								// updateStandRelation(oldStand, elementVersionByBrand, Boolean.FALSE);
							}
						}
					}
				}
			} else {
				log.info("ELEMENTS NOT USED ...");
			}
			log.info("END CREATE STAND AFTER USE ELEMNT VERSION: " + BaseDateTime.getCurrentDateTime());
		}
	}

	private Long saveNewStandFromOldOne(ComponentStandModel oldStand,
			Map<BrandModel, List<ElementVersionModel>> elementVersionByBrand, boolean owner) {
		ComponentStandModel newStand = new ComponentStandModel();
		newStand.setStandDate(BaseDateTime.getCurrentDateTime());
		Long maxNumber = componentVersionStandManager
				.getMaxNumberByComponentId(oldStand.getComponentVersion().getComponent().getComponentId());
		newStand.setNumber(maxNumber + 1);
		newStand.setComponentVersion(oldStand.getComponentVersion());
		Long standId = (Long) componentVersionStandManager.saveObject(newStand);
		// if (newStand.getNumber() > 1) {
		// ComponentStandChangeManagerImpl.get().updateChangeTypeForVersion(standId,
		// ChangeTypeValue.IN_PROCESS, ChangeTypeValue.NEW_STAND);
		// }
		log.info("CREATE NEW BAUSTEIN STAND  : " + newStand.getNumber() + " FOR [COMPONENT VERSION="
				+ oldStand.getComponentVersion().getNumber() + ",COMPONENT="
				+ oldStand.getComponentVersion().getComponent().getNumberAndDesignation() + "]");
		// TO-DO copy component elements
		List<ComponentElementModel> setComponentElementModel = componentElementManager
				.getComponentElementListByComponentStandId(oldStand.getId());
		if (setComponentElementModel != null) {
			for (ComponentElementModel gs : setComponentElementModel) {
				ComponentElementModel cloned = new ComponentElementModel();
				BeanUtils.copyProperties(gs, cloned);
				cloned.setLoaded(false);
				cloned.setComponentStand(newStand);

				boolean replaceByNewElement = Boolean.FALSE;
				ElementVersionModel elementVersionModel = null;

				if (owner) {
					if (oldStand.getComponentVersion().getComponent().getOwner()
							.equals(gs.getElementVersion().getElement().getOwner())) {
						replaceByNewElement = Boolean.TRUE;
					}
				} else {
					List<BrandModel> brands = elementVersionUsersManager
							.getBrandsByElement(gs.getElementVersion().getElement().getElementId());
					if (brands.contains(oldStand.getComponentVersion().getComponent().getOwner())) {
						replaceByNewElement = Boolean.TRUE;
					}
				}
				if (replaceByNewElement) {
					elementVersionModel = getNewInUseElementVersion(gs.getElementVersion().getElement().getElementId(),
							elementVersionByBrand);
				}
				if (elementVersionModel != null) {
					cloned.setElementVersion(elementVersionModel);
				} else {
					cloned.setElementVersion(gs.getElementVersion());
				}
				componentElementManager.saveObject(cloned);
			}
		}
		return standId;
	}

	private void updateStandRelation(ComponentStandModel stand,
			Map<BrandModel, List<ElementVersionModel>> elementVersionByBrand, boolean owner) {
		// TO-DO copy component elements

		List<ComponentElementModel> setComponentElementModel = componentElementManager
				.getComponentElementListByComponentStandId(stand.getId());
		if (setComponentElementModel != null) {
			for (ComponentElementModel gs : setComponentElementModel) {
				boolean replaceByNewElement = Boolean.FALSE;
				ElementVersionModel elementVersionModel = null;

				if (owner) {
					if (stand.getComponentVersion().getComponent().getOwner()
							.equals(gs.getElementVersion().getElement().getOwner())) {
						replaceByNewElement = Boolean.TRUE;
					}
				} else {
					List<BrandModel> brands = elementVersionUsersManager
							.getBrandsByElement(gs.getElementVersion().getElementVersionId());
					if (brands.contains(stand.getComponentVersion().getComponent().getOwner())) {
						replaceByNewElement = Boolean.TRUE;
					}
				}
				if (replaceByNewElement) {
					elementVersionModel = getNewInUseElementVersion(gs.getElementVersion().getElement().getElementId(),
							elementVersionByBrand);
					if (elementVersionModel != null) {
						gs.setElementVersion(elementVersionModel);
						componentElementManager.saveObject(gs);
					}
				}

			}
		}
	}

	/**
	 * Activation of Element Versions Users
	 */
	private void saveActivationComponentStandUsers() {
		BaseDateTime startFrom = new BaseDateTime(BaseDateTime.getCurrentDateTime().getDateSeparatedPt());
		log.info("START ACTIVATION COMPONENT STAND USERS JOB : " + BaseDateTime.getCurrentDateTime());
		log.info("ACTIVATION COMPONENT STAND USERS IS RUNNING FOR DAY : " + startFrom);
		List<ComponentStandUsersModel> inactivatedComponentStandUsers = componentVersionUsersManager
				.getInactivatedComponentStandUsers();
		for (ComponentStandUsersModel standUser : inactivatedComponentStandUsers) {
			standUser.setActive(true);
			log.info("COMPONENT STAND USERS ACTIVATED : " + standUser.getId());
			componentVersionUsersManager.saveObject(standUser);
		}
		log.info("END ACTIVATION COMPONENT STAND USERS JOB : " + BaseDateTime.getCurrentDateTime());
	}

	private ElementVersionModel getNewInUseElementVersion(Long elementId,
			Map<BrandModel, List<ElementVersionModel>> elementVersionByBrand) {
		for (Map.Entry<BrandModel, List<ElementVersionModel>> entry : elementVersionByBrand.entrySet()) {
			for (ElementVersionModel elementVersionModel : entry.getValue()) {
				if (elementId.equals(elementVersionModel.getElement().getElementId())) {
					return elementVersionModel;
				}
			}
		}
		return null;
	}

	/**
	 * method called by Spring cron job to release (set inuse) all approved comonents versions
	 */
	private void saveInUseStatusForComponents() {
		BaseDateTime validFrom = new BaseDateTime(BaseDateTime.getCurrentDateTime().getDateSeparatedPt());
		log.info("START SET IN USE COMPONENT VERSION JOB : " + BaseDateTime.getCurrentDateTime());
		log.info("SET IN USE STATUS IS RUNNING FOR DAY : " + validFrom);
		List<ComponentVersionModel> componentVersions = componentVersionManager.getComponentVersionToBeTraitedByBatch();
		if (componentVersions != null && !componentVersions.isEmpty()) {
			EnumStatusModel enumStatus = enumStatusManager.getObject(EnumStatusManager.Status.IN_USE.value());
			for (ComponentVersionModel componentVersion : componentVersions) {
				// 1- stop the last version in use
				ComponentVersionModel lastComponentVersionInUse = componentVersionManager
						.getLastComponentVersionInUse(componentVersion.getComponent().getComponentId());
				Long versionNumber = 1L;
				if (lastComponentVersionInUse != null) {
					log.info("Stop the last version in use :: " + lastComponentVersionInUse);
					lastComponentVersionInUse.setValidTo(validFrom.addSeconds(-1));
					versionNumber = lastComponentVersionInUse.getNumber() + 1;
					componentVersionManager.saveObject(lastComponentVersionInUse);
				}
				// 2-change elementVersion from 3 to 5 status (APPROVED to IN USE)
				log.info("SET COMPONENT VERSION " + componentVersion + " FROM APPROVED TO IN USE");
				componentVersion.setNumber(versionNumber);
				componentVersion.setEnumStatus(enumStatus);
				componentVersion.setValidFrom(validFrom);
				componentVersion.setValidTo(validFrom.addSeconds(59 * 60 + 23 * 60 * 60 + 59).addYears(200));
				componentVersionManager.saveObject(componentVersion);
				Long maxNumber = componentVersionStandManager
						.getMaxNumberByComponentId(componentVersion.getComponent().getComponentId());
				componentVersion.getComponentVersionStands().size();
				ComponentStandModel componentStand = new ArrayList<ComponentStandModel>(
						componentVersion.getComponentVersionStands()).get(0);
				componentStand.setNumber(maxNumber + 1);
				componentVersionStandManager.saveObject(componentStand);
				if (componentStand.getNumber() > 1) {
					ComponentStandChangeManagerImpl.get().updateChangeTypeForVersion(componentStand.getId(),
							ChangeTypeValue.IN_PROCESS, ChangeTypeValue.NEW_STAND);
				}
			}
		}
		log.info("END SET IN USE COMPONENT VERSION JOB : " + BaseDateTime.getCurrentDateTime());
	}

	/**
	 * method called by Spring cron job to anonomyze all inactive users with deactivation date older than 6 months
	 */
	public void saveAnonomyzedUsers() {
		log.info("START ANONOMYZE USERS JOB : " + BaseDateTime.getCurrentDateTime());
		List<UserModel> users = userManager.getUsersToBeAnonomyzedByBatch();
		if (users != null && !users.isEmpty()) {
			log.info("Number of Users to be anonomyzed : " + users.size());
			for (UserModel user : users) {
				log.info("Anonomyze User with id : " + user.getUserId());
				user.setGender("Anonymous");
				user.setFirstName("Anonymous");
				user.setLastName("Anonymous");
				user.setkUMSUserId("Anonymous" + user.getUserId());
				user.setEmail("anonymous@anonymous.com");
				user.setTelephone("Anonymous");
				user.setAnonymized(Boolean.TRUE);
				// user.setActivationCode(null);
				userManager.saveObject(user);
				ComponentStandChangeManagerImpl.get().anonomyzeUser(user.getUserId());
				ElementVersionChangeManagerImpl.get().anonomyzeUser(user.getUserId());
			}
		} else {
			log.info("No Users to anonomyze");
		}

		log.info("END ANONOMYZE USERS JOB : " + BaseDateTime.getCurrentDateTime());
	}

	/**
	 * method called by Spring cron job to deactivate all users with last login date older than 6 months
	 */
	public void saveDeactivateUsers() {
		log.info("START DEACTIVATE USERS JOB : " + BaseDateTime.getCurrentDateTime());
		List<UserModel> users = userManager.getUsersToBeDeactivatedByBatch();

		if (CollectionUtils.isNotEmpty(users)) {

			log.info("Number of Users to be deactivated : " + users.size());

			for (UserModel user : users) {

				log.info("Deactivate User with id : " + user.getUserId());
				log.info("LastLogInTime of this user : " + user.getLastLogInTime());

				user.setActive(Boolean.FALSE);
				user.setDeactivationDate(BaseDateTime.getCurrentDateTime());
				RandomString gen = new RandomString(CHECKSUM_LENGTH);
				user.setActivationCode(gen.nextString());
				user.setActivationCodeSentDate(BaseDateTime.getCurrentDateTime());
				userManager.saveObject(user);
				log.info("User " + user.getFirstName() + " has been deactivated at : " + user.getDeactivationDate());
				log.info("Generated code " + gen.nextString());

				// to send mail
				if (user.getEmail() != null) {
					try {
						mailService.sendMail(user);
						log.info("Mail has been sent to " + user.getEmail());
					} catch (Exception e) {
						log.error("No mail has been sent to this user", e);
					}
				} else {
					log.info("Mail could not be sent because Email-Address is null");
				}

			}

		} else {
			log.info("No Users to be deactivated");
		}
		log.info("END DEACTIVATE USERS JOB : " + BaseDateTime.getCurrentDateTime());
	}
	// end transactional methods

	// start manager setter
	public void setElementVersionManager(ElementVersionManager elementVersionManager) {
		this.elementVersionManager = elementVersionManager;
	}

	public void setEnumStatusManager(EnumStatusManager enumStatusManager) {
		this.enumStatusManager = enumStatusManager;
	}

	public void setComponentElementManager(ComponentElementManager componentElementManager) {
		this.componentElementManager = componentElementManager;
	}

	public void setComponentVersionManager(ComponentVersionManager componentVersionManager) {
		this.componentVersionManager = componentVersionManager;
	}

	public void setComponentVersionStandManager(ComponentVersionStandManager componentVersionStandManager) {
		this.componentVersionStandManager = componentVersionStandManager;
	}

	public void setElementVersionUsersManager(ElementVersionUsersManager elementVersionUsersManager) {
		this.elementVersionUsersManager = elementVersionUsersManager;
	}

	public void setComponentVersionUsersManager(ComponentVersionUsersManager componentVersionUsersManager) {
		this.componentVersionUsersManager = componentVersionUsersManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public MailService getMailService() {
		return mailService;
	}

	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}
	// end manager setter

}
