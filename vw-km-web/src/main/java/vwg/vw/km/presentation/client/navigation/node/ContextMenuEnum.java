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
package vwg.vw.km.presentation.client.navigation.node;

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
 * @author Sebri Zouhaier changed by $Author: zouhair $
 * @version $Revision: 1.19 $ $Date: 2013/11/19 13:37:48 $
 */
public enum ContextMenuEnum implements ContextMenuInterface {

	ObjectFolderContextMenu {
		@Override
		public Boolean getAddFolderActive() {
			return true;
		}

		@Override
		public Boolean getAddActive() {
			return true;
		}

		@Override
		public Boolean getCopyActive() {
			return false;
		}

		@Override
		public Boolean getCutActive() {
			return true;
		}

		@Override
		public Boolean getDeleteActive() {
			return true;
		}

		@Override
		public Boolean getEditActive() {
			return true;
		}

		@Override
		public Boolean getPasteActive() {
			return true;
		}

		@Override
		public Boolean getSortActive() {
			return false;
		}

		@Override
		public Boolean getSelectActive() {
			return false;
		}

	},
	ObjectFolderAdministrationContextMenu {
		@Override
		public Boolean getAddFolderActive() {
			return true;
		}

		@Override
		public Boolean getAddActive() {
			return true;
		}

		@Override
		public Boolean getCopyActive() {
			return false;
		}

		@Override
		public Boolean getCutActive() {
			return false;
		}

		@Override
		public Boolean getDeleteActive() {
			return false;
		}

		@Override
		public Boolean getEditActive() {
			return false;
		}

		@Override
		public Boolean getPasteActive() {
			return true;
		}

		@Override
		public Boolean getSortActive() {
			return false;
		}

		@Override
		public Boolean getSelectActive() {
			return false;
		}
	},

