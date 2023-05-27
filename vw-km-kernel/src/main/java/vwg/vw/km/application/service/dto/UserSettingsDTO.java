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
package vwg.vw.km.application.service.dto;

import java.util.List;

import vwg.vw.km.application.service.dto.base.BaseDTO;
import vwg.vw.km.common.type.BaseDateTime;
import vwg.vw.km.integration.persistence.model.BrandModel;
import vwg.vw.km.integration.persistence.model.HourlyRateCatalogModel;

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
 * @version $Revision: 1.13 $ $Date: 2015/12/11 08:00:44 $
 */
public class UserSettingsDTO extends BaseDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6341594789644325652L;

	private List<HourlyRateCatalogModel> hourlyRateCatalogs;

	private List<BrandModel> brands;

	private List<BrandModel> allBrands;

	private Long defaultStdSatzKatRefId;

	private String defaultBrand;

	private Long defaultLibrary;

	private Boolean statusElementPublish;

	private Boolean showEmptyFolders;

	// PTS requirement 22211:Benachrichtigungsfunktion
	private Boolean disableChangesNotifications;

	private BaseDateTime lastLogOutTime;

	private String imagesRootPath;

	public Long getDefaultStdSatzKatRefId() {
		return defaultStdSatzKatRefId;
	}

	public void setDefaultStdSatzKatRefId(Long defaultStdSatzKatRefId) {
		this.defaultStdSatzKatRefId = defaultStdSatzKatRefId;
	}

	public Boolean getStatusElementPublish() {
		return statusElementPublish;
	}

	public void setStatusElementPublish(Boolean statusElementPublish) {
		this.statusElementPublish = statusElementPublish;
	}

	public List<HourlyRateCatalogModel> getHourlyRateCatalogs() {
		return hourlyRateCatalogs;
	}

	public void setHourlyRateCatalogs(List<HourlyRateCatalogModel> hourlyRateCatalogs) {
		this.hourlyRateCatalogs = hourlyRateCatalogs;
	}

	public List<BrandModel> getBrands() {
		return brands;
	}

	public void setBrands(List<BrandModel> brands) {
		this.brands = brands;
	}

	public List<BrandModel> getAllBrands() {
		return allBrands;
	}

	public void setAllBrands(List<BrandModel> allBrands) {
		this.allBrands = allBrands;
	}

	public String getDefaultBrand() {
		return defaultBrand;
	}

	public void setDefaultBrand(String defaultBrand) {
		this.defaultBrand = defaultBrand;
	}

	public Long getDefaultLibrary() {
		return defaultLibrary;
	}

	public void setDefaultLibrary(Long defaultLibrary) {
		this.defaultLibrary = defaultLibrary;
	}

	// 28.08.2013: ZS: PTS_Requirement-22194: Add a new Mask for the user settings definition: empty folder setting
	public void setShowEmptyFolders(Boolean showEmptyFolders) {
		this.showEmptyFolders = showEmptyFolders;
	}

	public Boolean getShowEmptyFolders() {
		return showEmptyFolders;
	}

	// 18.11.2013: ABA: PTS_Requirement-22211: Add a new Mask for the user settings definition: show changes
	// notifications page setting
	public Boolean getDisableChangesNotifications() {
		return disableChangesNotifications;
	}

	public void setDisableChangesNotifications(Boolean disableChangesNotifications) {
		this.disableChangesNotifications = disableChangesNotifications;
	}

	public BaseDateTime getLastLogOutTime() {
		return lastLogOutTime;
	}

	public void setLastLogOutTime(BaseDateTime lastLogOutTime) {
		this.lastLogOutTime = lastLogOutTime;
	}

	public String getImagesRootPath() {
		return imagesRootPath;
	}

	public void setImagesRootPath(String imagesRootPath) {
		this.imagesRootPath = imagesRootPath;
	}

	public boolean dtoInited = false;
}
