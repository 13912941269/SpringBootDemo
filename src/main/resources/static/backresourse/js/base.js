/**
 * Created by Administrator on 2016/10/12 0012.
 */




//返回顶部
$(".toTop").hide();
$(window).scroll(function(){
    var scrollTop=$(window).scrollTop();
    if(scrollTop>0){
        $(".toTop").fadeIn(500);
    }
    else{
        $(".toTop").fadeOut(500);
    }
});
$(".toTop").find("a").click(function(){
    $("html,body").animate({"scrollTop":0},500);
});


    $(function () {
    	function slideFn(t){
    		var statis = t.parent().find("div").css("display");
			if(statis == "none"){
				t.parent().find("div").slideDown(200);
				t.parent().addClass('on');
			}else{
				t.parent().find("div").slideUp(200);
				t.parent().removeClass('on');
			}
    	}
    	
		$(".left-menu").delegate("p","click",function(){slideFn($(this));})
		$(".left-menu2").delegate("p","click",function(){slideFn($(this));})
		
		$(".left-menu").delegate("span","click",function(){
			$(this).addClass('on').siblings().removeClass('on');
			$(this).parents('li').siblings().find("span").removeClass('on');
			$(".left-menu2").find('h5').text($(this).find('em').text());
			var temp=$(this).find('ul').html();
			$(".left-menu2").find('ul').html(temp);
		})
		$(".left-menu2").delegate("span","click",function(){
			$(this).addClass('on').siblings().removeClass('on');
			$(this).parents('li').siblings().find("span").removeClass('on');
		})
		
		$(".folding").click(function(){		
			var lw=$(".left-menu").width();
			var sw=$(".sidebar").width();
			var cw=parseInt($(".content").css("margin-left").substr(0,3))
			if(lw>55){
				$(this).addClass('on');	
				$(".left-menu").animate({width:"55px"},400);
				sw-=134;
				$(".sidebar").animate({width:sw+"px"},400);
				cw-=134;
				$(".content").css("margin-left",cw+"px");
			}else{
				$(this).removeClass('on');	
				$(".left-menu").animate({width:"189px"},400);
				sw+=134;
				$(".sidebar").animate({width:sw+"px"},400);
				cw+=134;
				$(".content").css("margin-left",cw+"px");
			}			
		})
		
		$(".folding2").click(function(){
			var statis = $(".left-menu2").css("display");
			var sw=$(".sidebar").width();
			var cw=parseInt($(".content").css("margin-left").substr(0,3))
			if(statis == "none"){
				$(".left-menu2").show();
				sw+=198;
				$(".sidebar").animate({width:sw+"px"},400);
				cw+=198;
				$(".content").css("margin-left",cw+"px");
			}else{
				$(".left-menu2").hide();
				sw-=198;
				$(".sidebar").animate({width:sw+"px"},400);
				cw-=198;
				$(".content").css("margin-left",cw+"px");
			}			
		})
		

 });