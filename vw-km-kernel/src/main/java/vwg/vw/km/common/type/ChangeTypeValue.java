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
package vwg.vw.km.common.type;

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
 * @author Zied Saidi changed by $Author: saidi $
 * @version $Revision: 1.7 $ $Date: 2013/10/22 10:27:19 $
 */
public enum ChangeTypeValue {

	USE_STAND("Wechsel genutzter Stand"), UNUSE_STAND("Wechsel genutzter Stand"), NEW("Neu"), IN_PROCESS(
			"in Bearbeitung"), NEW_STAND("Neuer Stand"), ACTIVATE(
					"Aktiviert"), ADD_ELEMENT("Verknüpfung"), REMOVE_ELEMENT("Entfernung"), DEACTIVATE("Deaktiviert"),
	// 27.08.2013: ZS: PTS_Requirement-22186: Addition of new fields historicizing
	ELEMENT_DESCRIPTION("Kommentar Element"), COMPONENT_DESCRIPTION("Kommentar Baustein"),

	ELEMENT_NUMBER("Elementnummer"), COMPONENT_NUMBER("Bausteinnummer"),

	COMPONENT_DESIGNATION("Bausteinname"), ELEMENT_DESIGNATION("Elementname"),

	ELEMENT_CATALOG_NUMBER("Katalognummer Element"), COMPONENT_CATALOG_NUMBER("Katalognummer Baustein"),

	ELEMENT_CATEGORY("Kategorie Element"), COMPONENT_CATEGORY("Objekt Baustein"),

	ELEMENT_MANUFACTURER("Hersteller Element"), COMPONENT_MANUFACTURER("Hersteller Baustein"),
	// 01.12.2022 add costgroup for RADIOBUTTON By SDH
	COST_GROUP("Kostengruppe"),

	IMAGE_PREVIEW("Vorschaubild");

	void setValue(String desc) {
		this.desc = desc;
	}

	public String value() {
		return desc;
	}

	private ChangeTypeValue(String desc) {
		this.desc = desc;
	}

	private String desc;

}
