<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<div class="headd"></div>
<div class="top_head">
	<div class="left">
		<h1 class="logo"><a href="http://www.chemguan.com" target="_blank"></a></h1>
		<p class="p1">管理控制台</p>
		<p class="p2">管理<b><img src="images/httop_14.png"/></b></p>
		<div class="clear"></div>
	</div>
	<div class="right">  
		<div class="div8">
		${username }
			<div class="tkk">
				<ul>
					<div class="clear"></div>
				</ul>
				
				<h5><em><img src="images/htak_13.png"/></em><a href="backmangerloginout.do">退出管理控制台</a></h5>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
	$(function () {
		//退出登录排版
		var $lengths=$('.tkk ul li').length;
		for(var i=0;i<=($lengths-1);i++){
			var a=i+1;
			if(a%3==0){
				$('.tkk ul li').eq(i).addClass('on')
			}
		}
   });
</script>