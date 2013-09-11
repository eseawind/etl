package org.intercom.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.intercom.model.FileDetails;
import org.intercom.model.FileUploadBean;
import org.intercom.service.LocalUploadService;
import org.intercom.service.exceptions.BusinessLayerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;

@Controller
public class LocalUploadController {

	private static final int BUFF_SIZE = 4096; // 4 KB
	private static final byte[] buffer = new byte[BUFF_SIZE];

	@Autowired
	LocalUploadService uploadService;

	@Autowired
	ServletContext servletContext = null;

	@RequestMapping(value = "/upload_local", method = RequestMethod.GET)
	public void showForm(ModelMap model, Principal principal) {
		String name = principal.getName();
		model.addAttribute("username", name);
		FileUploadBean uploadBean = new FileUploadBean();
		model.addAttribute("FileUploadBean", uploadBean);
	}

	@RequestMapping(value = "/uploadform", method = RequestMethod.GET)
	public void showExistingFiles(HttpServletResponse response)
			throws IOException {
		Gson gson = new Gson();
		List<FileDetails> uploadList = new ArrayList<FileDetails>();
		try {
			uploadList = uploadService.getExistingFiles();
		} catch (BusinessLayerException e) {
			// do nothing
		}
		response.setContentType("text/plain");
		PrintWriter writer = response.getWriter();
		String json = gson.toJson(uploadList);
		writer.write(json);
		writer.close();
	}

	@RequestMapping(value = "/uploadform", method = RequestMethod.POST)
	public void handleFormUpload(@RequestParam("file") MultipartFile file,
			HttpServletResponse response) {
		String fileName = file.getOriginalFilename();
		String contentType = file.getContentType();
		long size = file.getSize();
		String upload_directory = servletContext.getRealPath("/uploads")
				+ System.getProperty("file.separator");
		File uploadDirectory = new File(upload_directory);
		if (uploadDirectory.exists()) {
			String filePath = upload_directory + fileName;
			InputStream in = null;
			OutputStream out = null;
			try {
				try {
					in = file.getInputStream();
					out = new FileOutputStream(filePath);
					int len;
					while ((len = in.read(buffer)) > 0) {
						out.write(buffer, 0, len);
					}
				} finally {
					if (in != null) {
						in.close();
					}
					if (out != null) {
						out.close();
					}
				}
				Gson gson = new Gson();
				List<FileDetails> uploadList = uploadService.getResponse(
						fileName, size, contentType);
				response.setContentType("text/plain");
				PrintWriter writer = response.getWriter();
				String json = gson.toJson(uploadList);
				writer.write(json);
				writer.close();
			} catch (IOException e) {
				// do nothing
			}
		}
	}

	@RequestMapping(value = "/download", method = RequestMethod.GET)
	public void downloadFile(@RequestParam("file") String fileName,
			HttpServletResponse response) {
		try {
			String name = fileName.substring(fileName.lastIndexOf("/") + 1);
			String mimetype = servletContext.getMimeType(fileName);
			File file = uploadService.getDownloadFile(fileName);
			int size = (int) file.length();
			response.setContentType(mimetype);
			response.setHeader("Content-Disposition", "attachment; filename=\""
					+ name + "\"");
			response.setContentLength(size);
			String upload_directory = servletContext.getRealPath("/uploads")
					+ System.getProperty("file.separator");
			String filePath = upload_directory + fileName;

			FileInputStream input = new FileInputStream(filePath);
			FileChannel channel = input.getChannel();
			byte[] buffer = new byte[256 * 1024];
			ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
			try {
				for (int length = 0; (length = channel.read(byteBuffer)) != -1;) {
					response.getOutputStream().write(buffer, 0, length);
					byteBuffer.clear();
				}
			} finally {
				input.close();
				response.getOutputStream().close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
	}

	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	public void deleteFile(@RequestParam("file") String fileName,
			HttpServletResponse response) throws IOException {
		Boolean result = false;
		try {
			result = uploadService.deleteFile(fileName);
		} catch (BusinessLayerException e) {
			// Do nothing
		}
		response.setContentType("text/plain");
		PrintWriter writer = response.getWriter();
		Gson gson = new Gson();
		String json = gson.toJson(result);
		writer.write(json);
		writer.close();
	}

}
