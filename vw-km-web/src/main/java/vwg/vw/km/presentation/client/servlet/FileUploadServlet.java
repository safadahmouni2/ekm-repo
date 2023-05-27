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
package vwg.vw.km.presentation.client.servlet;

import java.io.File;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.util.StringUtils;

import vwg.vw.km.application.service.dto.CostComponentDTO;
import vwg.vw.km.application.service.dto.CostElementDTO;
import vwg.vw.km.common.manager.Log;
import vwg.vw.km.common.manager.LogManager;
import vwg.vw.km.integration.persistence.model.ComponentVersionModel;
import vwg.vw.km.integration.persistence.model.DocumentPoolModel;
import vwg.vw.km.presentation.client.navigation.NavigationBean;

/**
 * <p>
 * Title: EKM
 * <p>
 * Description : Servlet used for the drag and drop upload files. This Servlet will prepare the files objects list and
 * load the specific bean from the session and invoke the method: uploadFile(List<FileItem> formItems)
 * </p>
 * <p>
 * Copyright: VW (c) 2015
 * </p>
 * 
 * @author saidi changed by $Author: zouhair $
 * @version $Revision: 1.4 $ $Date: 2018/11/09 11:13:31 $
 */
public class FileUploadServlet extends HttpServlet {

	private static final long serialVersionUID = 786379386446983012L;

	private final static Log log = LogManager.get().getLog(FileUploadServlet.class);

