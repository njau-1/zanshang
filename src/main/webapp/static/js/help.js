/*使用帮助*/
var jsonData={
		"question" : [
			{
			"problem" : "1、&nbsp;如何注册赞赏？","answer" : "在赞赏首页或赞赏手机版中点击“注册”，填写您的邮箱、笔名和密码后就注册成功了一个赞赏账户。</br>在未登录状态下赞赏某个项目，系统会为您填写的邮箱自动创建一个账户，请在支付成功后到邮箱接收激活邮件并点击其中的链接，按钮提示设置您的登录密码。"
			},
			{
			"problem" : "2、&nbsp;忘记登录密码怎么办？", "answer" : "在登录页面有“忘记密码”链接，点击后可填写邮箱发送验证邮件，从邮件中的链接可以打开重置密码的页面。"
			},
			{
			"problem" : "3、&nbsp;邮件中的激活链接失效了怎么办？" , "answer" : "如果您的激活链接超过了有效期限，请根据页面提示重发邮件，在新邮件中激活您的赞赏账户。"
			},
			{
			"problem" : "4、&nbsp;我可以用微信登录赞赏吗？","answer" : "可以的。在登录页面选择“微信授权登录”，然后补充一下您的手机号或邮箱，以后您就可以直接使用此微信号访问赞赏了。"
			}
		],
		"appreciation" : [
			{
			"problem" : "1、&nbsp;如何赞赏项目？","answer" : "在电脑或手机浏览器中点击项目，进入项目详情，选择支持的回报方案和份数，进行支付。支付方式包括支付宝和微信支付。"
			},
			{
			"problem" : "2、&nbsp;支付受限怎么办？", "answer" : "赞赏平台没有对您购买回报设置任何限制，但您有可能会受到支付宝或微信支付的限制。</br>微信支付限额为3000元单笔或单日。</br>当您的赞赏金额高于3000元时，请根据页面提示到电脑版赞赏网站对同一项目使用支付宝网银U盾进行支付。<a href='http://www.zan-shang.com/support/paymentflow'>支付宝网银U盾使用流程流程演示</a>"
			},
			{
			"problem" : "3、&nbsp;什么时间可以收到回报？" , "answer" : "一般众筹结束后三个月，您赞赏的作品就会印制完成。这些作品会先到作者处由他们签名，然后再连同其它礼物（如果有的话）一同寄送到您手上。如果您的回报是与作者会面，那么这个时间就由您和作者来一同确定。"
			},
			{
			"problem" : "4、&nbsp;我想修改收货地址应该怎么办？","answer" : "电脑：访问www.zan-shang.com，点击首页右上方的“我的订单”按钮，登录后可以看到您的订单记录，点击“修改收货地址”就能更新您的收货地址了。</br>手机：访问www.zan-shang.com，或者打开您收到的项目链接，在导航小浮球中点击“订单”，然后登录，同样可以看到您的订单记录，点击“修改收货地址”就能更新您的收货地址了。"
			},
			{
			"problem" : "5、&nbsp;项目失败如何退款？","answer" : "项目失败后，赞赏平台将为您办理退款，资金将原路返回您的支付宝或微信账户中。"
			}
		],
		"author" : [
			{
			"problem" : "1、&nbsp;如何发起项目？","answer" : "在注册成为赞赏用户后，点击首页右上方的“我要出版”按钮，根据提示提交作品和作者信息，完成后提交初核。"
			},
			{
			"problem" : "2、&nbsp;发布项目需要准备哪些资料？", "answer" : "书名、封面、关于书的故事、关于作者的故事。"
			},
			{
			"problem" : "3、&nbsp;项目文案怎样写？" , "answer" : "在“关于书”和“关于我”中，建议您尽可能使文案有创意和吸引力。"
			},
			{
			"problem" : "4、&nbsp;项目审核和发布流程是怎样的？","answer" : "一审：审核项目和作者的基本信息；</br>二审：审核项目的回报和筹款信息，二审通过即项目发布。"
			},
			{
			"problem" : "5、&nbsp;我可以修改项目信息吗？","answer" : "在一审结束之前您可以修改提交项目信息。"
			},
			{
			"problem" : "6、&nbsp;我可以预览项目发布后的页面吗？","answer" : "在提交项目时，点击页面上的“预览”按钮能看到生成的预览页面。"
			},
			{
			"problem" : "7、&nbsp;筹款金额如何设置？","answer" : "当您设置了方案壹即基础方案的价格时，系统会根据您的项目信息为您计算出最低筹款金额，您设置的最终金额只要大于此金额即可。"
			},
			{
			"problem" : "8、&nbsp;回报方案如何设置？","answer" : "除书本和沙龙VIP外，您可以添加其他回报，建议添加与项目相关的衍生品。多样的选择能增加项目的筹款成功率。"
			},
			{
			"problem" : "9、&nbsp;赞赏可以提供哪些营销支持？","answer" : "赞赏自有微信公众号（服务号+订阅号）推文；</br>在赞赏的友情自媒体中选择与项目匹配的账号做推广；</br>与项目匹配的其他媒体渠道和线下活动。"
			}
		],
};
var nav1="";
$(function(){
	for(var i=0;i<jsonData.question.length;i++){
		nav1 +="<li><h4>"+jsonData.question[i].problem+"</h4><p>"+jsonData.question[i].answer+"</p></li>"
	}
	$(".help-content1").html(nav1);
	$(".help-list1").mouseover(function(){
		nav1="";
		$(this).addClass("help-hover");
		for(var i=0;i<jsonData.question.length;i++){
				nav1 +="<li><h4>"+jsonData.question[i].problem+"</h4><p>"+jsonData.question[i].answer+"</p></li>"
		}
		$(".help-content1").html(nav1);
	});
	$(".help-list1").mouseout(function(){
		$(this).removeClass("help-hover");
		nav1="";
	});
	$(".help-list2").mouseover(function(){
		$(this).addClass("help-hover");
		for(var i=0;i<jsonData.appreciation.length;i++){
				nav1 +="<li><h4>"+jsonData.appreciation[i].problem+"</h4><p>"+jsonData.appreciation[i].answer+"</p></li>"
		}
		$(".help-content1").html(nav1);
	});
	$(".help-list2").mouseout(function(){
		$(this).removeClass("help-hover");
		nav1="";
	});
	$(".help-list3").mouseover(function(){
		$(this).addClass("help-hover");
		for(var i=0;i<jsonData.author.length;i++){
				nav1 +="<li><h4>"+jsonData.author[i].problem+"</h4><p>"+jsonData.author[i].answer+"</p></li>"
		}
		$(".help-content1").html(nav1);
	});
	$(".help-list3").mouseout(function(){
		$(this).removeClass("help-hover");
		nav1="";
	});
	$(".help-list4").mouseover(function(){
		$(this).addClass("help-hover");
		nav1="<p style='display: block;height: 200px;font-size: 35px;width: 670px;margin: 45px 140px;line-height: 100px;'>暂无此类问题</p>";
		$(".help-content1").html(nav1);
	});
	$(".help-list4").mouseout(function(){
		$(this).removeClass("help-hover");
		nav1="";
	});
	$(".help-right").click(function(){
		$(".Feedback").fadeIn(500);
		$(".comments").css("display","block");
		$(".comments-success").css("display","none");
	});
	$(".FeedbackClose").click(function(){
		$(".Feedback").fadeOut(500);
	});
	$(".suggestion-textarea").keydown(function(){
		if(this.value.length >= 300) {
			alert("反馈请不要超过300字");
		}
	});
	$(".suggestion-input").keydown(function(){
		if(this.value.length >= 50) {
			alert("联系方式请不要超过50字");
		}
	});
	$(".comments-submit").click(function(){
		var suggestion=$(".comments-message").find("textarea").val();
		var contact=$(".comments-message").find("input").val();
		if(suggestion==""){
			alert("反馈不能为空");
			return false;
		}
		if(suggestion.length > 300) {
			alert("反馈请不要超过300字");
			return false;
		}
		if(contact==""){
			alert("联系方式不能为空");
			return false;
		}
		if(contact.length > 50) {
			alert("联系方式请不要超过50字");
			return false;
		}
		if(suggestion!=""&&contact!=""){
			$(".comments").css("display","none");
			$(".comments-success").css("display","block");
		}
		reg_ajax.setComments(suggestion,contact);
	});
})