<#escape x as x?html>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>赞赏科技</title>
    <meta name="description"
          content="赞赏社交出版平台”是一个服务于作者、出版机构与读者的出版平台。我们秉持“让中国所有写字的人都能站着挣钱”的理念，坚持“人人都能赞赏成书”的初心，以创新精神，致力于探寻一种全新的出版模式，让一部作品的出版变得简单容易。"/>
	<#include "./inc/head.ftl">
</head>
<body>
<div id="picView" class="imgView">
    <video class="video" width="100%" height="100%" controls preload="metadata" poster="http://7xizu1.com1.z0.glb.clouddn.com/@/image/56020512e4b07c0e5c5a974b.jpg">
        <source src="http://7xl5od.media1.z0.glb.clouddn.com/@/video/a649e27c6bcc95d09a9d7fc2a0ac48e2.mp4" type="video/mp4">
    </video>
        <#--<a href="${requestContext.contextPath}/projects/55e65547e4b033b99cf284dd"><img src="http://7xizu1.com1.z0.glb.clouddn.com/@/image/55f2a95ae4b0ff3f033dd201.jpg"/></a>-->
</div>
<div id="ph" class="home">
    <a href="${requestContext.contextPath}/"><img src="${requestContext.contextPath}/static/img/logo_white.png"
                                                  id="logo"/></a>
    <ul id="topMenu">
        <li><a href="${requestContext.contextPath}/" class="cur">首页</a></li>
        <li><a href="${requestContext.contextPath}/projects">浏览</a></li>
        <li><a href="${requestContext.contextPath}/salons">沙龙</a></li>
    </ul>
    <ul id="msgRight">
		<#if visitor??>
			<#if isPerson?? && isPerson == true>
                <li>
                    <a class="btn btnPublish" href="${requestContext.contextPath}/profile/orders">我的订单</a>
                </li>
                <li>
                    <a class="btn btnPublish" href="${requestContext.contextPath}/projects/creation">我要出版</a>
                </li>
			</#if>
            <li id="userPhoto">
                <img src="${imageRoot}${visitor.getAvatar()}">

                <div id="menuControl">
                    <div id="hoverMenu" class="shadow">
                        <div class="arrowUp">
                            &nbsp;</div>
                        <ul>
                            <li><a href="${requestContext.contextPath}/profile/${visitor.getUid()}">个人主页</a></li>
                            <li><a href="${requestContext.contextPath}/settings">个人设置</a></li>
                            <li><a href="${requestContext.contextPath}/profile/orders">参与的项目</a></li>
                            <li><a href="${requestContext.contextPath}/notifications">站内信</a></li>
                            <li><a onclick="document.getElementById('logout').submit();" href="#">退出</a></li>
                        </ul>
                    </div>
                </div>
            </li>
            <form action="${requestContext.contextPath}/authentication" method="post" id="logout">
                <input type="hidden" name="_method_" value="DELETE"/>
            </form>
		<#else>
            <li>
                <a class="btn btnPublish" href="${requestContext.contextPath}/authentication?return=/profile/orders">我的订单</a>
            </li>
            <a class="btn btnlogo" href="${requestContext.contextPath}/authentication">登录/注册</a>
		</#if>
    </ul>
</div>
<div class="evaluate">
    <a href="${requestContext.contextPath}/projects/55e65547e4b033b99cf284dd">
        <p>
            虽然之前我已出版过好几本书，但无一例外是经济管理和改革方面的书，这次通过赞赏出版的《心画怒放》画册，应是我“自说自画”的“处男作”，各位朋友，我把“他”奉献给你们了！
        </p>
        <ul>
            <li>《心画怒放》</li>
            <li class="line">|</li>
            <li>赞赏人<span>90</span></li>
            <li class="line">|</li>
            <li>赞赏金额<span>219600</span></li>
        </ul>
    </a>

    <div class="authorSm">
        <a href="${requestContext.contextPath}/profile/55e5657de4b00e620c44329e"><img
                src="http://7xizu1.com1.z0.glb.clouddn.com/@/image/55e570b8e4b00e620c4484d1.jpg" alt="卓福民"/></a><label><a
            href="${requestContext.contextPath}/profile/55e5657de4b00e620c44329e">卓福民
        作者</a><span>|</span><a href="${requestContext.contextPath}/salons/55e5657de4b00e620c44329e">沙龙</a></label>
    </div>