	private static final int MAX_IMAGE_PREVIEW_SIZE = 1048576;// in bytes = 1MB.

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
		DiskFileItemFactory factory = new DiskFileItemFactory();
		File file = new File(this.getClass().getResource("/uploadRef.properties").getPath());
		factory.setRepository(file.getParentFile());
		ServletFileUpload upload = new ServletFileUpload(factory);
		try {
			List<FileItem> formItems = upload.parseRequest(request);
			if (formItems != null && formItems.size() > 0) {
				String target = request.getParameter("target");
				NavigationBean bean = (NavigationBean) request.getSession().getAttribute("navigationTree");
				if ("EL".equals(target)) {
					uploadFileEl(formItems, bean);
				} else if ("COMP_PREVIEW_IMAGE".equals(target)) {
					uploadFileCompPreviewImage(formItems, bean);
				} else {
					uploadFileComp(formItems, bean);
				}
			}
		} catch (FileUploadException e) {
			log.error("FileUploadException: " + e);
		}
	}

	public void uploadFileEl(List<FileItem> formItems, NavigationBean navigationTree) {
		log.info("########### Element attachment FILE upload ########");
		CostElementDTO dto = (CostElementDTO) navigationTree.getSelectedNodeObject().getDto();
		List<DocumentPoolModel> documentList = dto.getDocumentList();

		for (FileItem item : formItems) {
			if (!item.isFormField()) {

				String contentType = item.getContentType();
				log.info("Uploading File name :" + item.getName() + " -File ContentType : " + contentType);

				// Check if the uploaded file extention is one of those extension
				// zip,jpeg,jpg,png,gif,pdf,pptx,ppt,xlsx,xls,docx,doc,xlsm,xltx,xltm if not, the file will not be
				// uploaded and a pop-up
				// message
				// will be displayed
				if (!contentType.equalsIgnoreCase("application/x-zip-compressed")
						&& !contentType.equalsIgnoreCase("image/pjpeg") && !contentType.equalsIgnoreCase("image/jpeg")
						&& !contentType.equalsIgnoreCase("image/x-png") && !contentType.equalsIgnoreCase("image/png")
						&& !contentType.equalsIgnoreCase("image/gif")
						&& !contentType.equalsIgnoreCase("application/pdf")
						&& !contentType.equalsIgnoreCase("text/plain")
						&& !contentType.equalsIgnoreCase(
								"application/vnd.openxmlformats-officedocument.presentationml.presentation")
						&& !contentType.equalsIgnoreCase("application/vnd.ms-powerpoint")
						&& !contentType
								.equalsIgnoreCase("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")// .xlsx
						&& !contentType.equalsIgnoreCase(
								"application/vnd.openxmlformats-officedocument.spreadsheetml.template") // .xltx
						&& !contentType.equalsIgnoreCase("application/vnd.ms-excel.sheet.macroEnabled.12")// .xlsm
						&& !contentType.equalsIgnoreCase("application/vnd.ms-excel.template.macroEnabled.12")// .xltm
						&& !contentType.equalsIgnoreCase("application/vnd.ms-excel")// .xls,.xlt,.xla
						&& !contentType.equalsIgnoreCase(
								"application/vnd.openxmlformats-officedocument.wordprocessingml.document")
						&& !contentType.equalsIgnoreCase("application/msword")) {
					log.error("Attempt to upload an invalid document type");
					navigationTree.openErrorPopup(navigationTree.getMessage("documentPool.documentInvalidExtension"));
					break;
				} else {
					// if (fileInfo.isSaved()) {
					// handle uploaded file
					String fullFileName = item.getName();
					log.info(item.toString());
					DocumentPoolModel documentPool = new DocumentPoolModel();
					fullFileName = StringUtils.cleanPath(fullFileName);
					String description = StringUtils.getFilename(fullFileName);
					if (description.lastIndexOf(".") > -1) {
						description = description.substring(0, description.lastIndexOf("."));
					}

					documentPool.setDescription(description);
					documentPool.setFileType(StringUtils.getFilenameExtension(fullFileName));
					documentPool.setPath(StringUtils.getFilename(fullFileName));
					// testing if the document already exists
					boolean testExistDoc = false;
					for (DocumentPoolModel document : documentList) {
						if (document.getDescription().equals(documentPool.getDescription())) {
							testExistDoc = true;
							break;
						}
					}
					if (testExistDoc) {
						log.error("Attempt to upload an existant document");
						navigationTree
								.openErrorPopup(navigationTree.getMessage("documentPool.documentAlreadyExistant"));
						break;
					}

					documentPool.setContent(item.get());
					documentList.add(documentPool);
					navigationTree.setDownActionDefintion(navigationTree.getMessage("action.costElement.uploadFile"));
					navigationTree.getSelectedNodeObject().getDto().getPageChanges().add("document_upload");
				}

			}

		}
		dto.setDocumentList(documentList);
	}

	public void uploadFileCompPreviewImage(List<FileItem> formItems, NavigationBean navigationTree) {

		log.info("########### Component Image Preview upload ########");
		CostComponentDTO dto = (CostComponentDTO) navigationTree.getSelectedNodeObject().getDto();
		ComponentVersionModel componentVersionModel = dto.getComponentVersion();

		for (FileItem item : formItems) {

			if (!item.isFormField()) {

				String contentType = item.getContentType();
				log.info("Uploading File name :" + item.getName() + " -Image Preview ContentType : " + contentType);

				// Check if the uploaded image extention is one of those extensions
				// jpg,png,gif if not,
				// the file will not be uploaded
				// and a pop-up message
				// will be displayed
				if (!contentType.equalsIgnoreCase("image/jpeg") && !contentType.equalsIgnoreCase("image/png")
						&& !contentType.equalsIgnoreCase("image/gif")) {

					log.error("Attempt to upload an invalid image preview type");
					navigationTree.openErrorPopup(navigationTree.getMessage("validation.imagepreview.format"));
					break;

				} else {
					long fileSize = item.getSize();

					if (fileSize > MAX_IMAGE_PREVIEW_SIZE) {

						// file too big error
						log.error("file too big error. size in Bytes: " + fileSize);
						navigationTree.openErrorPopup(navigationTree.getMessage("validation.imagepreview.maxlength"));
						break;

					} else {
						componentVersionModel.setImagePreviewContent(item.get());
						componentVersionModel.setImagePreviewName(item.getName());
						navigationTree
								.setDownActionDefintion(navigationTree.getMessage("action.costComponent.uploadFile"));
						navigationTree.getSelectedNodeObject().getDto().getPageChanges().add("image_preview_upload");
					}
				}
			}

		}

	}

	public void uploadFileComp(List<FileItem> formItems, NavigationBean navigationTree) {
		log.info("########### Component attachment FILE upload ########");
		CostComponentDTO dto = (CostComponentDTO) navigationTree.getSelectedNodeObject().getDto();
		List<DocumentPoolModel> documentList = dto.getDocumentList();

		for (FileItem item : formItems) {
			if (!item.isFormField()) {

				String contentType = item.getContentType();
				log.info("Uploading File name :" + item.getName() + " -File ContentType : " + contentType);

				// Check if the uploaded file extention is one of those extension
				// zip,jpeg,jpg,png,gif,pdf,pptx,ppt,xlsx,xls,docx,doc if not, the file will not be uploaded and a
				// pop-up
				// message
				// will be displayed
				if (!contentType.equals("application/x-zip-compressed") && !contentType.equals("image/pjpeg")
						&& !contentType.equals("image/jpeg") && !contentType.equals("image/x-png")
						&& !contentType.equals("image/png") && !contentType.equals("image/gif")
						&& !contentType.equals("application/pdf") && !contentType.equals("text/plain")
						&& !contentType
								.equals("application/vnd.openxmlformats-officedocument.presentationml.presentation")
						&& !contentType.equals("application/vnd.ms-powerpoint")
						&& !contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
						&& !contentType.equals("application/vnd.ms-excel")
						&& !contentType
								.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
						&& !contentType.equals("application/msword")) {
					log.error("Attempt to upload an invalid document type");
					navigationTree.openErrorPopup(navigationTree.getMessage("documentPool.documentInvalidExtension"));
					break;
				} else {
					// if (fileInfo.isSaved()) {
					// handle uploaded file
					String fullFileName = item.getName();
					log.info(item.toString());
					DocumentPoolModel documentPool = new DocumentPoolModel();
					fullFileName = StringUtils.cleanPath(fullFileName);
					String description = StringUtils.getFilename(fullFileName);
					if (description.lastIndexOf(".") > -1) {
						description = description.substring(0, description.lastIndexOf("."));
					}
					documentPool.setDescription(description);
					documentPool.setFileType(StringUtils.getFilenameExtension(fullFileName));
					documentPool.setPath(fullFileName);
					// testing if the document already exists
					boolean testExistDoc = false;
					for (DocumentPoolModel document : documentList) {
						if (document.getDescription().equals(documentPool.getDescription())) {
							testExistDoc = true;
							break;
						}
					}
					if (testExistDoc) {
						log.error("Attempt to upload an existant document");
						navigationTree
								.openErrorPopup(navigationTree.getMessage("documentPool.documentAlreadyExistant"));
						break;
					}

					documentPool.setContent(item.get());
					documentList.add(documentPool);
					navigationTree.setDownActionDefintion(navigationTree.getMessage("action.costElement.uploadFile"));
					dto.getPageChanges().add("document_upload");

					// }

					// else
					// switch (fileStatus) {
					// case MAX_FILE_SIZE_EXCEEDED: {
					// // file too big error
					// log.error("file too big error");
					// navigationTree.openErrorPopup(getMessage("com.icesoft.faces.component.inputfile.SIZE_LIMIT_EXCEEDED"));
					// break;
					// }
					// case INVALID: {
					// // invalid file error
					// log.error("invalid file error");
					// navigationTree.openErrorPopup(getMessage("com.icesoft.faces.component.inputfile.INVALID_FILE"));
					// break;
					// }
					// case UNSPECIFIED_NAME: {
					//
					// log.error("empty file name");
					// navigationTree.openErrorPopup(getMessage("com.icesoft.faces.component.inputfile.UNSPECIFIED_NAME"));
					// break;
					// }
					// case INVALID_CONTENT_TYPE: {
					//
					// log.error("empty file name");
					// navigationTree.openErrorPopup(getMessage("com.icesoft.faces.component.inputfile.INVALID_CONTENT_TYPE"));
					// break;
					// }
					// case UNKNOWN_SIZE: {
					//
					// log.error("Unknown file size");
					// navigationTree.openErrorPopup(getMessage("com.icesoft.faces.component.inputfile.UNKNOWN_SIZE"));
					// break;
					// }
					// }
				}

			}

		}
		dto.setDocumentList(documentList);
	}
}