	ObjectContextMenu {
		@Override
		public Boolean getAddFolderActive() {
			return false;
		}

		@Override
		public Boolean getAddActive() {
			return false;
		}

		@Override
		public Boolean getCopyActive() {
			return true;
		}

		@Override
		public Boolean getCutActive() {
			return true;
		}

		@Override
		public Boolean getDeleteActive() {
			return false;
		}

		@Override
		public Boolean getEditActive() {
			return true;
		}

		@Override
		public Boolean getPasteActive() {
			return false;
		}

		@Override
		public Boolean getSortActive() {
			return true;
		}

		@Override
		public Boolean getSelectActive() {
			return true;
		}

	},
	CatalogObjectContextMenu {
		@Override
		public Boolean getAddFolderActive() {
			return false;
		}

		@Override
		public Boolean getAddActive() {
			return false;
		}

		@Override
		public Boolean getCopyActive() {
			return true;
		}

		@Override
		public Boolean getCutActive() {
			return true;
		}

		@Override
		public Boolean getDeleteActive() {
			return true;
		}

		@Override
		public Boolean getEditActive() {
			return true;
		}

		@Override
		public Boolean getPasteActive() {
			return false;
		}

		@Override
		public Boolean getSortActive() {
			return false;
		}

		@Override
		public Boolean getSelectActive() {
			return false;
		}
	},
	CatalogFolderObjectContextMenu {
		@Override
		public Boolean getAddFolderActive() {
			return true;
		}

		@Override
		public Boolean getAddActive() {
			return true;
		}

		@Override
		public Boolean getCopyActive() {
			return false;
		}

		@Override
		public Boolean getCutActive() {
			return true;
		}

		@Override
		public Boolean getDeleteActive() {
			return true;
		}

		@Override
		public Boolean getEditActive() {
			return true;
		}

		@Override
		public Boolean getPasteActive() {
			return true;
		}

		@Override
		public Boolean getSortActive() {
			return false;
		}

		@Override
		public Boolean getSelectActive() {
			return false;
		}
	},
	CatalogFolderAdministrationObjectContextMenu {
		@Override
		public Boolean getAddFolderActive() {
			return true;
		}

		@Override
		public Boolean getAddActive() {
			return true;
		}

		@Override
		public Boolean getCopyActive() {
			return false;
		}

		@Override
		public Boolean getCutActive() {
			return false;
		}

		@Override
		public Boolean getDeleteActive() {
			return false;
		}

		@Override
		public Boolean getEditActive() {
			return false;
		}

		@Override
		public Boolean getPasteActive() {
			return true;
		}

		@Override
		public Boolean getSortActive() {
			return false;
		}

		@Override
		public Boolean getSelectActive() {
			return false;
		}
	},
	StaticFolderContextMenu {
		@Override
		public Boolean getAddFolderActive() {
			return false;
		}

		@Override
		public Boolean getAddActive() {
			return true;
		}

		@Override
		public Boolean getCopyActive() {
			return false;
		}

		@Override
		public Boolean getCutActive() {
			return false;
		}

		@Override
		public Boolean getDeleteActive() {
			return false;
		}

		@Override
		public Boolean getEditActive() {
			return false;
		}

		@Override
		public Boolean getPasteActive() {
			return false;
		}

		@Override
		public Boolean getSortActive() {
			return false;
		}

		@Override
		public Boolean getSelectActive() {
			return false;
		}
	},
	SearchObjectContextMenu {
		@Override
		public Boolean getAddFolderActive() {
			return false;
		}

		@Override
		public Boolean getAddActive() {
			return false;
		}

		@Override
		public Boolean getCopyActive() {
			return true;
		}

		@Override
		public Boolean getCutActive() {
			return false;
		}

		@Override
		public Boolean getDeleteActive() {
			return false;
		}

		@Override
		public Boolean getEditActive() {
			return false;
		}

		@Override
		public Boolean getPasteActive() {
			return false;
		}

		@Override
		public Boolean getSortActive() {
			return false;
		}

		@Override
		public Boolean getSelectActive() {
			return true;
		}
	},
	ElementToComponentAssignContextMenu {
		@Override
		public Boolean getAddFolderActive() {
			return false;
		}

		@Override
		public Boolean getAddActive() {
			return false;
		}

		@Override
		public Boolean getCopyActive() {
			return false;
		}

		@Override
		public Boolean getCutActive() {
			return false;
		}

		@Override
		public Boolean getDeleteActive() {
			return false;
		}

		@Override
		public Boolean getEditActive() {
			return true;
		}

		@Override
		public Boolean getPasteActive() {
			return false;
		}

		@Override
		public Boolean getSortActive() {
			return false;
		}

		@Override
		public Boolean getSelectActive() {
			return false;
		}
	},
	WorkObjectContextMenu {
		@Override
		public Boolean getAddFolderActive() {
			return true;
		}

		@Override
		public Boolean getAddActive() {
			return true;
		}

		@Override
		public Boolean getCopyActive() {
			return false;
		}

		@Override
		public Boolean getCutActive() {
			return false;
		}

		@Override
		public Boolean getDeleteActive() {
			return false;
		}

		@Override
		public Boolean getEditActive() {
			return false;
		}

		@Override
		public Boolean getPasteActive() {
			return true;
		}

		@Override
		public Boolean getSortActive() {
			return false;
		}

		@Override
		public Boolean getSelectActive() {
			return false;
		}
	},
	AdminWorkObjectContextMenu {
		@Override
		public Boolean getAddFolderActive() {
			return false;
		}

		@Override
		public Boolean getAddActive() {
			return false;
		}

		@Override
		public Boolean getCopyActive() {
			return true;
		}

		@Override
		public Boolean getCutActive() {
			return true;
		}

		@Override
		public Boolean getDeleteActive() {
			return false;
		}

		@Override
		public Boolean getEditActive() {
			return false;
		}

		@Override
		public Boolean getPasteActive() {
			return false;
		}

		@Override
		public Boolean getSortActive() {
			return false;
		}

		@Override
		public Boolean getSelectActive() {
			return false;
		}
	}
}
