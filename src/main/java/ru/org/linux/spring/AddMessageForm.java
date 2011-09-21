/*
 * Copyright 1998-2010 Linux.org.ru
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package ru.org.linux.spring;

import com.google.common.collect.ImmutableList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;
import ru.org.linux.site.Poll;
import ru.org.linux.site.ScriptErrorException;
import ru.org.linux.site.Template;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// TODO: move content to AddMessageRequest
public class AddMessageForm {
  private static final Log logger = LogFactory.getLog(AddMessageForm.class);

  private String sessionId = null;
  private String image = "";
  private final String postIP;
  private final ImmutableList<String> pollList;
  public static final int MAX_MESSAGE_LENGTH_ANONYMOUS = 4096;
  public static final int MAX_MESSAGE_LENGTH = 16384;
  private boolean multiSelect = false;

  public String getSessionId() {
    return sessionId;
  }

  public String getImage() {
    return image;
  }

  public AddMessageForm(HttpServletRequest request, Template tmpl) throws IOException, ScriptErrorException {
    postIP = request.getRemoteAddr();

    sessionId = request.getParameter("session");

    if (request instanceof MultipartHttpServletRequest) {
      MultipartFile multipartFile = ((MultipartRequest) request).getFile("image");
      if (multipartFile != null && !multipartFile.isEmpty()) {
        File uploadedFile = File.createTempFile("preview", "", new File(tmpl.getObjectConfig().getPathPrefix() + "/linux-storage/tmp/"));
        image = uploadedFile.getPath();
        if ((uploadedFile.canWrite() || uploadedFile.createNewFile())) {
          try {
            logger.debug("Transfering upload to: " + image);
            multipartFile.transferTo(uploadedFile);
          } catch (Exception e) {
            throw new ScriptErrorException("Failed to write uploaded file", e);
          }
        } else {
          logger.info("Bad target file name: " + image);
        }
      }
    }

    List<String> pollList = new ArrayList<String>();

    for (int i = 0; i < Poll.MAX_POLL_SIZE; i++) {
      String poll = request.getParameter("var" + i);

      if (poll != null) {
        pollList.add(poll);
      }
    }

    if (pollList.isEmpty()) {
      this.pollList = null;
    } else {
      this.pollList = ImmutableList.copyOf(pollList);
    }

    if (request.getParameter("multiSelect")!=null) {
      multiSelect = true;
    }
  }

  public String getPostIP() {
    return postIP;
  }

  public ImmutableList<String> getPollList() {
    return pollList;
  }

  public boolean getMultiSelect() {
    return multiSelect;
  }
}
