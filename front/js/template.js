// 定义一个名为 button-counter 的新组件
var commonFooter = Vue.component('common-footer', {
  data: function () {
    return {
      count: 0
    }
  },
  template: ` <footer th:fragment="footer" class="ui inverted vertical segment m-padded-tb-massive">

                 <div class="ui center aligned container">
                     <div class="ui inverted divided stackable grid">
                         <div class="three wide column">
                             <div class="ui inverted link list">
                                 <div class="item">
                                     <img src="../static/images/wechat.png" th:src="@{/images/wechat.png}"
                                          class="ui rounded image" alt="" style="width: 110px">
                                 </div>
                             </div>
                         </div>
                         <div class="three wide column">
                             <h4 class="ui inverted header m-text-thin m-text-spaced ">Contact</h4>
                             <div class="ui inverted link list">
                                 <a href="#" class="item m-text-thin" id="mail">Email</a>
                                 <p class="item m-text-thin">今天过的真不错</p>
                             </div>
                         </div>
                         <div class="seven wide column">
                             <h4 class="ui inverted header m-text-thin m-text-spaced ">Blog</h4>
                             <p class="m-text-thin m-text-spaced m-opacity-mini">今天又是美丽的一天！</p>
                             <p class="m-text-thin m-text-spaced m-opacity-mini">有事儿点击Email联系</p>
                         </div>
                     </div>
                     <div class="ui inverted section divider"></div>
                     <p class="m-text-thin m-text-spaced m-opacity-tiny">Copyright © 2019 - 2020 Q Designed by Lirenmi</p>
                     <a target="_blank" style="color: #809297;margin-top: 10px"
                        href="http://beian.miit.gov.cn/">浙ICP备20013225号</a>
                 </div>

                 <div class="ui fullscreen modal">
                     <i class="close icon" id="close"></i>
                     <div class="header">
                         Update Your Settings
                     </div>
                     <div class="content">
                         <div class="ui form">
                             <h4 class="ui dividing header">Give me your feedback</h4>
                             <div class="field">
                                 <label>Feedback</label>
                                 <textarea id="discribe" placeholder="discribe your problem..."></textarea>
                             </div>
                             <div class="field">
                                 <div class="ui left icon input">
                                     <i class="envelope icon"></i>
                                     <input type="email" id="email" name="email" placeholder="enter your email">
                                 </div>
                             </div>
                         </div>
                     </div>
                     <div class="actions">
                         <div id="send" class="ui green button">Send</div>
                     </div>
                 </div>

             </footer>
`
});

new Vue({
    el: '#footer'
})