</div>
<div class="evaluate">
    <a href="${requestContext.contextPath}/projects/55b92755421aa9519dbd6ed9">
        <p>
            “赞”的人多了，“赏”自然就有了，新媒体的成功之路往往如此。
        </p>
        <ul>
            <li>《新媒体十讲》</li>
            <li class="line">|</li>
            <li>赞赏人<span>502</span></li>
            <li class="line">|</li>
            <li>赞赏金额<span>192200</span></li>
        </ul>
    </a>

    <div class="authorSm">
        <a href="${requestContext.contextPath}/profile/55b92752421aa9519dbd2d76"><img
                src="${requestContext.contextPath}/static/img/fanwefeng.jpg" alt="范卫锋"/></a><label><a
            href="${requestContext.contextPath}/profile/55b92752421aa9519dbd2d76">范卫锋
        作者</a><span>|</span><a href="${requestContext.contextPath}/salons/55b92752421aa9519dbd2d76">沙龙</a></label>
    </div>
</div>
<div class="evaluate">
    <a href="${requestContext.contextPath}/projects/55b92755421aa9519dbd6ee8">
        <p>
            在来到赞赏之前，我是一个高产“作家”——每周报纸3个版的汽车产经产品新闻评论。但我骨子里却是一个热爱摩托车所带来的极致自由与刺激的人，一个不安分的人，在我骑行横穿过中国大陆，环行过台湾岛，绕行全中国边境线之后，我想要留下一点对于人文社会观察的记录，也是为自己的骑行留下一点纪念，所以我选择赞赏众筹成书。
        </p>
        <ul>
            <li>《像恐龙一样不停奔跑》</li>
            <li class="line">|</li>
            <li>赞赏人<span>225</span></li>
            <li class="line">|</li>
            <li>赞赏金额<span>38600</span></li>
        </ul>
    </a>

    <div class="authorSm">
        <a href="${requestContext.contextPath}/profile/55b92752421aa9519dbd37f8"><img
                src="${requestContext.contextPath}/static/img/dinosaur.jpg" alt="狂奔的恐龙"/></a><label><a
            href="${requestContext.contextPath}/profile/55b92752421aa9519dbd37f8">狂奔的恐龙
        作者</a><span>|</span><a href="${requestContext.contextPath}/salons/55b92752421aa9519dbd37f8">沙龙</a></label>
    </div>
</div>
<div class="evaluate">
    <a href="${requestContext.contextPath}/projects/55b92755421aa9519dbd6edf">
        <p>
            我，晓斌。音乐工作者，从小喜欢阅读，字里行间满是快乐，翻开书本就是打开一个未知的世界。喜欢阅读，喜欢写下一些内心的感受。感谢赞赏社交出版，让我可以拥有一本书，属于自己的书，在书里记录下我真诚而敏感的灵魂叙说。
        </p>
        <ul>
            <li>《葡萄酒101夜》</li>
            <li class="line">|</li>
            <li>赞赏人<span>128</span></li>
            <li class="line">|</li>
            <li>赞赏金额<span>34800</span></li>
        </ul>
    </a>

    <div class="authorSm">
        <a href="${requestContext.contextPath}/profile/55b92752421aa9519dbd2d7e"><img
                src="${requestContext.contextPath}/static/img/yuanxiaobing.jpg" alt="袁晓斌"/></a><label><a
            href="${requestContext.contextPath}/profile/55b92752421aa9519dbd2d7e">袁晓斌
        作者</a><span>|</span><a href="${requestContext.contextPath}/salons/55b92752421aa9519dbd2d7e">沙龙</a></label>
    </div>
</div>
<div class="evaluate">
    <a href="${requestContext.contextPath}/projects/55b92755421aa9519dbd6f09">
        <p>
            在来到赞赏之前，我是一个严谨的企业管理者，但是隐藏在心中的浪漫和童趣，让我写了一本倾注了爱的童话，为了将这份爱传递给更多有童心的大人和孩子，所以我选择赞赏众筹成书。
        </p>
        <ul>
            <li>《公主爱上熊》</li>
            <li class="line">|</li>
            <li>赞赏人<span>153</span></li>
            <li class="line">|</li>
            <li>赞赏金额<span>38600</span></li>
        </ul>
    </a>

    <div class="authorSm">
        <a href="${requestContext.contextPath}/profile/55b92752421aa9519dbd4306"><img
                src="${requestContext.contextPath}/static/img/huweixiong.jpg"/></a><label><a
            href="${requestContext.contextPath}/profile/55b92752421aa9519dbd4306">音乐驱动灵魂 作者</a><span>|</span><a
            href="${requestContext.contextPath}/salons/55b92752421aa9519dbd4306">沙龙</a></label>
    </div>
