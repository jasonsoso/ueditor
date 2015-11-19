<%@page import="com.wfnhj.server.framework.util.PropertiesUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	import="com.jason.ueditor.ActionEnter"
    pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%
    request.setCharacterEncoding( "utf-8" );
	response.setHeader("Content-Type" , "text/html");
	
	String rootPath = application.getRealPath( "/" );
	String baseFile = PropertiesUtils.getEntryValue("baseFile",rootPath);
	String urlPrefix = PropertiesUtils.getEntryValue("imagesPrefix","");
	
	out.write( new ActionEnter( request, rootPath,baseFile,urlPrefix ).exec() );
	
%>