<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.chemguan.bean.PageBean"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  

<%
PageBean pb = (PageBean)request.getAttribute("pageBean");
 %>
      
<div class="pages">
<c:if test="${pageBean.currentPage>1}">
    <a href="javascript:void(0)" onclick="gotoPageNum(${pageBean.currentPage-1})" class="prev"> < 上一页 </a>
</c:if>
<c:if test="${pageBean.currentPage==1}">
   	<a href="javascript:void(0)" class="prev on">上一页</a>  
</c:if>

        <span class="number">
        
        <%
        for(int i=pb.getCurrentPage()>4?pb.getCurrentPage()+5>pb.getPageCount()?pb.getPageCount()>10?pb.getPageCount()-9:1:pb.getCurrentPage()-4:1;
		i<=(pb.getCurrentPage()>4?pb.getCurrentPage()+5>pb.getPageCount()?pb.getPageCount():pb.getCurrentPage()+5:pb.getPageCount()>10?10:pb.getPageCount());
		i++){
		
		if(pb.getCurrentPage()==i){
		%>
		<a href="javascript:void(0)" class="on"><%=i %></a>
		<%
		}else{
		%> 
		<a href="javascript:void(0)" onclick="gotoPageNum(<%=i %>)"><%=i %></a>
		<%
		}
		}%>     
         
        </span>
        
<c:if test="${pageBean.currentPage<pageBean.pageCount }">        
    <a href="javascript:void(0)" onclick="gotoPageNum(${pageBean.currentPage+1})" class="next">下一页></a>
</c:if>
<c:if test="${pageBean.currentPage>=pageBean.pageCount }">
	<a href="javascript:void(0)"  class="next on">下一页></a>
</c:if>

        <span>
            &nbsp;共${pageBean.pageCount }页　共&nbsp;
            <input type="text" class="text" value="${pageBean.recordCount }" readonly="readonly"/>条
        </span>
</div>
		
		
		
		
		
		<script type="text/javascript">
			function gotoPageNum(pageNum){
					$("#pageForm").append('<input type="hidden" value="'+pageNum+'" name="currentPage"/>');
					$("#pageForm").submit();
				}
		</script>