</div>
<div class="evaluate">
    <a href="${requestContext.contextPath}/projects/55b92755421aa9519dbd6eeb">
        <p>
            这是一部诞生于束河古镇的作品，作者曾在写作期间，分别向我们展示了束河的蓝天、束河的下水沟、束河的麦当劳，和束河的小客栈。就是在这令人心驰神往的环境中，作者奋笔疾书，完成了这一部探讨媒体出路的作品。我已赞！
        </p>
        <ul>
            <li>仇勇《新媒体革命》</li>
            <li class="line">|</li>
            <li>赞赏人<span>172</span></li>
            <li class="line">|</li>
            <li>赞赏金额<span>72500</span></li>
        </ul>
    </a>

    <div class="authorSm">
        <a href="${requestContext.contextPath}/profile/55b92752421aa9519dbd3c31">
            <img src="${requestContext.contextPath}/static/img/sophia.jpg"/></a>
        <label>
            <a href="${requestContext.contextPath}/profile/55b92752421aa9519dbd3c31">Sophia 赞赏人</a>
        </label>
    </div>
</div>
<div class="evaluate">
    <a href="${requestContext.contextPath}/projects/55b92755421aa9519dbd6ee9">
        <p>
            今天距离第一次见到皙智已经过去整整一年零十天。还记得去年四月，在距离上海一万五千公里的曼哈顿，我们一起欣赏了曼哈顿的梦幻夜景，分享了彼此的经历和感悟，我发现皙智是我见过为数不多的不但拥有自己的梦想，并且敢于冲破各种约束去实现梦想的女生。很高兴一年之后看到她依旧执着地前行在属于她自己的道路之上。
        </p>
        <ul>
            <li>邵皙智《自由·修行·幸福—101次对话》</li>
            <li class="line">|</li>
            <li>赞赏人<span>166</span></li>
            <li class="line">|</li>
            <li>赞赏金额<span>28500</span></li>
        </ul>
    </a>

    <div class="authorSm">
        <a href="${requestContext.contextPath}/profile/55b92752421aa9519dbd3ac4">
            <img src="${requestContext.contextPath}/static/img/xiachaoyang.jpg"/>
        </a>
        <label>
            <a href="${requestContext.contextPath}/profile/55b92752421aa9519dbd3ac4">夏朝阳 赞赏人</a>
        </label>
    </div>
</div>
<div class="evaluate">
    <a href="${requestContext.contextPath}/projects/55b92755421aa9519dbd6f2d">
        <p>
            非常好的书，暑假快到了，与其把孩子困在各种培训班，不如带孩子走遍南京的博物馆，冬暖夏凉，哈哈！晓扬老师一定要给我签名版，我要按图索骥，在今年暑假全部去一遍！
        </p>
        <ul>
            <li>尤里卡教育《博物馆城市书@南京》</li>
            <li class="line">|</li>
            <li>赞赏人<span>138</span></li>
            <li class="line">|</li>
            <li>赞赏金额<span>28600</span></li>
    </a>
    </ul>
    <div class="authorSm">
        <a href="${requestContext.contextPath}/profile/55b92752421aa9519dbd4e22">
            <img src="${requestContext.contextPath}/static/img/malizhi.png"/></a>
        <label>
            <a href="${requestContext.contextPath}/profile/55b92752421aa9519dbd4e22">马黎燕 赞赏人</a>
        </label>
        </a>
    </div>
