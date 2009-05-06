<%--
  ~ Copyright 1998-2009 Linux.org.ru
  ~    Licensed under the Apache License, Version 2.0 (the "License");
  ~    you may not use this file except in compliance with the License.
  ~    You may obtain a copy of the License at
  ~
  ~        http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~    Unless required by applicable law or agreed to in writing, software
  ~    distributed under the License is distributed on an "AS IS" BASIS,
  ~    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~    See the License for the specific language governing permissions and
  ~    limitations under the License.
  --%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="lor" uri="http://www.linux.org.ru" %>
<lor:cache key="tagcloud.boxlet?count=${count}" expire="${5 * 60 * 1000}">
  <h2>Облако Меток</h2>

  <div class="boxlet_content">
    <div align="center">
      <c:forEach var="tag" items="${tags}">
        <c:url value="/view-news.jsp" var="tag_url">
          <c:param name="section" value="1"/>
          <c:param name="tag" value="${tag.value}"/>
        </c:url>
        <a class="cloud${tag.weight}" href="${tag_url}">${tag.value}</a>
      </c:forEach>
    </div>
  </div>
</lor:cache>