</div>
<div class="evaluate">
    <a href="${requestContext.contextPath}/projects/55b92755421aa9519dbd6f30">
        <p>
            因为妈妈生病的缘故，读了不少关于癌症的书：《众病之王》、《细胞叛逆者》、《进化的遗产》，可是没有一本国内作者关于癌症领域的书。偶然间在赞赏看到这本书，就直接赞赏了一本，因为我相信赞赏出书的品质，我也相信治疗癌症，必须遵循科学的方法，良好的心态，严格的自律，健康的饮食和适量的运动，才能变成与人体共存的慢性病。前几天亲历李开复的采访，他充分发挥了理工男的特性，自己在配合治疗的同时，大量阅读论文和资料得出的结论也是：科学的治疗，比偏激的、盲目的更能提高生存率。祝大家健康，祝作者能有更大的突破。
        </p>
        <ul>
            <li>菠萝《癌症·真相：医生也在读》</li>
            <li class="line">|</li>
            <li>赞赏人<span>266</span></li>
            <li class="line">|</li>
            <li>赞赏金额<span>48900</span></li>
        </ul>
    </a>

    <div class="authorSm">
        <a href="${requestContext.contextPath}/profile/55b92752421aa9519dbd3cb1">
            <img src="${requestContext.contextPath}/static/img/zhangtao.jpg"/>
        </a>
        <label>
            <a href="${requestContext.contextPath}/profile/55b92752421aa9519dbd3cb1">张涛 赞赏人</a>
        </label>
    </div>
</div>
<div class="evaluate">
    <a href="${requestContext.contextPath}/profile/55b92752421aa9519dbd34b8">
        <p>
            很多人说，网络改变了出版。但赞赏给传统出版社带来的影响却是正向的——传统出版服务的定位没变，传播纽带的角色没变，市场主体地位没变，从赞赏那里获得的是更小的风险，更精准的市场，更简化的程序。赞赏平台挖掘作者的能力将无与伦比。一切只是刚刚开始……
        </p>
        <ul>
            <li>已出版赞赏平台图书4种</li>
            <li class="line">|</li>
            <li>4种图书共获赞赏人<span>520</span></li>
            <li class="line">|</li>
            <li>赞赏金额<span>113600</span></li>
        </ul>
    </a>

    <div class="authorSm">
        <a href="${requestContext.contextPath}/profile/55b92752421aa9519dbd34b8">
            <img src="${requestContext.contextPath}/static/img/dingfeng.jpg"/></a>
        <label>
            <a href="${requestContext.contextPath}/profile/55b92752421aa9519dbd34b8">东方出版中心 出版人</a>
        </label>
    </div>
</div>
<div id="slogan">
    <h3>不赞赏 ，无思想。</h3>

    <div id="zsCount">
        <label class="zs">
            已赞赏</label>
		<#if countPrice??>
			<#list countPrice as s>
                <span>${s}</span>
			</#list>
		</#if>
        <label class="currency">元</label></div>
    <a class="btn" href="${requestContext.contextPath}/projects">人人都能赞赏成书</a>
</div>
<div id="siteMap">
    <div class="pc">
        <ul>
            <li class="tt"><a href="${requestContext.contextPath}/support/about">关于赞赏</a></li>
            <li><a href="${requestContext.contextPath}/support/about">About Zanshang</a></li>
        </ul>
        <ul>
            <li class="tt"><a href="${requestContext.contextPath}/support/media">媒体报道</a></li>
            <li><a href="${requestContext.contextPath}/support/media">Media Coverage</a></li>
        </ul>
        <ul>
            <li class="tt"><a href="${requestContext.contextPath}/support/join">加入我们</a></li>
            <li><a href="${requestContext.contextPath}/support/join">Join Us</a></li>
        </ul>
        <ul>
            <li class="tt"><a href="${requestContext.contextPath}/support/faq">帮助中心</a></li>
            <li><a href="${requestContext.contextPath}/support/faq">Help Center</a></li>

        </ul>
        <ul>
            <li class="tt"><a href="${requestContext.contextPath}/support/wechat">微信</a></li>
            <li><a href="${requestContext.contextPath}/support/wechat">WeChat Accounts</a></li>

        </ul>
    </div>
</div>
<div id="pf">
    <div id="copyright">
        COPYRIGHT © 2013-2015 Zanshang. ALL RIGHTS RESERVED<a>京ICP备13028457号-2</a></div>
</div>
    <#include "./inc/help.ftl">
<script src="${requestContext.contextPath}/static/js/jquery-1.4.1.min.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/js/jquery.slides.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/js/base.js" type="text/javascript"></script>
<script src="${requestContext.contextPath}/static/js/index.js" type="text/javascript"></script>
</body>
</html>
</#escape>